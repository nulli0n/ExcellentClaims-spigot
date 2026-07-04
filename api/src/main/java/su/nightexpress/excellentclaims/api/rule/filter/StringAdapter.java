package su.nightexpress.excellentclaims.api.rule.filter;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface StringAdapter<T> {

    @Nullable
    T deserialize(String string);

    String serialize(@NonNull T value);
}
