package su.nightexpress.excellentclaims.api.highlight;

import java.util.HashSet;
import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;

@NullMarked
public class WireframeCalculator implements HighlightCalculator {

    @Override
    public Set<HighlightPoint> calculateVisiblePoints(Cuboid selectedRegion, Cuboid playerFOV) {
        Set<HighlightPoint> points = new HashSet<>();

        // If bounding boxes don't intersect at all, return empty
        if (!selectedRegion.isIntersectingWith(playerFOV)) {
            return points;
        }

        collectCorners(selectedRegion, playerFOV, points);
        collectXEdges(selectedRegion, playerFOV, points);
        collectYEdges(selectedRegion, playerFOV, points);
        collectZEdges(selectedRegion, playerFOV, points);

        return points;
    }

    private void collectCorners(Cuboid selected, Cuboid view, Set<HighlightPoint> out) {
        // Define all 8 corners
        BlockPos[] corners = {new BlockPos(selected.minX(), selected.minY(), selected.minZ()), new BlockPos(selected
            .minX(), selected.minY(), selected.maxZ()), new BlockPos(selected.minX(), selected.maxY(), selected
                .minZ()), new BlockPos(selected.minX(), selected.maxY(), selected.maxZ()), new BlockPos(selected
                    .maxX(), selected
                        .minY(), selected.minZ()), new BlockPos(selected.maxX(), selected.minY(), selected
                            .maxZ()), new BlockPos(selected.maxX(), selected.maxY(), selected
                                .minZ()), new BlockPos(selected
                                    .maxX(), selected.maxY(), selected.maxZ())
        };

        for (BlockPos corner : corners) {
            if (view.contains(corner)) {
                out.add(new HighlightPoint(corner, NodeType.CORNER));
            }
        }
    }

    private void collectXEdges(Cuboid selected, Cuboid view, Set<HighlightPoint> out) {
        int startX = Math.max(selected.minX(), view.minX());
        int endX = Math.min(selected.maxX(), view.maxX());
        if (startX > endX) return;

        int[] yCoords = {selected.minY(), selected.maxY()};
        int[] zCoords = {selected.minZ(), selected.maxZ()};

        for (int y : yCoords) {
            if (y < view.minY() || y > view.maxY()) continue;
            for (int z : zCoords) {
                if (z < view.minZ() || z > view.maxZ()) continue;

                // Start + 1 and End - 1 to prevent overriding corners
                for (int x = startX + 1; x < endX; x++) {
                    out.add(new HighlightPoint(new BlockPos(x, y, z), NodeType.EDGE_X));
                }
            }
        }
    }

    private void collectYEdges(Cuboid selected, Cuboid view, Set<HighlightPoint> out) {
        int startY = Math.max(selected.minY(), view.minY());
        int endY = Math.min(selected.maxY(), view.maxY());
        if (startY > endY) return;

        int[] xCoords = {selected.minX(), selected.maxX()};
        int[] zCoords = {selected.minZ(), selected.maxZ()};

        for (int x : xCoords) {
            if (x < view.minX() || x > view.maxX()) continue;
            for (int z : zCoords) {
                if (z < view.minZ() || z > view.maxZ()) continue;

                // Exclude corners to prevent duplicate BlockPos allocations
                for (int y = startY + 1; y < endY; y++) {
                    out.add(new HighlightPoint(new BlockPos(x, y, z), NodeType.EDGE_Y));
                }
            }
        }
    }

    private void collectZEdges(Cuboid selected, Cuboid view, Set<HighlightPoint> out) {
        int startZ = Math.max(selected.minZ(), view.minZ());
        int endZ = Math.min(selected.maxZ(), view.maxZ());
        if (startZ > endZ) return;

        int[] xCoords = {selected.minX(), selected.maxX()};
        int[] yCoords = {selected.minY(), selected.maxY()};

        for (int x : xCoords) {
            if (x < view.minX() || x > view.maxX()) continue;
            for (int y : yCoords) {
                if (y < view.minY() || y > view.maxY()) continue;

                for (int z = startZ + 1; z < endZ; z++) {
                    out.add(new HighlightPoint(new BlockPos(x, y, z), NodeType.EDGE_Z));
                }
            }
        }
    }
}