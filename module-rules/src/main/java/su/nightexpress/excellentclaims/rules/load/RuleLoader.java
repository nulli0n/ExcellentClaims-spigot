package su.nightexpress.excellentclaims.rules.load;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.LifecycleComponent;
import su.nightexpress.excellentclaims.api.rule.ClaimRule;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.api.rule.RuleSpec;
import su.nightexpress.excellentclaims.api.rule.RuleType;
import su.nightexpress.excellentclaims.rules.RuleRegistry;
import su.nightexpress.excellentclaims.rules.registry.RegisteredRule;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.bridge.key.KeyDomain;
import su.nightexpress.nightcore.bridge.key.KeyPathResolver;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;

@NullMarked
public class RuleLoader implements LifecycleComponent {

    private final RuleRegistry    registry;
    private final KeyDomain       domain;
    private final KeyPathResolver pathResolver;
    //private final Logger          logger;

    private final Map<AdaptedKey, RuleSpec<?, ?>> ruleSpecs;

    public RuleLoader(KeyDomain domain, KeyPathResolver pathResolver, RuleRegistry registry, Logger logger) {
        this.domain = domain;
        this.pathResolver = pathResolver;
        this.registry = registry;
        //this.logger = logger;

        this.ruleSpecs = new HashMap<>();
    }

    @Override
    public void reload() {
        this.loadRules();
    }

    @Override
    public void shutdown() {
        this.ruleSpecs.clear();
    }

    @Override
    public void start() {
        this.loadRules();
    }

    public <E extends Event, T> void addRuleSpec(String id, RuleSpec<E, T> spec) {
        AdaptedKey key = this.domain.make(id);
        this.addRuleSpec(key, spec);
    }

    public <E extends Event, T> void addRuleSpec(AdaptedKey key, RuleSpec<E, T> spec) {
        this.ruleSpecs.put(key, spec);
    }

    public void loadRules() {
        // TODO Check disabled props

        this.ruleSpecs.forEach((key, spec) -> {
            Path file = this.pathResolver.resolveFile(key);
            this.loadRule(key, spec, file, "");
        });
    }

    @SuppressWarnings("unchecked")
    private <E extends Event, T> void loadRule(AdaptedKey key,
                                               RuleSpec<E, T> spec,
                                               Path file,
                                               String path) {
        RuleType<T> type = spec.getType();
        ConfigCodec<T> codec = type.getCodec();
        RuleCategory category = spec.getCategory();
        RuleDefinition initOptions = spec.getDefaultDefinition();
        T initValue = spec.getDefaultValue();

        FileConfig config = FileConfig.load(file);
        RuleBehavior<E, T> behavior = spec.createBehavior();

        //try {
        RuleDefinition options = config.getOrSet(path + ".Options", RuleDefinition.class, initOptions);
        T defaultValue = config.getOrSet(path + ".Default-Value", codec, initValue);

        ClaimRule<T> registered = (ClaimRule<T>) this.registry.get(key);
        if (registered != null) {
            registered.update(options, defaultValue);
            return;
        }

        RegisteredRule<E, T> rule = new RegisteredRule<>(key, category, type, behavior, options, defaultValue);
        this.registry.register(rule);
        /* }
        catch (IllegalArgumentException exception) {
            this.logger.log(Level.SEVERE, "Unable to read settings of %s property".formatted(key), exception);
        
            property = new RegisteredRule<>(key, category, type, behavior, initOptions, initValue);
        } */

        config.saveChanges();
    }
}
