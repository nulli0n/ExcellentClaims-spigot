package su.nightexpress.excellentclaims.region.selection.ui.highlight;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;
import su.nightexpress.nightcore.util.BukkitThing;

@NullMarked
public final class HighlightSettingsSchema {

    public static final ConfigProperty<int[]> COLOR = ConfigProperty.of(
        ConfigCodecs.INT_ARRAY,
        "Highlight.Color",
        new int[]{255, 0, 255, 255},
        "Sets highlight color in ARGB format."
    );

    public static final ConfigProperty<Integer> RENDER_DISTANCE = ConfigProperty.of(
        ConfigCodecs.INT,
        "Highlight.Render-Distance",
        32,
        "Sets the render distance (in blocks) of selection borders.",
        "Works via client-side packets. High values may lead to client framerate degradation.",
        "[Default is 32]"
    );

    public static final ConfigProperty<Material> CORNER_TYPE = ConfigProperty.of(
        ConfigCodecs.MATERIAL,
        "Highlight.Corner-Type",
        Material.STONE_BRICK_WALL,
        "Block to display on the corners of the selection border highlight.",
        "[Default is " + BukkitThing.getAsString(Material.STONE_BRICK_WALL) + "]"
    );

    public static final ConfigProperty<Material> CONNECTION_TYPE = ConfigProperty.of(
        ConfigCodecs.MATERIAL,
        "Highlight.Connection-Type",
        Material.IRON_CHAIN,
        "Block connecting the corners of the selection border highlight.",
        "[Default is " + BukkitThing.getAsString(Material.IRON_CHAIN) + "]"
    );

    private HighlightSettingsSchema() {
    }
}
