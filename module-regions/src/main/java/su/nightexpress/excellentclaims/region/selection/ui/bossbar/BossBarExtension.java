package su.nightexpress.excellentclaims.region.selection.ui.bossbar;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.region.claim.validation.RegionQuotaValidator;
import su.nightexpress.excellentclaims.region.selection.ui.SelectionUIComponent;
import su.nightexpress.excellentclaims.region.selection.ui.UIComponentExtension;

@NullMarked
public class BossBarExtension implements UIComponentExtension {

    private final RegionQuotaValidator quotaValidator;
    private final BossBarSettings      settings;

    public BossBarExtension(RegionQuotaValidator quotaValidator, BossBarSettings settings) {
        this.quotaValidator = quotaValidator;
        this.settings = settings;
    }

    @Override
    public SelectionUIComponent createComponent() {
        return new BossBarComponent(this.quotaValidator, this.settings);
    }
}
