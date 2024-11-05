package su.nightexpress.excellentclaims.flag.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.Placeholders;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.flag.ClaimFlag;
import su.nightexpress.excellentclaims.api.flag.FlagCategory;
import su.nightexpress.excellentclaims.config.Perms;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.api.Menu;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.placeholder.PlaceholderMap;
import su.nightexpress.nightcore.util.text.tag.Tags;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFlag<T> implements ClaimFlag<T> {

    private final String         id;
    private final FlagCategory   category;
    private final Class<T>       valueType;
    private final PlaceholderMap placeholders;
    private final String         permission;

    private T defaultValue;

    protected String       displayName;
    protected List<String> description;
    protected ItemStack    icon;

    public AbstractFlag(@NotNull String id,
                        @NotNull FlagCategory category,
                        @NotNull Class<T> valueType,
                        @NotNull T defaultValue,
                        @NotNull ItemStack icon,
                        @NotNull String... description) {
        this(id, category, valueType, defaultValue, icon, Lists.modify(Lists.newList(description), Tags.LIGHT_GRAY::enclose));
    }

    public AbstractFlag(@NotNull String id,
                        @NotNull FlagCategory category,
                        @NotNull Class<T> valueType,
                        @NotNull T defaultValue,
                        @NotNull ItemStack icon,
                        @NotNull List<String> description) {
        this.id = StringUtil.lowerCaseUnderscore(id);
        this.category = category;
        this.valueType = valueType;
        this.permission = Perms.PREFIX_FLAG + this.getId();

        this.displayName = StringUtil.capitalizeUnderscored(this.id);
        this.description = description;
        this.icon = icon;
        this.defaultValue = defaultValue;

        this.placeholders = Placeholders.forFlag(this);
    }

    public void loadSettings(@NotNull FileConfig config, @NotNull String path) {
        this.defaultValue = this.readValue(config, path + ".Default_Value");
        this.displayName = ConfigValue.create(path + ".DisplayName", this.displayName).read(config);
        this.description = ConfigValue.create(path + ".Description", this.description).read(config);
        this.icon = ConfigValue.create(path + ".Icon", this.icon).read(config);
    }

    @NotNull
    @Override
    public PlaceholderMap getPlaceholders() {
        return this.placeholders;
    }

    //@NotNull
    //public abstract String getManageType();

    public boolean isManageAvailable(@NotNull Claim claim) {
        return true;
    }

    @NotNull
    public abstract List<String> getManageInfo();

    public abstract void onManageClick(@NotNull Menu menu, @NotNull MenuViewer viewer, @NotNull InventoryClickEvent event, @NotNull Claim claim);

    @Override
    public boolean hasPermission(@NotNull Player player) {
        return player.hasPermission(Perms.FLAG) || player.hasPermission(this.permission);
    }

    @NotNull
    public ParsedFlag<?, T> parse(@NotNull FileConfig config, @NotNull String path) {
        return new ParsedFlag<>(this, this.readValue(config, path));
    }

    @Override
    @NotNull
    public ParsedFlag<?, T> asValue(@NotNull T value) {
        return new ParsedFlag<>(this, value);
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
    public ItemStack getIcon() {
        return new ItemStack(this.icon);
    }

    @NotNull
    @Override
    public String getPermission() {
        return this.permission;
    }

    @NotNull
    public Class<T> getValueType() {
        return this.valueType;
    }

    @NotNull
    public T getDefaultValue() {
        return this.defaultValue;
    }
}
