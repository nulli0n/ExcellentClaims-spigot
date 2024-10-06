package su.nightexpress.excellentclaims.claim;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.Action;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.Placeholders;
import su.nightexpress.excellentclaims.api.claim.*;
import su.nightexpress.excellentclaims.api.event.chunk.*;
import su.nightexpress.excellentclaims.api.event.region.RegionCreateEvent;
import su.nightexpress.excellentclaims.api.event.region.RegionCreatedEvent;
import su.nightexpress.excellentclaims.api.event.region.RegionRemoveEvent;
import su.nightexpress.excellentclaims.api.event.region.RegionRemovedEvent;
import su.nightexpress.excellentclaims.claim.impl.AbstractClaim;
import su.nightexpress.excellentclaims.claim.impl.ClaimedChunk;
import su.nightexpress.excellentclaims.claim.impl.ClaimedRegion;
import su.nightexpress.excellentclaims.claim.listener.FlagListener;
import su.nightexpress.excellentclaims.claim.listener.GenericListener;
import su.nightexpress.excellentclaims.config.Config;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.config.Perms;
import su.nightexpress.excellentclaims.flag.impl.list.BooleanFlag;
import su.nightexpress.excellentclaims.flag.list.PlayerFlags;
import su.nightexpress.excellentclaims.flag.type.ListMode;
import su.nightexpress.excellentclaims.flag.type.MaterialList;
import su.nightexpress.excellentclaims.selection.Selection;
import su.nightexpress.excellentclaims.util.*;
import su.nightexpress.excellentclaims.util.pos.BlockPos;
import su.nightexpress.excellentclaims.util.pos.ChunkPos;
import su.nightexpress.excellentclaims.util.pos.DirectionalPos;
import su.nightexpress.nightcore.language.entry.LangText;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.util.*;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ClaimManager extends AbstractManager<ClaimPlugin> {

    private final ClaimMap claimMap;

    public ClaimManager(@NotNull ClaimPlugin plugin) {
        super(plugin);
        this.claimMap = new ClaimMap();
    }

    @Override
    protected void onLoad() {
        this.loadClaims();

        this.addListener(new GenericListener(this.plugin, this));
        this.addListener(new FlagListener(this.plugin, this));

        this.addTask(plugin.createTask(this::saveClaims).setSecondsInterval(Config.GENERAL_SAVE_INTERVAL.get()));
    }

    @Override
    protected void onShutdown() {
        this.saveClaims();
        this.claimMap.clear();
    }

    public void saveClaims() {
        this.saveClaims(true);
    }

    public void saveClaims(boolean whenRequired) {
        this.getClaims().forEach(claim -> {
            if (whenRequired && !claim.isSaveRequired()) return;

            this.saveClaim(claim);
        });
    }

    public void saveClaim(@NotNull Claim claim) {
        claim.save();
        claim.setSaveRequired(false);
        plugin.debug("Claim saved on the disk: " + claim.getId());
    }

    @NotNull
    public ClaimMap getClaimMap() {
        return this.claimMap;
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
    }

    @NotNull
    private AbstractClaim initClaim(@NotNull ClaimType type, @NotNull File file) {
        return switch (type) {
            case CHUNK -> new ClaimedChunk(plugin, file);
            case REGION -> new ClaimedRegion(plugin, file);
        };
    }

    @NotNull
    private File getClaimFile(@NotNull String id, @NotNull ClaimType type, @NotNull World world) {
        return new File(this.getClaimsDirectory(type, world), id + ".yml");
    }

    @NotNull
    private <T extends AbstractClaim> T createClaim(@NotNull Player owner,
                                                    @NotNull String id,
                                                    @NotNull ClaimType type,
                                                    @NotNull World world,
                                                    @NotNull Function<File, T> supplier,
                                                    @NotNull Consumer<T> consumer) {
        File file = this.getClaimFile(id, type, world);
        T claim = supplier.apply(file);

        claim.setWorldName(world.getName());
        claim.setDisplayName(ClaimUtils.getDefaultName(id, owner, type));
        claim.setIcon(Config.getDefaultIcon(type));
        claim.setPriority(Config.getDefaultPriority(type));
        claim.setOwner(owner);
        consumer.accept(claim);
        claim.activate(world);
        claim.save();
        this.loadClaim(claim);

        return claim;
    }

    @NotNull
    private ClaimedChunk createChunkClaim(@NotNull Player player, @NotNull World world, @NotNull ChunkPos chunkPos) {
        String id = ClaimUtils.createChunkClaimID(player, chunkPos);
        return this.createClaim(player, id, ClaimType.CHUNK, world, file -> new ClaimedChunk(plugin, file), claimedChunk -> {
            claimedChunk.setSpawnLocation(DirectionalPos.from(world.getHighestBlockAt(chunkPos.getX(), chunkPos.getZ())));
            claimedChunk.getPositions().add(chunkPos);
        });
    }

    @NotNull
    private ClaimedRegion createRegionClaim(@NotNull Player player, @NotNull World world, @NotNull Cuboid cuboid, @NotNull String id) {
        return this.createClaim(player, id, ClaimType.REGION, world, file -> new ClaimedRegion(plugin, file), claimedRegion -> {
            claimedRegion.setSpawnLocation(DirectionalPos.from(cuboid.getCenter()));
            claimedRegion.setCuboid(cuboid);
        });
    }

    public boolean claimChunk(@NotNull Player player, @NotNull Location location, @Nullable String name) {
        World world = location.getWorld();
        if (world == null) {
            Lang.LAND_CLAIM_ERROR_INVALID_WORLD.getMessage().send(player);
            return false;
        }

        return this.claimChunk(player, world, BlockPos.from(location), name);
    }

    public boolean claimChunk(@NotNull Player player, @NotNull World world, @NotNull BlockPos blockPos, @Nullable String name) {
        if (!player.hasPermission(Perms.BYPASS_CHUNK_CLAIM_WORLD)) {
            Set<String> badWorlds = Config.LAND_DISABLED_WORLDS.get();
            if (badWorlds.contains(world.getName().toLowerCase())) {
                Lang.LAND_CLAIM_ERROR_BAD_WORLD.getMessage().send(player);
                return false;
            }
        }

        int maxRegions = this.getMaxClaimsAmount(player, ClaimType.CHUNK);
        if (maxRegions >= 0 && this.getClaimsAmount(player, ClaimType.CHUNK) >= maxRegions) {
            Lang.LAND_CLAIM_ERROR_MAX_AMOUNT.getMessage().replace(Placeholders.GENERIC_AMOUNT, NumberUtil.format(maxRegions)).send(player);
            return false;
        }

        ChunkPos chunkPos = ChunkPos.from(blockPos);
        ChunkClaim current = this.getChunkClaim(world, chunkPos);
        if (current != null) {
            LangText message = current.isOwner(player) ? Lang.LAND_CLAIM_ERROR_ALREADY_CLAIMED : Lang.LAND_CLAIM_ERROR_OCCUPIED;
            message.getMessage().send(player);
            return false;
        }

        if (!player.hasPermission(Perms.BYPASS_CHUNK_CLAIM_OVERLAP)) {
            boolean canOverlapWithRegions = Config.GENERAL_ALLOW_CHUNK_TO_REGION_OVERLAP.get();
            Set<RegionClaim> regions = this.getRegionClaims(world, chunkPos);
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

        ChunkClaimEvent claimEvent = new ChunkClaimEvent(player, world, chunkPos);
        this.plugin.getPluginManager().callEvent(claimEvent);
        if (claimEvent.isCancelled()) return false;

        ClaimedChunk claimedChunk = this.createChunkClaim(player, world, chunkPos);
        if (name != null) {
            claimedChunk.setDisplayName(StringUtil.capitalizeUnderscored(name));
        }

        Lang.LAND_CLAIM_SUCCESS.getMessage().replace(claimedChunk.replacePlaceholders()).send(player);

        ChunkClaimedEvent claimedEvent = new ChunkClaimedEvent(claimedChunk, player);
        this.plugin.getPluginManager().callEvent(claimedEvent);

        return true;
    }

    public boolean startMerge(@NotNull Player player, @NotNull MergeType type) {
        ChunkClaim claim = this.plugin.getClaimManager().getChunkClaim(player.getWorld(), ChunkPos.from(player.getLocation()));
        if (claim == null) {
            Lang.ERROR_NO_CHUNK.getMessage().send(player);
            return false;
        }
        return this.startMerge(player, type, claim);
    }

    public boolean startMerge(@NotNull Player player, @NotNull MergeType type, @NotNull ChunkClaim claim) {
        if (!claim.hasPermission(player, type == MergeType.MERGE ? ClaimPermission.MERGE_CLAIM : ClaimPermission.SEPARATE_CLAIM)) {
            Lang.ERROR_NO_CLAIM_PERMISSION.getMessage().send(player);
            return false;
        }
        if (type == MergeType.SEPARATE && claim.isSingle()) {
            Lang.LAND_SEPARATE_ERROR_NOT_MERGED.getMessage().send(player);
            return false;

        }
        (type == MergeType.MERGE ? Lang.LAND_MERGE_INFO : Lang.LAND_SEPARATE_INFO).getMessage().send(player);

        this.plugin.getSelectionManager().addInMerge(player, type, claim);
        return true;
    }

    public boolean separateChunk(@NotNull Player player, @NotNull ChunkClaim source, @NotNull BlockPos blockPos) {
        return this.separateChunk(player, source, ChunkPos.from(blockPos));
    }

    public boolean separateChunk(@NotNull Player player, @NotNull ChunkClaim source, @NotNull ChunkPos chunkPos) {
        World world = player.getWorld();
        ChunkClaim target = this.getChunkClaim(world, chunkPos);
        if (target == null) {
            Lang.LAND_SEPARATE_ERROR_NOTHING.getMessage().send(player);
            return false;
        }
        if (target != source) {
            Lang.LAND_SEPARATE_ERROR_DIFFERENT.getMessage().send(player);
            return false;
        }
        if (source.isSingle()) {
            Lang.LAND_SEPARATE_ERROR_NOT_MERGED.getMessage().send(player);
            return false;
        }

        ChunkSeparateEvent separateEvent = new ChunkSeparateEvent(source, player, chunkPos);
        this.plugin.getPluginManager().callEvent(separateEvent);
        if (separateEvent.isCancelled()) return false;

        source.getPositions().remove(chunkPos);
        source.save();

        this.claimMap.update(source);

        ClaimedChunk claimedChunk = this.createChunkClaim(player, world, chunkPos);
        ClaimUtils.inheritanceSettings(claimedChunk, source);

        Lang.LAND_SEPARATE_SUCCESS.getMessage().replace(claimedChunk.replacePlaceholders()).send(player);

        ChunkSeparatedEvent separatedEvent = new ChunkSeparatedEvent(source, player, claimedChunk);
        this.plugin.getPluginManager().callEvent(separatedEvent);

        return true;
    }

    public boolean mergeChunk(@NotNull Player player, @NotNull ChunkClaim source, @NotNull BlockPos blockPos) {
        ChunkClaim target = this.getChunkClaim(player.getWorld(), blockPos);
        if (target == null) {
            Lang.LAND_MERGE_ERROR_NOTHING.getMessage().send(player);
            return false;
        }

        return this.mergeChunk(player, source, target);
    }

    public boolean mergeChunk(@NotNull Player player, @NotNull ChunkClaim source, @NotNull ChunkClaim target) {
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

        int maxChunks = plugin.getClaimManager().getMaxClaimChunksAmount(player);
        if (maxChunks >= 0 && target.getChunksAmount() >= maxChunks) {
            Lang.LAND_MERGE_ERROR_MAX_CHUNKS.getMessage().replace(Placeholders.GENERIC_AMOUNT, maxChunks).send(player);
            return false;
        }

        ChunkMergeEvent mergeEvent = new ChunkMergeEvent(source, player, target);
        this.plugin.getPluginManager().callEvent(mergeEvent);
        if (mergeEvent.isCancelled()) return false;

        source.getMembers().forEach(target::addMember);
        target.getPositions().addAll(source.getPositions());
        target.save();

        this.deleteClaim(source);
        this.claimMap.update(target);

        Lang.LAND_MERGE_SUCCESS.getMessage().replace(target.replacePlaceholders()).send(player);

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

        String id = StringUtil.lowerCaseUnderscoreStrict(name);
        if (id.isBlank()) {
            Lang.REGION_CREATE_ERROR_BAD_NAME.getMessage().send(player);
            return false;
        }

        int maxRegions = this.getMaxClaimsAmount(player, ClaimType.REGION);
        if (maxRegions >= 0 && this.getClaimsAmount(player, ClaimType.REGION) >= maxRegions) {
            Lang.REGION_CREATE_ERROR_MAX_AMOUNT.getMessage().replace(Placeholders.GENERIC_AMOUNT, NumberUtil.format(maxRegions)).send(player);
            return false;
        }

        RegionClaim current = this.getRegionClaim(world, id);
        if (current != null) {
            Lang.REGION_CREATE_ERROR_ALREADY_EXISTS.getMessage().send(player);
            return false;
        }

        if (!player.hasPermission(Perms.BYPASS_REGION_CLAIM_OVERLAP)) {
            boolean canOverlapWithChunks = Config.GENERAL_ALLOW_REGION_TO_CHUNK_OVERLAP.get();
            Set<ChunkClaim> chunks = this.getChunkClaims(world, cuboid);
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

            Set<RegionClaim> regions = this.getRegionClaims(world, cuboid);
            if (!regions.isEmpty()) {
                if (regions.stream().anyMatch(region -> !region.isOwner(player))) {
                    Lang.REGION_CREATE_ERROR_OVERLAP_FOREIGN_REGION.getMessage().send(player);
                    return false;
                }
            }
        }

        RegionCreateEvent createEvent = new RegionCreateEvent(player, world, cuboid, id);
        this.plugin.getPluginManager().callEvent(createEvent);
        if (createEvent.isCancelled()) return false;

        ClaimedRegion claimedRegion = this.createRegionClaim(player, world, cuboid, id);

        Lang.REGION_CREATE_SUCCESS.getMessage().replace(claimedRegion.replacePlaceholders()).send(player);

        RegionCreatedEvent createdEvent = new RegionCreatedEvent(claimedRegion, player);
        this.plugin.getPluginManager().callEvent(createdEvent);

        return true;
    }

    public boolean removeRegion(@NotNull Player player, @NotNull String name) {
        return this.removeRegion(player, player.getWorld(), name);
    }

    public boolean removeRegion(@NotNull Player player, @NotNull World world, @NotNull String name) {
        RegionClaim claim = this.getRegionClaim(world, name);
        if (claim == null) {
            Lang.REGION_REMOVE_ERROR_NO_REGION.getMessage().send(player);
            return false;
        }

        return this.removeRegion(player, claim);
    }

    public boolean removeRegion(@NotNull Player player, @NotNull RegionClaim claim) {
//        if (!claim.isOwner(player)) {
//            Lang.REGION_ERROR_NOT_OWNER.getMessage().replace(claim.replacePlaceholders()).send(player);
//            return false;
//        }
        if (!claim.hasPermission(player, ClaimPermission.REMOVE_CLAIM)) {
            Lang.ERROR_NO_CLAIM_PERMISSION.getMessage().send(player);
            return false;
        }

        RegionRemoveEvent removeEvent = new RegionRemoveEvent(claim, player);
        this.plugin.getPluginManager().callEvent(removeEvent);
        if (removeEvent.isCancelled()) return false;

        this.deleteClaim(claim);

        Lang.REGION_REMOVE_SUCCESS.getMessage().replace(claim.replacePlaceholders()).send(player);

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

        return this.unclaimChunk(player, world, ChunkPos.from(location));
    }

    public boolean unclaimChunk(@NotNull Player player, @NotNull World world, @NotNull ChunkPos chunkPos) {
        ChunkClaim claim = this.getChunkClaim(world, chunkPos);
        if (claim == null) {
            Lang.LAND_UNCLAIM_ERROR_NOTHING.getMessage().send(player);
            return false;
        }

        return this.unclaimChunk(player, claim);
    }

    public boolean unclaimChunk(@NotNull Player player, @NotNull ChunkClaim claim) {
        if (!claim.hasPermission(player, ClaimPermission.REMOVE_CLAIM)) {
            Lang.ERROR_NO_CLAIM_PERMISSION.getMessage().send(player);
            return false;
        }

        ChunkUnclaimEvent unclaimEvent = new ChunkUnclaimEvent(claim, player);
        this.plugin.getPluginManager().callEvent(unclaimEvent);
        if (unclaimEvent.isCancelled()) return false;

        this.deleteClaim(claim);

        claim.getPositions().clear();

        Lang.LAND_UNCLAIM_SUCCESS.getMessage().replace(claim.replacePlaceholders()).send(player);

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

        claim.setSpawnLocation(DirectionalPos.from(location));
        claim.setSaveRequired(true);

        Lang.CLAIM_SET_SPAWN_SUCCESS.getMessage().replace(claim.replacePlaceholders()).send(player);

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
            String raw = NightMessage.stripAll(name);
            int maxLength = Config.GENERAL_MAX_NAME_LENGTH.get();
            if (raw.length() > maxLength) {
                Lang.CLAIM_RENAME_ERROR_TOO_LONG.getMessage().replace(Placeholders.GENERIC_AMOUNT, maxLength).send(player);
                return false;
            }
        }

        claim.setDisplayName(name);
        claim.setSaveRequired(true);
        Lang.CLAIM_RENAME_SUCCESS.getMessage().replace(claim.replacePlaceholders()).send(player);
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
            String raw = NightMessage.stripAll(description);
            int maxLength = Config.GENERAL_MAX_DESCRIPTION_LENGTH.get();
            if (raw.length() > maxLength) {
                Lang.CLAIM_DESCRIPTION_ERROR_TOO_LONG.getMessage().replace(Placeholders.GENERIC_AMOUNT, maxLength).send(player);
                return false;
            }
        }

        claim.setDescription(description);
        claim.setSaveRequired(true);
        Lang.CLAIM_DESCRIPTION_SUCCESS.getMessage().replace(claim.replacePlaceholders()).send(player);
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
            Lang.CLAIM_TRANSFER_ERROR_YOURSELF.getMessage().replace(claim.replacePlaceholders()).send(player);
            return false;
        }

        ClaimType claimType = claim.getType();
        int claimsAmount = this.getClaimsAmount(target, claimType);
        int maxClaims = this.getMaxClaimsAmount(target, claimType);
        if (maxClaims >= 0 && claimsAmount >= maxClaims) {
            Lang.CLAIM_TRANSFER_ERROR_TOO_MANY.getMessage().replace(Placeholders.forPlayer(target)).send(player);
            return false;
        }

        claim.removeMember(target);
        claim.addMember(player);
        claim.setOwner(UserInfo.of(target));
        claim.setSaveRequired(true);
        this.claimMap.update(claim);

        Lang.CLAIM_TRANSFER_SUCCESS.getMessage().replace(claim.replacePlaceholders()).replace(Placeholders.forPlayer(target)).send(player);
        Lang.CLAIM_TRANSFER_NOTIFY.getMessage().replace(claim.replacePlaceholders()).replace(Placeholders.forPlayer(player)).send(target);

        return true;
    }

    private void loadClaim(@NotNull AbstractClaim claim) {
        if (claim.load()) {
            claim.reactivate();
            this.claimMap.add(claim);
        }
        else this.plugin.error("Claim not loaded: '" + claim.getFile().getPath() + "'!");
    }

    private void deleteClaim(@NotNull Claim claim) {
        claim.deactivate();
        claim.getFile().delete();
        this.claimMap.remove(claim);
    }



    public boolean canUseBlock(@NotNull Entity entity, @NotNull Block block, @Nullable Action action) {
        Material blockType = block.getType();
        Location location = block.getLocation();
        BlockState blockState = block.getState();

        // First of all check if block is falls under specific flag.
        BooleanFlag explicitFlag = null;
        ClaimPermission explicitPerm = ClaimPermission.BLOCK_INTERACT;

        if (Tag.PRESSURE_PLATES.isTagged(blockType) && (action == Action.PHYSICAL || action == null)) {
            explicitFlag = PlayerFlags.USE_PLATES;
        }
        else if ((Tag.BUTTONS.isTagged(blockType) || blockType == Material.LEVER) && action != Action.PHYSICAL) {
            explicitFlag = PlayerFlags.USE_BUTTONS;
        }
        else if ((Tag.DOORS.isTagged(blockType) || Tag.TRAPDOORS.isTagged(blockType) || Tag.FENCE_GATES.isTagged(blockType)) && action != Action.PHYSICAL) {
            explicitFlag = PlayerFlags.USE_DOORS;
        }
        else if (blockType == Material.TURTLE_EGG && (action == Action.PHYSICAL || action == null)) {
            explicitFlag = PlayerFlags.BLOCK_TRAMPLING;
        }
        else if (blockType == Material.TRIPWIRE && (action == Action.PHYSICAL || action == null)) {
            explicitFlag = PlayerFlags.USE_TRIPWIRES;
        }
        else if (blockState instanceof Container container) {
            explicitPerm = ClaimPermission.CONTAINERS;

            if (container instanceof Chest) {
                explicitFlag = PlayerFlags.CHEST_ACCESS;
            }
            else explicitFlag = PlayerFlags.CONTAINER_ACCESS;
        }


        // If no flag present and block is not interactable, then allow use.
        if (explicitFlag == null && !blockType.isInteractable()) return true;

        Player user = null;
        BlockProjectileSource blockSource = null;

        if (entity instanceof Player player) {
            user = player;
        }
        else if (entity instanceof Projectile projectile) {
            ProjectileSource source = projectile.getShooter();
            if (source instanceof Player player) user = player;
            else if (source instanceof BlockProjectileSource bs) blockSource = bs;
        }

        if (user != null) {
            if (plugin.getMemberManager().isAdminMode(user)) return true;
        }

        // If there is a flag related to the block type, check it then.
        if (explicitFlag != null) {
            Location originLoc = blockSource == null ? location : blockSource.getBlock().getLocation();
            Relation relation = this.getRelation(originLoc, location);
            if (relation.checkTargetFlag(explicitFlag)) return true;

            if (user != null) {
                return relation.isTargetMember(user) && relation.hasTargetPermission(user, explicitPerm);
            }

            return relation.getType() != RelationType.INVADE;
        }

        Claim claim = this.getPrioritizedClaim(location);
        if (claim == null) return true;

        if (user != null) {
            if (claim.isOwnerOrMember(user)) return true;
        }

        // Otherwise check that interactable block for generic block interact flags.
        ListMode mode = claim.getFlag(PlayerFlags.BLOCK_INTERACT_MODE);
        MaterialList materialList = claim.getFlag(PlayerFlags.BLOCK_INTERACT_LIST);
        if (materialList.isAllowed(mode, blockType)) return true;

        return user != null && claim.isOwnerOrMember(user) && claim.hasPermission(user, explicitPerm);
    }

    public boolean canBreak(@NotNull Player player, @NotNull Block block) {
        return this.canBreak(player, block.getLocation());
    }

    public boolean canBreak(@NotNull Player player, @NotNull Location location) {
        if (plugin.getMemberManager().isAdminMode(player)) return true;

        Relation relation = this.getRelation(location);
        return relation.hasTargetPermission(player, ClaimPermission.BUILDING) || relation.checkTargetFlag(PlayerFlags.BLOCK_BREAK);
    }

    public boolean canBuild(@NotNull Player player, @NotNull Block block) {
        return this.canBuild(player, block.getLocation());
    }

    public boolean canBuild(@NotNull Player player, @NotNull Location location) {
        if (plugin.getMemberManager().isAdminMode(player)) return true;

        Relation relation = this.getRelation(location);
        return relation.hasTargetPermission(player, ClaimPermission.BUILDING) || relation.checkTargetFlag(PlayerFlags.BLOCK_PLACE);
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

    public int getMaxClaimChunksAmount(@NotNull Player player) {
        return Config.LAND_CHUNKS_AMOUNT_PER_RANK.get().getGreatestOrNegative(player);
    }

    public int getMaxClaimsAmount(@NotNull Player player, @NotNull ClaimType type) {
        RankMap<Integer> rankMap = switch (type) {
            case CHUNK -> Config.LAND_AMOUNT_PER_RANK.get();
            case REGION -> Config.REGION_AMOUNT_PER_RANK.get();
        };

        return rankMap.getGreatestOrNegative(player);
    }

    public int getClaimsAmount(@NotNull Player player, @NotNull ClaimType type) {
        return this.getClaimsAmount(player.getUniqueId(), type);
    }

    public int getClaimsAmount(@NotNull UUID playerId, @NotNull ClaimType type) {
        return this.getClaims(playerId, type).size();
    }

    // Check is location is occipied by a claim or region.

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

    public boolean isClaimed(@NotNull String worldName, @NotNull BlockPos pos) {
        return this.getChunkClaim(worldName, pos) != null || !this.getRegionClaims(worldName, pos).isEmpty();
    }

    // Check is chunk is claimed by chunk position.

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
        return this.getChunkClaim(worldName, pos) != null;
    }


    // Check is there are one or more regions at given location.

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
        return !this.getRegionClaims(worldName, pos).isEmpty();
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
    public Claim getPrioritizedClaim(@NotNull Block block) {
        //return this.getPrioritizedClaim(block.getWorld(), BlockPos.from(block));
        return this.getPrioritizedClaim(block, null);
    }

    @Nullable
    public Claim getPrioritizedClaim(@NotNull Block block, @Nullable ClaimType type) {
        return this.getPrioritizedClaim(block.getWorld(), BlockPos.from(block), type);
    }

    @Nullable
    public Claim getPrioritizedClaim(@NotNull Location location) {
//        World world = location.getWorld();
//        if (world == null) return null;
//
//        return this.getPrioritizedClaim(world, BlockPos.from(location));
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
        //return this.getPrioritizedClaim(world.getName(), blockPos);
        return this.getPrioritizedClaim(world, blockPos, null);
    }

    @Nullable
    public Claim getPrioritizedClaim(@NotNull World world, @NotNull BlockPos blockPos, @Nullable ClaimType type) {
        return this.getPrioritizedClaim(world.getName(), blockPos, type);
    }

    @Nullable
    public Claim getPrioritizedClaim(@NotNull String worldName, @NotNull BlockPos blockPos, @Nullable ClaimType type) {
        if (type != null) {
            if (type == ClaimType.CHUNK) return this.getChunkClaim(worldName, blockPos);

            return this.getRegionClaims(worldName, blockPos).stream().max(Comparator.comparingInt(Claim::getPriority)).orElse(null);
        }

        Set<Claim> set = new HashSet<>(this.getRegionClaims(worldName, blockPos));

        ChunkClaim chunkClaim = this.getChunkClaim(worldName, blockPos);
        if (chunkClaim != null) {
            set.add(chunkClaim);
        }

        return set.stream().max(Comparator.comparingInt(Claim::getPriority)).orElse(null);
    }

    // Get all (in)active claims.

    @NotNull
    public Set<Claim> getActiveClaims() {
        return this.getClaims().stream().filter(Claim::isActive).collect(Collectors.toSet());
    }

    @NotNull
    public Set<Claim> getInactiveClaims() {
        return this.getClaims().stream().filter(Predicate.not(Claim::isActive)).collect(Collectors.toSet());
    }

    // Get all claims.

    @NotNull
    public Set<Claim> getClaims() {
        Set<Claim> set = new HashSet<>();
        set.addAll(this.getRegionClaims());
        set.addAll(this.getChunkClaims());
        return set;
    }

    @NotNull
    public Set<Claim> getClaims(@NotNull ClaimType type) {
        return this.getClaims(type, claim -> true);
    }

    @NotNull
    public Set<Claim> getClaims(@NotNull ClaimType type, @NotNull Predicate<Claim> predicate) {
        return (type == ClaimType.CHUNK ? this.getChunkClaims() : this.getRegionClaims()).stream().filter(predicate).collect(Collectors.toCollection(HashSet::new));
    }

    @NotNull
    public Set<Claim> getClaims(@NotNull World world) {
        return this.getClaims(world.getName());
    }

    @NotNull
    public Set<Claim> getClaims(@NotNull String worldName) {
        Set<Claim> set = new HashSet<>();
        set.addAll(this.getRegionClaims(worldName));
        set.addAll(this.getChunkClaims(worldName));
        return set;
    }

    @NotNull
    public Set<Claim> getClaims(@NotNull World world, @NotNull ClaimType type) {
        return this.getClaims(world.getName(), type);
    }

    @NotNull
    public Set<Claim> getClaims(@NotNull String worldName, @NotNull ClaimType type) {
        return new HashSet<>(type == ClaimType.CHUNK ? this.getChunkClaims(worldName) : this.getRegionClaims(worldName));
    }

//    @NotNull
//    public Set<Claim> getClaims(@NotNull World world, @NotNull BlockPos blockPos, @NotNull ClaimType type) {
//        return this.getClaims(world.getName(), blockPos, type);
//    }
//
//    @NotNull
//    public Set<Claim> getClaims(@NotNull String worldName, @NotNull BlockPos blockPos, @NotNull ClaimType type) {
//        if (type == ClaimType.CHUNK) return Lists.newSet(this.getChunkClaim(worldName, blockPos));
//
//        return new HashSet<>(this.getRegionClaims(worldName, blockPos));
//    }

    @NotNull
    public Set<Claim> getClaims(@NotNull World world, @NotNull Cuboid cuboid) {
        return this.getClaims(world.getName(), cuboid);
    }

    @NotNull
    public Set<Claim> getClaims(@NotNull String worldName, @NotNull Cuboid cuboid) {
        Set<Claim> set = new HashSet<>();
        set.addAll(this.getRegionClaims(worldName, cuboid));
        set.addAll(this.getChunkClaims(worldName, cuboid));
        return set;
    }

    // Get claims by owner UUID.

    @NotNull
    public Set<Claim> getClaims(@NotNull Player player) {
        return this.getClaims(player.getUniqueId());
    }

    @NotNull
    public Set<Claim> getClaims(@NotNull UUID playerId) {
        Set<Claim> set = new HashSet<>();
        set.addAll(this.getRegionClaims(playerId));
        set.addAll(this.getChunkClaims(playerId));
        return set;
    }

    @NotNull
    public Set<Claim> getClaims(@NotNull Player player, @NotNull ClaimType type) {
        return this.getClaims(player.getUniqueId(), type);
    }

    @NotNull
    public Set<Claim> getClaims(@NotNull UUID playerId, @NotNull ClaimType type) {
        return new HashSet<>(type == ClaimType.CHUNK ? this.getChunkClaims(playerId) : this.getRegionClaims(playerId));
    }

    @NotNull
    public Set<Claim> getClaims(@NotNull Player player, @NotNull World world) {
        return this.getClaims(player.getUniqueId(), world);
    }

    @NotNull
    public Set<Claim> getClaims(@NotNull UUID playerId, @NotNull World world) {
        return this.getClaims(playerId, world.getName());
    }

    @NotNull
    public Set<Claim> getClaims(@NotNull UUID playerId, @NotNull String worldName) {
        Set<Claim> set = new HashSet<>();
        set.addAll(this.getRegionClaims(playerId, worldName));
        set.addAll(this.getChunkClaims(playerId, worldName));
        return set;
    }

    // Get claims by locations.

    @NotNull
    public Set<Claim> getClaims(@NotNull Block block) {
        return this.getClaims(block.getWorld(), BlockPos.from(block));
    }

    @NotNull
    public Set<Claim> getClaims(@NotNull Location location) {
        World world = location.getWorld();
        if (world == null) return Collections.emptySet();

        return this.getClaims(world, BlockPos.from(location));
    }

    @NotNull
    public Set<Claim> getClaims(@NotNull World world, @NotNull BlockPos blockPos) {
        return this.getClaims(world.getName(), blockPos);
    }

    @NotNull
    public Set<Claim> getClaims(@NotNull String worldName, @NotNull BlockPos blockPos) {
        Set<Claim> set = new HashSet<>(this.getRegionClaims(worldName, blockPos));

        ChunkClaim chunkClaim = this.getChunkClaim(worldName, blockPos);
        if (chunkClaim != null) {
            set.add(chunkClaim);
        }

        return set;
    }

    // Get all chunk claims.

    @NotNull
    public Set<ChunkClaim> getChunkClaims() {
        Set<ChunkClaim> set = new HashSet<>();
        this.claimMap.getChunkClaimMap().values().forEach(map -> set.addAll(map.values()));
        return set;
    }

    @NotNull
    public Set<ChunkClaim> getChunkClaims(@NotNull World world) {
        return this.getChunkClaims(world.getName());
    }

    @NotNull
    public Set<ChunkClaim> getChunkClaims(@NotNull String worldName) {
        return new HashSet<>(this.claimMap.getChunkClaimMap(worldName).values());
    }

    // Get chunk claims by owner UUID.

    @NotNull
    public Set<ChunkClaim> getChunkClaims(@NotNull Player player) {
        return this.getChunkClaims(player.getUniqueId());
    }

    @NotNull
    public Set<ChunkClaim> getChunkClaims(@NotNull UUID playerId) {
        Set<ChunkClaim> set = new HashSet<>();
        this.claimMap.getPlayerChunks(playerId).values().forEach(set::addAll);
        return set;
    }

    @NotNull
    public Set<ChunkClaim> getChunkClaims(@NotNull UUID playerId, @NotNull World world) {
        return this.getChunkClaims(playerId, world.getName());
    }

    @NotNull
    public Set<ChunkClaim> getChunkClaims(@NotNull UUID playerId, @NotNull String worldName) {
        return new HashSet<>(this.claimMap.getPlayerChunks(playerId, worldName));
    }

    // Get chunk claims by locations.

    @NotNull
    public Set<ChunkClaim> getChunkClaims(@NotNull World world, @NotNull Cuboid cuboid) {
        return this.getChunkClaims(world.getName(), cuboid);
    }

    @NotNull
    public Set<ChunkClaim> getChunkClaims(@NotNull String worldName, @NotNull Cuboid cuboid) {
        Set<ChunkClaim> set = new HashSet<>();
        cuboid.getIntersectingChunkPositions().forEach(chunkPos -> {
            ChunkClaim claim = this.getChunkClaim(worldName, chunkPos);
            if (claim != null) set.add(claim);
        });
        return set;
    }

    // Get exact chunk claim by chunk position.

    @Nullable
    public ChunkClaim getChunkClaim(@NotNull Location location) {
        World world = location.getWorld();
        if (world == null) return null;

        return this.getChunkClaim(world, ChunkPos.from(location));
    }

    @Nullable
    public ChunkClaim getChunkClaim(@NotNull Chunk chunk) {
        return this.getChunkClaim(chunk.getWorld(), ChunkPos.from(chunk));
    }

    @Nullable
    public ChunkClaim getChunkClaim(@NotNull World world, int x, int z) {
        return this.getChunkClaim(world, new ChunkPos(x, z));
    }

    @Nullable
    public ChunkClaim getChunkClaim(@NotNull World world, @NotNull BlockPos blockPos) {
        return this.getChunkClaim(world.getName(), ChunkPos.from(blockPos));
    }

    @Nullable
    public ChunkClaim getChunkClaim(@NotNull String worldName, @NotNull BlockPos blockPos) {
        return this.getChunkClaim(worldName, ChunkPos.from(blockPos));
    }

    @Nullable
    public ChunkClaim getChunkClaim(@NotNull World world, @NotNull ChunkPos chunkPos) {
        return this.getChunkClaim(world.getName(), chunkPos);
    }

    @Nullable
    public ChunkClaim getChunkClaim(@NotNull String worldName, @NotNull ChunkPos chunkPos) {
        return this.claimMap.getChunkClaimMap(worldName).get(chunkPos);
    }



    // Get region names.

    @NotNull
    public List<String> getRegionNames(@NotNull World world) {
        return this.getRegionNames(world.getName());
    }

    @NotNull
    public List<String> getRegionNames(@NotNull String worldName) {
        return this.getRegionNames(worldName, claim -> true);
    }

    @NotNull
    public List<String> getRegionNames(@NotNull Player player, @NotNull ClaimPermission permission) {
        return this.getRegionNames(player, claim -> claim.hasPermission(player, permission));
    }

    @NotNull
    public List<String> getRegionNames(@NotNull Player player, @NotNull Predicate<Claim> predicate) {
        return this.getRegionNames(player.getWorld(), predicate);
    }

    @NotNull
    public List<String> getRegionNames(@NotNull World world, @NotNull Predicate<Claim> predicate) {
        return this.getRegionNames(world.getName(), predicate);
    }

    @NotNull
    public List<String> getRegionNames(@NotNull String worldName, @NotNull Predicate<Claim> predicate) {
        return this.getRegionClaims(worldName).stream().filter(predicate).map(Claim::getId).toList();
    }



    // Get all region claims.

    @NotNull
    public Set<RegionClaim> getRegionClaims() {
        Set<RegionClaim> set = new HashSet<>();
        this.claimMap.getRegionClaimMap().values().forEach(map -> set.addAll(map.values()));
        return set;
    }

    @NotNull
    public Set<RegionClaim> getRegionClaims(@NotNull World world) {
        return this.getRegionClaims(world.getName());
    }

    @NotNull
    public Set<RegionClaim> getRegionClaims(@NotNull String worldName) {
        return new HashSet<>(this.claimMap.getRegionClaimMap(worldName).values());
    }

    // Get region claims by owner UUID.

    @NotNull
    public Set<RegionClaim> getRegionClaims(@NotNull Player player) {
        return this.getRegionClaims(player.getUniqueId());
    }

    @NotNull
    public Set<RegionClaim> getRegionClaims(@NotNull UUID playerId) {
        Set<RegionClaim> set = new HashSet<>();
        this.claimMap.getPlayerRegions(playerId).values().forEach(set::addAll);
        return set;
    }

    @NotNull
    public Set<RegionClaim> getRegionClaims(@NotNull UUID playerId, @NotNull World world) {
        return this.getRegionClaims(playerId, world.getName());
    }

    @NotNull
    public Set<RegionClaim> getRegionClaims(@NotNull UUID playerId, @NotNull String worldName) {
        return new HashSet<>(this.claimMap.getPlayerRegions(playerId, worldName));
    }

    // Get region claims by locations.

    @NotNull
    public Set<RegionClaim> getRegionClaims(@NotNull Location location) {
        World world = location.getWorld();
        if (world == null) return Collections.emptySet();

        return this.getRegionClaims(world, ChunkPos.from(location));
    }

    @NotNull
    public Set<RegionClaim> getRegionClaims(@NotNull World world, @NotNull Cuboid cuboid) {
        return this.getRegionClaims(world.getName(), cuboid);
    }

    @NotNull
    public Set<RegionClaim> getRegionClaims(@NotNull String worldName, @NotNull Cuboid cuboid) {
        Set<RegionClaim> set = new HashSet<>();
        cuboid.getIntersectingChunkPositions().forEach(chunkPos -> {
            this.getRegionClaims(worldName, chunkPos).forEach(region -> {
                if (region.getCuboid().isIntersectingWith(cuboid)) {
                    set.add(region);
                }
            });
        });
        return set;
    }

    @NotNull
    public Set<RegionClaim> getRegionClaims(@NotNull Block block) {
        return this.getRegionClaims(block.getWorld(), BlockPos.from(block));
    }

    @NotNull
    public Set<RegionClaim> getRegionClaims(@NotNull World world, @NotNull BlockPos blockPos) {
        return this.getRegionClaims(world.getName(), blockPos);
    }

    @NotNull
    public Set<RegionClaim> getRegionClaims(@NotNull String worldName, @NotNull BlockPos blockPos) {
        Set<RegionClaim> set = this.getRegionClaims(worldName, ChunkPos.from(blockPos));
        set.removeIf(claim -> !claim.isInside(blockPos));
        return set;
    }

    // Get region claims by chunk position.

    @NotNull
    public Set<RegionClaim> getRegionClaims(@NotNull World world, int chunkX, int chunkZ) {
        return this.getRegionClaims(world.getName(), new ChunkPos(chunkX, chunkZ));
    }

    @NotNull
    public Set<RegionClaim> getRegionClaims(@NotNull World world, @NotNull ChunkPos chunkPos) {
        return this.getRegionClaims(world.getName(), chunkPos);
    }

    @NotNull
    public Set<RegionClaim> getRegionClaims(@NotNull String worldName, @NotNull ChunkPos chunkPos) {
        return new HashSet<>(this.claimMap.getRegionsByChunk(worldName, chunkPos));
    }

    // Get exact region by ID.

    @Nullable
    public RegionClaim getRegionClaim(@NotNull World world, @NotNull String id) {
        return this.getRegionClaim(world.getName(), id);
    }

    @Nullable
    public RegionClaim getRegionClaim(@NotNull String worldName, @NotNull String id) {
        return this.claimMap.getRegionClaimMap(worldName).get(id);
    }
}
