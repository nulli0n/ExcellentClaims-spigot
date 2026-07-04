package su.nightexpress.excellentclaims.land.command;

import su.nightexpress.excellentclaims.api.claim.ClaimRepository;
import su.nightexpress.excellentclaims.land.command.argument.LandByNameArgumentType;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.nightcore.commands.Arguments;

public final class LandCommandArgumentRegistrar {

    private LandCommandArgumentRegistrar() {
    }

    public static void registerArgumentTypes(ClaimRepository<LandClaim> repository) {
        Arguments.register(LandClaim.class, new LandByNameArgumentType(repository));
    }
}
