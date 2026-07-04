package su.nightexpress.excellentclaims.api.core;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.id.Identifier;

@NullMarked
public abstract class AbstractModule implements NightModule {

    protected final Identifier    id;
    protected final ComponentCore components;

    private boolean isRunning;

    public AbstractModule(Identifier id) {
        this.id = id;
        this.components = new ComponentCore();
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public void start() {
        if (this.isRunning) return;

        this.onStart();
        this.components.start();

        this.isRunning = true;
    }

    @Override
    public void shutdown() {
        if (!this.isRunning) return;

        this.components.shutdown();
        this.onShutdown();

        this.isRunning = false;
    }

    @Override
    public void reload() {

        this.components.reload();
        this.onReload();
    }

    protected abstract void onStart();

    protected abstract void onShutdown();

    protected abstract void onReload();

    public void addComponent(LifecycleComponent component) {
        this.components.register(component);
    }

    public <T extends LifecycleComponent> T getComponent(Class<T> componentType) {
        return this.components.getComponent(componentType);
    }

    public List<LifecycleComponent> getComponents() {
        return this.components.getComponents();
    }
}
