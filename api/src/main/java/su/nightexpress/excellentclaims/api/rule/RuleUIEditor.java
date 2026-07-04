package su.nightexpress.excellentclaims.api.rule;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;

@NullMarked
public interface RuleUIEditor<T> {

    void onClick(Player player, Claim claim, ClaimRule<T> rule, EditorContext context);
}
