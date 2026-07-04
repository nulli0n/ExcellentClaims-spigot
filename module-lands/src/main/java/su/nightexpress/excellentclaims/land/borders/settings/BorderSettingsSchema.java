package su.nightexpress.excellentclaims.land.borders.settings;

import org.bukkit.Material;

import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;
import su.nightexpress.nightcore.util.BukkitThing;

public final class BorderSettingsSchema {

    public static final ConfigProperty<Long> BORDER_REFRESH_RATE = ConfigProperty.of(
        ConfigCodecs.LONG,
        "Settings.Refresh-Rate",
        10L,
        "Sets the refresh rate of the chunk border highlighting.",
        "[Asynchronous]",
        "[Default is 10 (0.5 seconds)]"
    );

    public static final ConfigProperty<Integer> BORDER_RENDER_DISTANCE = ConfigProperty.of(
        ConfigCodecs.INT,
        "Settings.Render-Distance",
        0,
        "Sets the render distance of chunk borders.",
        "[*] 0 = only current chunk",
        "[*] 1 or more = current chunk + specified amount of chunks around it",
        "Works via client-side packets. High values may lead to client framerate degradation.",
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
        "Block to display on the corners of the chunk border highlight.",
        "[Default is " + BukkitThing.getAsString(Material.STONE_BRICK_WALL) + "]"
    );

    public static final ConfigProperty<Material> BORDER_WIRE_TYPE = ConfigProperty.of(
        ConfigCodecs.MATERIAL,
        "Settings.Wire-Type",
        Material.IRON_CHAIN,
        "Block connecting the corners of the chunk border highlight.",
        "[Default is " + BukkitThing.getAsString(Material.IRON_CHAIN) + "]"
    );

    private BorderSettingsSchema() {
    }
}
