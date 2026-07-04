package su.nightexpress.excellentclaims.region.command;

import su.nightexpress.excellentclaims.region.RegionsRepository;
import su.nightexpress.excellentclaims.region.command.argument.RegionByNameArgumentType;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.nightcore.commands.Arguments;

public final class RegionCommandArgumentRegistrar {

    private RegionCommandArgumentRegistrar() {
    }

    public static void registerArgumentTypes(RegionsRepository repository) {
        Arguments.register(RegionClaim.class, new RegionByNameArgumentType(repository));
    }
}
