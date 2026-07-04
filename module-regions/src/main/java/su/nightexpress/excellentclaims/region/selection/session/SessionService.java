package su.nightexpress.excellentclaims.region.selection.session;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.region.lang.RegionLang;
import su.nightexpress.excellentclaims.region.selection.SelectionPosition;
import su.nightexpress.excellentclaims.region.selection.SelectionService;
import su.nightexpress.excellentclaims.region.selection.ui.SelectionUIService;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;

@NullMarked
public class SessionService {

    private final SessionManager   sessionManager;
    private final SelectionService selectionService;

    @Nullable
    private final SelectionUIService uiService;

    public SessionService(SessionManager sessionManager,
                          SelectionService selectionService,
                          @Nullable SelectionUIService uiService) {
        this.sessionManager = sessionManager;
        this.selectionService = selectionService;
        this.uiService = uiService;
    }

    public boolean isInSelection(Player player) {
        return this.sessionManager.hasSession(player.getUniqueId());
    }

    public ActionResult toggleSession(Player player) {
        if (this.sessionManager.hasSession(player.getUniqueId())) {
            return this.endSession(player);
        }
        else {
            return this.startSession(player);
        }
    }

    public ActionResult startSession(Player player) {
        UUID playerId = player.getUniqueId();

        if (this.sessionManager.hasSession(playerId)) {
            return ActionResult.fail();
        }

        SelectionSession session = new SelectionSession();

        this.sessionManager.addSession(playerId, session);

        if (this.uiService != null) {
            this.uiService.createSessionUI(player, session);
        }

        return ActionResult.ok(RegionLang.SELECTION_START);
    }

    public ActionResult endSession(Player player) {
        UUID playerId = player.getUniqueId();

        SelectionSession session = this.sessionManager.getSession(playerId);
        if (session == null) {
            return ActionResult.fail();
        }

        this.sessionManager.removeSession(playerId);

        if (this.uiService != null) {
            this.uiService.clearSessionUI(player, session);
        }

        return ActionResult.ok(RegionLang.SELECTION_QUIT);
    }

    public ActionResult toggleSelectionPause(Player player) {
        SelectionSession session = this.sessionManager.getSession(player.getUniqueId());
        if (session == null) return ActionResult.fail();

        session.setPaused(!session.isPaused());

        if (this.uiService != null) {
            this.uiService.updateSessionUI(player, session);
        }

        if (session.isPaused()) {
            return ActionResult.ok(RegionLang.SELECTION_PAUSED);
        }
        else {
            return ActionResult.ok(RegionLang.SELECTION_RESUMED);
        }
    }

    public ActionResult selectBlockPosition(Player player, BlockPos blockPos, SelectionPosition position) {
        SelectionSession session = this.sessionManager.getSession(player.getUniqueId());
        if (session == null || session.isPaused()) return ActionResult.fail();

        ActionResult result = this.selectionService.selectBlockPosition(player, session, blockPos, position);
        if (result.success() && this.uiService != null) {
            this.uiService.updateSessionUI(player, session);
        }

        return result;
    }

    public ActionResult createRegionFromSelection(Player player, String name) {
        UUID playerId = player.getUniqueId();

        SelectionSession session = this.sessionManager.getSession(playerId);
        if (session == null) {
            return ActionResult.fail(RegionLang.SELECTION_CREATE_NO_SELECTION);
        }

        BlockPos first = session.getFirst();
        BlockPos second = session.getSecond();
        if (first == null || second == null) {
            return ActionResult.fail(RegionLang.SELECTION_CREATE_INCOMPLETE);
        }

        Cuboid cuboid = new Cuboid(first, second);
        ActionResult result = this.selectionService.claimRegion(player, cuboid, name);

        if (result.success()) {
            this.endSession(player);
        }

        return result;
    }
}
