package su.nightexpress.excellentclaims.api.core.id;

import org.jspecify.annotations.NonNull;

public interface Identifiable {

    @NonNull
    Identifier getId();

    default Identifier id() {
        return this.getId();
    }
}
