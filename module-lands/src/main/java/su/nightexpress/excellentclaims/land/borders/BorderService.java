package su.nightexpress.excellentclaims.land.borders;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Axis;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Orientable;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.highlight.EntityReference;
import su.nightexpress.excellentclaims.api.highlight.HighlightAPI;
import su.nightexpress.excellentclaims.land.LandsRepository;
import su.nightexpress.excellentclaims.land.borders.session.BorderSession;
import su.nightexpress.excellentclaims.land.borders.settings.BorderSettings;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;

@NullMarked
public class BorderService {

    private final HighlightAPI    highlightAPI;
    private final ClaimRegistry   claims;
    private final LandsRepository repository;
    private final BorderSettings  settings;

    public BorderService(HighlightAPI highlightAPI,
                         ClaimRegistry claims,
                         LandsRepository repository,
                         BorderSettings settings) {
        this.highlightAPI = highlightAPI;
        this.claims = claims;
        this.repository = repository;
        this.settings = settings;
    }

    public void clearHighlight(Player player, BorderSession session) {
        this.highlightAPI.clearHighlight(player, session.getEntities());

        session.clearReferences();
    }

    public void highlightChunkBorders(Player player, BorderSession session) {
        Location playerLocation = player.getLocation();
        if (playerLocation == null) return;

        int offset = 16 * this.settings.getRenderDistance();
        if (offset < 0) return;

        int centerX = playerLocation.getBlockX();
        int centerY = playerLocation.getBlockY();
        int centerZ = playerLocation.getBlockZ();

        int minX = centerX - offset;
        int minZ = centerZ - offset;
        int maxX = centerX + offset;
        int maxZ = centerZ + offset;

        World world = player.getWorld();
        BoundingBox box = new BoundingBox(minX, centerY, minZ, maxX, centerY, maxZ);

        Set<ChunkPos> positions = world.getIntersectingChunks(box).stream()
            .filter(Chunk::isLoaded)
            .map(ChunkPos::from)
            .collect(Collectors.toSet());

        // Remove highlighting of chunks that are out of render distance
        session.getChunks().forEach(cachedPos -> {
            if (!positions.contains(cachedPos)) {
                this.highlightAPI.clearHighlight(player, session.getEntities(cachedPos));
                session.clear(cachedPos);
            }
        });

        positions.forEach(chunkPos -> {
            this.highlightBorders(player, chunkPos, session);
        });
    }

    public void highlightBorders(Player player, ChunkPos chunkPos, BorderSession session) {
        Location playerLocation = player.getLocation();
        if (playerLocation == null) return;

        World world = player.getWorld();

        int maxY = world.getHighestBlockAt(playerLocation).getY();
        int playerY = playerLocation.getBlockY();
        int y = Math.min(maxY, playerY) + 1;
        Cuboid cuboid = this.createChunkCuboid(chunkPos, y, this.settings.getVerticalDistance());

        Set<Claim> claims = this.claims.getInChunk(world, chunkPos);
        LandClaim land = this.repository.getPrioritizedClaim(world, chunkPos);

        boolean isInside = cuboid.contains(playerLocation);
        boolean overlaps = land != null && claims.stream()
            .anyMatch(other -> !other.isBackgroundClaim() && other != land);

        Color color = this.getBlockColor(player, land, overlaps);
        float size = isInside ? 0.98f : 0.5f; // Size 1f will cause texture glitch when inside a block.

        if (session.hasChunk(chunkPos)) {
            session.getEntities(chunkPos).forEach(reference -> {
                this.highlightAPI.updateHighlight(player, reference, color, size);
            });
            return;
        }

        this.prepareBlockContext(cuboid).forEach((position, data) -> {
            EntityReference entityRef = this.highlightAPI.highlightBlock(player, position, data, color, size);
            session.addReference(chunkPos, entityRef);
        });
    }

    private Map<BlockPos, BlockData> prepareBlockContext(Cuboid cuboid) {
        Material cornerType = this.settings.getCornerType();
        Material wireType = this.settings.getWireType();

        Map<BlockPos, BlockData> blockContexts = new HashMap<>();

        BlockData wireDataX = this.createBlockData(wireType, Axis.X);
        BlockData wireDataZ = this.createBlockData(wireType, Axis.Z);

        createContexts(blockContexts, cuboid.getCorners(), cornerType.createBlockData());
        createContexts(blockContexts, cuboid.getCornerWiresY(), wireType.createBlockData());

        createContexts(blockContexts, cuboid.getCornerWiresX(), wireDataX);
        createContexts(blockContexts, cuboid.getCornerWiresZ(), wireDataZ);

        return blockContexts;
    }

    private Color getBlockColor(Player player, @Nullable LandClaim land, boolean overlaps) {
        if (land != null) {
            if (land.isOwner(player)) return Color.LIME;
            if (land.isMember(player)) return Color.BLUE;
        }
        if (overlaps) {
            return Color.PURPLE;
        }

        return land == null ? Color.WHITE : Color.RED;
    }

    private static void createContexts(Map<BlockPos, BlockData> map, Collection<BlockPos> positions, BlockData data) {
        positions.forEach(position -> {
            map.put(position, data);
        });
    }

    private Cuboid createChunkCuboid(ChunkPos chunk, int y, int verticalOffset) {
        int x = chunk.getX() << 4;
        int z = chunk.getZ() << 4;

        BlockPos min = new BlockPos(x, y - verticalOffset, z);
        BlockPos max = new BlockPos(x + 15, y + verticalOffset, z + 15);
        return new Cuboid(min, max);
    }

    private BlockData createBlockData(Material material, Axis axis) {
        BlockData data = material.createBlockData();
        if (data instanceof Orientable orientable) {
            orientable.setAxis(axis);
        }
        return data;
    }
}
