package su.nightexpress.excellentclaims.api.command;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.commands.tree.ExecutableNode;

public interface CommandExtension {

    @NonNull
    ExecutableNode createCommand();
}
