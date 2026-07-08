package su.nightexpress.excellentclaims.premium;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.premium.PremiumFeatures;

@NullMarked
public class PremiumUnlocked implements PremiumFeatures {

    @Override
    public boolean isPremium() {
        return true;
    }

}
