package su.nightexpress.excellentclaims.region.member.ui;

import java.nio.file.Path;

import su.nightexpress.excellentclaims.api.core.LifecycleComponent;
import su.nightexpress.excellentclaims.api.core.MenuLoader;
import su.nightexpress.excellentclaims.region.member.MemberMenus;

public class MemberMenuLoader implements LifecycleComponent {

    private final Path        menuDir;
    private final MemberMenus menus;

    public MemberMenuLoader(Path menuDir, MemberMenus menus) {
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
        MenuLoader.load(this.menuDir, "region_member_list.yml", this.menus.membersMenu());
        MenuLoader.load(this.menuDir, "region_member_actions.yml", this.menus.actionsMenu());
    }
}
