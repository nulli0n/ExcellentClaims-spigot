package su.nightexpress.excellentclaims.land.command.argument;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimRepository;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.lang.LandLang;
import su.nightexpress.nightcore.commands.SuggestionsProvider;
import su.nightexpress.nightcore.commands.argument.ArgumentReader;
import su.nightexpress.nightcore.commands.argument.ArgumentType;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.CommandContextBuilder;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.core.config.CoreLang;

@NullMarked
public class LandByNameArgumentType implements ArgumentType<LandClaim>, SuggestionsProvider {

    private final ClaimRepository<LandClaim> repository;

    public LandByNameArgumentType(ClaimRepository<LandClaim> repository) {
        this.repository = repository;
    }

    @Override
    public LandClaim parse(CommandContextBuilder context, String string) throws CommandSyntaxException {
        if (!(context.getSender() instanceof Player)) {
            throw CommandSyntaxException.custom(CoreLang.COMMAND_EXECUTION_PLAYER_ONLY);
        }

        Identifier id = Identifier.parse(string).orElse(null);
        if (id == null) throw CommandSyntaxException.custom(LandLang.COMMAND_SYNTAX_INVALID_LAND_CLAIM);

        LandClaim claim = this.repository.get(id);
        if (claim == null) throw CommandSyntaxException.custom(LandLang.COMMAND_SYNTAX_INVALID_LAND_CLAIM);

        return claim;
    }

    @Override
    public List<String> suggest(ArgumentReader reader, CommandContext context) {
        return new ArrayList<>(this.repository.idValues());
    }
}
