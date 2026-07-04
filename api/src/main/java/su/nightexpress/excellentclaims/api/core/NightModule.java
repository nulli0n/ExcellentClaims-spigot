package su.nightexpress.excellentclaims.api.core;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.id.Identifiable;

@NullMarked
public interface NightModule extends LifecycleComponent, Identifiable {

    void addComponent(LifecycleComponent component);

    <T extends LifecycleComponent> T getComponent(Class<T> componentType);

    List<LifecycleComponent> getComponents();
}
