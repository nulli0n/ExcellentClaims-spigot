package su.nightexpress.excellentclaims.rules.api;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.data.DirtyFlagger;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RulesAPI;
import su.nightexpress.excellentclaims.api.rule.tester.RuleEvaluators;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.rules.RuleRegistry;
import su.nightexpress.excellentclaims.rules.evaluation.tester.RuleEvaluatorsProvider;
import su.nightexpress.excellentclaims.rules.ui.RuleUIService;
import su.nightexpress.excellentclaims.rules.ui.menu.RuleListContext;

@NullMarked
public class RulesAPIProvider implements RulesAPI {

    private final RuleRegistry           registry;
    private final RuleUIService          uiService;
    private final RuleEvaluatorsProvider evaluators;

    public RulesAPIProvider(RuleRegistry registry, RuleUIService uiService, RuleEvaluatorsProvider evaluators) {
        this.registry = registry;
        this.uiService = uiService;
        this.evaluators = evaluators;
    }

    @Override
    public RuleEvaluators getEvaluators() {
        return this.evaluators;
    }

    @Override
    public ActionResult canOpenRules(Player player, Claim claim) {
        return this.uiService.canOpenRules(player, claim);
    }

    @Override
    public <T extends Claim> ActionResult openRulesMenu(Player player,
                                                        T claim,
                                                        DirtyFlagger<T> flagger,
                                                        @Nullable RuleCategory category,
                                                        @Nullable Runnable onBack) {

        Runnable dirtFlag = () -> flagger.markDirty(claim);
        RuleListContext context = new RuleListContext(claim, dirtFlag, category, onBack);

        return this.uiService.openRules(player, context);
    }

    @Override
    public RuleRegistry getRegistry() {
        return this.registry;
    }
}
