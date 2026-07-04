package su.nightexpress.excellentclaims.region.command;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.command.CommandRegistry;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.region.RegionsModule;
import su.nightexpress.excellentclaims.region.RegionsRepository;
import su.nightexpress.excellentclaims.region.settings.RegionSettings;

@NullMarked
public final class RegionCommandConfiguration {

    private RegionCommandConfiguration() {
    }

    public static void configure(RegionsModule module, DependencyContainer container) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        RegionSettings settings = container.get(RegionSettings.class);
        RegionsRepository repository = container.get(RegionsRepository.class);

        RegionCommandArgumentRegistrar.registerArgumentTypes(repository);

        RegionCommandResolver resolver = new RegionCommandResolver(repository);
        RegionCommandSuggestions suggestions = new RegionCommandSuggestions(repository);
        RegionCommandController commands = new RegionCommandController(plugin, settings);

        container.register(RegionCommandResolver.class, resolver);
        container.register(RegionCommandSuggestions.class, suggestions);
        container.register(CommandRegistry.class, commands);

        module.addComponent(commands);
    }
}
