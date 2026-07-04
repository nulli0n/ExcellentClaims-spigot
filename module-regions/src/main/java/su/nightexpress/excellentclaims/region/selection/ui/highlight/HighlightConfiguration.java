package su.nightexpress.excellentclaims.region.selection.ui.highlight;

import java.nio.file.Path;
import java.util.logging.Logger;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.highlight.HighlightAPI;
import su.nightexpress.excellentclaims.core.settings.SettingsFactory;
import su.nightexpress.excellentclaims.region.RegionsModule;
import su.nightexpress.excellentclaims.region.selection.SelectionModule;
import su.nightexpress.excellentclaims.region.selection.ui.SelectionUIService;

@NullMarked
public final class HighlightConfiguration {

    private static final String SETTINGS_FILE = "selection.ui.highlight.yml";

    private HighlightConfiguration() {
    }

    public static void configure(SelectionModule module, DependencyContainer container) {
        Logger logger = container.get(Logger.class);

        HighlightAPI highlightAPI = container.getOrNull(HighlightAPI.class);
        if (highlightAPI == null) {
            logger.warning(
                "Highlight Region Selection component can not be loaded because no Highlight API is available.");
            return;
        }

        RegionsModule rgModule = container.get(RegionsModule.class);
        Path filePath = rgModule.getModuleDir().resolve(SETTINGS_FILE);
        HighlightSettings settings = SettingsFactory.create(module, container, filePath, HighlightSettings::new);

        HighlightExtension extension = new HighlightExtension(highlightAPI, settings);

        SelectionUIService uiService = container.get(SelectionUIService.class);

        uiService.addComponent(extension);
    }
}
