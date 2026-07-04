package su.nightexpress.excellentclaims.rules.registry;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.ClaimRule;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.api.rule.RuleType;
import su.nightexpress.excellentclaims.rules.RulesPlaceholders;
import su.nightexpress.excellentclaims.rules.permission.RulesPermissions;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.PlaceholderResolver;

@NullMarked
public class RegisteredRule<E extends Event, T> implements ClaimRule<T> {

    private final AdaptedKey         key;
    private final RuleCategory       category;
    private final RuleType<T>        type;
    private final RuleBehavior<E, T> behavior;

    private RuleDefinition definition;
    private T              defaultValue;

    public RegisteredRule(AdaptedKey key,
                          RuleCategory category,
                          RuleType<T> type,
                          RuleBehavior<E, T> behavior,
                          RuleDefinition definition,
                          T defaultValue) {
        this.key = key;
        this.category = category;
        this.type = type;
        this.behavior = behavior;
        this.definition = definition;
        this.defaultValue = defaultValue;
    }

    // A simple method to update values from the config in reloads.
    @Override
    public void update(RuleDefinition definition, T defaultValue) {
        this.definition = definition;
        this.defaultValue = defaultValue;
    }

    @Override
    public PlaceholderResolver placeholders() {
        return RulesPlaceholders.RULE.resolver(this);
    }

    @Override
    public AdaptedKey getKey() {
        return this.key;
    }

    @Override
    public RuleCategory getCategory() {
        return this.category;
    }

    @Override
    public RuleType<T> getType() {
        return this.type;
    }

    @Override
    public RuleBehavior<E, T> getBehavior() {
        return this.behavior;
    }

    @Override
    public T getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public String getDisplayName() {
        return this.definition.name();
    }

    @Override
    public List<String> getDescription() {
        return List.copyOf(this.definition.description());
    }

    @Override
    public NightItem getIcon() {
        return this.definition.icon().copy();
    }

    @Override
    public String getPermission() {
        return RulesPermissions.forRule(this.key.value()).getName(); // Use .value() only to avoid plugin namespace for built-in rules
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.hasPermission(this.getPermission());
    }

    @Override
    public String toString() {
        return "RegisteredRule [key=" + key + ", category=" + category + "]";
    }
}
