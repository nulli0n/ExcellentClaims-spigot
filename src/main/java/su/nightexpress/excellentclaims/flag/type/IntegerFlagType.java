package su.nightexpress.excellentclaims.flag.type;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.flag.impl.ClaimFlag;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.dialog.Dialog;
import su.nightexpress.nightcore.ui.menu.Menu;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.util.NumberUtil;

public class IntegerFlagType extends ClaimFlagType<Integer> {

    public IntegerFlagType() {
        super(Integer.class);
    }

    @Override
    @NotNull
    public Integer read(@NotNull FileConfig config, @NotNull String path) {
        return config.getInt(path);
    }

    @Override
    @NotNull
    public String getLocalized(@NotNull Integer value) {
        return NumberUtil.format(value);
    }

    @Override
    public void onManageClick(@NotNull Menu menu, @NotNull MenuViewer viewer, @NotNull InventoryClickEvent event, @NotNull Claim claim, @NotNull ClaimFlag<Integer> flag) {
        menu.handleInput(Dialog.builder(viewer.getPlayer(), input -> {
            claim.setFlag(flag, input.asInt(0));
            claim.setSaveRequired(true);
            return true;
        }));
    }
}
