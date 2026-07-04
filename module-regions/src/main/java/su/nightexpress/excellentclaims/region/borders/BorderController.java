package su.nightexpress.excellentclaims.region.borders;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.core.AbstractController;
import su.nightexpress.excellentclaims.region.borders.session.BorderSession;
import su.nightexpress.excellentclaims.region.borders.session.BorderSessionManager;
import su.nightexpress.excellentclaims.region.borders.settings.BorderSettings;
import su.nightexpress.nightcore.util.Players;

@NullMarked
public class BorderController extends AbstractController {

    private final BorderSessionManager sessionManager;
    private final BorderService        service;
    private final BorderSettings       settings;

    public BorderController(ClaimPlugin plugin,
                            BorderSessionManager sessionManager,
                            BorderService service,
                            BorderSettings settings) {
        super(plugin);
        this.sessionManager = sessionManager;
        this.service = service;
        this.settings = settings;
    }

    @Override
    protected void onReload() {
        this.stopTasks();
        this.runRefreshTask();
    }

    @Override
    protected void onShutdown() {
        this.sessionManager.forEach((playerId, session) -> {
            Player player = Players.getPlayer(playerId);
            if (player == null) return;

            this.clearHighlights(player);
        });
    }

    @Override
    protected void onStart() {
        this.runRefreshTask();
    }

    private void runRefreshTask() {
        this.addAsyncTickTask(this::highlightBounds, this.settings.getRefreshRate());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.clearHighlights(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        this.clearHighlights(event.getPlayer());
    }

    private void clearHighlights(Player player) {
        BorderSession session = this.sessionManager.getSession(player.getUniqueId());
        if (session == null) return;

        this.service.clearHighlight(player, session);
        this.sessionManager.endSession(player.getUniqueId());
    }

    public void highlightBounds() {
        this.sessionManager.forEach((uuid, session) -> {
            Player player = this.plugin.getServer().getPlayer(uuid);
            if (player == null) return;

            this.service.highlightChunkBorders(player, session);
        });
    }
}
