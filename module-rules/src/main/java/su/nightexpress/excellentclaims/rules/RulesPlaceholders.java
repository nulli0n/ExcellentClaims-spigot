package su.nightexpress.excellentclaims.rules;

import su.nightexpress.excellentclaims.api.rule.ClaimRule;
import su.nightexpress.nightcore.util.placeholder.TypedPlaceholder;

public final class RulesPlaceholders {

    public static final String RULE_KEY         = "%rule_key%";
    public static final String RULE_NAME        = "%rule_name%";
    public static final String RULE_DESCRIPTION = "%rule_description%";

    public static final TypedPlaceholder<ClaimRule<?>> RULE = TypedPlaceholder.<ClaimRule<?>>builder()
        .with(RULE_KEY, rule -> rule.key().asString())
        .with(RULE_NAME, ClaimRule::getDisplayName)
        .with(RULE_DESCRIPTION, rule -> String.join("\n", rule.getDescription()))
        .build();

    private RulesPlaceholders() {
    }
}
