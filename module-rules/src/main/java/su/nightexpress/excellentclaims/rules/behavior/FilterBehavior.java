package su.nightexpress.excellentclaims.rules.behavior;

import java.util.Set;

import org.bukkit.event.Event;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.rules.filter.FilteredSet;

@NullMarked
public interface FilterBehavior<E extends Event, T> extends RuleBehavior<E, FilteredSet<T>> {

    Set<T> getAllEntries();

    boolean isAllowedEntry(T entry);
}
