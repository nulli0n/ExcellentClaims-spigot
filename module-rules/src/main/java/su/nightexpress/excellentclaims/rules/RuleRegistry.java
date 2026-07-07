package su.nightexpress.excellentclaims.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleRegistryLookup;
import su.nightexpress.excellentclaims.api.rule.context.ActionContext;
import su.nightexpress.excellentclaims.rules.registry.RegisteredRule;
import su.nightexpress.nightcore.bridge.key.KeyedRegistry;

@NullMarked
public class RuleRegistry extends KeyedRegistry<RegisteredRule<?, ?>> implements RuleRegistryLookup<RegisteredRule<?, ?>> {

    private final Map<RuleCategory, Set<RegisteredRule<?, ?>>>                    rulesByCategory;
    private final Map<Class<? extends ActionContext>, List<RegisteredRule<?, ?>>> rulesByContext;

    public RuleRegistry() {
        super();
        this.rulesByCategory = new HashMap<>();
        this.rulesByContext = new HashMap<>();
    }

    @Override
    public void register(RegisteredRule<?, ?> rule) {
        super.register(rule);

        this.rulesByCategory.computeIfAbsent(rule.getCategory(), k -> new HashSet<>()).add(rule);
        this.bindRuleToContext(rule);
    }

    private <E extends ActionContext> void bindRuleToContext(RegisteredRule<E, ?> rule) {
        RuleBehavior<E, ?> behavior = rule.getBehavior();
        Class<E> contextType = behavior.getContextType();

        List<RegisteredRule<?, ?>> rules = this.rulesByContext
            .computeIfAbsent(contextType, k -> new ArrayList<>());

        rules.add(rule);

        // Sort rules by weight (lightweights go first)
        rules.sort(Comparator.comparingInt(registered -> registered.getBehavior().getWeight()));

    }

    public Set<RegisteredRule<?, ?>> getRulesByCategory(RuleCategory category) {
        return Set.copyOf(this.rulesByCategory.getOrDefault(category, Collections.emptySet()));
    }

    public <C extends ActionContext> List<RegisteredRule<?, ?>> getRulesByContext(Class<C> contextType) {
        return List.copyOf(this.rulesByContext.getOrDefault(contextType, List.of()));
    }
}
