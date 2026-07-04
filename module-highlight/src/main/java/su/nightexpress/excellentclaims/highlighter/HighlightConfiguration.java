package su.nightexpress.excellentclaims.highlighter;

import java.util.logging.Logger;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.highlight.BlockHighlighter;
import su.nightexpress.excellentclaims.api.highlight.HighlightAPI;
import su.nightexpress.excellentclaims.integration.packetevents.BlockPacketsHighlighter;
import su.nightexpress.excellentclaims.integration.protocollib.BlockProtocolHighlighter;
import su.nightexpress.nightcore.util.Plugins;

@NullMarked
public final class HighlightConfiguration {

    private static final String PACKET_EVENTS = "packetevents";
    private static final String PROTOCOL_LIB  = "ProtocolLib";

    private HighlightConfiguration() {
    }

    public static void configure(DependencyContainer container) {
        Logger logger = container.get(Logger.class);

        BlockHighlighter highlighter = configureHighlighter();
        if (highlighter == null) {
            logger.warning("No packet library found. Highlighter feature will be unavailable.");
            return;
        }

        HighlightService service = new HighlightService(highlighter);

        container.register(HighlightAPI.class, service);

        logger.info("HightlighterAPI initialized.");
    }

    private static @Nullable BlockHighlighter configureHighlighter() {
        if (Plugins.isInstalled(PACKET_EVENTS)) {
            return new BlockPacketsHighlighter();
        }

        if (Plugins.isInstalled(PROTOCOL_LIB)) {
            return new BlockProtocolHighlighter();
        }

        return null;
    }
}
