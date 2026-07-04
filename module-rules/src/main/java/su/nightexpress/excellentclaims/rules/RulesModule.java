package su.nightexpress.excellentclaims.rules;

import java.nio.file.Path;
import java.util.Optional;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.core.AbstractModule;
import su.nightexpress.excellentclaims.api.core.ConfigurableModule;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.api.rule.ClaimRule;
import su.nightexpress.excellentclaims.api.rule.RuleResolver;
import su.nightexpress.nightcore.bridge.key.KeyDomain;

@NullMarked
public class RulesModule extends AbstractModule implements ConfigurableModule, RuleResolver {

    private final Path         moduleDir;
    private final KeyDomain    domain;
    private final RuleRegistry registry;

    public RulesModule(Path moduleDir, KeyDomain domain, RuleRegistry registry) {
        super(Identifier.of("rules"));
        this.moduleDir = moduleDir;
        this.domain = domain;
        this.registry = registry;
    }

    @Override
    protected void onReload() {

    }

    @Override
    protected void onShutdown() {
        this.registry.clear();
    }


    @Override
    protected void onStart() {

    }

    @Override
    public Path getModuleDir() {
        return this.moduleDir;
    }

    @Override
    public @Nullable ClaimRule<?> resolve(String name) {
        Optional<ClaimRule<?>> optional = this.domain.resolve(name).map(this.registry::get);
        return optional.orElse(null);
    }

    public RuleRegistry getRegistry() {
        return this.registry;
    }
}
