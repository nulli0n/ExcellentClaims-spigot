package su.nightexpress.excellentclaims.engine;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.core.AbstractModule;
import su.nightexpress.excellentclaims.api.core.id.Identifier;

@NullMarked
public class ClaimEngine extends AbstractModule {

    public ClaimEngine(ClaimPlugin plugin, ClaimRegistry claims, ModuleRegistry modules) {
        super(Identifier.of("engine"));
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onShutdown() {

    }

    @Override
    protected void onReload() {

    }
}
