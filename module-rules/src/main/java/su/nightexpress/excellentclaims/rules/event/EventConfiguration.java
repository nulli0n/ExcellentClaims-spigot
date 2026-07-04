package su.nightexpress.excellentclaims.rules.event;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.rules.RuleRegistry;
import su.nightexpress.excellentclaims.rules.RulesModule;

public final class EventConfiguration {

    private EventConfiguration() {
    }

    public static void configure(RulesModule module, DependencyContainer container) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        ClaimRegistry claims = container.get(ClaimRegistry.class);
        RuleRegistry rules = container.get(RuleRegistry.class);
        ClaimPermissionAPI permissions = container.get(ClaimPermissionAPI.class);
        MessageDispatcher dispatcher = container.get(MessageDispatcher.class);

        EventController eventManager = new EventController(plugin, rules, claims, permissions, dispatcher);

        module.addComponent(eventManager);
    }
}
