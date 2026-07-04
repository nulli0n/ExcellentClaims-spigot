package su.nightexpress.excellentclaims.wilderness.command.argument;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.wilderness.WildernessRepository;
import su.nightexpress.excellentclaims.wilderness.data.model.WildernessRegion;
import su.nightexpress.excellentclaims.wilderness.lang.WildernessLang;
import su.nightexpress.nightcore.commands.SuggestionsProvider;
import su.nightexpress.nightcore.commands.argument.ArgumentReader;
import su.nightexpress.nightcore.commands.argument.ArgumentType;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.CommandContextBuilder;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.core.config.CoreLang;

@NullMarked
public class WildernessByNameArgumentType implements ArgumentType<WildernessRegion>, SuggestionsProvider {

    private final WildernessRepository repository;

    public WildernessByNameArgumentType(WildernessRepository repository) {
        this.repository = repository;
    }

    @Override
    public WildernessRegion parse(CommandContextBuilder context, String string) throws CommandSyntaxException {
        if (!(context.getSender() instanceof Player)) {
            throw CommandSyntaxException.custom(CoreLang.COMMAND_EXECUTION_PLAYER_ONLY);
        }

        Identifier id = Identifier.parse(string).orElse(null);
        if (id == null) throw CommandSyntaxException.custom(WildernessLang.COMMAND_SYNTAX_INVALID_WILDERNESS);

        WildernessRegion claim = this.repository.get(id);
        if (claim == null) throw CommandSyntaxException.custom(WildernessLang.COMMAND_SYNTAX_INVALID_WILDERNESS);

        return claim;
    }

    @Override
    public List<String> suggest(ArgumentReader reader, CommandContext context) {
        return new ArrayList<>(this.repository.idValues());
    }
}
