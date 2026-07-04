package su.nightexpress.excellentclaims.land.merge.session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jspecify.annotations.Nullable;

public class SessionCache {

    //private final int                timeout;
    private final Map<UUID, Session> cacheMap;

    public SessionCache() {
        //this.timeout = timeout;
        this.cacheMap = new HashMap<>();
    }

    public void addSession(Session session) {
        this.cacheMap.put(session.getPlayerId(), session);
    }

    public void removeSession(UUID playerId) {
        this.cacheMap.remove(playerId);
    }

    public @Nullable Session getSession(UUID playerId) {
        return this.cacheMap.get(playerId);
    }
}
