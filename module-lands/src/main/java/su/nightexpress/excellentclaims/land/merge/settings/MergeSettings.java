package su.nightexpress.excellentclaims.land.merge.settings;

import java.nio.file.Path;

import su.nightexpress.excellentclaims.api.core.LifecycleComponent;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.bukkit.NightItem;

public class MergeSettings implements LifecycleComponent {

    private final Path file;

    private NightItem mergeTool;
    private NightItem splitTool;

    private int sessionTimeout;

    public MergeSettings(Path file) {
        this.file = file;
    }

    @Override
    public void reload() {
        this.loadFromFile();
    }

    @Override
    public void shutdown() {
        // Nothing
    }

    @Override
    public void start() {
        this.loadFromFile();
    }

    private void loadFromFile() {
        FileConfig config = FileConfig.load(this.file);

        this.sessionTimeout = config.getOrSet(MergeSettingsSchema.SESSION_TIME_OUT);

        this.mergeTool = config.getOrSet(MergeSettingsSchema.MERGE_TOOL);
        this.splitTool = config.getOrSet(MergeSettingsSchema.SPLIT_TOOL);
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public NightItem getMergeTool() {
        return mergeTool.copy();
    }

    public NightItem getSplitTool() {
        return splitTool.copy();
    }
}
