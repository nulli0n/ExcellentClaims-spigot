package su.nightexpress.excellentclaims.land.member;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.AbstractModule;
import su.nightexpress.excellentclaims.api.core.id.Identifier;

@NullMarked
public class MembersModule extends AbstractModule {

    public MembersModule(Identifier id) {
        super(id);
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
