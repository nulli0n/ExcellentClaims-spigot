package su.nightexpress.excellentclaims.region.borders.settings;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;
import su.nightexpress.nightcore.util.BukkitThing;

@NullMarked
public final class BorderSettingsSchema {

    public static final ConfigProperty<Long> BORDER_REFRESH_RATE = ConfigProperty.of(
        ConfigCodecs.LONG,
        "Settings.Refresh-Rate",
        10L,
        "Sets update (redraw) rate (in ticks) for chunk claim bounds.",
        "Updates only when player went to other chunk and/or on player's Y location change.",
        "Setting this to low values may result in increased network usage.",
        "[Asynchronous]",
        "[Default is 10 (0.5 seconds)]"
    );

    public static final ConfigProperty<Integer> BORDER_RENDER_DISTANCE = ConfigProperty.of(
        ConfigCodecs.INT,
        "Settings.Render-Distance",
        0,
        "Sets distance (in chunks) to render claim bounds.",
        "Setting this to high values may result in player's FPS drops.",
        "[Default is 0]"
    );

    public static final ConfigProperty<Integer> BORDER_VERTICAL_DISTANCE = ConfigProperty.of(
        ConfigCodecs.INT,
        "Settings.Vertical-Distance",
        5,
        ""
    );

    public static final ConfigProperty<Material> BORDER_CORNER_TYPE = ConfigProperty.of(
        ConfigCodecs.MATERIAL,
        "Settings.Corner-Type",
        Material.STONE_BRICK_WALL,
        "Block type used for a fake block display entity for chunk bound's corners.",
        "[Default is " + BukkitThing.getAsString(Material.STONE_BRICK_WALL) + "]"
    );

    public static final ConfigProperty<Material> BORDER_WIRE_TYPE = ConfigProperty.of(
        ConfigCodecs.MATERIAL,
        "Settings.Wire-Type",
        Material.IRON_CHAIN,
        "Block type used for a fake block display entity for chunk bound's corners connections.",
        "[Default is " + BukkitThing.getAsString(Material.IRON_CHAIN) + "]"
    );

    private BorderSettingsSchema() {
    }
}
