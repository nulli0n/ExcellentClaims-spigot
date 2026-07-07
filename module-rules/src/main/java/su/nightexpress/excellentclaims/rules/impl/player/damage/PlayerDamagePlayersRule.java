package su.nightexpress.excellentclaims.rules.impl.player.damage;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityDamageContext;
import su.nightexpress.excellentclaims.rules.impl.base.BasePlayerDamageEntityRule;

@NullMarked
public class PlayerDamagePlayersRule extends BasePlayerDamageEntityRule {

    public PlayerDamagePlayersRule(ClaimPermissionAPI permissions) {
        super(permissions);
    }

    @Override
    protected boolean shouldHandle(EntityDamageContext context, Entity entity) {
        return entity instanceof Player;
    }

    @Override
    protected @Nullable ClaimPermission getClaimPermission() {
        return null; // So claim members can not bypass this.
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("PvP")
            .description(
                "Controls whether players",
                "can damage each other here."
            )
            .icon(Material.DIAMOND_SWORD)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return false;
    }
}
