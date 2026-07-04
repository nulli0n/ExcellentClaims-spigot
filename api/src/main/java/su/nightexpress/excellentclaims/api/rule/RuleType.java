package su.nightexpress.excellentclaims.api.rule;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.configuration.codec.ConfigCodec;

@NullMarked
public interface RuleType<T> {

    ConfigCodec<T> getCodec();

    String formatSummary(@NonNull T value);
}
