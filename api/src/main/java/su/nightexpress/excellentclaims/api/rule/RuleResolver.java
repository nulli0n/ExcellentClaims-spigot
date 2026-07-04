package su.nightexpress.excellentclaims.api.rule;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface RuleResolver {

    @Nullable
    ClaimRule<?> resolve(String name);

}
