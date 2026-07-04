package su.nightexpress.excellentclaims.rules.registrar;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.rules.impl.player.item.UseChorusFruitsRule;
import su.nightexpress.excellentclaims.rules.impl.player.item.UseEnderPearlesRule;
import su.nightexpress.excellentclaims.rules.impl.player.item.UseFireworks;
import su.nightexpress.excellentclaims.rules.impl.player.item.UseHornsRule;
import su.nightexpress.excellentclaims.rules.impl.player.item.UseLighterRule;
import su.nightexpress.excellentclaims.rules.impl.player.item.UseSpawnEggsRule;
import su.nightexpress.excellentclaims.rules.impl.player.projectile.ShootBowRule;
import su.nightexpress.excellentclaims.rules.impl.player.projectile.ThrowEggsRule;
import su.nightexpress.excellentclaims.rules.impl.player.projectile.ThrowEnderEyesRule;
import su.nightexpress.excellentclaims.rules.impl.player.projectile.ThrowPotionsRule;
import su.nightexpress.excellentclaims.rules.impl.player.projectile.ThrowTridentsRule;
import su.nightexpress.excellentclaims.rules.impl.player.projectile.ThrowWindChargesRule;
import su.nightexpress.excellentclaims.rules.load.RuleLoader;

public final class PlayerItemRulesRegistrar {

    private PlayerItemRulesRegistrar() {
    }

    public static void register(RuleLoader loader, ClaimPermissionAPI permissions) {
        loader.addRuleSpec("use_chorus_fruits", new UseChorusFruitsRule(permissions));
        loader.addRuleSpec("use_ender_pearles", new UseEnderPearlesRule(permissions));
        loader.addRuleSpec("use_fireworks", new UseFireworks(permissions));
        loader.addRuleSpec("use_horns", new UseHornsRule(permissions));
        loader.addRuleSpec("use_lighter", new UseLighterRule(permissions));
        loader.addRuleSpec("use_spawn_eggs", new UseSpawnEggsRule(permissions));

        loader.addRuleSpec("throw_eggs", new ThrowEggsRule());
        loader.addRuleSpec("throw_ender_eyes", new ThrowEnderEyesRule());
        loader.addRuleSpec("throw_potions", new ThrowPotionsRule());
        loader.addRuleSpec("throw_tridents", new ThrowTridentsRule());
        loader.addRuleSpec("throw_wind_charges", new ThrowWindChargesRule());
        loader.addRuleSpec("shoot_bow", new ShootBowRule());
    }
}
