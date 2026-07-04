package su.nightexpress.excellentclaims.rules.event;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.rules.registry.RegisteredRule;

@NullMarked
public class EventRegistry {

    private final Map<Class<? extends Event>, Map<EventPriority, List<RegisteredRule<?, ?>>>> eventRules;

    public EventRegistry() {
        this.eventRules = new HashMap<>();
    }

    public void clear() {
        this.eventRules.clear();
    }

    public boolean isRegistered(Class<? extends Event> eventType, EventPriority priority) {
        return this.eventRules.getOrDefault(eventType, Map.of()).containsKey(priority);
    }

    public <E extends Event> void bindRule(Class<E> eventType,
                                           EventPriority priority,
                                           RegisteredRule<E, ?> rule) {
        List<RegisteredRule<?, ?>> rules = this.eventRules
            .computeIfAbsent(eventType, k -> new EnumMap<>(EventPriority.class))
            .computeIfAbsent(priority, k -> new ArrayList<>());

        rules.add(rule);

        // Sort rules by weight (lightweights go first)
        rules.sort(Comparator.comparingInt(registered -> registered.getBehavior().getWeight()));

    }

    public List<RegisteredRule<?, ?>> getRules(Class<? extends Event> eventType, EventPriority priority) {
        return this.eventRules.getOrDefault(eventType, Map.of()).getOrDefault(priority, List.of());
    }
}
