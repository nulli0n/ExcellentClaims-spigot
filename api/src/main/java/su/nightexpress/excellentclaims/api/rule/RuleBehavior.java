package su.nightexpress.excellentclaims.api.rule;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.rule.context.ActionContext;

@NullMarked
public interface RuleBehavior<E extends ActionContext, T> {

    boolean shouldHandle(E context);

    RuleResult process(E context, ClaimRegistry registry, RuleLookup<T> resolver);

    Class<E> getContextType();

    int getWeight();
}
