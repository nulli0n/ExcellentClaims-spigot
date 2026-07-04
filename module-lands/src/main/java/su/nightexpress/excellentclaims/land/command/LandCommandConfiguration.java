package su.nightexpress.excellentclaims.land.command;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.command.CommandRegistry;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.land.LandsModule;
import su.nightexpress.excellentclaims.land.LandsRepository;
import su.nightexpress.excellentclaims.land.settings.LandSettings;

@NullMarked
public final class LandCommandConfiguration {

    private LandCommandConfiguration() {
    }

    public static void configure(LandsModule module, DependencyContainer container) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        LandSettings settings = container.get(LandSettings.class);
        LandsRepository repository = container.get(LandsRepository.class);

        LandCommandArgumentRegistrar.registerArgumentTypes(repository);

        LandCommandResolver resolver = new LandCommandResolver(repository);
        LandCommandSuggestions suggestions = new LandCommandSuggestions(repository);
        LandCommandController commands = new LandCommandController(plugin, settings);

        container.register(LandCommandResolver.class, resolver);
        container.register(LandCommandSuggestions.class, suggestions);
        container.register(CommandRegistry.class, commands);

        module.addComponent(commands);
    }
}
