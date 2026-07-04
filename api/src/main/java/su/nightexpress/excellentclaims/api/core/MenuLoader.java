package su.nightexpress.excellentclaims.api.core;

import java.nio.file.Path;

import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.inventory.Menu;

public final class MenuLoader {

    private MenuLoader() {
    }

    @Deprecated
    public static <T extends Menu> T load(NightCorePlugin plugin, String dir, String file, T menu) {
        Path menuPath = plugin.getDataFolder().toPath().resolve(dir).resolve(file);
        FileConfig config = FileConfig.load(menuPath);
        menu.load(config);
        return menu;
    }

    public static <T extends Menu> T load(Path dataPath, String dir, String file, T menu) {
        Path menuPath = dataPath.resolve(dir).resolve(file);
        FileConfig config = FileConfig.load(menuPath);
        menu.load(config);
        config.saveChanges();
        return menu;
    }

    public static <T extends Menu> T load(Path menuDir, String file, T menu) {
        Path menuPath = menuDir.resolve(file);
        FileConfig config = FileConfig.load(menuPath);
        menu.load(config);
        config.saveChanges();
        return menu;
    }
}
