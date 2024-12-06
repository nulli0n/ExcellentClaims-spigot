package su.nightexpress.excellentclaims.selection;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Orientable;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.*;
import su.nightexpress.excellentclaims.config.Config;
import su.nightexpress.excellentclaims.config.Keys;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.hook.Hooks;
import su.nightexpress.excellentclaims.selection.listener.SelectionListener;
import su.nightexpress.excellentclaims.selection.type.ItemType;
import su.nightexpress.excellentclaims.selection.visual.HighlightType;
import su.nightexpress.excellentclaims.selection.visual.Tracker;
import su.nightexpress.excellentclaims.selection.visual.highlight.BlockHighlighter;
import su.nightexpress.excellentclaims.selection.visual.highlight.BlockPacketsHighlighter;
import su.nightexpress.excellentclaims.selection.visual.highlight.BlockProtocolHighlighter;
import su.nightexpress.excellentclaims.util.ClaimUtils;
import su.nightexpress.excellentclaims.util.pos.BlockPos;
import su.nightexpress.excellentclaims.util.pos.ChunkPos;
import su.nightexpress.excellentclaims.util.Cuboid;
import su.nightexpress.excellentclaims.util.DimensionType;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.excellentclaims.Placeholders;
import su.nightexpress.nightcore.util.*;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class SelectionManager extends AbstractManager<ClaimPlugin> {

    private final Map<UUID, Selection>  selectionMap;
    private final Map<UUID, Tracker>   chunkTracker;
    private final Map<UUID, LandClaim> mergeMap;

    private BlockHighlighter highlighter;

    public SelectionManager(@NotNull ClaimPlugin plugin) {
        super(plugin);
        this.selectionMap = new HashMap<>();
        this.chunkTracker = new ConcurrentHashMap<>();
        this.mergeMap = new HashMap<>();
    }

    @Override
    protected void onLoad() {
        this.loadHighlighter();

        this.addListener(new SelectionListener(this.plugin, this));
        this.addAsyncTask(this::displaySelectionInfo, Config.GENERAL_SELECTION_INFO_RATE.get());
    }

    @Override
    protected void onShutdown() {
        if (this.highlighter != null) {
            this.highlighter.clear();
            this.highlighter = null;
        }

        this.plugin.getServer().getOnlinePlayers().forEach(this::removeAll);
    }

    private void loadHighlighter() {
        if (Plugins.isInstalled(Hooks.PACKET_EVENTS)) {
            this.highlighter = new BlockPacketsHighlighter(this.plugin);
        }
        else if (Plugins.isInstalled(Hooks.PROTOCOL_LIB)) {
            this.highlighter = new BlockProtocolHighlighter(this.plugin);
        }
        else return;

        this.addAsyncTask(this::highlightBounds, Config.HIGHLIGHT_CHUNK_UPDATE_RATE.get());
    }

    // TODO Region bounds command

    /**
     * Reset current player's chunk position to force trigger chunk bounds render on next task execution.
     */
    public void resetTrackedChunks() {
        this.chunkTracker.values().forEach(tracker -> tracker.setPreviousPos(null));
    }

    /**
     * Creates cuboid object for a chunk.
     */
    @NotNull
    private Cuboid getCuboid(@NotNull Chunk chunk, int y, int verticalOffset) {
        int x = chunk.getX() << 4;
        int z = chunk.getZ() << 4;

        BlockPos min = new BlockPos(x, y, z);
        BlockPos max = new BlockPos(x + 15, y + verticalOffset, z + 15);
        return new Cuboid(min, max);
    }

    public void highlightBounds() {
        this.chunkTracker.forEach((uuid, tracker) -> {
            Player player = this.plugin.getServer().getPlayer(uuid);
            if (player == null) return;

            Location playerLocation = player.getLocation();
            BlockPos oldPlayerPos = tracker.getPreviousPos();

            // Check if we should skip render.
            if (oldPlayerPos != null && !oldPlayerPos.isEmpty()) {
                // Always shift rendering bounds to player's Y position.
                if (Math.abs(oldPlayerPos.getY() - playerLocation.getBlockY()) < 3) {
                    // Otherwise render only if player went to other chunk.
                    ChunkPos currentChunkPos = ChunkPos.from(playerLocation);
                    ChunkPos oldChunkPos = ChunkPos.from(oldPlayerPos);

                    if (currentChunkPos.equals(oldChunkPos)) return;
                }
            }

            if (tracker.isChunkBounds()) {
                this.highlightChunkBounds(player);
            }
            if (tracker.isSelection()) {
                this.highlightSelection(player);
            }

            tracker.setPreviousPos(BlockPos.from(playerLocation));
        });
    }

    public void displaySelectionInfo() {
        this.selectionMap.forEach((uuid, selection) -> {
            Player player = plugin.getServer().getPlayer(uuid);
            if (player == null) return;

            this.displaySelectionInfo(player, selection);
        });
    }

    public void displaySelectionInfo(@NotNull Player player, @NotNull Selection selection) {
        Cuboid cuboid = selection.toCuboid();
        if (cuboid == null) return;

        int max = ClaimUtils.getRegionBlocksLimit(player);

        Lang.SELECTION_REGION_INFO.getMessage().send(player, replacer -> replacer
            .replace(Placeholders.GENERIC_VOLUME, NumberUtil.formatCompact(cuboid.getVolume()))
            .replace(Placeholders.GENERIC_MAX, max < 0 ? Lang.OTHER_INFINITY.getString() : NumberUtil.formatCompact(max))
        );
    }

    public void highlightSelection(@NotNull Player player) {
        Selection selection = this.getSelection(player);
        if (selection == null) return;

        this.highlightCuboid(player, selection, HighlightType.SELECTION);
    }

    public void highlightChunkBounds(@NotNull Player player) {
        this.removeVisuals(player, HighlightType.CHUNK);

        World world = player.getWorld();
        Location playerLocation = player.getLocation();
        Chunk playerChunk = playerLocation.getChunk();

        int centerX = playerLocation.getBlockX();
        int centerY = playerLocation.getBlockY();
        int centerZ = playerLocation.getBlockZ();

        int offset = 16 * Config.HIGHLIGHT_CHUNK_RENDER_DISTANCE.get();
        if (offset <= 0) return;

        int minX = centerX - offset;
        int minZ = centerZ - offset;
        int maxX = centerX + offset;
        int maxZ = centerZ + offset;

        int maxY = world.getHighestBlockAt(playerLocation).getY();
        int playerY = playerLocation.getBlockY();
        int y = Math.min(maxY, playerY);

        BoundingBox box = new BoundingBox(minX, centerY, minZ, maxX, centerY, maxZ);
        Material cornerType = Config.HIGHLIGHT_CHUNK_BLOCK_CORNER.get();

        world.getIntersectingChunks(box).forEach(chunk -> {
            if (!chunk.isLoaded()) return;

            Cuboid cuboid = this.getCuboid(chunk, y, 4);
            this.highlightCuboid(player, cuboid, HighlightType.CHUNK, false/*, true*/);
        });
    }

    public void highlightCuboid(@NotNull Player player, @NotNull Selection selection, @NotNull HighlightType type) {
        this.highlightCuboid(player, type, selection.getFirst(), selection.getSecond());
    }

    private void highlightCuboid(@NotNull Player player, @NotNull HighlightType type, @Nullable BlockPos min, @Nullable BlockPos max) {
        if (min == null) min = BlockPos.empty();
        if (max == null) max = BlockPos.empty();
        if (min.isEmpty() && !max.isEmpty()) min = max;
        if (max.isEmpty() && !min.isEmpty()) max = min;

        this.highlightCuboid(player, new Cuboid(min, max), type);
    }

    public void highlightCuboid(@NotNull Player player, @NotNull Cuboid cuboid, @NotNull HighlightType type) {
        this.highlightCuboid(player, cuboid, type, true/*, true*/);
    }

    public void highlightCuboid(@NotNull Player player, @NotNull Cuboid cuboid, @NotNull HighlightType type, boolean reset/*, boolean checkIntersect*/) {
        if (this.highlighter == null) return;

        if (reset) {
            this.removeVisuals(player, type);
        }

        World world = player.getWorld();
        Location playerLocation = player.getLocation();
        Material cornerType = Config.getHighlightCorner(type);
        Material wireType = Config.getHighlightWire(type);
        Set<Pair<BlockPos, BlockData>> dataSet = new HashSet<>();

        boolean isRegionOrInChunk = type.isIntersect()
            || type == HighlightType.SELECTION
            || cuboid.contains(playerLocation, DimensionType._2D);

        boolean checkIntersect = !type.isIntersect();

        // Draw corners of the chunk/region all the time.
        this.collectBlockData(cuboid.getCorners(), dataSet, cornerType.createBlockData());
        this.collectBlockData(cuboid.getCornerWiresY(), dataSet, (type == HighlightType.CHUNK ? cornerType : wireType).createBlockData());

        // Draw connections only for regions or when player is inside a chunk.
        if (isRegionOrInChunk) {
            BlockData dataX = this.createBlockData(wireType, Axis.X);
            BlockData dataZ = this.createBlockData(wireType, Axis.Z);

            this.collectBlockData(cuboid.getCornerWiresX(), dataSet, dataX);
            this.collectBlockData(cuboid.getCornerWiresZ(), dataSet, dataZ);
        }

        // Draw all visual blocks at prepated positions with prepared block data.
        dataSet.forEach(pair -> {
            BlockPos blockPos = pair.getFirst();
            Location location = blockPos.toLocation(world);
            ChatColor color = this.getBlockColor(player, world, blockPos, cuboid, type, isRegionOrInChunk);
            float size = isRegionOrInChunk ? 0.98f : 0.5f; // Size 1f will cause texture glitch when inside a block.

            this.highlighter.addVisualBlock(player, location, type, pair.getSecond(), color, size);
        });

        // Draw intersecting regions only for region selection or when inside a chunk.
        if (checkIntersect && isRegionOrInChunk) {
            Set<Claim> intersecting = new HashSet<>();
            if (type == HighlightType.CHUNK) {
                intersecting.addAll(this.plugin.getClaimManager().getRegionClaims(world, ChunkPos.from(cuboid.getMin())));
            }
            else {
                intersecting.addAll(this.plugin.getClaimManager().getClaims(world, cuboid));
            }

            int playerY = playerLocation.getBlockY() - 2;
            Set<Cuboid> cuboids = new HashSet<>();
            intersecting.forEach(claim -> {
                if (claim instanceof RegionClaim regionClaim) {
                    cuboids.add(regionClaim.getCuboid());
                }
                if (claim instanceof LandClaim landClaim) {
                    landClaim.getChunks().forEach(chunk -> cuboids.add(this.getCuboid(chunk, playerY, 0)));
                }
            });

            cuboids.forEach(cube -> {
                if (cube.isSimilar(cuboid)) return; // Remove regions of the same cuboid positions to prevent loop.
                if (!cube.isIntersectingWith(cuboid, DimensionType._2D)) return; // Remove cuboids of merged chunks that are not intersecting.

                this.highlightCuboid(player, cube, type.getIntersectType(), false);
            });
        }
    }

    @NotNull
    private ChatColor getBlockColor(@NotNull Player player, @NotNull World world, @NotNull BlockPos blockPos, @NotNull Cuboid cuboid, @NotNull HighlightType type, boolean isIn) {
        ChatColor color;
        if (type.isIntersect()) {
            color = ChatColor.RED;
        }
        else if (type == HighlightType.SELECTION) {
            color = ChatColor.DARK_AQUA;
            if (blockPos.equals(cuboid.getMin()) || blockPos.equals(cuboid.getMax())) {
                color = ChatColor.AQUA;
            }
            if (this.plugin.getClaimManager().isClaimed(world, blockPos)) {
                color = ChatColor.RED;
            }
        }
        else {
            color = isIn ? ChatColor.WHITE : ChatColor.GRAY;
            Claim claim = plugin.getClaimManager().getLandClaim(world, blockPos);
            if (claim != null) {
                if (claim.isOwnerOrMember(player)) {
                    color = isIn ? ChatColor.GREEN : ChatColor.DARK_GREEN;
                }
                else color = isIn ? ChatColor.YELLOW : ChatColor.GOLD;
            }
            if (this.plugin.getClaimManager().isRegionClaim(world, blockPos)) {
                color = ChatColor.DARK_RED;
            }
        }
        return color;
    }

    private void collectBlockData(@NotNull Collection<BlockPos> source, @NotNull Set<Pair<BlockPos, BlockData>> target, @NotNull BlockData data) {
        if (data.getMaterial().isAir()) return;

        source.stream().filter(blockPos -> blockPos != null && !blockPos.isEmpty()).map(blockPos -> Pair.of(blockPos, data)).forEach(target::add);
    }

    @NotNull
    private BlockData createBlockData(@NotNull Material material, @NotNull Axis axis) {
        BlockData data = material.createBlockData();
        if (data instanceof Orientable orientable) {
            orientable.setAxis(axis);
        }
        return data;
    }



    @NotNull
    public ItemStack getItem(@NotNull ItemType itemType) {
        ConfigValue<NightItem> value = switch (itemType) {
            case REGION_WAND -> Config.REGION_WAND_ITEM;
            case CHUNK_MERGE -> Config.LAND_MERGE_ITEM;
            case CHUNK_SEPARATE -> Config.LAND_SEPARATE_ITEM;
        };

        ItemStack itemStack = value.get().getItemStack();
        PDCUtil.set(itemStack, Keys.itemType, itemType.name());
        return itemStack;
    }

    public boolean isItem(@NotNull ItemStack itemStack) {
        return this.getItemType(itemStack) != null;
    }

    public boolean isItem(@NotNull ItemStack itemStack, @NotNull ItemType itemType) {
        return this.getItemType(itemStack) == itemType;
    }

    @Nullable
    public ItemType getItemType(@NotNull ItemStack itemStack) {
        String raw = PDCUtil.getString(itemStack, Keys.itemType).orElse(null);
        return raw == null ? null : StringUtil.getEnum(raw, ItemType.class).orElse(null);
    }

    public void onItemUse(@NotNull Player player, @NotNull ItemType itemType, @NotNull Block block, @NotNull Action action) {
        switch (itemType) {
            case REGION_WAND -> this.selectPosition(player, block.getLocation(), action);
            case CHUNK_MERGE -> this.selectMergePosition(player, MergeType.MERGE, block.getLocation(), action);
            case CHUNK_SEPARATE -> this.selectMergePosition(player, MergeType.SEPARATE, block.getLocation(), action);
        }
    }

    public void onItemDrop(@NotNull Player player, @NotNull ItemType itemType) {
        switch (itemType) {
            case REGION_WAND -> this.stopSelection(player);
            case CHUNK_MERGE, CHUNK_SEPARATE -> this.removeFromMerge(player);
        }
    }

    public boolean isInSelection(@NotNull Player player) {
        return this.getSelection(player) != null;
    }

    @Nullable
    public Selection getSelection(@NotNull Player player) {
        return this.selectionMap.get(player.getUniqueId());
    }

    public boolean canSeeChunkBounds(@NotNull Player player) {
        Tracker tracker = this.getTracker(player);
        return tracker != null && tracker.isChunkBounds();
    }

    public void removeAll(@NotNull Player player) {
        if (this.canSeeChunkBounds(player)) {
            this.disableChunkBounds(player);
        }
        if (this.isInSelection(player)) {
            this.stopSelection(player);
        }
        if (this.isInMerge(player)) {
            this.removeFromMerge(player);
        }
        this.chunkTracker.remove(player.getUniqueId());
    }

    public void removeVisuals(@NotNull Player player, @NotNull HighlightType type) {
        if (this.highlighter != null) {
            this.highlighter.removeVisuals(player, type);
            if (!type.isIntersect()) {
                this.highlighter.removeVisuals(player, type.getIntersectType());
            }
        }
    }

    @NotNull
    public Tracker addTracker(@NotNull Player player) {
        return this.chunkTracker.computeIfAbsent(player.getUniqueId(), k -> new Tracker());
    }

    @Nullable
    public Tracker getTracker(@NotNull Player player) {
        return this.chunkTracker.get(player.getUniqueId());
    }

    public void removeTracker(@NotNull Player player, @NotNull Consumer<Tracker> consumer) {
        Tracker tracker = this.getTracker(player);
        if (tracker == null) return;

        consumer.accept(tracker);

        if (!tracker.isChunkBounds() && !tracker.isSelection()) {
            this.chunkTracker.remove(player.getUniqueId());
        }
    }

    public void removeTracker(@NotNull Player player) {
        this.chunkTracker.remove(player.getUniqueId());
    }



    public void toggleChunkBounds(@NotNull Player player) {
        if (this.canSeeChunkBounds(player)) {
            this.disableChunkBounds(player);
        }
        else this.enableChunkBounds(player);

        Lang.LAND_BOUNDS_TOGGLE.getMessage().send(player, replacer -> replacer.replace(Placeholders.GENERIC_VALUE, Lang.getEnabledOrDisabled(this.canSeeChunkBounds(player))));
    }

    public void enableChunkBounds(@NotNull Player player) {
        this.addTracker(player).setChunkBounds(true);
        //this.chunkTracker.put(player.getUniqueId(), BlockPos.from(player.getLocation()));
        this.highlightChunkBounds(player);
    }

    public void disableChunkBounds(@NotNull Player player) {
        this.removeVisuals(player, HighlightType.CHUNK);
        this.removeTracker(player, tracker -> tracker.setChunkBounds(false));
        //this.chunkTracker.remove(player.getUniqueId());
    }



    @NotNull
    public Selection startSelection(@NotNull Player player) {
        this.stopSelection(player);

        Selection selection = new Selection();
        this.selectionMap.put(player.getUniqueId(), selection);
        this.addTracker(player).setSelection(true);

        Players.addItem(player, this.getItem(ItemType.REGION_WAND));
        return selection;
    }

    public void stopSelection(@NotNull Player player) {
        this.removeVisuals(player, HighlightType.SELECTION);
        this.removeTracker(player, tracker -> tracker.setSelection(false));

        Players.takeItem(player, itemStack -> this.isItem(itemStack, ItemType.REGION_WAND));
        this.selectionMap.remove(player.getUniqueId());
    }

    public void selectPosition(@NotNull Player player, @NotNull Location location, @NotNull Action action) {
        Selection selection = this.getSelection(player);
        if (selection == null) return;

        BlockPos blockPos = BlockPos.from(location);
        int value;

        if (action == Action.LEFT_CLICK_BLOCK) {
            selection.setFirst(blockPos);
            value = 1;
        }
        else {
            selection.setSecond(blockPos);
            value = 2;
        }

        this.plugin.runTaskAsync(task -> {
            this.highlightSelection(player);
        });

        Lang.REGION_SELECTION_INFO.getMessage().send(player, replacer -> replacer.replace(Placeholders.GENERIC_VALUE, value));
    }



    public boolean isInMerge(@NotNull Player player) {
        return this.mergeMap.containsKey(player.getUniqueId());
    }

    public void addInMerge(@NotNull Player player, @NotNull MergeType type, @NotNull LandClaim claim) {
        this.removeFromMerge(player);
        this.mergeMap.put(player.getUniqueId(), claim);

        ItemType itemType = type == MergeType.MERGE ? ItemType.CHUNK_MERGE : ItemType.CHUNK_SEPARATE;
        Players.addItem(player, this.getItem(itemType));
    }

    public void removeFromMerge(@NotNull Player player) {
        Players.takeItem(player, itemStack -> this.isItem(itemStack, ItemType.CHUNK_MERGE) || this.isItem(itemStack, ItemType.CHUNK_SEPARATE));
        this.mergeMap.remove(player.getUniqueId());
    }

    public void selectMergePosition(@NotNull Player player, @NotNull MergeType type, @NotNull Location location, @NotNull Action action) {
        LandClaim source = this.mergeMap.get(player.getUniqueId());
        if (source == null) return;

        // If claim was unloaded or removed.
        if (source.isInactive() || source.isEmpty()) {
            Lang.LAND_MERGE_ERROR_INACTIVE.getMessage().send(player);
            this.removeFromMerge(player);
            return;
        }

        BlockPos blockPos = BlockPos.from(location);
        if (type == MergeType.MERGE) {
            if (this.plugin.getClaimManager().mergeChunk(player, source, blockPos)) {
                this.removeFromMerge(player);
            }
        }
        else {
            if (this.plugin.getClaimManager().separateChunk(player, source, blockPos)) {
                this.removeFromMerge(player);
            }
        }
    }
}
