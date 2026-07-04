package su.nightexpress.excellentclaims.rules.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleUIEditor;
import su.nightexpress.excellentclaims.rules.editor.EditorRegistry;
import su.nightexpress.excellentclaims.rules.editor.impl.BooleanEditor;
import su.nightexpress.excellentclaims.rules.filter.FilteredSet;
import su.nightexpress.excellentclaims.rules.type.FilterRuleType;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;
import su.nightexpress.excellentclaims.rules.ui.dialog.RuleDialogContext;
import su.nightexpress.excellentclaims.rules.ui.dialog.RuleListDialog;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogRegistry;
import su.nightexpress.nightcore.util.LowerCase;

@NullMarked
public final class RuleUIRegistrar {

    private RuleUIRegistrar() {
    }

    public static EditorRegistry createAndPopulateEditors(DialogRegistry dialogs) {
        EditorRegistry registry = new EditorRegistry();

        registry.register(RuleTypes.BOOLEAN, new BooleanEditor());

        registry.register(RuleTypes.COMMANDS,
            createFilterEditor(dialogs, "command", RuleTypes.COMMANDS)
        );

        registry.register(RuleTypes.MATERIALS,
            createFilterEditor(dialogs, "material", RuleTypes.MATERIALS)
        );

        registry.register(RuleTypes.ENTITY_TYPES,
            createFilterEditor(dialogs, "entity_type", RuleTypes.ENTITY_TYPES)
        );

        registry.register(RuleTypes.DAMAGE_TYPES,
            createFilterEditor(dialogs, "damage_type", RuleTypes.DAMAGE_TYPES)
        );

        return registry;
    }

    private static <T> RuleUIEditor<FilteredSet<T>> createFilterEditor(DialogRegistry dialogs,
                                                                       String key,
                                                                       FilterRuleType<T> type) {

        String id = LowerCase.internal(key);
        DialogKey<RuleDialogContext<FilteredSet<T>>> dialogKey = new DialogKey<>("rules.editor.filter_" + id);

        RuleUIEditor<FilteredSet<T>> editor = (player, claim, rule, context) -> {
            RuleDialogContext<FilteredSet<T>> dialogContext = new RuleDialogContext<FilteredSet<T>>(claim, rule, context::markDirty);
            dialogs.show(player, dialogKey, dialogContext, context::callback);
        };

        dialogs.register(dialogKey, new RuleListDialog<T>(type.getAdapter(), type.getDisplay()));

        return editor;
    }
}
