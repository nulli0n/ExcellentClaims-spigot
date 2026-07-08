package su.nightexpress.excellentclaims.engine.module;

import java.util.ArrayList;
import java.util.List;

import su.nightexpress.excellentclaims.api.claim.module.ClaimModuleBootstrap;

public final class EngineModuleBootstrapProvider {

    private EngineModuleBootstrapProvider() {
    }

    public static List<ClaimModuleBootstrap> provide() {
        List<ClaimModuleBootstrap> bootstraps = new ArrayList<>();

        loadIfPresent(bootstraps, "su.nightexpress.excellentclaims.region.RegionsBootstrap");
        loadIfPresent(bootstraps, "su.nightexpress.excellentclaims.wilderness.WildernessBootstrap");

        return bootstraps;
    }

    private static void loadIfPresent(List<ClaimModuleBootstrap> list, String className) {
        try {
            // Attempt to find the class on the classpath
            Class<?> clazz = Class.forName(className);

            // Ensure the target class actually implements the correct interface
            if (!ClaimModuleBootstrap.class.isAssignableFrom(clazz)) {
                throw new IllegalStateException("Class " + className + " does not implement ClaimModuleBootstrap");
            }

            // Instantiate using the default no-args constructor
            Object instance = clazz.getDeclaredConstructor().newInstance();
            list.add((ClaimModuleBootstrap) instance);

        }
        catch (ClassNotFoundException ignored) {
            // EXPECTED BEHAVIOR FOR LITE BUILD: 
            // The class is completely missing from the JAR, so we safely ignore it.
        }
        catch (ReflectiveOperationException e) {
            // UNEXPECTED BEHAVIOR: 
            // The class exists but has no default constructor, crashed during <init>, or is inaccessible.
            // We must not swallow this error.
            throw new IllegalStateException("Failed to initialize module: " + className, e);
        }
    }
}
