package su.nightexpress.excellentclaims.region.borders.session;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.region.borders.BorderService;
import su.nightexpress.excellentclaims.region.lang.RegionLang;

@NullMarked
public class BorderSessionService {

    private final BorderService        service;
    private final BorderSessionManager sessionManager;

    public BorderSessionService(BorderService service, BorderSessionManager sessionManager) {
        this.service = service;
        this.sessionManager = sessionManager;
    }

    public ActionResult toggleChunkBounds(Player player) {
        if (this.sessionManager.hasSession(player.getUniqueId())) {
            return this.disableChunkBounds(player);
        }
        else {
            return this.enableChunkBounds(player);
        }
    }

    public ActionResult enableChunkBounds(Player player) {
        if (this.sessionManager.hasSession(player.getUniqueId())) {
            return ActionResult.fail();
        }

        BorderSession session = new BorderSession();

        this.service.highlightChunkBorders(player, session);
        this.sessionManager.addSession(player.getUniqueId(), session);

        return ActionResult.ok(RegionLang.REGION_BORDERS_ENABLED);
    }

    public ActionResult disableChunkBounds(Player player) {
        BorderSession session = this.sessionManager.getSession(player.getUniqueId());
        if (session == null) {
            return ActionResult.fail();
        }

        this.service.clearHighlight(player, session);
        this.sessionManager.endSession(player.getUniqueId());

        return ActionResult.ok(RegionLang.REGION_BORDERS_DISABLED);
    }
}
