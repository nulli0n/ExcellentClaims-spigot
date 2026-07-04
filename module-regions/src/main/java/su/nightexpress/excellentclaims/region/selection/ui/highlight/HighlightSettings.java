package su.nightexpress.excellentclaims.region.selection.ui.highlight;

import org.bukkit.Color;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.settings.ConfigurableSettings;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class HighlightSettings implements ConfigurableSettings {

    private Color    color          = Color.AQUA;
    private int      renderDistance = 32;
    private Material cornerType     = Material.STONE_BRICK_WALL;
    private Material connectiontype = Material.IRON_CHAIN;

    @Override
    public void loadFrom(FileConfig config) {
        int[] argb = config.getOrSet(HighlightSettingsSchema.COLOR);

        this.color = Color.fromARGB(argb[0], argb[1], argb[2], argb[3]);
        this.renderDistance = config.getOrSet(HighlightSettingsSchema.RENDER_DISTANCE);
        this.cornerType = config.getOrSet(HighlightSettingsSchema.CORNER_TYPE);
        this.connectiontype = config.getOrSet(HighlightSettingsSchema.CONNECTION_TYPE);
    }

    public Color getColor() {
        return color;
    }

    public int getRenderDistance() {
        return renderDistance;
    }

    public Material getCornerType() {
        return cornerType;
    }

    public Material getConnectiontype() {
        return connectiontype;
    }
}
