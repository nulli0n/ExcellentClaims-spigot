package su.nightexpress.excellentclaims.api.highlight;

import java.util.Collection;

import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.util.geodata.pos.ExactPos;

@NullMarked
public interface BlockHighlighter {

    void highlightBlock(Player player, EntityReference reference, BlockContext context, Color color, float size);

    void updateHighlight(Player player, EntityReference reference, ExactPos pos, Color color, float size);

    void clearHighlight(Player player, Collection<Integer> entityIds);
}
