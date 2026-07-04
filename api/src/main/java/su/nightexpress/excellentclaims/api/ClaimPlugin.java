package su.nightexpress.excellentclaims.api;

import su.nightexpress.excellentclaims.api.core.EventPublisher;
import su.nightexpress.nightcore.NightCorePlugin;

public interface ClaimPlugin extends NightCorePlugin, EventPublisher {

    void reloadPlugin();
}
