package su.nightexpress.excellentclaims.api.rule;

import org.jspecify.annotations.NonNull;

import su.nightexpress.excellentclaims.api.rule.context.ActionContext;

public interface RuleSpec<E extends ActionContext, T> {

    @NonNull
    RuleCategory getCategory();

    @NonNull
    RuleType<T> getType();

    @NonNull
    RuleDefinition getDefaultDefinition();

    @NonNull
    T getDefaultValue();

    @NonNull
    RuleBehavior<E, T> createBehavior();
}
