package su.nightexpress.excellentclaims.rules.ui.menu;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.LifecycleComponent;
import su.nightexpress.excellentclaims.api.core.MenuLoader;

@NullMarked
public class RuleMenuLoader implements LifecycleComponent {

    private final Path      menuDir;
    private final RuleMenus menus;

    public RuleMenuLoader(Path menuDir, RuleMenus menus) {
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
        MenuLoader.load(this.menuDir, "claim_rules.yml", this.menus.rulesMenu());
    }
}
