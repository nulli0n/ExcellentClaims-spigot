package su.nightexpress.excellentclaims.region.editor;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.ClaimsConstants;
import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.command.CommandRegistry;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.region.RegionsModule;
import su.nightexpress.excellentclaims.region.command.RegionCommandResolver;
import su.nightexpress.excellentclaims.region.command.RegionCommandSuggestions;
import su.nightexpress.excellentclaims.region.data.RegionDataService;
import su.nightexpress.excellentclaims.region.editor.command.SetDescriptionCommand;
import su.nightexpress.excellentclaims.region.editor.command.SetNameCommand;
import su.nightexpress.excellentclaims.region.editor.command.SetPriorityCommand;
import su.nightexpress.excellentclaims.region.editor.command.SetSpawnCommand;
import su.nightexpress.excellentclaims.region.editor.ui.RegionEditorUIController;
import su.nightexpress.excellentclaims.region.editor.ui.RegionEditorUIService;
import su.nightexpress.excellentclaims.region.editor.ui.dialog.EditorDescriptionDialog;
import su.nightexpress.excellentclaims.region.editor.ui.dialog.EditorDialogKeys;
import su.nightexpress.excellentclaims.region.editor.ui.dialog.EditorNameDialog;
import su.nightexpress.excellentclaims.region.editor.ui.dialog.EditorPriorityDialog;
import su.nightexpress.excellentclaims.region.editor.ui.menu.EditorIconSelectionMenu;
import su.nightexpress.excellentclaims.region.editor.ui.menu.EditorMenuLoader;
import su.nightexpress.excellentclaims.region.editor.ui.menu.EditorMenus;
import su.nightexpress.excellentclaims.region.editor.ui.menu.button.EditorDescriptionButton;
import su.nightexpress.excellentclaims.region.editor.ui.menu.button.EditorHomeButton;
import su.nightexpress.excellentclaims.region.editor.ui.menu.button.EditorIconButton;
import su.nightexpress.excellentclaims.region.editor.ui.menu.button.EditorNameButton;
import su.nightexpress.excellentclaims.region.editor.ui.menu.button.EditorPriorityButton;
import su.nightexpress.excellentclaims.region.settings.RegionSettings;
import su.nightexpress.excellentclaims.region.ui.RegionUIService;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogRegistry;

@NullMarked
public final class RegionEditorConfiguration {

    private static final Identifier ID = Identifier.of("region_editor");

    private RegionEditorConfiguration() {
    }

    public static void configure(RegionsModule module, DependencyContainer container) {
        RegionSettings settings = container.get(RegionSettings.class);
        RegionDataService dataService = container.get(RegionDataService.class);
        ClaimPermissionAPI permissionService = container.get(ClaimPermissionAPI.class);

        RegionEditorService editor = new RegionEditorService(settings, dataService, permissionService);
        RegionEditorModule editorModule = new RegionEditorModule(ID);

        container.register(RegionEditorService.class, editor);

        configureUI(container, editor, editorModule);
        configureCommands(container, editor);

        module.addComponent(editorModule);
    }

    private static void configureCommands(DependencyContainer container, RegionEditorService editor) {
        CommandRegistry commands = container.get(CommandRegistry.class);

        RegionCommandResolver resolver = container.get(RegionCommandResolver.class);
        RegionCommandSuggestions suggestions = container.get(RegionCommandSuggestions.class);
        MessageDispatcher messages = container.get(MessageDispatcher.class);

        commands.registerCommand(new SetNameCommand(resolver, suggestions, editor, messages));
        commands.registerCommand(new SetDescriptionCommand(resolver, suggestions, editor, messages));
        commands.registerCommand(new SetPriorityCommand(resolver, suggestions, editor, messages));
        commands.registerCommand(new SetSpawnCommand(resolver, suggestions, editor, messages));
    }

    private static void configureUI(DependencyContainer container, RegionEditorService editor,
                                    RegionEditorModule module) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        DialogRegistry dialogs = container.get(DialogRegistry.class);
        MessageDispatcher dispatcher = container.get(MessageDispatcher.class);

        Path menuPath = plugin.dataPath().resolve(ClaimsConstants.DIR_MENU);

        RegionUIService coreUI = container.get(RegionUIService.class);
        RegionSettings settings = container.get(RegionSettings.class);

        RegionEditorUIService editorUI = new RegionEditorUIService(dialogs, coreUI, editor);
        RegionEditorUIController controller = new RegionEditorUIController(editor, editorUI, dispatcher);

        EditorIconSelectionMenu iconSelectionMenu = new EditorIconSelectionMenu(plugin, controller);
        EditorMenus menus = new EditorMenus(iconSelectionMenu);

        // Lifecycle component
        EditorMenuLoader menuLoader = new EditorMenuLoader(menuPath, menus);

        editorUI.registerMenus(menus);

        coreUI.registerButton(new EditorNameButton(editor, controller));
        coreUI.registerButton(new EditorDescriptionButton(editor, controller));
        coreUI.registerButton(new EditorPriorityButton(editor, controller));
        coreUI.registerButton(new EditorIconButton(settings, editor, controller));
        coreUI.registerButton(new EditorHomeButton(editor, controller));

        dialogs.register(EditorDialogKeys.NAME, () -> new EditorNameDialog(settings, editor, dispatcher));
        dialogs.register(EditorDialogKeys.DESCRIPTION, () -> new EditorDescriptionDialog(settings, editor, dispatcher));
        dialogs.register(EditorDialogKeys.PRIORITY, () -> new EditorPriorityDialog(editor, dispatcher));

        module.addComponent(menuLoader);
    }
}
