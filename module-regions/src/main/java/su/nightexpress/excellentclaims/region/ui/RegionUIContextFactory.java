package su.nightexpress.excellentclaims.region.ui;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.region.RegionsRepository;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.ui.context.InspectContext;
import su.nightexpress.nightcore.userdata.UserData;
import su.nightexpress.nightcore.userdata.UserDataManager;

@NullMarked
public class RegionUIContextFactory {

    private final ClaimPlugin       plugin;
    private final UserDataManager   users;
    private final RegionsRepository repository;

    public RegionUIContextFactory(ClaimPlugin plugin, UserDataManager users, RegionsRepository repository) {
        this.plugin = plugin;
        this.users = users;
        this.repository = repository;
    }

    public void createInspectContext(String playerName, Consumer<Optional<InspectContext>> consumer) {
        this.users.loadByNameAndCacheAsync(playerName).thenAcceptAsync(opt -> {
            if (opt.isEmpty()) {
                consumer.accept(Optional.empty());
                return;
            }

            UserData user = opt.get();
            List<RegionClaim> claims = this.repository.values()
                .stream()
                .filter(claim -> claim.isOwnerOrMember(user.getId()))
                .sorted(
                    Comparator.comparingInt((RegionClaim claim) -> claim.isOwner(user.getId()) ? 1 : 0)
                        .reversed()
                        .thenComparing(Comparator.comparing(claim -> claim.id().value()))
                )
                .toList();

            InspectContext context = new InspectContext(user, claims);
            consumer.accept(Optional.of(context));
        }, this.plugin::runTask);
    }
}
