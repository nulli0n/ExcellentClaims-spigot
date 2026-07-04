package su.nightexpress.excellentclaims.land.editor.ui.menu;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.LifecycleComponent;
import su.nightexpress.excellentclaims.api.core.MenuLoader;

@NullMarked
public class EditorMenuLoader implements LifecycleComponent {

    private final Path        menuPath;
    private final EditorMenus menus;

    public EditorMenuLoader(Path menuPath, EditorMenus menus) {
        this.menuPath = menuPath;
        this.menus = menus;
    }

    private void loadMenus() {
        MenuLoader.load(this.menuPath, "land_icon_selection.yml", this.menus.iconSelectionMenu());
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
}
