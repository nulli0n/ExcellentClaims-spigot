package su.nightexpress.excellentclaims.api;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.EventPublisher;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.bridge.placeholder.PlaceholderProvider;

@NullMarked
public interface ClaimPlugin extends NightCorePlugin, EventPublisher {

    void reloadPlugin();

    void addGlobalPlaceholders(PlaceholderProvider provider);
}
