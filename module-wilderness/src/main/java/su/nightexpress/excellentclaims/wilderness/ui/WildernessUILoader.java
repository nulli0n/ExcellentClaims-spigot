package su.nightexpress.excellentclaims.wilderness.ui;

import java.nio.file.Path;

import su.nightexpress.excellentclaims.api.core.LifecycleComponent;
import su.nightexpress.excellentclaims.api.core.MenuLoader;
import su.nightexpress.excellentclaims.wilderness.ui.menu.WildernessMenus;

public class WildernessUILoader implements LifecycleComponent {

    private final Path            menuDir;
    private final WildernessMenus menus;

    public WildernessUILoader(Path menuDir, WildernessMenus menus) {
        this.menuDir = menuDir;
        this.menus = menus;
    }

    @Override
    public void reload() {
        this.loadMenus();
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void start() {
        this.loadMenus();
    }

    private void loadMenus() {
        MenuLoader.load(this.menuDir, "wilderness_settings.yml", this.menus.settingsMenu());
        MenuLoader.load(this.menuDir, "wilderness_list.yml", this.menus.listMenu());
    }
}
