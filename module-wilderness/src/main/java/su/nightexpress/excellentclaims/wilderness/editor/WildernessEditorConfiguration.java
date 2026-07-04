package su.nightexpress.excellentclaims.wilderness.editor;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.ClaimsConstants;
import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.command.CommandRegistry;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.wilderness.WildernessModule;
import su.nightexpress.excellentclaims.wilderness.command.WildernessCommandResolver;
import su.nightexpress.excellentclaims.wilderness.command.WildernessCommandSuggestions;
import su.nightexpress.excellentclaims.wilderness.data.WildernessDataService;
import su.nightexpress.excellentclaims.wilderness.editor.command.SetDescriptionCommand;
import su.nightexpress.excellentclaims.wilderness.editor.command.SetNameCommand;
import su.nightexpress.excellentclaims.wilderness.editor.command.SetPriorityCommand;
import su.nightexpress.excellentclaims.wilderness.editor.command.SetSpawnCommand;
import su.nightexpress.excellentclaims.wilderness.editor.ui.WildernessEditorUIController;
import su.nightexpress.excellentclaims.wilderness.editor.ui.WildernessEditorUIService;
import su.nightexpress.excellentclaims.wilderness.editor.ui.dialog.EditorDescriptionDialog;
import su.nightexpress.excellentclaims.wilderness.editor.ui.dialog.EditorDialogKeys;
import su.nightexpress.excellentclaims.wilderness.editor.ui.dialog.EditorNameDialog;
import su.nightexpress.excellentclaims.wilderness.editor.ui.dialog.EditorPriorityDialog;
import su.nightexpress.excellentclaims.wilderness.editor.ui.menu.EditorIconSelectionMenu;
import su.nightexpress.excellentclaims.wilderness.editor.ui.menu.EditorMenuLoader;
import su.nightexpress.excellentclaims.wilderness.editor.ui.menu.EditorMenus;
import su.nightexpress.excellentclaims.wilderness.editor.ui.menu.button.EditorDescriptionButton;
import su.nightexpress.excellentclaims.wilderness.editor.ui.menu.button.EditorHomeButton;
import su.nightexpress.excellentclaims.wilderness.editor.ui.menu.button.EditorIconButton;
import su.nightexpress.excellentclaims.wilderness.editor.ui.menu.button.EditorNameButton;
import su.nightexpress.excellentclaims.wilderness.editor.ui.menu.button.EditorPriorityButton;
import su.nightexpress.excellentclaims.wilderness.settings.WildernessSettings;
import su.nightexpress.excellentclaims.wilderness.ui.WildernessUIService;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogRegistry;

@NullMarked
public final class WildernessEditorConfiguration {

    private static final Identifier ID = Identifier.of("wilderness_editor");

    private WildernessEditorConfiguration() {
    }

    public static void configure(WildernessModule module, DependencyContainer container) {
        WildernessSettings settings = container.get(WildernessSettings.class);
        WildernessDataService dataService = container.get(WildernessDataService.class);
        ClaimPermissionAPI permissionService = container.get(ClaimPermissionAPI.class);

        WildernessEditorService editor = new WildernessEditorService(settings, dataService, permissionService);
        WildernessEditorModule editorModule = new WildernessEditorModule(ID);

        container.register(WildernessEditorService.class, editor);

        configureUI(container, editor, editorModule);
        configureCommands(container, editor);

        module.addComponent(editorModule);
    }

    private static void configureCommands(DependencyContainer container, WildernessEditorService editor) {
        CommandRegistry commands = container.get(CommandRegistry.class);

        WildernessCommandResolver resolver = container.get(WildernessCommandResolver.class);
        WildernessCommandSuggestions suggestions = container.get(WildernessCommandSuggestions.class);
        MessageDispatcher messages = container.get(MessageDispatcher.class);

        commands.registerCommand(new SetNameCommand(resolver, suggestions, editor, messages));
        commands.registerCommand(new SetDescriptionCommand(resolver, suggestions, editor, messages));
        commands.registerCommand(new SetPriorityCommand(resolver, suggestions, editor, messages));
        commands.registerCommand(new SetSpawnCommand(resolver, suggestions, editor, messages));
    }

    private static void configureUI(DependencyContainer container, WildernessEditorService editor,
                                    WildernessEditorModule module) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        DialogRegistry dialogs = container.get(DialogRegistry.class);
        MessageDispatcher dispatcher = container.get(MessageDispatcher.class);

        Path menuPath = plugin.dataPath().resolve(ClaimsConstants.DIR_MENU);

        WildernessUIService coreUI = container.get(WildernessUIService.class);
        WildernessSettings settings = container.get(WildernessSettings.class);

        WildernessEditorUIService editorUI = new WildernessEditorUIService(dialogs, coreUI, editor);
        WildernessEditorUIController controller = new WildernessEditorUIController(editor, editorUI, dispatcher);

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
