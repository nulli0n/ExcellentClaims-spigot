package su.nightexpress.excellentclaims.api.rank;

import java.util.Comparator;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.core.id.IdentifiableRegistry;

@NullMarked
public class RankRegistry extends IdentifiableRegistry<Rank> {

    public @Nullable Rank getNextRank(Rank from) {
        return this.stream().filter(rank -> rank.getPriority() > from.getPriority()).min(Comparator
            .comparingInt(Rank::getPriority)).orElse(null);
    }

    public @Nullable Rank getPreviousRank(Rank from) {
        return this.stream().filter(rank -> rank.getPriority() < from.getPriority()).max(Comparator
            .comparingInt(Rank::getPriority)).orElse(null);
    }

    public Rank getHighestRank() {
        return this.stream().max(Comparator.comparingInt(Rank::getPriority)).orElseThrow();
    }

    public Rank getLowestRank() {
        return this.stream().min(Comparator.comparingInt(Rank::getPriority)).orElseThrow();
    }
}
