package su.nightexpress.excellentclaims.rules.handle;

import org.bukkit.event.Event;
import org.jspecify.annotations.NonNull;

public interface PropertyEventCondition<E extends Event> {

    boolean shouldHandle(@NonNull E event);
}
