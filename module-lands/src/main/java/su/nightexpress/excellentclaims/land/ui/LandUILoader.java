package su.nightexpress.excellentclaims.land.ui;

import java.nio.file.Path;

import su.nightexpress.excellentclaims.api.core.LifecycleComponent;
import su.nightexpress.excellentclaims.api.core.MenuLoader;
import su.nightexpress.excellentclaims.land.ui.menu.LandMenus;

public class LandUILoader implements LifecycleComponent {

    private final Path      menuDir;
    private final LandMenus menus;

    public LandUILoader(Path menuDir, LandMenus menus) {
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
        MenuLoader.load(this.menuDir, "land_settings.yml", this.menus.settingsMenu());
        MenuLoader.load(this.menuDir, "land_list.yml", this.menus.listMenu());
        MenuLoader.load(this.menuDir, "land_inspect.yml", this.menus.inspectMenu());
    }
}
