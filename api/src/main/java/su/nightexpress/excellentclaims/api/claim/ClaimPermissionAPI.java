package su.nightexpress.excellentclaims.api.claim;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ClaimPermissionAPI {

    boolean hasBypass(Player player);

    boolean hasPermission(Player player, Claim claim, ClaimPermission permission);

}
