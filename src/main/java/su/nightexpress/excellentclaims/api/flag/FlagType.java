package su.nightexpress.excellentclaims.api.flag;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.flag.impl.ClaimFlag;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.menu.Menu;
import su.nightexpress.nightcore.ui.menu.MenuViewer;

import java.util.Optional;

public interface FlagType<T> {

    @NotNull T read(@NotNull FileConfig config, @NotNull String path);

    void write(@NotNull FileConfig config, @NotNull String path, @NotNull T value);

    @NotNull Optional<T> unboxed(@NotNull FlagValue value);

    @NotNull String getLocalized(@NotNull T value);

    void onManageClick(@NotNull Menu menu, @NotNull MenuViewer viewer, @NotNull InventoryClickEvent event, @NotNull Claim claim, @NotNull ClaimFlag<T> flag);
}
