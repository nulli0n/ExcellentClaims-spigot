package su.nightexpress.excellentclaims.rules.registrar;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.rules.impl.player.entity.BreakHangingRule;
import su.nightexpress.excellentclaims.rules.impl.player.entity.BreakVehiclesRule;
import su.nightexpress.excellentclaims.rules.impl.player.entity.EntityPlaceRule;
import su.nightexpress.excellentclaims.rules.impl.player.entity.MobInteractionFilterRule;
import su.nightexpress.excellentclaims.rules.impl.player.entity.PlaceHangingRule;
import su.nightexpress.excellentclaims.rules.impl.player.entity.UseArmorStandsRule;
import su.nightexpress.excellentclaims.rules.impl.player.entity.UseItemFramesRule;
import su.nightexpress.excellentclaims.rules.impl.player.entity.UseVehiclesRule;
import su.nightexpress.excellentclaims.rules.impl.player.entity.UseVillagersRule;
import su.nightexpress.excellentclaims.rules.load.RuleLoader;

public final class PlayerEntityRulesRegistrar {

    private PlayerEntityRulesRegistrar() {
    }

    public static void register(RuleLoader loader, ClaimPermissionAPI permissions) {
        loader.addRuleSpec("mob_interaction_filter", new MobInteractionFilterRule(permissions));
        loader.addRuleSpec("use_villagers", new UseVillagersRule(permissions));
        loader.addRuleSpec("use_armor_stands", new UseArmorStandsRule(permissions));
        loader.addRuleSpec("use_item_frames", new UseItemFramesRule(permissions));
        loader.addRuleSpec("use_vehicles", new UseVehiclesRule(permissions));

        loader.addRuleSpec("entity_place", new EntityPlaceRule(permissions));

        loader.addRuleSpec("place_hangings", new PlaceHangingRule(permissions));
        loader.addRuleSpec("break_hangings", new BreakHangingRule(permissions));
        loader.addRuleSpec("break_vehicles", new BreakVehiclesRule(permissions));
    }
}
