package su.nightexpress.excellentclaims.api.command;

import org.jspecify.annotations.NonNull;

public interface CommandRegistry {

    void registerCommand(@NonNull CommandExtension extension);
}
