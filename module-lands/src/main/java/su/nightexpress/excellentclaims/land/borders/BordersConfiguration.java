package su.nightexpress.excellentclaims.land.borders;

import java.util.logging.Logger;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandRegistry;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.api.highlight.HighlightAPI;
import su.nightexpress.excellentclaims.land.LandsModule;
import su.nightexpress.excellentclaims.land.LandsRepository;
import su.nightexpress.excellentclaims.land.borders.command.BordersCommand;
import su.nightexpress.excellentclaims.land.borders.session.BorderSessionManager;
import su.nightexpress.excellentclaims.land.borders.session.BorderSessionService;
import su.nightexpress.excellentclaims.land.borders.settings.BorderSettings;
import su.nightexpress.excellentclaims.land.borders.settings.BorderSettingsConfiguration;

@NullMarked
public final class BordersConfiguration {

    private static final Identifier ID = Identifier.of("lands_borders");

    private BordersConfiguration() {
    }

    public static void configure(LandsModule module, DependencyContainer container) {
        Logger logger = container.get(Logger.class);

        HighlightAPI highlightAPI = container.getOrNull(HighlightAPI.class);
        if (highlightAPI == null) {
            logger.info("Highlight API is not available, Lands Bounds Highlight feature will be unavailable.");
            return;
        }

        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        ClaimRegistry claims = container.get(ClaimRegistry.class);
        MessageDispatcher dispatcher = container.get(MessageDispatcher.class);

        LandsRepository repository = container.get(LandsRepository.class);
        CommandRegistry commands = container.get(CommandRegistry.class);

        BordersModule bordersModule = new BordersModule(ID);

        BorderSettings settings = BorderSettingsConfiguration.configure(bordersModule, container);
        BorderService service = new BorderService(highlightAPI, claims, repository, settings);

        BorderSessionManager sessionManager = new BorderSessionManager();
        BorderSessionService sessionService = new BorderSessionService(service, sessionManager);

        BorderController controller = new BorderController(plugin, sessionManager, service, settings);

        commands.registerCommand(new BordersCommand(sessionService, dispatcher));

        bordersModule.addComponent(controller);

        module.addComponent(bordersModule);
    }
}
