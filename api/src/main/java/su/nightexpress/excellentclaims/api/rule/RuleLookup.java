package su.nightexpress.excellentclaims.api.rule;

import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.bridge.registry.NRegistry;

public interface RuleLookup<T extends ClaimRule<?>> extends NRegistry<AdaptedKey, T> {

}
