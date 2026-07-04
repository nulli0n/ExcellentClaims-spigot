package su.nightexpress.excellentclaims.region.selection.ui.highlight;

import org.bukkit.Axis;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Orientable;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.api.highlight.EntityReference;
import su.nightexpress.excellentclaims.api.highlight.HighlightAPI;
import su.nightexpress.excellentclaims.api.highlight.HighlightDelta;
import su.nightexpress.excellentclaims.api.highlight.HighlightSession;
import su.nightexpress.excellentclaims.api.highlight.NodeType;
import su.nightexpress.excellentclaims.region.selection.session.SelectionSession;
import su.nightexpress.excellentclaims.region.selection.ui.SelectionUIComponent;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;

@NullMarked
public class HighlightComponent implements SelectionUIComponent {

    public static final Identifier ID = Identifier.of("highlight");

    private final HighlightSettings settings;
    private final HighlightAPI      highlightAPI;
    private final HighlightSession  highlightSession;

    public HighlightComponent(HighlightSettings settings, HighlightAPI highlightAPI, HighlightSession session) {
        this.settings = settings;
        this.highlightAPI = highlightAPI;
        this.highlightSession = session;
    }

    @Override
    public void onAttach(Player player, SelectionSession session) {

    }

    @Override
    public void onDetach(Player player, SelectionSession session) {
        this.highlightAPI.clearHighlight(player, this.highlightSession.getEntityReferences());
        this.highlightSession.terminate();
    }

    @Override
    public void onUpdate(Player player, SelectionSession session, @Nullable Cuboid cuboid) {
        if (cuboid == null) return;

        Location location = player.getLocation();
        if (location == null) return;

        int renderDistance = this.settings.getRenderDistance();
        BlockPos playerPos = BlockPos.from(location);
        Cuboid playerViewBox = Cuboid.fromCenterAndRadius(playerPos, renderDistance); // 32 block radius
        HighlightDelta delta = this.highlightSession.update(cuboid, playerViewBox);
        Color color = this.settings.getColor();

        if (delta.hasChanges()) {
            delta.toRemove().forEach(point -> {
                BlockPos blockPos = point.pos();

                EntityReference reference = this.highlightSession.getEntityReference(blockPos);
                if (reference == null) return;

                this.highlightAPI.clearHighlight(player, reference);
                this.highlightSession.removeEntityReference(blockPos);
            });

            delta.toAdd().forEach(point -> {
                BlockPos addPos = point.pos();
                NodeType type = point.type();
                BlockData data = this.createBlockData(type);

                EntityReference reference = this.highlightAPI.highlightBlock(player, addPos, data, color, 0.98f);
                this.highlightSession.addEntityReference(reference);
            });
        }
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    private BlockData createBlockData(NodeType type) {
        if (type == NodeType.CORNER) {
            return this.settings.getCornerType().createBlockData();
        }

        Material material = this.settings.getConnectiontype();
        BlockData data = material.createBlockData();
        if (data instanceof Orientable orientable) {
            orientable.setAxis(type == NodeType.EDGE_Y ? Axis.Y : type == NodeType.EDGE_X ? Axis.X : Axis.Z);
        }
        return data;
    }
}
