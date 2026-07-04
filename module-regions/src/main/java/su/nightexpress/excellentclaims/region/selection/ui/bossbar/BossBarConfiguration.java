package su.nightexpress.excellentclaims.region.selection.ui.bossbar;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.core.settings.SettingsFactory;
import su.nightexpress.excellentclaims.region.RegionsModule;
import su.nightexpress.excellentclaims.region.claim.validation.RegionQuotaValidator;
import su.nightexpress.excellentclaims.region.selection.SelectionModule;
import su.nightexpress.excellentclaims.region.selection.ui.SelectionUIService;

@NullMarked
public final class BossBarConfiguration {

    private static final String SETTINGS_FILE = "selection.ui.bossbar.yml";

    private BossBarConfiguration() {
    }

    public static void configure(SelectionModule module, DependencyContainer container) {
        RegionsModule rgModule = container.get(RegionsModule.class);
        Path filePath = rgModule.getModuleDir().resolve(SETTINGS_FILE);

        BossBarSettings settings = SettingsFactory.create(module, container, filePath, BossBarSettings::new);

        RegionQuotaValidator quotaValidator = container.get(RegionQuotaValidator.class);
        SelectionUIService uiService = container.get(SelectionUIService.class);

        uiService.addComponent(new BossBarExtension(quotaValidator, settings));
    }
}
