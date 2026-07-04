package su.nightexpress.excellentclaims.rank;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.claim.ClaimMember;
import su.nightexpress.excellentclaims.api.claim.OwnableClaim;
import su.nightexpress.excellentclaims.api.core.AbstractModule;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.api.rank.Rank;
import su.nightexpress.excellentclaims.api.rank.RankRegistry;
import su.nightexpress.excellentclaims.api.rank.RanksAPI;

@NullMarked
public class RanksModule extends AbstractModule implements RanksAPI {

    private final RankRegistry  registry;
    private final RankIOService ioService;

    public RanksModule(RankRegistry registry, RankIOService ioService) {
        super(Identifier.of("ranks"));
        this.registry = registry;
        this.ioService = ioService;
    }

    @Override
    protected void onReload() {
        this.loadRanks();
    }

    @Override
    protected void onShutdown() {
        this.registry.clear();
    }

    @Override
    protected void onStart() {
        this.loadRanks();
    }

    private void loadRanks() {
        this.ioService.loadRanks();
        this.ioService.recalculatePermissions();
    }

    @Override
    public RankRegistry getRanks() {
        return registry;
    }

    public RankIOService getIOService() {
        return ioService;
    }

    // TODO "Fallback Owner" rank for claim owners

    @Override
    public Rank resolveRank(ClaimMember member) {
        Identifier rankId = member.getRank();
        return this.registry.lookup(rankId).orElse(this.registry.getLowestRank());
    }

    @Override
    public @Nullable Rank resolveRank(OwnableClaim claim, UUID memberId) {
        ClaimMember member = claim.getMember(memberId);
        return member == null ? null : this.resolveRank(member);
    }

    @Override
    public @Nullable Rank getNextRank(Rank rank) {
        return this.registry.getNextRank(rank);
    }

    @Override
    public List<Rank> getNextRanks(Rank rank) {
        return this.registry.values().stream()
            .filter(other -> other.isAbove(rank))
            .sorted(Comparator.comparingInt(Rank::getPriority))
            .toList();
    }

    @Override
    public @Nullable Rank getPreviousRank(Rank rank) {
        return this.registry.getPreviousRank(rank);
    }

    @Override
    public List<Rank> getPreviousRanks(Rank rank) {
        return this.registry.values().stream()
            .filter(other -> other.isBehind(rank))
            .sorted(Comparator.comparingInt(Rank::getPriority).reversed())
            .toList();
    }
}
