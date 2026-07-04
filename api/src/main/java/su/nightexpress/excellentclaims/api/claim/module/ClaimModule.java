package su.nightexpress.excellentclaims.api.claim.module;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimRepository;
import su.nightexpress.excellentclaims.api.claim.data.DataService;
import su.nightexpress.excellentclaims.api.core.ConfigurableModule;

@NullMarked
public interface ClaimModule extends ConfigurableModule {

    ClaimRepository<?> getRepository();

    DataService<?> getDataService();
}
