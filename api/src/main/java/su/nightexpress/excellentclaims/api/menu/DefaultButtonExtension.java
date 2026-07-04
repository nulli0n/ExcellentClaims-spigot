package su.nightexpress.excellentclaims.api.menu;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.ui.inventory.menu.AbstractMenuBase;

public interface DefaultButtonExtension extends ButtonExtension {

    void onLayoutDefine(@NonNull AbstractMenuBase menu);
}
