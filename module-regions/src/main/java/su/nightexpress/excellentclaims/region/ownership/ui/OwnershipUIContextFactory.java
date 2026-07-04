package su.nightexpress.excellentclaims.region.ownership.ui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.ownership.OwnershipService;
import su.nightexpress.excellentclaims.region.ownership.ui.context.TransferTargetContext;
import su.nightexpress.nightcore.userdata.UserData;
import su.nightexpress.nightcore.userdata.UserDataManager;
import su.nightexpress.nightcore.util.Players;

@NullMarked
public class OwnershipUIContextFactory {

    //private final ClaimPlugin          plugin;
    private final UserDataManager  users;
    private final OwnershipService service;

    public OwnershipUIContextFactory(ClaimPlugin plugin, UserDataManager users, OwnershipService service) {
        //this.plugin = plugin;
        this.users = users;
        this.service = service;
    }

    public TransferTargetContext createTargetContext(Player player, RegionClaim claim) {
        List<UserData> eligibles = new ArrayList<>();

        Players.getOnline().forEach(target -> {
            ActionResult check = this.service.canTransferOwnership(player, claim, target);
            if (!check.success()) return;

            UserData user = this.users.getData(target);
            eligibles.add(user);
        });

        return new TransferTargetContext(claim, eligibles);
    }
}
