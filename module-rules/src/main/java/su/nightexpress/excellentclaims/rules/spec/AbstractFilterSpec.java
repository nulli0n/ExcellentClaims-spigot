package su.nightexpress.excellentclaims.rules.spec;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.rules.behavior.AbstractFilterBehavior;
import su.nightexpress.excellentclaims.rules.filter.FilteredSet;
import su.nightexpress.excellentclaims.rules.type.FilterRuleType;

@NullMarked
public abstract class AbstractFilterSpec<E extends Event, T> extends AbstractSpec<E, FilteredSet<T>> {

    protected final FilterRuleType<T> filterType;

    public AbstractFilterSpec(Class<E> eventType, FilterRuleType<T> type, RuleCategory category) {
        super(eventType, type, category);
        this.filterType = type;
    }

    public AbstractFilterBehavior.Builder<E, T> behaviorBuilder() {
        return new AbstractFilterBehavior.Builder<>(this.eventType);
    }

    public AbstractFilterBehavior.Builder<E, T> behaviorBuilder(EventPriority priority) {
        return new AbstractFilterBehavior.Builder<>(this.eventType, priority);
    }
}
