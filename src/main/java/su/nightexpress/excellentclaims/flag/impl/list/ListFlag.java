package su.nightexpress.excellentclaims.flag.impl.list;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.flag.FlagCategory;
import su.nightexpress.excellentclaims.flag.impl.AbstractFlag;
import su.nightexpress.excellentclaims.flag.type.EntryList;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.dialog.Dialog;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.api.Menu;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.text.tag.Tags;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class ListFlag<E, T extends EntryList<E>> extends AbstractFlag<T> {

    public ListFlag(@NotNull String id,
                    @NotNull FlagCategory category,
                    @NotNull Class<T> valueType,
                    @NotNull T defaultValue,
                    @NotNull ItemStack icon,
                    @NotNull String... description) {
        super(id, category, valueType, defaultValue, icon, description);
    }

    @Override
    @NotNull
    public String localize(@NotNull T value) {
        return value.getLocalizedString();
    }

    @Nullable
    public abstract E entryFromString(@NotNull String raw);

    @NotNull
    public abstract String entryToString(@NotNull E entry);

    @NotNull
    public abstract T createList(@NotNull Set<E> entries);

    protected abstract void onManagePrompt(@NotNull Player player, @NotNull Dialog dialog);

    @Override
    @NotNull
    public T readValue(@NotNull FileConfig config, @NotNull String path) {
        Set<E> types = Lists.modify(config.getStringSet(path), this::entryFromString);
        types.removeIf(Objects::isNull);
        return this.createList(types);
    }

    @Override
    public void writeValue(@NotNull FileConfig config, @NotNull String path, @NotNull T value) {
        config.set(path, value.getEntries().stream().map(this::entryToString).toList());
    }

    @Override
    @NotNull
    public List<String> getManageInfo() {
        return Lists.newList(
            Tags.LIGHT_GRAY.enclose(Tags.LIGHT_YELLOW.enclose("[▶]") + " Left-Click to " + Tags.LIGHT_YELLOW.enclose("add") + "."),
            Tags.LIGHT_GRAY.enclose(Tags.LIGHT_YELLOW.enclose("[▶]") + " Right-Click to " + Tags.LIGHT_YELLOW.enclose("remove all") + ".")
        );
    }

    @Override
    public void onManageClick(@NotNull Menu menu, @NotNull MenuViewer viewer, @NotNull InventoryClickEvent event, @NotNull Claim claim) {
        T list = claim.getFlag(this);
        Player player = viewer.getPlayer();

        if (event.isLeftClick()) {
            Dialog dg = Dialog.create(viewer.getPlayer(), (dialog, input) -> {
                E type = this.entryFromString(input.getTextRaw());
                if (type != null) {
                    list.getEntries().add(type);
                    claim.setFlag(this, list);
                    claim.setSaveRequired(true);
                }
                return true;
            });

            menu.runNextTick(player::closeInventory);
            this.onManagePrompt(player, dg);
        }
        else if (event.isRightClick()) {
            list.getEntries().clear();
            claim.setFlag(this, list);
            claim.setSaveRequired(true);
            menu.runNextTick(() -> menu.flush(viewer));
        }
    }
}
