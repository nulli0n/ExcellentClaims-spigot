package su.nightexpress.excellentclaims.api.flag;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

public interface Flag<T> {

    @NotNull FlagType<T> getType();

    @NotNull FlagValue boxed(@NotNull T value);

    @NotNull Optional<T> unboxed(@NotNull FlagValue value);

    @NotNull T getDefaultValue();

    @NotNull UnaryOperator<String> replacePlaceholders();

    @NotNull String getId();

    @NotNull FlagCategory getCategory();

    @NotNull String getDisplayName();

    @NotNull List<String> getDescription();

    @NotNull NightItem getIcon();

    @NotNull String getPermission();

    boolean hasPermission(@NotNull Player player);
}
