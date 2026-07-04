package su.nightexpress.excellentclaims.region.member.ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.ClaimMember;
import su.nightexpress.excellentclaims.api.rank.Rank;
import su.nightexpress.excellentclaims.api.rank.RanksAPI;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.member.ui.context.MemberListUIContext;
import su.nightexpress.excellentclaims.region.member.ui.context.OnlineMemberContext;
import su.nightexpress.excellentclaims.region.member.ui.list.MemberDisplayData;
import su.nightexpress.nightcore.userdata.UserData;
import su.nightexpress.nightcore.userdata.UserDataManager;
import su.nightexpress.nightcore.util.Players;

@NullMarked
public class MemberUIContextFactory {

    private final ClaimPlugin     plugin;
    private final UserDataManager users;
    private final RanksAPI        ranksAPI;

    public MemberUIContextFactory(ClaimPlugin plugin, UserDataManager users, RanksAPI ranksAPI) {
        this.plugin = plugin;
        this.users = users;
        this.ranksAPI = ranksAPI;
    }

    public void createMembersContext(RegionClaim claim, Consumer<MemberListUIContext> callback) {
        List<UUID> memberIds = claim.getMembers().stream().map(ClaimMember::getPlayerId).toList();

        this.users.loadAllByIdsAndCacheAsync(memberIds).thenAcceptAsync(userMap -> {
            List<MemberDisplayData> displayDatas = new ArrayList<>();

            userMap.forEach(userData -> {
                ClaimMember member = claim.getMember(userData.getId());
                if (member == null) return;

                Rank rank = this.ranksAPI.resolveRank(member);
                displayDatas.add(new MemberDisplayData(member, userData, rank));
            });

            displayDatas.sort(Comparator.comparingInt((MemberDisplayData d) -> d.rank().getPriority()).reversed());

            callback.accept(new MemberListUIContext(claim, displayDatas));

        }, this.plugin::runTask);
    }

    public OnlineMemberContext createOnlineMembersContext(RegionClaim claim) {
        List<UserData> users = new ArrayList<>();
        Players.getOnline().forEach(online -> {
            if (!claim.isOwnerOrMember(online)) {
                UserData data = this.users.getData(online);
                users.add(data);
            }
        });

        // Sort for easier navigation in dialog UI
        users.sort(Comparator.comparing(UserData::getName));

        return new OnlineMemberContext(claim, users);
    }

    public void resolveUserContext(UUID playerId, Consumer<Optional<UserData>> callback) {
        this.users.loadByIdAndCacheAsync(playerId).thenAcceptAsync(users -> {
            callback.accept(users);
        }, this.plugin::runTask);
    }

    public void resolveUserContext(String playerName, Consumer<Optional<UserData>> callback) {
        this.users.loadByNameAndCacheAsync(playerName).thenAcceptAsync(users -> {
            callback.accept(users);
        }, this.plugin::runTask);
    }
}
