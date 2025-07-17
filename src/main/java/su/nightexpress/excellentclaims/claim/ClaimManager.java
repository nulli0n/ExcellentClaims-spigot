package su.nightexpress.excellentclaims.claim;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.economybridge.api.Currency;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.Placeholders;
import su.nightexpress.excellentclaims.api.claim.*;
import su.nightexpress.excellentclaims.api.event.chunk.*;
import su.nightexpress.excellentclaims.api.event.region.RegionCreateEvent;
import su.nightexpress.excellentclaims.api.event.region.RegionCreatedEvent;
import su.nightexpress.excellentclaims.api.event.region.RegionRemoveEvent;
import su.nightexpress.excellentclaims.api.event.region.RegionRemovedEvent;
import su.nightexpress.excellentclaims.claim.impl.AbstractClaim;
import su.nightexpress.excellentclaims.claim.impl.ClaimedLand;
import su.nightexpress.excellentclaims.claim.impl.ClaimedRegion;
import su.nightexpress.excellentclaims.claim.impl.Wilderness;
import su.nightexpress.excellentclaims.claim.listener.FlagListener;
import su.nightexpress.excellentclaims.claim.listener.GenericListener;
import su.nightexpress.excellentclaims.claim.lookup.ClaimLookup;
import su.nightexpress.excellentclaims.claim.lookup.ClaimStorage;
import su.nightexpress.excellentclaims.config.Config;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.config.Perms;
import su.nightexpress.excellentclaims.flag.FlagUtils;
import su.nightexpress.excellentclaims.selection.Selection;
import su.nightexpress.excellentclaims.util.ClaimUtils;
import su.nightexpress.excellentclaims.util.Relation;
import su.nightexpress.excellentclaims.util.UserInfo;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.language.entry.LangText;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.util.FileUtil;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.DimensionType;
import su.nightexpress.nightcore.util.geodata.GeoUtils;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;
import su.nightexpress.nightcore.util.geodata.pos.ExactPos;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ClaimManager extends AbstractManager<ClaimPlugin> {

    private final ClaimStorage storage;

    public ClaimManager(@NotNull ClaimPlugin plugin) {
        super(plugin);
        this.storage = new ClaimStorage();
    }

    @Override
    protected void onLoad() {
        this.loadClaims();

        this.addListener(new GenericListener(this.plugin, this));
        this.addListener(new FlagListener(this.plugin, this));

        this.addAsyncTask(this::saveClaims, Config.GENERAL_SAVE_INTERVAL.get());
    }

    @Override
    protected void onShutdown() {
        this.saveClaims();
        this.storage.clear();
    }

    public void saveClaims() {
        this.saveClaims(true);
    }

    public void saveClaims(boolean whenRequired) {
        Set<Claim> claims = this.storage.getClaims();
        claims.addAll(this.storage.getWildernesses());


        claims.forEach(claim -> {
            if (whenRequired && !claim.isSaveRequired()) return;

            this.saveClaim(claim);
        });
    }

    public void saveClaim(@NotNull Claim claim) {
        claim.save();
        claim.setSaveRequired(false);
        //plugin.debug("Claim saved on the disk: " + claim.getId());
    }

    @NotNull
    public ClaimStorage getStorage() {
        return this.storage;
    }

    @NotNull
    public ClaimLookup<LandClaim> landLookup() {
        return this.storage.landLookup();
    }

    @NotNull
    public ClaimLookup<RegionClaim> regionLookup() {
        return this.storage.regionLookup();
    }

    private void loadClaims() {
        for (ClaimType type : ClaimType.values()) {
            for (File worldFolder : FileUtil.getFolders(this.plugin.getDataFolder() + this.getDirectoryName(type))) {
                for (File file : FileUtil.getConfigFiles(this.getClaimsDirectory(type, worldFolder.getName()))) {
                    AbstractClaim claim = this.initClaim(type, file);
                    this.loadClaim(claim);
                }
            }
        }

        this.plugin.getServer().getWorlds().forEach(this::loadWilderness);
    }

    private void loadClaim(@NotNull AbstractClaim claim) {
        if (claim.load()) {
            claim.reactivate();
            this.storage.add(claim);
        }
        else this.plugin.error("Claim not loaded: '" + claim.getFile().getPath() + "'!");
    }

    private void loadWilderness(@NotNull World world) {
        Wilderness wilderness = this.storage.getWilderness(world);
        if (wilderness == null) {
            this.createWilderness(world);
        }
        else if (!wilderness.isActive()) wilderness.activate(world);
    }

    private void deleteClaim(@NotNull Claim claim) {
        claim.deactivate();
        claim.getFile().delete();
        this.storage.remove(claim);
    }

    @NotNull
    private AbstractClaim initClaim(@NotNull ClaimType type, @NotNull File file) {
        if (FileConfig.getName(file).equalsIgnoreCase(Wilderness.ID)) return new Wilderness(plugin, file);

        return switch (type) {
            case CHUNK -> new ClaimedLand(plugin, file);
            case REGION -> new ClaimedRegion(plugin, file);
        };
    }

    @NotNull
    private File getClaimFile(@NotNull String id, @NotNull ClaimType type, @NotNull World world) {
        return new File(this.getClaimsDirectory(type, world), FileConfig.withExtension(id));
    }

    @NotNull
    private <T extends AbstractClaim> T createClaim(@NotNull Player owner,
                                                    @NotNull String id,
                                                    @NotNull ClaimType type,
                                                    @NotNull World world,
                                                    @NotNull Function<File, T> supplier,
                                                    @NotNull Consumer<T> consumer) {
        return this.createClaim(id, type, world, supplier, claim -> {
            claim.setDisplayName(ClaimUtils.getDefaultName(id, owner, type));
            claim.setOwner(owner);
            consumer.accept(claim);
        });
    }

    @NotNull
    private <T extends AbstractClaim> T createClaim(@NotNull String id,
                                                    @NotNull ClaimType type,
                                                    @NotNull World world,
                                                    @NotNull Function<File, T> supplier,
                                                    @NotNull Consumer<T> consumer) {
        File file = this.getClaimFile(id, type, world);
        T claim = supplier.apply(file);

        claim.setWorldName(world.getName());
        claim.setIcon(Config.getDefaultIcon(type));
        claim.setPriority(Config.getDefaultPriority(type));
        consumer.accept(claim);
        claim.activate(world);
        claim.save();
        this.loadClaim(claim);

        return claim;
    }

    @NotNull
    private ClaimedLand createLandClaim(@NotNull Player player, @NotNull World world, @NotNull ChunkPos chunkPos, @NotNull String id) {
        return this.createClaim(player, id, ClaimType.CHUNK, world, file -> new ClaimedLand(plugin, file), claimedChunk -> {
            int blockX = GeoUtils.shiftToCoord(chunkPos.getX());
            int blockZ = GeoUtils.shiftToCoord(chunkPos.getZ());

            claimedChunk.setSpawnLocation(ExactPos.from(world.getHighestBlockAt(blockX, blockZ)));
            claimedChunk.getPositions().add(chunkPos);
        });
    }

    @NotNull
    private ClaimedRegion createRegionClaim(@NotNull Player player, @NotNull World world, @NotNull Cuboid cuboid, @NotNull String id) {
        return this.createClaim(player, id, ClaimType.REGION, world, file -> new ClaimedRegion(plugin, file), claimedRegion -> {
            claimedRegion.setSpawnLocation(ExactPos.from(cuboid.getCenter()));
            claimedRegion.setCuboid(cuboid);
        });
    }

    @NotNull
    private Wilderness createWilderness(@NotNull World world) {
        return this.createClaim(Wilderness.ID, ClaimType.REGION, world, file -> new Wilderness(plugin, file), wilderness -> {

        });
    }

    public void handleWorldLoad(@NotNull World world) {
        this.loadWilderness(world);
        this.storage.getClaims(world).forEach(claim -> claim.activate(world));
    }

    public void handleWorldUnload(@NotNull World world) {
        Wilderness wilderness = this.storage.getWilderness(world);
        if (wilderness != null) wilderness.deactivate(world);

        this.storage.getClaims(world).forEach(claim -> claim.deactivate(world));
    }

    public boolean claimLand(@NotNull Player player, @NotNull Location location, @NotNull String name) {
        World world = location.getWorld();
        if (world == null) {
            Lang.LAND_CLAIM_ERROR_INVALID_WORLD.getMessage().send(player);
            return false;
        }

        return this.claimLand(player, world, BlockPos.from(location), name);
    }

    public boolean claimLand(@NotNull Player player, @NotNull World world, @NotNull BlockPos blockPos, @NotNull String name) {
        if (!player.hasPermission(Perms.BYPASS_CHUNK_CLAIM_WORLD)) {
            Set<String> badWorlds = Config.LAND_DISABLED_WORLDS.get();
            if (badWorlds.contains(world.getName().toLowerCase())) {
                Lang.LAND_CLAIM_ERROR_BAD_WORLD.getMessage().send(player);
                return false;
            }
        }

        name = StringUtil.transformForID(name.toLowerCase());
        if (name.isBlank() || name.equalsIgnoreCase(Wilderness.ID)) {
            Lang.LAND_CLAIM_ERROR_BAD_NAME.getMessage().send(player);
            return false;
        }

        LandClaim landByName = this.landLookup().getById(player.getWorld(), name);

        // Check if player can create more lands if land with specified name is not occupied by others.
        if (landByName == null) {
            int maxLands = ClaimUtils.getMaxClaimsAmount(player, ClaimType.CHUNK);
            if (maxLands >= 0 && this.countClaims(player, ClaimType.CHUNK) >= maxLands) {
                Lang.LAND_CLAIM_ERROR_MAX_LANDS.getMessage().send(player, replacer -> replacer.replace(Placeholders.GENERIC_AMOUNT, NumberUtil.format(maxLands)));
                return false;
            }
        }

        ChunkPos chunkPos = ChunkPos.from(blockPos);

        // Check if player owns the land for provided name.
        if (landByName != null) {
            if (!landByName.isOwner(player)) {
                Lang.ERROR_NOT_OWNER.getMessage().send(player);
                return false;
            }
            if (landByName.contains(chunkPos)) {
                Lang.LAND_CLAIM_ERROR_ALREADY_CLAIMED.getMessage().send(player);
                return false;
            }

            int maxChunks = ClaimUtils.getMaxClaimChunksAmount(player);
            if (maxChunks >= 0 && landByName.getChunksAmount() >= maxChunks) {
                Lang.LAND_CLAIM_ERROR_MAX_CHUNKS.getMessage().send(player, replacer -> replacer.replace(Placeholders.GENERIC_AMOUNT, NumberUtil.format(maxChunks)));
                return false;
            }
        }

        // Check if there is other land claims by chunk position.
        Claim landByChunk = this.getPrioritizedClaim(world, blockPos, ClaimType.CHUNK);
        if (landByChunk != null && !landByChunk.isWilderness()) {
            (landByChunk.isOwner(player) ? Lang.LAND_CLAIM_ERROR_ALREADY_CLAIMED : Lang.LAND_CLAIM_ERROR_OCCUPIED).getMessage().send(player);
            return false;
        }

        if (!player.hasPermission(Perms.BYPASS_CHUNK_CLAIM_OVERLAP)) {
            boolean canOverlapWithRegions = Config.GENERAL_ALLOW_CHUNK_TO_REGION_OVERLAP.get();
            Set<RegionClaim> regions = this.regionLookup().getAt(world, chunkPos);
            if (!regions.isEmpty()) {
                if (!canOverlapWithRegions) {
                    Lang.LAND_CLAIM_ERROR_OVERLAP_DISABLED.getMessage().send(player);
                    return false;
                }

                if (regions.stream().anyMatch(region -> !region.isOwner(player))) {
                    Lang.LAND_CLAIM_ERROR_OVERLAP_FOREIGN.getMessage().send(player);
                    return false;
                }
            }
        }

        if (Config.isEconomyEnabled() && Plugins.hasEconomyBridge()) {
            if (!this.payForClaim(player, Config.ECONOMY_LAND_CLAIM_COST.get(), Lang.LAND_CLAIM_ERROR_INSUFFICIENT_FUNDS)) {
                return false;
            }
        }

        ChunkClaimEvent claimEvent = new ChunkClaimEvent(player, world, chunkPos);
        this.plugin.getPluginManager().callEvent(claimEvent);
        if (claimEvent.isCancelled()) return false;

        LandClaim claim;
        if (landByName == null) {
            claim = this.createLandClaim(player, world, chunkPos, name);
            claim.setDisplayName(StringUtil.capitalizeUnderscored(name));
        }
        else {
            claim = landByName;
            landByName.getPositions().add(chunkPos);
            landByName.setSaveRequired(true);
            this.storage.update(landByName);
        }

        Lang.LAND_CLAIM_SUCCESS.getMessage().send(player, replacer -> replacer.replace(claim.replacePlaceholders()));

        ChunkClaimedEvent claimedEvent = new ChunkClaimedEvent(claim, player);
        this.plugin.getPluginManager().callEvent(claimedEvent);

        return true;
    }

    public boolean startMerge(@NotNull Player player, @NotNull MergeType type) {
        LandClaim claim = this.getPrioritizedLand(player.getWorld(), BlockPos.from(player.getLocation()));
        if (claim == null) {
            Lang.ERROR_NO_CHUNK.getMessage().send(player);
            return false;
        }
        return this.startMerge(player, type, claim);
    }

    public boolean startMerge(@NotNull Player player, @NotNull MergeType type, @NotNull LandClaim claim) {
        if (!claim.hasPermission(player, type == MergeType.MERGE ? ClaimPermission.MERGE_CLAIM : ClaimPermission.SPLIT_CLAIM)) {
            Lang.ERROR_NO_CLAIM_PERMISSION.getMessage().send(player);
            return false;
        }
        if (type == MergeType.SPLIT && claim.isSingle()) {
            Lang.LAND_SPLIT_ERROR_NOT_MERGED.getMessage().send(player);
            return false;

        }
        (type == MergeType.MERGE ? Lang.LAND_MERGE_INFO : Lang.LAND_SPLIT_INFO).getMessage().send(player);

        this.plugin.getSelectionManager().addInMerge(player, type, claim);
        return true;
    }

    public boolean separateChunk(@NotNull Player player, @NotNull LandClaim source, @NotNull BlockPos blockPos) {
        World world = player.getWorld();
        ChunkPos chunkPos = blockPos.toChunkPos();

        LandClaim target = this.getPrioritizedLand(world, blockPos);
        if (target == null) {
            Lang.LAND_SPLIT_ERROR_NOTHING.getMessage().send(player);
            return false;
        }
        if (target != source) {
            Lang.LAND_SPLIT_ERROR_DIFFERENT.getMessage().send(player);
            return false;
        }
        if (source.isSingle()) {
            Lang.LAND_SPLIT_ERROR_NOT_MERGED.getMessage().send(player);
            return false;
        }

        ChunkSeparateEvent separateEvent = new ChunkSeparateEvent(source, player, chunkPos);
        this.plugin.getPluginManager().callEvent(separateEvent);
        if (separateEvent.isCancelled()) return false;

        source.getPositions().remove(chunkPos);
        source.save();

        this.storage.update(source);

        String name = ClaimUtils.createChunkClaimID(player, chunkPos);
        ClaimedLand land = this.createLandClaim(player, world, chunkPos, name);
        ClaimUtils.inheritanceSettings(land, source);

        Lang.LAND_SPLIT_SUCCESS.getMessage().send(player, replacer -> replacer.replace(land.replacePlaceholders()));

        ChunkSeparatedEvent separatedEvent = new ChunkSeparatedEvent(source, player, land);
        this.plugin.getPluginManager().callEvent(separatedEvent);

        return true;
    }

    public boolean mergeChunk(@NotNull Player player, @NotNull LandClaim source, @NotNull BlockPos blockPos) {
        LandClaim target = this.getPrioritizedLand(player.getWorld(), blockPos);
        if (target == null) {
            Lang.LAND_MERGE_ERROR_NOTHING.getMessage().send(player);
            return false;
        }

        return this.mergeChunk(player, source, target);
    }

    public boolean mergeChunk(@NotNull Player player, @NotNull LandClaim source, @NotNull LandClaim target) {
        if (!target.hasPermission(player, ClaimPermission.MERGE_CLAIM)) {
            Lang.ERROR_NO_CLAIM_PERMISSION.getMessage().send(player);
            return false;
        }
        if (!source.getOwnerId().equals(target.getOwnerId())) {
            Lang.LAND_MERGE_ERROR_FOREIGN.getMessage().send(player);
            return false;
        }
        if (source == target) {
            Lang.LAND_MERGE_ERROR_SAME.getMessage().send(player);
            return false;
        }
        if (!source.getWorldName().equalsIgnoreCase(target.getWorldName())) {
            Lang.LAND_MERGE_ERROR_DIFFERENT_WORLD.getMessage().send(player);
            return false;
        }

        int maxChunks = ClaimUtils.getMaxClaimChunksAmount(player);
        if (maxChunks >= 0 && target.getChunksAmount() >= maxChunks) {
            Lang.LAND_MERGE_ERROR_MAX_CHUNKS.getMessage().send(player, replacer -> replacer.replace(Placeholders.GENERIC_AMOUNT, maxChunks));
            return false;
        }

        ChunkMergeEvent mergeEvent = new ChunkMergeEvent(source, player, target);
        this.plugin.getPluginManager().callEvent(mergeEvent);
        if (mergeEvent.isCancelled()) return false;

        source.getMembers().forEach(target::addMember);
        target.getPositions().addAll(source.getPositions());
        target.save();

        this.deleteClaim(source);
        this.storage.update(target);

        Lang.LAND_MERGE_SUCCESS.getMessage().send(player, replacer -> replacer.replace(target.replacePlaceholders()));

        ChunkMergedEvent mergedEvent = new ChunkMergedEvent(source, player, target);
        this.plugin.getPluginManager().callEvent(mergedEvent);

        return true;
    }

    public boolean claimRegionFromSelection(@NotNull Player player, @NotNull String name) {
        Selection selection = this.plugin.getSelectionManager().getSelection(player);
        if (selection == null) {
            Lang.REGION_CREATE_ERROR_NO_SELECTION.getMessage().send(player);
            return false;
        }

        Cuboid cuboid = selection.toCuboid();
        if (cuboid == null) {
            Lang.REGION_CREATE_ERROR_INCOMPLETE_SELECTION.getMessage().send(player);
            return false;
        }

        World world = player.getWorld();
        if (Config.REGION_CLAIM_MAX_HEIGHT.get()) {
            cuboid = cuboid.maxHeight(world);
        }

        if (!this.claimRegion(player, world, cuboid, name)) {
            return false;
        }

        this.plugin.getSelectionManager().removeAll(player);
        return true;
    }

    public boolean claimRegion(@NotNull Player player, @NotNull World world, @NotNull BlockPos from, @NotNull BlockPos to, @NotNull String name) {
        return this.claimRegion(player, world, new Cuboid(from, to), name);
    }

    public boolean claimRegion(@NotNull Player player, @NotNull World world, @NotNull Cuboid cuboid, @NotNull String name) {
        if (!player.hasPermission(Perms.BYPASS_REGION_CLAIM_WORLD)) {
            Set<String> badWorlds = Config.REGION_DISABLED_WORLDS.get();
            if (badWorlds.contains(world.getName().toLowerCase())) {
                Lang.REGION_CREATE_ERROR_BAD_WORLD.getMessage().send(player);
                return false;
            }
        }

        String id = StringUtil.transformForID(name.toLowerCase());
        if (id.isBlank() || id.equalsIgnoreCase(Wilderness.ID)) {
            Lang.REGION_CREATE_ERROR_BAD_NAME.getMessage().send(player);
            return false;
        }

        int maxBlocks = ClaimUtils.getRegionBlocksLimit(player);
        // Do not care about Y axis if all regions must be expanded to the whole Y axis.
        DimensionType type = Config.isRegionsMaxHeight() ? DimensionType._2D : DimensionType._3D;
        int volume = cuboid.getVolume(type);

        if (maxBlocks > 0 && !player.hasPermission(Perms.BYPASS_REGION_BLOCKS_AMOUNT) && volume > maxBlocks) {
            Lang.REGION_CREATE_ERROR_MAX_BLOCKS.getMessage().send(player, replacer -> replacer
                .replace(Placeholders.GENERIC_AMOUNT, NumberUtil.formatCompact(maxBlocks))
            );
            return false;
        }

        int maxRegions = ClaimUtils.getMaxClaimsAmount(player, ClaimType.REGION);
        if (maxRegions >= 0 && this.countClaims(player, ClaimType.REGION) >= maxRegions) {
            Lang.REGION_CREATE_ERROR_MAX_AMOUNT.getMessage().send(player, replacer -> replacer.replace(Placeholders.GENERIC_AMOUNT, NumberUtil.format(maxRegions)));
            return false;
        }

        RegionClaim current = this.regionLookup().getById(world, id);
        if (current != null) {
            Lang.REGION_CREATE_ERROR_ALREADY_EXISTS.getMessage().send(player);
            return false;
        }

        if (!player.hasPermission(Perms.BYPASS_REGION_CLAIM_OVERLAP)) {
            boolean canOverlapWithChunks = Config.GENERAL_ALLOW_REGION_TO_CHUNK_OVERLAP.get();
            Set<LandClaim> chunks = this.landLookup().getInCuboid(world, cuboid);
            if (!chunks.isEmpty()) {
                if (!canOverlapWithChunks) {
                    Lang.REGION_CREATE_ERROR_OVERLAP_CHUNK.getMessage().send(player);
                    return false;
                }

                if (chunks.stream().anyMatch(chunk -> !chunk.isOwner(player))) {
                    Lang.REGION_CREATE_ERROR_OVERLAP_FOREIGN_CHUNK.getMessage().send(player);
                    return false;
                }
            }

            Set<RegionClaim> regions = this.regionLookup().getInCuboid(world, cuboid);
            if (!regions.isEmpty()) {
                if (regions.stream().anyMatch(region -> !region.isOwner(player))) {
                    Lang.REGION_CREATE_ERROR_OVERLAP_FOREIGN_REGION.getMessage().send(player);
                    return false;
                }
            }
        }

        if (Config.isEconomyEnabled() && Plugins.hasEconomyBridge()) {
            if (!this.payForClaim(player, Config.ECONOMY_REGION_CLAIM_COST.get(), Lang.REGION_CREATE_ERROR_INSUFFICIENT_FUNDS)) {
                return false;
            }
        }

        RegionCreateEvent createEvent = new RegionCreateEvent(player, world, cuboid, id);
        this.plugin.getPluginManager().callEvent(createEvent);
        if (createEvent.isCancelled()) return false;

        ClaimedRegion claimedRegion = this.createRegionClaim(player, world, cuboid, id);

        Lang.REGION_CREATE_SUCCESS.getMessage().send(player, replacer -> replacer.replace(claimedRegion.replacePlaceholders()));

        RegionCreatedEvent createdEvent = new RegionCreatedEvent(claimedRegion, player);
        this.plugin.getPluginManager().callEvent(createdEvent);

        return true;
    }

    public boolean removeRegion(@NotNull Player player, @NotNull String name) {
        return this.removeRegion(player, player.getWorld(), name);
    }

    public boolean removeRegion(@NotNull Player player, @NotNull World world, @NotNull String name) {
        RegionClaim claim = this.regionLookup().getById(world, name);
        if (claim == null) {
            Lang.REGION_REMOVE_ERROR_NO_REGION.getMessage().send(player);
            return false;
        }

        return this.removeRegion(player, claim);
    }

    public boolean removeRegion(@NotNull Player player, @NotNull RegionClaim claim) {
        if (!claim.hasPermission(player, ClaimPermission.REMOVE_CLAIM)) {
            Lang.ERROR_NO_CLAIM_PERMISSION.getMessage().send(player);
            return false;
        }

        RegionRemoveEvent removeEvent = new RegionRemoveEvent(claim, player);
        this.plugin.getPluginManager().callEvent(removeEvent);
        if (removeEvent.isCancelled()) return false;

        this.deleteClaim(claim);

        Lang.REGION_REMOVE_SUCCESS.getMessage().send(player, replacer -> replacer.replace(claim.replacePlaceholders()));

        RegionRemovedEvent removedEvent = new RegionRemovedEvent(claim, player);
        this.plugin.getPluginManager().callEvent(removedEvent);

        return true;
    }

    public boolean unclaimChunk(@NotNull Player player, @NotNull Location location) {
        World world = location.getWorld();
        if (world == null) {
            Lang.LAND_CLAIM_ERROR_INVALID_WORLD.getMessage().send(player);
            return false;
        }

        return this.unclaimChunk(player, world, BlockPos.from(location));
    }

    public boolean unclaimChunk(@NotNull Player player, @NotNull World world, @NotNull BlockPos blockPos) {
        LandClaim claim = this.getPrioritizedLand(world, blockPos);
        if (claim == null) {
            Lang.LAND_UNCLAIM_ERROR_NOTHING.getMessage().send(player);
            return false;
        }

        return this.unclaimChunk(player, claim);
    }

    public boolean unclaimChunk(@NotNull Player player, @NotNull LandClaim claim) {
        if (!claim.hasPermission(player, ClaimPermission.REMOVE_CLAIM)) {
            Lang.ERROR_NO_CLAIM_PERMISSION.getMessage().send(player);
            return false;
        }

        ChunkUnclaimEvent unclaimEvent = new ChunkUnclaimEvent(claim, player);
        this.plugin.getPluginManager().callEvent(unclaimEvent);
        if (unclaimEvent.isCancelled()) return false;

        this.deleteClaim(claim);

        claim.getPositions().clear();

        Lang.LAND_UNCLAIM_SUCCESS.getMessage().send(player, replacer -> replacer.replace(claim.replacePlaceholders()));

        ChunkUnclaimedEvent unclaimedEvent = new ChunkUnclaimedEvent(claim, player);
        this.plugin.getPluginManager().callEvent(unclaimedEvent);

        return true;
    }

    public boolean setSpawnLocation(@NotNull Player player, @NotNull ClaimType type) {
        Location location = player.getLocation();

        Claim claim = this.getPrioritizedClaim(location);
        if (claim == null || claim.getType() != type) {
            LangText message = type == ClaimType.CHUNK ? Lang.ERROR_NO_CHUNK : Lang.ERROR_NO_REGION;
            message.getMessage().send(player);
            return false;
        }

        return this.setSpawnLocation(player, claim, location);
    }

    public boolean setSpawnLocation(@NotNull Player player, @NotNull Claim claim, @NotNull Location location) {
        if (!claim.hasPermission(player, ClaimPermission.SET_CLAIM_SPAWN)) {
            Lang.ERROR_NO_CLAIM_PERMISSION.getMessage().send(player);
            return false;
        }
        if (!claim.isInside(location)) {
            Lang.CLAIM_SET_SPAWN_ERROR_OUTSIDE.getMessage().send(player);
            return false;
        }
        if (!ClaimUtils.isSafeLocation(location)) {
            Lang.CLAIM_SET_SPAWN_ERROR_UNSAFE.getMessage().send(player);
            return false;
        }

        claim.setSpawnLocation(ExactPos.from(location));
        claim.setSaveRequired(true);

        Lang.CLAIM_SET_SPAWN_SUCCESS.getMessage().send(player, replacer -> replacer.replace(claim.replacePlaceholders()));

        return true;
    }

    public boolean setName(@NotNull Player player, @NotNull ClaimType type, @NotNull String name) {
        Claim claim = this.getPrioritizedClaim(player.getLocation());
        if (claim == null || claim.getType() != type) {
            LangText message = type == ClaimType.CHUNK ? Lang.ERROR_NO_CHUNK : Lang.ERROR_NO_REGION;
            message.getMessage().send(player);
            return false;
        }

        return this.setName(player, claim, name);
    }

    public boolean setName(@NotNull Player player, @NotNull Claim claim, @NotNull String name) {
        if (!claim.hasPermission(player, ClaimPermission.RENAME_CLAIM)) {
            Lang.ERROR_NO_CLAIM_PERMISSION.getMessage().send(player);
            return false;
        }

        if (player.hasPermission(Perms.BYPASS_NAME_LENGTH)) {
            String raw = NightMessage.stripTags(name);
            int maxLength = Config.GENERAL_MAX_NAME_LENGTH.get();
            if (raw.length() > maxLength) {
                Lang.CLAIM_RENAME_ERROR_TOO_LONG.getMessage().send(player, replacer -> replacer.replace(Placeholders.GENERIC_AMOUNT, maxLength));
                return false;
            }
        }

        claim.setDisplayName(name);
        claim.setSaveRequired(true);
        Lang.CLAIM_RENAME_SUCCESS.getMessage().send(player, replacer -> replacer.replace(claim.replacePlaceholders()));
        return true;
    }

    public boolean setDescription(@NotNull Player player, @NotNull ClaimType type, @NotNull String name) {
        Claim claim = this.getPrioritizedClaim(player.getLocation());
        if (claim == null || claim.getType() != type) {
            LangText message = type == ClaimType.CHUNK ? Lang.ERROR_NO_CHUNK : Lang.ERROR_NO_REGION;
            message.getMessage().send(player);
            return false;
        }

        return this.setDescription(player, claim, name);
    }

    public boolean setDescription(@NotNull Player player, @NotNull Claim claim, @NotNull String description) {
        if (!claim.hasPermission(player, ClaimPermission.SET_CLAIM_DESCRIPTION)) {
            Lang.ERROR_NO_CLAIM_PERMISSION.getMessage().send(player);
            return false;
        }

        if (player.hasPermission(Perms.BYPASS_DESCRIPTION_LENGTH)) {
            String raw = NightMessage.stripTags(description);
            int maxLength = Config.GENERAL_MAX_DESCRIPTION_LENGTH.get();
            if (raw.length() > maxLength) {
                Lang.CLAIM_DESCRIPTION_ERROR_TOO_LONG.getMessage().send(player, replacer -> replacer.replace(Placeholders.GENERIC_AMOUNT, maxLength));
                return false;
            }
        }

        claim.setDescription(description);
        claim.setSaveRequired(true);
        Lang.CLAIM_DESCRIPTION_SUCCESS.getMessage().send(player, replacer -> replacer.replace(claim.replacePlaceholders()));
        return true;
    }

    public boolean transferOwnership(@NotNull Player player, @NotNull ClaimType type, @NotNull Player target) {
        Claim claim = this.getPrioritizedClaim(player.getLocation());
        if (claim == null || claim.getType() != type) {
            LangText message = type == ClaimType.CHUNK ? Lang.ERROR_NO_CHUNK : Lang.ERROR_NO_REGION;
            message.getMessage().send(player);
            return false;
        }

        return this.transferOwnership(player, claim, target);
    }

    public boolean transferOwnership(@NotNull Player player, @NotNull Claim claim, @NotNull Player target) {
        if (!claim.hasPermission(player, ClaimPermission.TRANSFER_CLAIM)) {
            Lang.ERROR_NO_CLAIM_PERMISSION.getMessage().send(player);
            return false;
        }
        if (player == target) {
            Lang.CLAIM_TRANSFER_ERROR_YOURSELF.getMessage().send(player, replacer -> replacer.replace(claim.replacePlaceholders()));
            return false;
        }

        ClaimType claimType = claim.getType();
        int claimsAmount = this.countClaims(target, claimType);
        int maxClaims = ClaimUtils.getMaxClaimsAmount(target, claimType);
        if (maxClaims >= 0 && claimsAmount >= maxClaims) {
            Lang.CLAIM_TRANSFER_ERROR_TOO_MANY.getMessage().send(player, replacer -> replacer.replace(Placeholders.forPlayer(target)));
            return false;
        }

        this.storage.remove(claim);
        claim.removeMember(target);
        claim.addMember(player);
        claim.setOwner(UserInfo.of(target));
        claim.setSaveRequired(true);
        this.storage.add(claim);

        Lang.CLAIM_TRANSFER_SUCCESS.getMessage().send(player, replacer -> replacer.replace(claim.replacePlaceholders()).replace(Placeholders.forPlayer(target)));
        Lang.CLAIM_TRANSFER_NOTIFY.getMessage().send(target, replacer -> replacer.replace(claim.replacePlaceholders()).replace(Placeholders.forPlayer(player)));

        return true;
    }



    public boolean testClaim(@NotNull Location location, @NotNull Supplier<Predicate<Claim>> predicateSupplier) {
        Claim claim = this.getPrioritizedClaim(location);
        if (claim == null) return true;

        Predicate<Claim> predicate = predicateSupplier.get();
        return predicate == null || predicate.test(claim);
    }

    public boolean canMobSpawn(@NotNull Entity entity, @NotNull Location location) {
        if (entity instanceof Player) return true;

        return this.testClaim(entity.getLocation(), () -> FlagUtils.getMobSpawnPredicate(entity));
    }

    public boolean canUseBlock(@NotNull Entity entity, @NotNull Block block, @Nullable Action action) {
        return this.testClaim(block.getLocation(), () -> FlagUtils.getBlockInteractionPredicate(entity, block, action));
    }

    public boolean canUseItem(@NotNull Player player, @NotNull Location location, @NotNull ItemStack itemStack) {
        return this.testClaim(location, () -> FlagUtils.getItemUsagePredicate(player, itemStack));
    }

    public boolean canUseEntity(@NotNull Player player, @NotNull Location location, @NotNull Entity entity) {
        return this.testClaim(location, () -> FlagUtils.getEntityInteractPredicate(player, entity));
    }

    public boolean canDamage(@Nullable Entity damager, @NotNull Entity target, @NotNull DamageType damageType) {
        return this.testClaim(target.getLocation(), () -> FlagUtils.getEntityDamagePredicate(target, damager, damageType));
    }

    public boolean canThrowProjectile(@NotNull Player player, @NotNull Projectile projectile) {
        return this.testClaim(player.getLocation(), () -> FlagUtils.getProjectileThrowPredicate(player, projectile));
    }

    public boolean canUseCommand(@NotNull Player player, @NotNull Command command) {
        return this.testClaim(player.getLocation(), () -> FlagUtils.getCommandUsagePredicate(player, command));
    }

    public boolean canBreak(@NotNull Player player, @NotNull Block block) {
        return this.canBreak(player, block.getLocation());
    }

    public boolean canBreak(@NotNull Player player, @NotNull Location location) {
        return this.testClaim(location, () -> FlagUtils.getBreakingPredicate(player));
    }

    public boolean canBuild(@NotNull Player player, @NotNull Block block) {
        return this.canBuild(player, block.getLocation());
    }

    public boolean canBuild(@NotNull Player player, @NotNull Location location) {
        return this.testClaim(location, () -> FlagUtils.getBuildingPredicate(player));
    }

    public boolean canHarvest(@NotNull Player player, @NotNull Block block) {
        return this.canHarvest(player, block.getLocation());
    }

    public boolean canHarvest(@NotNull Player player, @NotNull Location location) {
        return this.testClaim(location, () -> FlagUtils.getHarvestingPredicate(player));
    }

    @NotNull
    public String getClaimName(@NotNull Location location, @Nullable ClaimType type) {
        return this.getClaimInfo(location, type, Claim::getDisplayName);
    }

    @NotNull
    public String getClaimId(@NotNull Location location, @Nullable ClaimType type) {
        return this.getClaimInfo(location, type, Claim::getId);
    }

    @NotNull
    public String getClaimOwnerName(@NotNull Location location, @Nullable ClaimType type) {
        return this.getClaimInfo(location, type, Claim::getOwnerName);
    }

    @NotNull
    private String getClaimInfo(@NotNull Location location, @Nullable ClaimType type, @NotNull Function<Claim, String> function) {
        Claim claim = this.getPrioritizedClaim(location, type);
        return claim == null ? Lang.OTHER_NO_CLAIM.getString() : function.apply(claim);
    }

    private boolean payForClaim(@NotNull Player player, double cost, @NotNull LangText error) {
        if (cost <= 0D) return true;
        if (ClaimUtils.hasClaimCostBypass(player)) return true;

        Currency currency = ClaimUtils.getEconomyProvider();
        if (currency == null) return true;

        if (currency.getBalance(player) < cost) {
            error.getMessage().send(player, replacer -> replacer.replace(Placeholders.GENERIC_AMOUNT, currency.format(cost)));
            return false;
        }

        currency.take(player, cost);
        return true;
    }

    @NotNull
    public Relation getRelation(@NotNull Location sourceLocation) {
        return this.getRelation(sourceLocation, null);
    }

    @NotNull
    public Relation getRelation(@Nullable Location sourceLocation, @Nullable Location targetLocation) {
        Claim source = sourceLocation == null ? null : this.getPrioritizedClaim(sourceLocation);
        Claim target = targetLocation == null || targetLocation.equals(sourceLocation) ? source : this.getPrioritizedClaim(targetLocation);

        return new Relation(source, target);
    }

    @NotNull
    public Relation getRelation(@Nullable Claim source, @NotNull Location targetLocation) {
        Claim target = this.getPrioritizedClaim(targetLocation);
        return new Relation(source, target);
    }

    public int countClaims(@NotNull Player player, @NotNull ClaimType type) {
        return this.countClaims(player.getUniqueId(), type);
    }

    public int countClaims(@NotNull UUID playerId, @NotNull ClaimType type) {
        return this.storage.getClaimsByOwner(playerId, type).size();
    }

    // Check if location is occipied by a claim or region.

    public boolean isClaimed(@NotNull Location location) {
        World world = location.getWorld();
        return world != null && this.isClaimed(world.getName(), BlockPos.from(location));
    }

    public boolean isClaimed(@NotNull Block block) {
        return this.isClaimed(block.getWorld(), BlockPos.from(block));
    }

    public boolean isClaimed(@NotNull World world, @NotNull BlockPos pos) {
        return this.isClaimed(world.getName(), pos);
    }

    public boolean isClaimed(@NotNull String worldName, @NotNull BlockPos blockPos) {
        return this.isChunkClaimed(worldName, blockPos.toChunkPos()) || this.isRegionClaim(worldName, blockPos);
    }

    // Check if chunk is claimed by chunk position.

    public boolean isChunkClaimed(@NotNull Location location) {
        World world = location.getWorld();
        return world != null && this.isChunkClaimed(world.getName(), ChunkPos.from(location));
    }

    public boolean isChunkClaimed(@NotNull Block block) {
        return this.isChunkClaimed(block.getWorld(), ChunkPos.from(block));
    }

    public boolean isChunkClaimed(@NotNull World world, int x, int z) {
        return this.isChunkClaimed(world, ChunkPos.from(x, z));
    }

    public boolean isChunkClaimed(@NotNull World world, @NotNull ChunkPos pos) {
        return this.isChunkClaimed(world.getName(), pos);
    }

    public boolean isChunkClaimed(@NotNull String worldName, @NotNull ChunkPos pos) {
        return !this.landLookup().getAt(worldName, pos).isEmpty();
    }


    // Check if there are one or more regions at given location.

    public boolean isRegionClaim(@NotNull Location location) {
        World world = location.getWorld();
        return world != null && this.isRegionClaim(world, BlockPos.from(location));
    }

    public boolean isRegionClaim(@NotNull Block block) {
        return this.isRegionClaim(block.getWorld(), BlockPos.from(block));
    }

    public boolean isRegionClaim(@NotNull World world, @NotNull BlockPos pos) {
        return this.isRegionClaim(world.getName(), pos);
    }

    public boolean isRegionClaim(@NotNull String worldName, @NotNull BlockPos pos) {
        return !this.regionLookup().getAt(worldName, pos).isEmpty();
    }



    @NotNull
    public String getClaimsDirectory(@NotNull ClaimType type, @NotNull World world) {
        return this.getClaimsDirectory(type, world.getName());
    }

    @NotNull
    public String getClaimsDirectory(@NotNull ClaimType type, @NotNull String worldName) {
        return this.plugin.getDataFolder().getAbsolutePath() + this.getDirectoryName(type) + worldName;
    }

    @NotNull
    public String getDirectoryName(@NotNull ClaimType type) {
        return type == ClaimType.CHUNK ? Config.DIR_CLAIM_CHUNK : Config.DIR_CLAIM_REGION;
    }

    // Get claim with the greatest priority by location.

    @Nullable
    public LandClaim getPrioritizedLand(@NotNull Block block) {
        return this.getPrioritizedLand(this.getPrioritizedClaim(block));
    }

    @Nullable
    public LandClaim getPrioritizedLand(@NotNull Location location) {
        return this.getPrioritizedLand(this.getPrioritizedClaim(location));
    }

    @Nullable
    public LandClaim getPrioritizedLand(@NotNull World world, @NotNull BlockPos blockPos) {
        return this.getPrioritizedLand(this.getPrioritizedClaim(world, blockPos));
    }

    @Nullable
    private LandClaim getPrioritizedLand(@Nullable Claim claim) {
        return claim instanceof LandClaim landClaim ? landClaim : null;
    }



    @Nullable
    public RegionClaim getPrioritizedRegion(@NotNull Block block) {
        return this.getPrioritizedRegion(this.getPrioritizedClaim(block));
    }

    @Nullable
    public RegionClaim getPrioritizedRegion(@NotNull Location location) {
        return this.getPrioritizedRegion(this.getPrioritizedClaim(location));
    }

    @Nullable
    public RegionClaim getPrioritizedRegion(@NotNull World world, @NotNull BlockPos blockPos) {
        return this.getPrioritizedRegion(this.getPrioritizedClaim(world, blockPos));
    }

    @Nullable
    private RegionClaim getPrioritizedRegion(@Nullable Claim claim) {
        return claim instanceof RegionClaim regionClaim ? regionClaim : null;
    }



    @Nullable
    public Claim getPrioritizedClaim(@NotNull Block block) {
        return this.getPrioritizedClaim(block, null);
    }

    @Nullable
    public Claim getPrioritizedClaim(@NotNull Block block, @Nullable ClaimType type) {
        return this.getPrioritizedClaim(block.getWorld(), BlockPos.from(block), type);
    }

    @Nullable
    public Claim getPrioritizedClaim(@NotNull Location location) {
        return this.getPrioritizedClaim(location, null);
    }

    @Nullable
    public Claim getPrioritizedClaim(@NotNull Location location, @Nullable ClaimType type) {
        World world = location.getWorld();
        if (world == null) return null;

        return this.getPrioritizedClaim(world, BlockPos.from(location), type);
    }

    @Nullable
    public Claim getPrioritizedClaim(@NotNull World world, @NotNull BlockPos blockPos) {
        return this.getPrioritizedClaim(world, blockPos, null);
    }

    @Nullable
    public Claim getPrioritizedClaim(@NotNull World world, @NotNull BlockPos blockPos, @Nullable ClaimType type) {
        return this.getPrioritizedClaim(world.getName(), blockPos, type);
    }

    @Nullable
    public Claim getPrioritizedClaim(@NotNull String worldName, @NotNull BlockPos blockPos, @Nullable ClaimType type) {
        Set<Claim> found = new HashSet<>(type == null ? this.storage.getClaimsAt(worldName, blockPos) : this.storage.lookup(type).getAt(worldName, blockPos));
        return this.getHighestPriorityClaim(worldName, found);
    }

    @Nullable
    private Claim getHighestPriorityClaim(@NotNull String worldName, @NotNull Collection<Claim> found) {
        return found.stream().max(Comparator.comparingInt(Claim::getPriority)).orElse(this.storage.getWilderness(worldName));
    }
}
