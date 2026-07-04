package su.nightexpress.excellentclaims.region.borders.settings;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.settings.ConfigurableSettings;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class BorderSettings implements ConfigurableSettings {

    private long     refreshRate;
    private int      renderDistance;
    private int      verticalDistance;
    private Material cornerType = Material.STONE_BRICK_WALL;
    private Material wireType   = Material.IRON_CHAIN;

    @Override
    public void loadFrom(FileConfig config) {
        this.refreshRate = config.getOrSet(BorderSettingsSchema.BORDER_REFRESH_RATE);
        this.renderDistance = config.getOrSet(BorderSettingsSchema.BORDER_RENDER_DISTANCE);
        this.verticalDistance = config.getOrSet(BorderSettingsSchema.BORDER_VERTICAL_DISTANCE);
        this.cornerType = config.getOrSet(BorderSettingsSchema.BORDER_CORNER_TYPE);
        this.wireType = config.getOrSet(BorderSettingsSchema.BORDER_WIRE_TYPE);
    }

    public long getRefreshRate() {
        return refreshRate;
    }

    public int getRenderDistance() {
        return renderDistance;
    }

    public int getVerticalDistance() {
        return verticalDistance;
    }

    public Material getCornerType() {
        return cornerType;
    }

    public Material getWireType() {
        return wireType;
    }
}
