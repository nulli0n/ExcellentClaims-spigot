package su.nightexpress.excellentclaims.region.selection.session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class SessionManager {

    private final Map<UUID, SelectionSession> sessionMap;

    public SessionManager() {
        this.sessionMap = new HashMap<>();
    }

    public boolean hasSession(UUID playerId) {
        return this.sessionMap.containsKey(playerId);
    }

    public @Nullable SelectionSession getSession(UUID playerId) {
        return this.sessionMap.get(playerId);
    }

    public void addSession(UUID playerId, SelectionSession session) {
        this.sessionMap.put(playerId, session);
    }

    public void removeSession(UUID playerId) {
        this.sessionMap.remove(playerId);
    }

    public void forEach(BiConsumer<UUID, SelectionSession> consumer) {
        Map.copyOf(this.sessionMap).forEach(consumer);
    }
}
