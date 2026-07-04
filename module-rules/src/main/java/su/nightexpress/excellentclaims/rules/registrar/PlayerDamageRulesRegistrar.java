package su.nightexpress.excellentclaims.rules.registrar;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.rules.impl.player.damage.PlayerDamageAnimalsRule;
import su.nightexpress.excellentclaims.rules.impl.player.damage.PlayerDamageArmorStandsRule;
import su.nightexpress.excellentclaims.rules.impl.player.damage.PlayerDamagePlayersRule;
import su.nightexpress.excellentclaims.rules.impl.player.damage.PlayerDamageVillagersRule;
import su.nightexpress.excellentclaims.rules.impl.player.damage.PlayerTakeDamageFilterRule;
import su.nightexpress.excellentclaims.rules.load.RuleLoader;

public final class PlayerDamageRulesRegistrar {

    private PlayerDamageRulesRegistrar() {
    }

    public static void register(RuleLoader loader, ClaimPermissionAPI permissions) {
        loader.addRuleSpec("pvp", new PlayerDamagePlayersRule(permissions));
        loader.addRuleSpec("player_damage_villagers", new PlayerDamageVillagersRule(permissions));
        loader.addRuleSpec("player_damage_animals", new PlayerDamageAnimalsRule(permissions));
        loader.addRuleSpec("player_damage_decorations", new PlayerDamageArmorStandsRule(permissions));
        loader.addRuleSpec("player_incoming_damage_filter", new PlayerTakeDamageFilterRule());
    }
}
