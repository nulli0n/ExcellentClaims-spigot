package su.nightexpress.excellentclaims.member;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.Placeholders;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.member.Member;
import su.nightexpress.excellentclaims.api.member.MemberRank;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.member.listener.MemberListener;
import su.nightexpress.excellentclaims.util.UserInfo;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.manager.AbstractManager;

import java.util.*;

public class MemberManager extends AbstractManager<ClaimPlugin> {

    private final Map<String, MemberRank> rankMap;

    public MemberManager(@NotNull ClaimPlugin plugin) {
        super(plugin);
        this.rankMap = new HashMap<>();
    }

    @Override
    protected void onLoad() {
        FileConfig config = this.plugin.getConfig();

        this.loadRanks(config);

        this.addListener(new MemberListener(this.plugin, this));
    }

    @Override
    protected void onShutdown() {
        this.rankMap.clear();
    }

    private void loadRanks(@NotNull FileConfig config) {
        // Fill default ranks so the section is always filled.
        if (config.getSection("MemberRanks").isEmpty()) {
            this.loadDefaults(config);
        }

        config.getSection("MemberRanks").forEach(id -> {
            MemberRank rank = ClaimMemberRank.read(config, "MemberRanks." + id, id);
            this.rankMap.put(rank.getId(), rank);
        });

        // In case something went really wrong (should never happen normally).
        if (this.rankMap.isEmpty()) {
            this.plugin.error("No member ranks defined. This is fatal! Adding some dummy ones...");
            this.plugin.error("You MUST fill/regenerate the 'MemberRanks' section in the plugin config!");
            ClaimMemberRank.getDefaultRanks().forEach(rank -> {
                this.rankMap.put(rank.getId(), rank);
            });
        }

        this.plugin.info("Loaded " + this.rankMap.size() + " member ranks.");
    }

    private void loadDefaults(@NotNull FileConfig config) {
        ClaimMemberRank.getDefaultRanks().forEach(rank -> {
            rank.write(config, "MemberRanks." + rank.getId());
        });
    }

    @NotNull
    public Map<String, MemberRank> getRankMap() {
        return this.rankMap;
    }

    @NotNull
    public Set<MemberRank> getRanks() {
        return new HashSet<>(this.rankMap.values());
    }

    @Nullable
    public MemberRank getRank(@NotNull String id) {
        return this.rankMap.get(id.toLowerCase());
    }

    @Nullable
    public MemberRank getNextRank(@NotNull MemberRank from) {
        return this.getRanks().stream().filter(rank -> rank.getPriority() > from.getPriority()).min(Comparator.comparingInt(MemberRank::getPriority)).orElse(null);
    }

    @Nullable
    public MemberRank getPreviousRank(@NotNull MemberRank from) {
        return this.getRanks().stream().filter(rank -> rank.getPriority() < from.getPriority()).max(Comparator.comparingInt(MemberRank::getPriority)).orElse(null);
    }

    @NotNull
    public MemberRank getHighestRank() {
        return this.getRanks().stream().max(Comparator.comparingInt(MemberRank::getPriority)).orElseThrow();
    }

    @NotNull
    public MemberRank getLowestRank() {
        return this.getRanks().stream().min(Comparator.comparingInt(MemberRank::getPriority)).orElseThrow();
    }

    public boolean isAdminMode(@NotNull Player player) {
        return this.plugin.getUserManager().getUserData(player).isAdminMode();
    }

    public void addMember(@NotNull Player player, @NotNull Claim claim, @NotNull String name) {
//        if (name.equalsIgnoreCase(player.getName())) {
//            Lang.MEMBER_ADD_ERROR_SELF.getMessage()
//                .replace(claim.replacePlaceholders())
//                .replace(Placeholders.forPlayer(player))
//                .send(player);
//            return;
//        }

        this.plugin.getUserManager().manageUser(name, user -> {
            if (user == null) {
                Lang.ERROR_INVALID_PLAYER.getMessage(plugin).send(player);
                return;
            }

            if (claim.isOwner(user.getId())) {
                Lang.MEMBER_ADD_ERROR_OWNER.getMessage()
                    .replace(claim.replacePlaceholders())
                    .replace(Placeholders.PLAYER_NAME, user.getName())
                    .send(player);
                return;
            }

            if (claim.isMember(user.getId())) {
                Lang.MEMBER_ADD_ERROR_ALREADY.getMessage()
                    .replace(claim.replacePlaceholders())
                    .replace(Placeholders.PLAYER_NAME, user.getName())
                    .send(player);
                return;
            }

            MemberRank rank = this.getLowestRank();
            Member member = new ClaimMember(UserInfo.of(user), rank);
            claim.addMember(member);
            claim.setSaveRequired(true);

            Lang.MEMBER_ADD_SUCCESS.getMessage()
                .replace(rank.replacePlaceholders())
                .replace(claim.replacePlaceholders())
                .replace(Placeholders.PLAYER_NAME, user.getName())
                .send(player);
        });
    }
}