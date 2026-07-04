package su.nightexpress.excellentclaims.rules.editor.impl;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimRules;
import su.nightexpress.excellentclaims.api.rule.ClaimRule;
import su.nightexpress.excellentclaims.api.rule.EditorContext;
import su.nightexpress.excellentclaims.api.rule.RuleUIEditor;

@NullMarked
public class BooleanEditor implements RuleUIEditor<Boolean> {

    @Override
    public void onClick(Player player, Claim claim, ClaimRule<Boolean> rule, EditorContext context) {
        ClaimRules rules = claim.getRules();
        boolean current = rules.getOrDefault(rule);

        rules.set(rule, !current);
        context.markDirty();
        context.callback();
    }
}
