package su.nightexpress.excellentclaims.land.merge;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.AbstractModule;
import su.nightexpress.excellentclaims.api.core.id.Identifier;

@NullMarked
public class MergeModule extends AbstractModule {

    public MergeModule() {
        super(Identifier.of("lands_merge"));
    }

    @Override
    protected void onReload() {

    }

    @Override
    protected void onShutdown() {

    }

    @Override
    protected void onStart() {

    }
}
