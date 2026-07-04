package su.nightexpress.excellentclaims.land.borders.session;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

import org.jspecify.annotations.Nullable;

public class BorderSessionManager {

    private final Map<UUID, BorderSession> sessionMap;

    public BorderSessionManager() {
        this.sessionMap = new ConcurrentHashMap<>();
    }

    public boolean hasSession(UUID playerId) {
        return this.getSession(playerId) != null;
    }

    public void addSession(UUID playerId, BorderSession session) {
        this.sessionMap.put(playerId, session);
    }

    public void endSession(UUID playerId) {
        this.sessionMap.remove(playerId);
    }

    public @Nullable BorderSession getSession(UUID playerId) {
        return this.sessionMap.get(playerId);
    }

    public void forEach(BiConsumer<UUID, BorderSession> consumer) {
        Map.copyOf(this.sessionMap).forEach(consumer);
    }
}
