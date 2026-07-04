package su.nightexpress.excellentclaims.api.rule.filter;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface ElementDisplay<T> {

    String getNameLocalized(@NonNull T value);

    @Nullable
    String getSpriteTag(@NonNull T value);
}
