package su.nightexpress.excellentclaims.wilderness.command;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.command.CommandRegistry;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.wilderness.WildernessModule;
import su.nightexpress.excellentclaims.wilderness.WildernessRepository;
import su.nightexpress.excellentclaims.wilderness.settings.WildernessSettings;

@NullMarked
public final class WildernessCommandConfiguration {

    private WildernessCommandConfiguration() {
    }

    public static void configure(WildernessModule module, DependencyContainer container) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        WildernessSettings settings = container.get(WildernessSettings.class);
        WildernessRepository repository = container.get(WildernessRepository.class);

        WildernessCommandArgumentRegistrar.registerArgumentTypes(repository);

        WildernessCommandResolver resolver = new WildernessCommandResolver(repository);
        WildernessCommandSuggestions suggestions = new WildernessCommandSuggestions(repository);
        WildernessCommandController commands = new WildernessCommandController(plugin, settings);

        container.register(WildernessCommandResolver.class, resolver);
        container.register(WildernessCommandSuggestions.class, suggestions);
        container.register(CommandRegistry.class, commands);

        module.addComponent(commands);
    }
}
