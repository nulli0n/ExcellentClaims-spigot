package su.nightexpress.excellentclaims.rules.type;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.EntityType;

import su.nightexpress.excellentclaims.api.rule.RuleType;
import su.nightexpress.excellentclaims.rules.filter.adapter.StandardAdapters;
import su.nightexpress.excellentclaims.rules.filter.adapter.impl.CommandAdapter;
import su.nightexpress.excellentclaims.rules.filter.display.CommandDisplay;
import su.nightexpress.excellentclaims.rules.filter.display.DamageTypeDisplay;
import su.nightexpress.excellentclaims.rules.filter.display.EntityTypeDisplay;
import su.nightexpress.excellentclaims.rules.filter.display.MaterialDisplay;
import su.nightexpress.excellentclaims.rules.type.impl.BoolRuleType;
import su.nightexpress.excellentclaims.rules.type.impl.DoubleRuleType;
import su.nightexpress.excellentclaims.rules.type.impl.IntRuleType;
import su.nightexpress.excellentclaims.rules.type.impl.LongRuleType;
import su.nightexpress.excellentclaims.rules.type.impl.StringRuleType;

public final class RuleTypes {

    public static final RuleType<Boolean> BOOLEAN = BoolRuleType.INSTANCE;
    public static final RuleType<Integer> INT     = IntRuleType.INSTANCE;
    public static final RuleType<Long>    LONG    = LongRuleType.INSTANCE;
    public static final RuleType<Double>  DOUBLE  = DoubleRuleType.INSTANCE;
    public static final RuleType<String>  STRING  = StringRuleType.INSTANCE;

    public static final FilterRuleType<Command>    COMMANDS     = new FilterRuleType<>(CommandAdapter.INSTANCE, CommandDisplay.INSTANCE);
    public static final FilterRuleType<Material>   MATERIALS    = new FilterRuleType<>(StandardAdapters.MATERIAL, MaterialDisplay.INSTANCE);
    public static final FilterRuleType<EntityType> ENTITY_TYPES = new FilterRuleType<>(StandardAdapters.ENTITY_TYPE, EntityTypeDisplay.INSTANCE);
    public static final FilterRuleType<DamageType> DAMAGE_TYPES = new FilterRuleType<>(StandardAdapters.DAMAGE_TYPE, DamageTypeDisplay.INSTANCE);

    private RuleTypes() {
    }
}
