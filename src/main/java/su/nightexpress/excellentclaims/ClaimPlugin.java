package su.nightexpress.excellentclaims;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.claim.ClaimManager;
import su.nightexpress.excellentclaims.command.impl.BaseCommands;
import su.nightexpress.excellentclaims.command.impl.LandCommands;
import su.nightexpress.excellentclaims.command.impl.RegionCommands;
import su.nightexpress.excellentclaims.config.Config;
import su.nightexpress.excellentclaims.config.Keys;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.config.Perms;
import su.nightexpress.excellentclaims.data.ClaimUser;
import su.nightexpress.excellentclaims.data.DataHandler;
import su.nightexpress.excellentclaims.data.UserManager;
import su.nightexpress.excellentclaims.flag.FlagRegistry;
import su.nightexpress.excellentclaims.member.MemberManager;
import su.nightexpress.excellentclaims.menu.MenuManager;
import su.nightexpress.excellentclaims.selection.SelectionManager;
import su.nightexpress.nightcore.NightDataPlugin;
import su.nightexpress.nightcore.command.experimental.ImprovedCommands;
import su.nightexpress.nightcore.config.PluginDetails;

public class ClaimPlugin extends NightDataPlugin<ClaimUser> implements ImprovedCommands {

    private DataHandler dataHandler;
    private UserManager userManager;

    private MemberManager    memberManager;
    private ClaimManager     claimManager;
    private SelectionManager selectionManager;
    private MenuManager      menuManager;

    @Override
    @NotNull
    protected PluginDetails getDefaultDetails() {
        return PluginDetails.create("Claims", new String[]{"eclaim", "eclaims", "excellentclaims"})
            .setConfigClass(Config.class)
            .setLangClass(Lang.class)
            .setPermissionsClass(Perms.class)
            ;
    }

    @Override
    public void enable() {
        this.loadAPI();
        this.loadFlags();
        this.loadCommands();

        this.dataHandler = new DataHandler(this);
        this.dataHandler.setup();

        this.userManager = new UserManager(this);
        this.userManager.setup();

        this.memberManager = new MemberManager(this);
        this.memberManager.setup();

        this.claimManager = new ClaimManager(this);
        this.claimManager.setup();

        this.menuManager = new MenuManager(this);
        this.menuManager.setup();

        this.selectionManager = new SelectionManager(this);
        this.selectionManager.setup();
    }

    @Override
    public void disable() {
        if (this.menuManager != null) this.menuManager.shutdown();
        if (this.selectionManager != null) this.selectionManager.shutdown();
        if (this.claimManager != null) this.claimManager.shutdown();
        if (this.memberManager != null) this.memberManager.shutdown();

        this.unloadCommands();

        FlagRegistry.shutdown();
        ClaimsAPI.shutdown();
        Keys.shutdown();
    }

    private void loadAPI() {
        Keys.load(this);
        ClaimsAPI.load(this);
    }

    private void loadFlags() {
        FlagRegistry.load(this);
    }

    private void loadCommands() {
        BaseCommands.load(this);
        RegionCommands.load(this);
        LandCommands.load(this);
    }

    private void unloadCommands() {
        RegionCommands.unload();
        LandCommands.unload();
    }

    @Override
    @NotNull
    public DataHandler getData() {
        return dataHandler;
    }

    @NotNull
    @Override
    public UserManager getUserManager() {
        return userManager;
    }

    @NotNull
    public MenuManager getMenuManager() {
        return menuManager;
    }

    @NotNull
    public MemberManager getMemberManager() {
        return memberManager;
    }

    @NotNull
    public ClaimManager getClaimManager() {
        return claimManager;
    }

    @NotNull
    public SelectionManager getSelectionManager() {
        return selectionManager;
    }
}