package su.nightexpress.excellentclaims.region.selection.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.core.AbstractController;
import su.nightexpress.excellentclaims.region.selection.session.SessionManager;
import su.nightexpress.excellentclaims.region.selection.ui.settings.SelectionUISettings;
import su.nightexpress.nightcore.util.Players;

@NullMarked
public class SelectionUIController extends AbstractController {

    private final SelectionUISettings settings;
    private final SessionManager      sessionManager;
    private final SelectionUIService  uiService;

    public SelectionUIController(ClaimPlugin plugin,
                                 SelectionUISettings settings,
                                 SessionManager sessionManager,
                                 SelectionUIService uiService) {
        super(plugin);
        this.settings = settings;
        this.sessionManager = sessionManager;
        this.uiService = uiService;
    }

    @Override
    protected void onReload() {
        this.stopTasks();
        this.runUpdateTask();
    }

    @Override
    protected void onShutdown() {

    }

    @Override
    protected void onStart() {
        this.runUpdateTask();
    }

    private void runUpdateTask() {
        this.addAsyncTickTask(this::updateSelectionHighlight, this.settings.getComponentRefreshRate());
    }

    private void updateSelectionHighlight() {
        this.sessionManager.forEach((playerId, session) -> {
            Player player = Players.getPlayer(playerId);
            if (player == null) return;

            this.uiService.updateSessionUI(player, session);
        });
    }
}
