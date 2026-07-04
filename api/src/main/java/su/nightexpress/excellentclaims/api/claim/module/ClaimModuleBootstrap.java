package su.nightexpress.excellentclaims.api.claim.module;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.DependencyContainer;

@NullMarked
public interface ClaimModuleBootstrap {

    String getId();

    ClaimModule bootstrap(DependencyContainer container);
}
