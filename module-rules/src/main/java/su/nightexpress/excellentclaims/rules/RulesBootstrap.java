package su.nightexpress.excellentclaims.rules;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.ClaimsConstants;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.core.SimpleDependencies;
import su.nightexpress.excellentclaims.api.rule.RuleResolver;
import su.nightexpress.excellentclaims.api.rule.RulesAPI;
import su.nightexpress.excellentclaims.rules.event.EventConfiguration;
import su.nightexpress.excellentclaims.rules.lang.RulesLangInjector;
import su.nightexpress.excellentclaims.rules.load.RuleLoaderConfiguration;
import su.nightexpress.excellentclaims.rules.permission.RulesPermissionsInjector;
import su.nightexpress.excellentclaims.rules.registrar.DefaultRulesRegistrar;
import su.nightexpress.excellentclaims.rules.settings.RulesSettingsConfiguration;
import su.nightexpress.excellentclaims.rules.ui.RuleUIConfiguration;
import su.nightexpress.excellentclaims.rules.ui.RuleUIService;
import su.nightexpress.nightcore.bridge.key.KeyDomain;

@NullMarked
public final class RulesBootstrap {

    private RulesBootstrap() {
    }

    public static RulesModule bootstrap(DependencyContainer container) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        DependencyContainer rulesContainer = new SimpleDependencies(container);

        KeyDomain domain = KeyDomain.of(plugin);
        Path moduleDir = plugin.dataPath().resolve(ClaimsConstants.DIR_CONFIG).resolve("rules");

        RuleRegistry rules = new RuleRegistry();
        RulesModule module = new RulesModule(moduleDir, domain, rules);

        rulesContainer.register(RuleRegistry.class, rules);
        rulesContainer.register(RuleResolver.class, module);

        RulesLangInjector.inject(plugin);
        RulesPermissionsInjector.inject(plugin);

        // Register config codecs & default rule specs.
        RulesCodecsRegistrar.register(rulesContainer);

        // Configure settings & message dispatcher using prefix from settings.
        RulesSettingsConfiguration.configure(module, rulesContainer);

        // Configure the rule loader.
        RuleLoaderConfiguration.configure(module, domain, rulesContainer);

        // Register default rules into loader.
        DefaultRulesRegistrar.register(rulesContainer);

        // Configure event controller to register listeners for registered (loaded) rules.
        EventConfiguration.configure(module, rulesContainer);

        // Configure UI
        RuleUIConfiguration.configure(module, rulesContainer);

        // Configure public API
        RulesAPI api = configureAPI(rulesContainer);

        container.register(RulesAPI.class, api);

        return module;
    }

    private static RulesAPI configureAPI(DependencyContainer container) {
        RuleRegistry rules = container.get(RuleRegistry.class);
        RuleUIService uiService = container.get(RuleUIService.class);

        return new RulesAPIProvider(rules, uiService);
    }
}
