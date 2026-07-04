package su.nightexpress.excellentclaims.land.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimRepository;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.nightcore.commands.SuggestionsProvider;

@NullMarked
public class LandCommandSuggestions {

    private final ClaimRepository<LandClaim> repository;

    public LandCommandSuggestions(ClaimRepository<LandClaim> repository) {
        this.repository = repository;
    }

    /**
     * Suggests all lands in the repository.
     */
    public SuggestionsProvider all() {
        return (reader, context) -> new ArrayList<>(this.repository.idValues());
    }

    /**
     * Suggests only lands where the executing player is an owner or member.
     */
    public SuggestionsProvider memberOnly() {
        return (reader, context) -> {
            Player player = context.getPlayer();
            if (player == null) return List.of(); // Console gets no suggestions here

            return this.repository.stream()
                .filter(claim -> claim.isOwnerOrMember(player))
                .map(claim -> claim.id().value())
                .toList();
        };
    }

    /**
     * Suggests only lands where the executing player is an owner or member.
     */
    public SuggestionsProvider ownerOnly() {
        return (reader, context) -> {
            Player player = context.getPlayer();
            if (player == null) return List.of(); // Console gets no suggestions here

            return this.repository.getByOwner(player)
                .stream()
                .map(claim -> claim.id().value())
                .toList();
        };
    }
}