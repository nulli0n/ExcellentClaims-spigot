package su.nightexpress.excellentclaims.rules.filter.display;

import org.bukkit.command.Command;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.rule.filter.ElementDisplay;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class CommandDisplay implements ElementDisplay<Command> {

    public static final CommandDisplay INSTANCE = new CommandDisplay();

    @Override
    public String getNameLocalized(Command command) {
        return "/" + command.getName();
    }

    @Override
    public @Nullable String getSpriteTag(Command command) {
        return TagWrappers.SPRITE_BLOCKS.apply("block/command_block_back");
    }
}
