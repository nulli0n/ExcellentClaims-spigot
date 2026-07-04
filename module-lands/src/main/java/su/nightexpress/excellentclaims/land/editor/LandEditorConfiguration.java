package su.nightexpress.excellentclaims.land.editor;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.ClaimsConstants;
import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.command.CommandRegistry;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.land.LandsModule;
import su.nightexpress.excellentclaims.land.command.LandCommandResolver;
import su.nightexpress.excellentclaims.land.command.LandCommandSuggestions;
import su.nightexpress.excellentclaims.land.data.LandDataService;
import su.nightexpress.excellentclaims.land.editor.command.SetDescriptionCommand;
import su.nightexpress.excellentclaims.land.editor.command.SetNameCommand;
import su.nightexpress.excellentclaims.land.editor.command.SetPriorityCommand;
import su.nightexpress.excellentclaims.land.editor.command.SetSpawnCommand;
import su.nightexpress.excellentclaims.land.editor.ui.LandEditorUIController;
import su.nightexpress.excellentclaims.land.editor.ui.LandEditorUIService;
import su.nightexpress.excellentclaims.land.editor.ui.dialog.EditorDescriptionDialog;
import su.nightexpress.excellentclaims.land.editor.ui.dialog.EditorDialogKeys;
import su.nightexpress.excellentclaims.land.editor.ui.dialog.EditorNameDialog;
import su.nightexpress.excellentclaims.land.editor.ui.dialog.EditorPriorityDialog;
import su.nightexpress.excellentclaims.land.editor.ui.menu.EditorIconSelectionMenu;
import su.nightexpress.excellentclaims.land.editor.ui.menu.EditorMenuLoader;
import su.nightexpress.excellentclaims.land.editor.ui.menu.EditorMenus;
import su.nightexpress.excellentclaims.land.editor.ui.menu.button.EditorDescriptionButton;
import su.nightexpress.excellentclaims.land.editor.ui.menu.button.EditorHomeButton;
import su.nightexpress.excellentclaims.land.editor.ui.menu.button.EditorIconButton;
import su.nightexpress.excellentclaims.land.editor.ui.menu.button.EditorNameButton;
import su.nightexpress.excellentclaims.land.editor.ui.menu.button.EditorPriorityButton;
import su.nightexpress.excellentclaims.land.settings.LandSettings;
import su.nightexpress.excellentclaims.land.ui.LandUIService;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogRegistry;

@NullMarked
public final class LandEditorConfiguration {

    private static final Identifier ID = Identifier.of("land_editor");

    private LandEditorConfiguration() {
    }

    public static void configure(LandsModule module, DependencyContainer container) {
        LandSettings settings = container.get(LandSettings.class);
        LandDataService dataService = container.get(LandDataService.class);
        ClaimPermissionAPI permissionService = container.get(ClaimPermissionAPI.class);

        LandEditorService editor = new LandEditorService(settings, dataService, permissionService);
        LandEditorModule editorModule = new LandEditorModule(ID);

        container.register(LandEditorService.class, editor);

        configureUI(container, editor, editorModule);
        configureCommands(container, editor);

        module.addComponent(editorModule);
    }

    private static void configureCommands(DependencyContainer container, LandEditorService editor) {
        CommandRegistry commands = container.get(CommandRegistry.class);

        LandCommandResolver resolver = container.get(LandCommandResolver.class);
        LandCommandSuggestions suggestions = container.get(LandCommandSuggestions.class);
        MessageDispatcher messages = container.get(MessageDispatcher.class);

        commands.registerCommand(new SetNameCommand(resolver, suggestions, editor, messages));
        commands.registerCommand(new SetDescriptionCommand(resolver, suggestions, editor, messages));
        commands.registerCommand(new SetPriorityCommand(resolver, suggestions, editor, messages));
        commands.registerCommand(new SetSpawnCommand(resolver, suggestions, editor, messages));
    }

    private static void configureUI(DependencyContainer container, LandEditorService editor, LandEditorModule module) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        DialogRegistry dialogs = container.get(DialogRegistry.class);
        MessageDispatcher dispatcher = container.get(MessageDispatcher.class);

        Path menuPath = plugin.dataPath().resolve(ClaimsConstants.DIR_MENU);

        LandUIService coreUI = container.get(LandUIService.class);
        LandSettings settings = container.get(LandSettings.class);

        LandEditorUIService editorUI = new LandEditorUIService(dialogs, coreUI, editor);
        LandEditorUIController controller = new LandEditorUIController(editor, editorUI, dispatcher);

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
