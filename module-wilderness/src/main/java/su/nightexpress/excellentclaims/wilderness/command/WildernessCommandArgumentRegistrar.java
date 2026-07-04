package su.nightexpress.excellentclaims.wilderness.command;

import su.nightexpress.excellentclaims.wilderness.WildernessRepository;
import su.nightexpress.excellentclaims.wilderness.command.argument.WildernessByNameArgumentType;
import su.nightexpress.excellentclaims.wilderness.data.model.WildernessRegion;
import su.nightexpress.nightcore.commands.Arguments;

public final class WildernessCommandArgumentRegistrar {

    private WildernessCommandArgumentRegistrar() {
    }

    public static void registerArgumentTypes(WildernessRepository repository) {
        Arguments.register(WildernessRegion.class, new WildernessByNameArgumentType(repository));
    }
}
