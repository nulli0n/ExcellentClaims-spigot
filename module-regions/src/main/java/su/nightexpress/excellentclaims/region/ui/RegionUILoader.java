package su.nightexpress.excellentclaims.region.ui;

import java.nio.file.Path;

import su.nightexpress.excellentclaims.api.core.LifecycleComponent;
import su.nightexpress.excellentclaims.api.core.MenuLoader;
import su.nightexpress.excellentclaims.region.ui.menu.RegionMenus;

public class RegionUILoader implements LifecycleComponent {

    private final Path        menuDir;
    private final RegionMenus menus;

    public RegionUILoader(Path menuDir, RegionMenus menus) {
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
        MenuLoader.load(this.menuDir, "region_settings.yml", this.menus.settingsMenu());
        MenuLoader.load(this.menuDir, "region_list.yml", this.menus.listMenu());
        MenuLoader.load(this.menuDir, "region_inspect.yml", this.menus.inspectMenu());
    }
}
