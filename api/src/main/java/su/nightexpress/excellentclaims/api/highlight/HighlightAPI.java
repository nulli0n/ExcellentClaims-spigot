package su.nightexpress.excellentclaims.api.highlight;

import java.util.Collection;

import org.bukkit.Color;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.util.geodata.pos.BlockPos;

@NullMarked
public interface HighlightAPI {

    EntityReference highlightBlock(Player player, BlockPos position, BlockData data, Color color, float size);

    void updateHighlight(Player player, EntityReference reference, Color color, float size);

    void clearHighlight(Player player, EntityReference entity);

    void clearHighlight(Player player, Collection<EntityReference> entities);
}
