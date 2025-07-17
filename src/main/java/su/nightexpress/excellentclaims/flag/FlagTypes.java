package su.nightexpress.excellentclaims.flag;

import su.nightexpress.excellentclaims.api.flag.FlagType;
import su.nightexpress.excellentclaims.flag.type.BooleanFlagType;
import su.nightexpress.excellentclaims.flag.type.DoubleFlagType;
import su.nightexpress.excellentclaims.flag.type.IntegerFlagType;

public class FlagTypes {

    public static final FlagType<Boolean> BOOLEAN = new BooleanFlagType();
    public static final FlagType<Integer> INTEGER = new IntegerFlagType();
    public static final FlagType<Double>  DOUBLE  = new DoubleFlagType();
}
