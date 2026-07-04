package su.nightexpress.excellentclaims.region.member;

import java.nio.file.Path;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.ClaimsConstants;
import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.command.CommandRegistry;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.api.rank.RanksAPI;
import su.nightexpress.excellentclaims.region.RegionsModule;
import su.nightexpress.excellentclaims.region.command.RegionCommandResolver;
import su.nightexpress.excellentclaims.region.command.RegionCommandSuggestions;
import su.nightexpress.excellentclaims.region.data.RegionDataService;
import su.nightexpress.excellentclaims.region.member.command.MembersCommand;
import su.nightexpress.excellentclaims.region.member.ui.MemberMenuLoader;
import su.nightexpress.excellentclaims.region.member.ui.MemberUIContextFactory;
import su.nightexpress.excellentclaims.region.member.ui.MemberUIController;
import su.nightexpress.excellentclaims.region.member.ui.MemberUIService;
import su.nightexpress.excellentclaims.region.member.ui.action.MemberActionsMenu;
import su.nightexpress.excellentclaims.region.member.ui.button.MembersMenuButton;
import su.nightexpress.excellentclaims.region.member.ui.dialog.AddMemberByOnlinePlayerDialog;
import su.nightexpress.excellentclaims.region.member.ui.dialog.KickMemberDialog;
import su.nightexpress.excellentclaims.region.member.ui.dialog.MemberDialogKeys;
import su.nightexpress.excellentclaims.region.member.ui.dialog.PurgeDialog;
import su.nightexpress.excellentclaims.region.member.ui.dialog.RankUpdateDialog;
import su.nightexpress.excellentclaims.region.member.ui.list.MemberListMenu;
import su.nightexpress.excellentclaims.region.ui.RegionUIService;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogRegistry;
import su.nightexpress.nightcore.userdata.UserDataManager;

public final class MemberConfiguration {

    private static final Identifier ID = Identifier.of("region_members");

    private MemberConfiguration() {
    }

    public static void configure(RegionsModule module, DependencyContainer container) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        ClaimPermissionAPI permissions = container.get(ClaimPermissionAPI.class);
        RanksAPI ranksAPI = container.get(RanksAPI.class);
        RegionDataService dataService = container.get(RegionDataService.class);

        MembersModule membersModule = new MembersModule(ID);
        MemberService memberService = new MemberService(dataService, permissions, ranksAPI);

        plugin.injectLang(MemberLang.class);

        // Configurations
        configureUI(container, membersModule, memberService);
        configureCommands(container);

        module.addComponent(membersModule);
    }

    private static void configureCommands(DependencyContainer container) {
        CommandRegistry commands = container.get(CommandRegistry.class);
        MessageDispatcher dispatcher = container.get(MessageDispatcher.class);

        RegionCommandResolver resolver = container.get(RegionCommandResolver.class);
        RegionCommandSuggestions suggestions = container.get(RegionCommandSuggestions.class);

        MemberUIService uiService = container.get(MemberUIService.class);
        MemberUIContextFactory uiContextFactory = container.get(MemberUIContextFactory.class);

        commands.registerCommand(new MembersCommand(resolver, suggestions, uiService, uiContextFactory, dispatcher));
    }

    private static void configureUI(DependencyContainer container, MembersModule module, MemberService service) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        DialogRegistry dialogs = container.get(DialogRegistry.class);
        UserDataManager users = container.get(UserDataManager.class);
        MessageDispatcher dispatcher = container.get(MessageDispatcher.class);

        Path menuPath = plugin.dataPath().resolve(ClaimsConstants.DIR_MENU);

        RanksAPI ranksAPI = container.get(RanksAPI.class);

        //LandSettings settings = container.get(LandSettings.class);
        RegionUIService coreUI = container.get(RegionUIService.class);

        MemberUIContextFactory uiContextFactory = new MemberUIContextFactory(plugin, users, ranksAPI);
        MemberUIService uiService = new MemberUIService(dialogs, coreUI, service);
        MemberUIController uiController = new MemberUIController(service, uiService, uiContextFactory, dispatcher);

        container.register(MemberService.class, service);
        container.register(MemberUIService.class, uiService);
        container.register(MemberUIContextFactory.class, uiContextFactory);

        coreUI.registerButton(new MembersMenuButton(service, uiController));

        MemberListMenu memberListMenu = new MemberListMenu(plugin, uiController, service);
        MemberActionsMenu actionsMenu = new MemberActionsMenu(plugin, service, uiController);
        MemberMenus menus = new MemberMenus(memberListMenu, actionsMenu);
        MemberMenuLoader menuLoader = new MemberMenuLoader(menuPath, menus);

        uiService.registerMenus(menus);

        dialogs.register(MemberDialogKeys.ADD_ONLINE, new AddMemberByOnlinePlayerDialog(uiController));
        dialogs.register(MemberDialogKeys.KICK, new KickMemberDialog(uiController));
        dialogs.register(MemberDialogKeys.RANK_UPDATE, new RankUpdateDialog(uiController));
        dialogs.register(MemberDialogKeys.PURGE, new PurgeDialog(uiController));

        module.addComponent(menuLoader);
    }
}
