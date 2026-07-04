package su.nightexpress.excellentclaims.rules.filter.adapter.impl;

import org.bukkit.command.Command;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.rule.filter.StringAdapter;
import su.nightexpress.nightcore.util.CommandUtil;

@NullMarked
public class CommandAdapter implements StringAdapter<Command> {

    public static final CommandAdapter INSTANCE = new CommandAdapter();

    @Override
    public @Nullable Command deserialize(String string) {
        return CommandUtil.getCommand(string).orElse(null);
    }

    @Override
    public String serialize(Command command) {
        return command.getName();
    }
}
