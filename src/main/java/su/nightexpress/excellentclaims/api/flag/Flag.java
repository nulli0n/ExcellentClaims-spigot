package su.nightexpress.excellentclaims.api.flag;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.placeholder.Placeholder;

import java.util.List;

public interface Flag extends Placeholder {

    @NotNull String getId();

    @NotNull FlagCategory getCategory();

    @NotNull String getDisplayName();

    @NotNull List<String> getDescription();

    @NotNull ItemStack getIcon();

    @NotNull String getPermission();

    @NotNull Class<?> getValueType();

    @NotNull Object getDefaultValue();

    boolean hasPermission(@NotNull Player player);
}
