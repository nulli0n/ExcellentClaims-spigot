package su.nightexpress.excellentclaims.menu.type;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.claim.Claim;

public interface ClaimMenu {

    boolean hasPermission(@NotNull Player player, @NotNull Claim claim);
}
