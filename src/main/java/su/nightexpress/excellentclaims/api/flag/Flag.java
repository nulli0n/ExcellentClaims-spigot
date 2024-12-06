package su.nightexpress.excellentclaims.api.flag;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.List;
import java.util.function.UnaryOperator;

public interface Flag {

    @NotNull UnaryOperator<String> replacePlaceholders();

    @NotNull String getId();

    @NotNull FlagCategory getCategory();

    @NotNull String getDisplayName();

    @NotNull List<String> getDescription();

    @NotNull NightItem getIcon();

    @NotNull String getPermission();

    @NotNull Class<?> getValueType();

    @NotNull Object getDefaultValue();

    boolean hasPermission(@NotNull Player player);
}
