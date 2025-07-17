package su.nightexpress.excellentclaims.flag.type;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.flag.impl.ClaimFlag;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.menu.Menu;
import su.nightexpress.nightcore.ui.menu.MenuViewer;

public class BooleanFlagType extends ClaimFlagType<Boolean> {

    public BooleanFlagType() {
        super(Boolean.class);
    }

    @Override
    @NotNull
    public Boolean read(@NotNull FileConfig config, @NotNull String path) {
        return config.getBoolean(path);
    }

    @Override
    @NotNull
    public String getLocalized(@NotNull Boolean value) {
        return Lang.getEnabledOrDisabled(value);
    }

    @Override
    public void onManageClick(@NotNull Menu menu, @NotNull MenuViewer viewer, @NotNull InventoryClickEvent event, @NotNull Claim claim, @NotNull ClaimFlag<Boolean> flag) {
        boolean value = !claim.getFlag(flag);
        claim.setFlag(flag, value);
        claim.setSaveRequired(true);
        menu.runNextTick(() -> menu.flush(viewer));
    }
}
