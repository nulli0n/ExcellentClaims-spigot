package su.nightexpress.excellentclaims.flag.impl.list;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.flag.FlagCategory;
import su.nightexpress.excellentclaims.flag.impl.AbstractFlag;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.menu.Menu;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.tag.Tags;

import java.util.List;

public abstract class EnumFlag<E extends Enum<E>> extends AbstractFlag<E> {

    public EnumFlag(@NotNull String id,
                    @NotNull FlagCategory category,
                    @NotNull Class<E> valueType,
                    @NotNull E defaultValue,
                    @NotNull NightItem icon,
                    @NotNull String... description) {
        super(id, category, valueType, defaultValue, icon, description);
    }

    @Override
    @NotNull
    public E readValue(@NotNull FileConfig config, @NotNull String path) {
        E value = config.getEnum(path, this.getValueType());
        return value == null ? this.getValueType().getEnumConstants()[0] : value;
    }

    @Override
    public void writeValue(@NotNull FileConfig config, @NotNull String path, @NotNull E value) {
        config.set(path, value.name().toLowerCase());
    }

    @Override
    @NotNull
    public List<String> getManageInfo() {
        return Lists.newList(
            Tags.LIGHT_GRAY.enclose(Tags.LIGHT_YELLOW.enclose("[▶]") + " Click to " + Tags.LIGHT_YELLOW.enclose("toggle") + ".")
        );
    }

    @Override
    public void onManageClick(@NotNull Menu menu, @NotNull MenuViewer viewer, @NotNull InventoryClickEvent event, @NotNull Claim claim) {
        E value = claim.getFlag(this);
        claim.setFlag(this, Lists.next(value));
        claim.setSaveRequired(true);
        menu.runNextTick(() -> menu.flush(viewer));
    }
}
