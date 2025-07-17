package su.nightexpress.excellentclaims.flag.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.Placeholders;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.flag.*;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.config.Perms;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.menu.Menu;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.tag.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class ClaimFlag<T> implements Flag<T> {

    private final String       id;
    private final FlagCategory category;
    private final FlagType<T>  type;
    private final String       permission;

    private T defaultValue;

    protected String       displayName;
    protected List<String> description;
    protected NightItem    icon;

    public ClaimFlag(@NotNull String id,
                     @NotNull FlagCategory category,
                     @NotNull FlagType<T> type,
                     @NotNull T defaultValue,
                     @NotNull NightItem icon,
                     @NotNull List<String> description) {
        this.id = StringUtil.lowerCaseUnderscore(id);
        this.category = category;
        this.type = type;
        this.permission = Perms.PREFIX_FLAG + this.getId();

        this.displayName = StringUtil.capitalizeUnderscored(this.id);
        this.description = description;
        this.icon = icon;
        this.defaultValue = defaultValue;
    }

    public void loadSettings(@NotNull FileConfig config, @NotNull String path) {
        this.defaultValue = ConfigValue.create(path + ".Default_Value", this.type::read, this.type::write, () -> this.defaultValue).read(config);
        this.displayName = ConfigValue.create(path + ".DisplayName", this.displayName).read(config);
        this.description = ConfigValue.create(path + ".Description", this.description).read(config);
        this.icon = ConfigValue.create(path + ".Icon", this.icon).read(config);
    }

    @NotNull
    public static <T> Builder<T> builder(@NotNull String id, @NotNull FlagType<T> type) {
        return new Builder<>(id, type);
    }

    @Override
    @NotNull
    public FlagType<T> getType() {
        return this.type;
    }

    @Override
    @NotNull
    public UnaryOperator<String> replacePlaceholders() {
        return Placeholders.FLAG.replacer(this);
    }

    @NotNull
    public String getValueLocalized(@NotNull Claim claim) {
        if (!claim.hasFlag(this)) {
            return Lang.OTHER_UNSET.getString();
        }

        return this.type.getLocalized(claim.getFlag(this));
    }

    public void onManageClick(@NotNull Menu menu, @NotNull MenuViewer viewer, @NotNull InventoryClickEvent event, @NotNull Claim claim) {
        this.type.onManageClick(menu, viewer, event, claim, this);
    }

    @Override
    public boolean hasPermission(@NotNull Player player) {
        return player.hasPermission(Perms.FLAG) || player.hasPermission(this.permission);
    }

    @NotNull
    public ParsedFlag<T> parse(@NotNull FileConfig config, @NotNull String path) {
        return this.boxed(this.type.read(config, path));
    }

    @Override
    @NotNull
    public ParsedFlag<T> boxed(@NotNull T value) {
        return new ParsedFlag<>(this.type, value);
    }

    @Override
    @NotNull
    public Optional<T> unboxed(@NotNull FlagValue value) {
        return this.type.unboxed(value);
    }

    @NotNull
    public String getId() {
        return this.id;
    }

    @NotNull
    @Override
    public FlagCategory getCategory() {
        return this.category;
    }

    @NotNull
    public String getDisplayName() {
        return this.displayName;
    }

    @NotNull
    @Override
    public List<String> getDescription() {
        return new ArrayList<>(this.description);
    }

    @NotNull
    @Override
    public NightItem getIcon() {
        return this.icon.copy();
    }

    @NotNull
    @Override
    public String getPermission() {
        return this.permission;
    }

    @NotNull
    public T getDefaultValue() {
        return this.defaultValue;
    }

    public static class Builder<T> {

        private final String      id;
        private final FlagType<T> type;

        private FlagCategory category;
        private T            defaultValue;
        private List<String> description;
        private NightItem    icon;

        public Builder(@NotNull String id, @NotNull FlagType<T> type) {
            this.id = id;
            this.type = type;
        }

        public ClaimFlag<T> build() {
            return new ClaimFlag<>(this.id, this.category, this.type, this.defaultValue, this.icon, this.description);
        }

        @NotNull
        public Builder<T> category(@NotNull FlagCategory category) {
            this.category = category;
            return this;
        }

        @NotNull
        public Builder<T> defaultValue(@NotNull T defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        @NotNull
        public Builder<T> description(@NotNull String...description) {
            return this.description(Lists.newList(description));
        }

        @NotNull
        public Builder<T> description(@NotNull List<String> description) {
            this.description = Lists.modify(description, Tags.GRAY::wrap);
            return this;
        }

        @NotNull
        public Builder<T> icon(@NotNull String skinURL) {
            return this.icon(NightItem.asCustomHead(skinURL));
        }

        @NotNull
        public Builder<T> icon(@NotNull Material material) {
            return this.icon(NightItem.fromType(material));
        }

        @NotNull
        public Builder<T> icon(@NotNull NightItem icon) {
            this.icon = icon;
            return this;
        }
    }
}
