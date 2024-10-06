package su.nightexpress.excellentclaims.menu.impl;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.click.ClickAction;

public interface Confirmation {

    void onAccept(@NotNull MenuViewer viewer, @NotNull InventoryClickEvent event);

    void onDecline(@NotNull MenuViewer viewer, @NotNull InventoryClickEvent event);

    @NotNull
    static Confirmation create(@NotNull ClickAction accept, @NotNull ClickAction decline) {
        return new Confirmation() {

            @Override
            public void onAccept(@NotNull MenuViewer viewer, @NotNull InventoryClickEvent event) {
                accept.onClick(viewer, event);
            }

            @Override
            public void onDecline(@NotNull MenuViewer viewer, @NotNull InventoryClickEvent event) {
                decline.onClick(viewer, event);
            }
        };
    }
}
