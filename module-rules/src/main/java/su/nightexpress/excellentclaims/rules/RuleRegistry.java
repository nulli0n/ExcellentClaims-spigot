package su.nightexpress.excellentclaims.rules;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleLookup;
import su.nightexpress.excellentclaims.rules.registry.RegisteredRule;
import su.nightexpress.nightcore.bridge.key.KeyedRegistry;

@NullMarked
public class RuleRegistry extends KeyedRegistry<RegisteredRule<?, ?>> implements RuleLookup<RegisteredRule<?, ?>> {

    private final Map<RuleCategory, Set<RegisteredRule<?, ?>>> categoryMap;

    public RuleRegistry() {
        super();
        this.categoryMap = new HashMap<>();
    }

    @Override
    public void register(RegisteredRule<?, ?> rule) {
        super.register(rule);
        this.categoryMap.computeIfAbsent(rule.getCategory(), k -> new HashSet<>()).add(rule);
    }

    public Set<RegisteredRule<?, ?>> values(RuleCategory category) {
        return Set.copyOf(this.categoryMap.getOrDefault(category, Collections.emptySet()));
    }
}
