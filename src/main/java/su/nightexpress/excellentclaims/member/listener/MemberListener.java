package su.nightexpress.excellentclaims.member.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.config.Config;
import su.nightexpress.excellentclaims.member.MemberManager;
import su.nightexpress.nightcore.manager.AbstractListener;

public class MemberListener extends AbstractListener<ClaimPlugin> {

    //private final MemberManager manager;

    public MemberListener(@NotNull ClaimPlugin plugin, @NotNull MemberManager manager) {
        super(plugin);
        //this.manager = manager;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        if (!Config.GENERAL_UPDATE_PLAYER_NAMES.get()) return;
        if (!plugin.getServer().getOnlineMode()) return;

        Player player = event.getPlayer();

        this.plugin.runTaskAsync(task -> {
            this.plugin.getClaimManager().getStorage().getClaimsByOwner(player).forEach(claim -> {
                if (claim.getOwner().updatePlayerName(player)) {
                    claim.setSaveRequired(true);
                }
            });
        });
    }
}
