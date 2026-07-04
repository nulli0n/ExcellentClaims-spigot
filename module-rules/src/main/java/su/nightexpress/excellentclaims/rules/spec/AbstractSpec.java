package su.nightexpress.excellentclaims.rules.spec;

import org.bukkit.event.Event;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleSpec;
import su.nightexpress.excellentclaims.api.rule.RuleType;

@NullMarked
public abstract class AbstractSpec<E extends Event, T> implements RuleSpec<E, T> {

    protected final Class<E>     eventType;
    protected final RuleType<T>  type;
    protected final RuleCategory category;

    public AbstractSpec(Class<E> eventType, RuleType<T> type, RuleCategory category) {
        this.eventType = eventType;
        this.type = type;
        this.category = category;
    }

    @Override
    public RuleCategory getCategory() {
        return this.category;
    }

    @Override
    public RuleType<T> getType() {
        return this.type;
    }
}
