package su.nightexpress.excellentclaims.rules.behavior;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleLookup;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.api.rule.context.ActionContext;

@NullMarked
public class AbstractBehavior<E extends ActionContext, T> implements RuleBehavior<E, T> {

    private final Class<E> eventType;
    private final int      weight;

    private final HandleCondition<E>  condition;
    private final RuleProcessor<E, T> processor;

    AbstractBehavior(BaseBuilder<E, T, ?> builder) {
        this.eventType = builder.contextType;
        this.weight = builder.weight;

        this.condition = builder.condition;
        this.processor = builder.processor;
    }

    @Override
    public RuleResult process(E event, ClaimRegistry claims, RuleLookup<T> context) {
        return this.processor.process(event, claims, context);
    }

    @Override
    public boolean shouldHandle(E event) {
        return this.condition.shouldHandle(event);
    }

    @Override
    public Class<E> getContextType() {
        return this.eventType;
    }

    @Override
    public int getWeight() {
        return this.weight;
    }

    @FunctionalInterface
    public interface HandleCondition<E extends ActionContext> {

        boolean shouldHandle(E event);
    }

    @FunctionalInterface
    public interface RuleProcessor<E extends ActionContext, T> {

        RuleResult process(@NonNull E event, ClaimRegistry registry, RuleLookup<T> context);
    }

    public static abstract class BaseBuilder<E extends ActionContext, T, B extends BaseBuilder<E, T, B>> {

        protected final Class<E> contextType;
        protected int            weight;

        protected HandleCondition<E>  condition;
        protected RuleProcessor<E, T> processor;

        public BaseBuilder(Class<E> eventType) {
            this.contextType = eventType;
            this.condition = event -> true;

            this.processor = (event, registry, resolved) -> {
                return RuleResult.pass();
            };
        }

        protected abstract B getThis();

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
    }

    public static class Builder<E extends ActionContext, T> extends BaseBuilder<E, T, Builder<E, T>> {

        public Builder(Class<E> eventType) {
            super(eventType);
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
