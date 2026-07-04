package su.nightexpress.excellentclaims.api.menu;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;

@NullMarked
public interface DynamicButtonExtension extends ButtonExtension {

    MenuItem getItem(ViewerContext context);
}
