package su.nightexpress.excellentclaims.rules.behavior;

import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.context.ActionContext;
import su.nightexpress.excellentclaims.rules.filter.FilteredSet;

@NullMarked
public class AbstractFilterBehavior<E extends ActionContext, T> extends AbstractBehavior<E, FilteredSet<T>> implements FilterBehavior<E, T> {

    private final Supplier<Set<T>> valuesSupplier;
    private final Predicate<T>     entryCondition;

    public AbstractFilterBehavior(Builder<E, T> builder) {
        super(builder);
        this.valuesSupplier = builder.valuesSupplier;
        this.entryCondition = builder.entryCondition;
    }

    @Override
    public Set<T> getAllEntries() {
        return this.valuesSupplier.get();
    }

    @Override
    public boolean isAllowedEntry(T entry) {
        return this.entryCondition.test(entry);
    }

    public static class Builder<E extends ActionContext, T> extends AbstractBehavior.BaseBuilder<E, FilteredSet<T>, Builder<E, T>> {

        private Supplier<Set<T>> valuesSupplier = Set::of;
        private Predicate<T>     entryCondition = item -> true;

        public Builder(Class<E> eventType) {
            super(eventType);
        }

        @Override
        protected Builder<E, T> getThis() {
            return this;
        }

        public AbstractFilterBehavior<E, T> build() {
            return new AbstractFilterBehavior<>(this);
        }

        public Builder<E, T> allValues(Supplier<Set<T>> valuesSupplier) {
            this.valuesSupplier = valuesSupplier;
            return this;
        }

        @Deprecated
        public Builder<E, T> entryCondition(Predicate<T> entryCondition) {
            this.entryCondition = entryCondition;
            return this;
        }

        // TODO Precache method to extract objects form event and use later in pipeline?
    }
}
