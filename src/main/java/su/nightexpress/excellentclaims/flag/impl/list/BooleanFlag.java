package su.nightexpress.excellentclaims.flag.impl.list;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.flag.FlagCategory;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.flag.impl.AbstractFlag;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.api.Menu;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.text.tag.Tags;

import java.util.List;

public class BooleanFlag extends AbstractFlag<Boolean> {

    public BooleanFlag(@NotNull String id,
                       @NotNull FlagCategory category,
                       @NotNull Boolean defaultValue,
                       @NotNull String skinURL,
                       @NotNull String... description) {
        this(id, category, defaultValue, ItemUtil.getSkinHead(skinURL), description);
    }

    public BooleanFlag(@NotNull String id,
                       @NotNull FlagCategory category,
                       @NotNull Boolean defaultValue,
                       @NotNull ItemStack icon,
                       @NotNull String... description) {
        super(id, category, Boolean.class, defaultValue, icon, description);
    }

    @Override
    @NotNull
    public String localize(@NotNull Boolean value) {
        return Lang.getEnabledOrDisabled(value);
    }

    @Override
    @NotNull
    public Boolean readValue(@NotNull FileConfig config, @NotNull String path) {
        return ConfigValue.create(path, this.getDefaultValue()).read(config);
    }

    @Override
    public void writeValue(@NotNull FileConfig config, @NotNull String path, @NotNull Boolean value) {
        config.set(path, value);
    }

//    @Override
//    @NotNull
//    public String getManageType() {
//        return "boolean_flag";
//    }

    @Override
    @NotNull
    public List<String> getManageInfo() {
        return Lists.newList(
            Tags.LIGHT_GRAY.enclose(Tags.LIGHT_YELLOW.enclose("[▶]") + " Click to " + Tags.LIGHT_YELLOW.enclose("toggle") + ".")
        );
    }

    @Override
    public void onManageClick(@NotNull Menu menu, @NotNull MenuViewer viewer, @NotNull InventoryClickEvent event, @NotNull Claim claim) {
        boolean value = !claim.getFlag(this);
        claim.setFlag(this, value);
        claim.setSaveRequired(true);
        menu.runNextTick(() -> menu.flush(viewer));
    }
}
