package su.nightexpress.excellentclaims.api.core;

import org.bukkit.event.Event;
import org.jspecify.annotations.NonNull;

public interface EventPublisher {

    boolean fireEvent(@NonNull Event event);
}
