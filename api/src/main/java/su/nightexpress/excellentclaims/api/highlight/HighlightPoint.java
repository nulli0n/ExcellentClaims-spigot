package su.nightexpress.excellentclaims.api.highlight;

import su.nightexpress.nightcore.util.geodata.pos.BlockPos;

public record HighlightPoint(BlockPos pos, NodeType type) {

    // Custom equals/hashCode targeting ONLY the BlockPos.
    // This ensures we don't spawn two different node types on the same block.
    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof HighlightPoint that && pos.equals(that.pos));
    }

    @Override
    public int hashCode() {
        return pos.hashCode();
    }
}