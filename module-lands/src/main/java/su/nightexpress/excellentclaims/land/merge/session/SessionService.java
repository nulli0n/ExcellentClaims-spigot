package su.nightexpress.excellentclaims.land.merge.session;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.land.MergeType;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.merge.settings.MergeSettings;
import su.nightexpress.excellentclaims.land.merge.tool.MergeToolService;
import su.nightexpress.nightcore.util.TimeUtil;

@NullMarked
public class SessionService {

    private final SessionCache     cache;
    private final MergeToolService tools;
    private final MergeSettings    settings;

    public SessionService(SessionCache cache, MergeToolService tools, MergeSettings settings) {
        this.cache = cache;
        this.tools = tools;
        this.settings = settings;
    }

    public void startSession(Player player, LandClaim claim, MergeType type) {
        if (type == MergeType.MERGE) this.tools.giveMergeTool(player);
        else this.tools.giveSplitTool(player);

        long timeoutDate = TimeUtil.createFutureTimestamp(this.settings.getSessionTimeout());

        Session session = new Session(player.getUniqueId(), claim, type, timeoutDate);

        this.cache.addSession(session);
    }

    public void endSession(Player player) {
        this.tools.takeAllTools(player);
        this.cache.removeSession(player.getUniqueId());
    }

    public @Nullable Session getActiveSession(Player player) {
        Session session = this.cache.getSession(player.getUniqueId());
        return session != null && session.isValid() ? session : null;
    }
}
