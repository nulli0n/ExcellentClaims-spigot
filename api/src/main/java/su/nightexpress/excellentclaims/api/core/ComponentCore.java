package su.nightexpress.excellentclaims.api.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class ComponentCore implements LifecycleComponent {

    private final List<LifecycleComponent> components;

    private final Map<Class<?>, LifecycleComponent> mirrorCache;

    public ComponentCore() {
        this.components = new ArrayList<>();
        this.mirrorCache = new HashMap<>();
    }

    public boolean isRegistered(LifecycleComponent component) {
        return this.components.contains(component);
    }

    public void register(LifecycleComponent component) {
        if (this.components.contains(component)) {
            throw new IllegalArgumentException("Component is already registered!");
        }

        this.components.add(component);

        this.mirrorCache.put(component.getClass(), component);
    }

    @Override
    public void reload() {
        this.getComponents().forEach(LifecycleComponent::reload);
    }

    @Override
    public void shutdown() {
        List<LifecycleComponent> list = this.getComponents();

        // Shutdown "highest-level" components first.
        for (int index = list.size() - 1; index >= 0; index--) {
            list.get(index).shutdown();
        }
    }

    @Override
    public void start() {
        //System.out.println("Start Core");
        //this.getComponents().forEach(LifecycleComponent::start);
        this.getComponents().forEach(comp -> {
            //System.out.println("Start Component: " + comp);
            comp.start();
        });
    }

    /**
     * Retrieves a registered component by its exact class or implemented interface.
     * The generic bound is <T> rather than <T extends LifecycleComponent> to allow
     * looking up pure API interfaces that don't expose lifecycle methods.
     */
    public <T> T getComponent(Class<T> componentType) {
        // Try an exact match first
        LifecycleComponent match = this.mirrorCache.get(componentType);

        // Fallback to polymorphic lookup if not yet cached
        if (match == null) {
            for (LifecycleComponent component : this.components) {
                if (componentType.isInstance(component)) {
                    match = component;

                    // Lazily populate the mirror map with the interface/superclass.
                    this.mirrorCache.put(componentType, match);
                    break;
                }
            }
        }

        if (match == null) {
            throw new IllegalArgumentException("Component " + componentType.getSimpleName() + " is not loaded!");
        }

        return componentType.cast(match);
    }

    public <T> Optional<T> component(Class<T> componentType) {
        try {
            return Optional.of(this.getComponent(componentType));
        }
        catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }

    public List<LifecycleComponent> getComponents() {
        return List.copyOf(this.components);
    }
}
