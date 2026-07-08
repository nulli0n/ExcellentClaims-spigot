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

    /**
     * Retrieves the evaluator factory used to test player, entity, and environment actions against claim rules.
     * 
     * @return
     */
    RuleEvaluators getEvaluators();

    /**
     * Evaluates if the specified player has the required permissions to modify the rules of the given claim.
     * 
     * @param player
     * @param claim
     * @return
     */
    ActionResult canOpenRules(Player player, Claim claim);

    /**
     * Opens the interactive GUI for managing rules of a specified claim. Accepts a DirtyFlagger for saving states, an
     * optional RuleCategory filter, and an optional back-button Runnable.
     * 
     * @param <T>
     * @param player
     * @param claim
     * @param flagger
     * @param category
     * @param onBack
     * @return
     */
    <T extends Claim> ActionResult openRulesMenu(Player player,
                                                 T claim,
                                                 DirtyFlagger<T> flagger,
                                                 @Nullable RuleCategory category,
                                                 @Nullable Runnable onBack);

    /**
     * Retrieves the centralized registry containing all registered rule
     * definitions.
     * 
     * @return
     */
    RuleRegistryLookup<?> getRegistry();
}
