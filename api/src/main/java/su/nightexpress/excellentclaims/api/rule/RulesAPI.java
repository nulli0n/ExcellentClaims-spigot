package su.nightexpress.excellentclaims.api.rule;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.data.DirtyFlagger;
import su.nightexpress.excellentclaims.api.rule.tester.RuleEvaluators;
import su.nightexpress.excellentclaims.api.service.ActionResult;

@NullMarked
public interface RulesAPI {

    RuleEvaluators getEvaluators();

    ActionResult canOpenRules(Player player, Claim claim);

    <T extends Claim> ActionResult openRulesMenu(Player player,
                                                 T claim,
                                                 DirtyFlagger<T> flagger,
                                                 @Nullable RuleCategory category,
                                                 @Nullable Runnable onBack);

    RuleRegistryLookup<?> getRegistry();
}
