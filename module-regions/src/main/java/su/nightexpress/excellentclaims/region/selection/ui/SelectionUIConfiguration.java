package su.nightexpress.excellentclaims.region.selection.ui;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.core.settings.SettingsFactory;
import su.nightexpress.excellentclaims.region.RegionsModule;
import su.nightexpress.excellentclaims.region.selection.SelectionModule;
import su.nightexpress.excellentclaims.region.selection.session.SessionManager;
import su.nightexpress.excellentclaims.region.selection.ui.bossbar.BossBarConfiguration;
import su.nightexpress.excellentclaims.region.selection.ui.highlight.HighlightConfiguration;
import su.nightexpress.excellentclaims.region.selection.ui.settings.SelectionUISettings;

@NullMarked
public final class SelectionUIConfiguration {

    private static final String SETTINGS_FILE = "selection.ui.yml";

    private SelectionUIConfiguration() {
    }

    public static void configure(SelectionModule module, DependencyContainer container) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);

        SelectionUISettings uiSettings = configureSettings(module, container);

        SessionManager sessionManager = container.get(SessionManager.class);
        SelectionUIService uiService = new SelectionUIService();

        container.register(SelectionUIService.class, uiService);

        if (uiSettings.isBossBarEnabled()) {
            BossBarConfiguration.configure(module, container);
        }
        if (uiSettings.isHighlightEnabled()) {
            HighlightConfiguration.configure(module, container);
        }

        module.addComponent(new SelectionUIController(plugin, uiSettings, sessionManager, uiService));
    }

    private static SelectionUISettings configureSettings(SelectionModule module, DependencyContainer container) {
        RegionsModule parent = container.get(RegionsModule.class);
        Path filePath = parent.getModuleDir().resolve(SETTINGS_FILE);

        return SettingsFactory.create(module, container, filePath, SelectionUISettings::new);
    }
}
