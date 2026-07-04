package su.nightexpress.excellentclaims.highlighter;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.highlight.BlockContext;
import su.nightexpress.excellentclaims.api.highlight.BlockHighlighter;
import su.nightexpress.excellentclaims.api.highlight.EntityReference;
import su.nightexpress.excellentclaims.api.highlight.HighlightAPI;
import su.nightexpress.nightcore.util.EntityUtil;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;
import su.nightexpress.nightcore.util.geodata.pos.ExactPos;

@NullMarked
public class HighlightService implements HighlightAPI {

    private final BlockHighlighter highlighter;

    public HighlightService(BlockHighlighter highlighter) {
        this.highlighter = highlighter;
    }

    @Override
    public void clearHighlight(Player player, EntityReference entity) {
        this.clearHighlight(player, Collections.singleton(entity));
    }

    @Override
    public void clearHighlight(Player player, Collection<EntityReference> entities) {
        this.highlighter.clearHighlight(player, entities.stream().mapToInt(EntityReference::id).boxed().toList());
    }

    @Override
    public EntityReference highlightBlock(Player player, BlockPos blockPos, BlockData data, Color color, float size) {
        ExactPos position = shiftPosition(blockPos, size);
        BlockContext context = new BlockContext(position, data);

        int entityId = EntityUtil.nextEntityId();
        UUID entityUniqueId = UUID.randomUUID();
        EntityReference reference = new EntityReference(entityId, entityUniqueId, blockPos);

        this.highlighter.highlightBlock(player, reference, context, color, size);

        return reference;
    }

    @Override
    public void updateHighlight(Player player, EntityReference reference, Color color, float size) {
        ExactPos pos = shiftPosition(reference.pos(), size);

        this.highlighter.updateHighlight(player, reference, pos, color, size);
    }

    private static ExactPos shiftPosition(BlockPos origin, float size) {
        // To shift scaled down/up displays to the center of a block location.
        float offset = 1f - size;
        // Half-sized (0.5f) block displays got shifted on 1/2 of the size difference, so 0.5f modifier comes from here.
        float shift = 0.5f * offset;

        double x = origin.getX() + shift;
        double y = origin.getY() + shift;
        double z = origin.getZ() + shift;

        return new ExactPos(x, y, z, 0f, 0f);
    }
}
