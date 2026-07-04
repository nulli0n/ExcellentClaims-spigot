package su.nightexpress.excellentclaims.rules.load;

import java.nio.file.Path;
import java.util.logging.Logger;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.rules.RuleRegistry;
import su.nightexpress.excellentclaims.rules.RulesModule;
import su.nightexpress.nightcore.bridge.key.KeyDomain;
import su.nightexpress.nightcore.bridge.key.KeyPathResolver;

public final class RuleLoaderConfiguration {

    private static final String DIR_RULES = "rules";

    private RuleLoaderConfiguration() {
    }

    public static void configure(RulesModule module, KeyDomain domain, DependencyContainer container) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        Path dir = plugin.dataPath().resolve(DIR_RULES);
        Logger logger = container.get(Logger.class);
        RuleRegistry rules = container.get(RuleRegistry.class);

        KeyPathResolver pathResolver = KeyPathResolver.of(dir, domain);

        RuleLoader loader = new RuleLoader(domain, pathResolver, rules, logger);

        container.register(RuleLoader.class, loader);

        module.addComponent(loader);
    }
}
