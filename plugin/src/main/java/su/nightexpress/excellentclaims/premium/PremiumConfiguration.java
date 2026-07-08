package su.nightexpress.excellentclaims.premium;

import java.util.logging.Logger;

import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.premium.PremiumFeatures;

public final class PremiumConfiguration {

    private static final String CLASS_PATH = "su.nightexpress.excellentclaims.premium.PremiumUnlocked";

    private PremiumConfiguration() {
    }

    public static void configure(DependencyContainer container) {
        try {
            Class<?> type = Class.forName(CLASS_PATH);

            if (!PremiumFeatures.class.isAssignableFrom(type)) {
                throw new IllegalStateException(CLASS_PATH + " is not PremiumFeatures provider.");
            }

            Logger logger = container.get(Logger.class);
            PremiumFeatures unlocked = (PremiumFeatures) type.getDeclaredConstructor().newInstance();
            container.register(PremiumFeatures.class, unlocked);

            logger.info("Thank you for using the Premium version! <3");
        }
        catch (ClassNotFoundException ignored) {
            fallbackToLite(container);
        }
        catch (ReflectiveOperationException e) {
            fallbackToLite(container);

            throw new IllegalStateException("Failed to unlock premium features: " + CLASS_PATH, e);
        }
    }

    private static void fallbackToLite(DependencyContainer container) {
        container.register(PremiumFeatures.class, new PremiumLocked());
    }
}
