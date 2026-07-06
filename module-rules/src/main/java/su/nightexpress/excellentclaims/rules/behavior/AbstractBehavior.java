package su.nightexpress.excellentclaims.rules.behavior;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleContext;
import su.nightexpress.excellentclaims.api.rule.RuleResult;

@NullMarked
public class AbstractBehavior<E extends Event, T> implements RuleBehavior<E, T> {

    private final Class<E>      eventType;
    private final EventPriority priority;
    private final int           weight;

    private final HandleCondition<E>  condition;
    private final RuleProcessor<E, T> processor;

    private final EventProcessor<E> eventDenier;
    private final EventProcessor<E> eventAllower;

    private final PlayerExtractor<E> playerExtractor;

    AbstractBehavior(BaseBuilder<E, T, ?> builder) {
        this.eventType = builder.eventType;
        this.priority = builder.priority;
        this.weight = builder.weight;

        this.condition = builder.condition;
        this.processor = builder.processor;

        this.eventDenier = builder.eventDenier;
        this.eventAllower = builder.eventAllower;

        this.playerExtractor = builder.playerExtractor;
    }

    @Override
    public @Nullable Player getUser(E event) {
        return this.playerExtractor.getPlayer(event);
    }

    @Override
    public RuleResult process(E event, ClaimRegistry claims, RuleContext<T> context) {
        return this.processor.process(event, claims, context);
    }

    @Override
    public boolean shouldHandle(E event) {
        return this.condition.shouldHandle(event);
    }

    @Override
    public void denyEvent(E event) {
        this.eventDenier.setResult(event);
    }

    @Override
    public void allowEvent(E event) {
        this.eventAllower.setResult(event);
    }

    @Override
    public Class<E> getEventType() {
        return this.eventType;
    }

    @Override
    public EventPriority getEventPriority() {
        return this.priority;
    }

    @Override
    public int getWeight() {
        return this.weight;
    }

    @FunctionalInterface
    public interface HandleCondition<E extends Event> {

        boolean shouldHandle(E event);
    }

    @FunctionalInterface
    public interface EventProcessor<E extends Event> {

        void setResult(E event);
    }

    @FunctionalInterface
    public interface RuleProcessor<E extends Event, T> {

        RuleResult process(@NonNull E event, ClaimRegistry registry, RuleContext<T> context);
    }

    @FunctionalInterface
    public interface PlayerExtractor<E extends Event> {

        @Nullable
        Player getPlayer(E event);
    }

    public static abstract class BaseBuilder<E extends Event, T, B extends BaseBuilder<E, T, B>> {

        protected final Class<E> eventType;

        protected EventPriority priority;
        protected int           weight;

        protected HandleCondition<E>  condition;
        protected RuleProcessor<E, T> processor;

        protected EventProcessor<E> eventDenier;
        protected EventProcessor<E> eventAllower;

        protected PlayerExtractor<E> playerExtractor;

        public BaseBuilder(Class<E> eventType) {
            this(eventType, EventPriority.NORMAL);
        }

        public BaseBuilder(Class<E> eventType, EventPriority priority) {
            this.eventType = eventType;
            this.priority = priority;
            this.condition = event -> true;

            this.processor = (event, registry, resolved) -> {
                return RuleResult.pass();
            };

            this.eventDenier = event -> {
                if (event instanceof Cancellable cancellable) {
                    cancellable.setCancelled(true);
                }
            };

            this.eventAllower = event -> {
                // Do nothing currently
            };

            this.playerExtractor = event -> null;
        }

        protected abstract B getThis();

        public B eventPriority(EventPriority priority) {
            this.priority = priority;
            return this.getThis();
        }

        public B weight(int weight) {
            this.weight = weight;
            return this.getThis();
        }

        public B shouldHandle(HandleCondition<E> condition) {
            this.condition = condition;
            return this.getThis();
        }

        public B process(RuleProcessor<E, T> processor) {
            this.processor = processor;
            return this.getThis();
        }

        public B onDeny(EventProcessor<E> processor) {
            this.eventDenier = processor;
            return this.getThis();
        }

        public B onAllow(EventProcessor<E> processor) {
            this.eventAllower = processor;
            return this.getThis();
        }

        public B playerExtractor(PlayerExtractor<E> playerExtractor) {
            this.playerExtractor = playerExtractor;
            return this.getThis();
        }
    }

    public static class Builder<E extends Event, T> extends BaseBuilder<E, T, Builder<E, T>> {

        public Builder(Class<E> eventType) {
            super(eventType);
        }

        public Builder(Class<E> eventType, EventPriority priority) {
            super(eventType, priority);
        }

        public AbstractBehavior<E, T> build() {
            return new AbstractBehavior<>(this);
        }

        @Override
        protected Builder<E, T> getThis() {
            return this;
        }
    }
}
