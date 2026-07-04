package su.nightexpress.excellentclaims.rules.ui.dialog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimsPlaceholders;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimRules;
import su.nightexpress.excellentclaims.api.rule.ClaimRule;
import su.nightexpress.excellentclaims.api.rule.filter.ElementDisplay;
import su.nightexpress.excellentclaims.api.rule.filter.StringAdapter;
import su.nightexpress.excellentclaims.rules.behavior.FilterBehavior;
import su.nightexpress.excellentclaims.rules.filter.FilterMode;
import su.nightexpress.excellentclaims.rules.filter.FilteredSet;
import su.nightexpress.excellentclaims.rules.lang.RulesLang;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.base.WrappedDialogAfterAction;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;
import su.nightexpress.nightcore.locale.entry.ButtonLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.DialogActions;
import su.nightexpress.nightcore.ui.dialog.build.DialogBases;
import su.nightexpress.nightcore.ui.dialog.build.DialogBodies;
import su.nightexpress.nightcore.ui.dialog.build.DialogButtons;
import su.nightexpress.nightcore.ui.dialog.build.DialogTypes;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;
import su.nightexpress.nightcore.util.Enums;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class RuleListDialog<T> extends Dialog<RuleDialogContext<FilteredSet<T>>> {

    private static final String ACTION_MODE    = "mode";
    private static final String ACTION_ELEMENT = "element";

    private static final String JSON_MODE         = "mode";
    private static final String JSON_ELEMENT_NAME = "element_name";

    private final StringAdapter<T>  adapter;
    private final ElementDisplay<T> display;

    public RuleListDialog(StringAdapter<T> filterType, ElementDisplay<T> display) {
        super();
        this.adapter = filterType;
        this.display = display;
    }

    @Override
    public WrappedDialog create(Player player, RuleDialogContext<FilteredSet<T>> context) {
        Claim claim = context.claim();
        ClaimRules rules = claim.getRules();
        ClaimRule<FilteredSet<T>> rule = context.rule();
        FilteredSet<T> filter = rules.getOrDefault(rule);

        List<WrappedActionButton> buttons = new ArrayList<>();

        Set<T> allowedTypes = new HashSet<>();
        if (rule.getBehavior() instanceof FilterBehavior<?, T> filterBehavior) {
            allowedTypes = filterBehavior.getAllEntries();
        }

        buttons.addAll(modeButtons(filter.getMode()));

        allowedTypes
            .stream()
            .sorted(Comparator.comparing(this.adapter::serialize))
            .forEach(element -> buttons.add(this.itemButton(filter, element)));

        return Dialogs.builder()
            .base(DialogBases.builder(rule.getDisplayName())
                .body(DialogBodies.plainMessage(String.join("\n", rule.getDescription())))
                .afterAction(WrappedDialogAfterAction.NONE)
                .build()
            )
            .type(DialogTypes.multiAction(buttons).columns(3).exitAction(DialogButtons.apply()).build())
            .handleResponse(ACTION_ELEMENT, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                String name = nbtHolder.getText(JSON_ELEMENT_NAME).orElse(null);
                if (name == null) return;

                T entry = this.adapter.deserialize(name);
                if (entry == null) return;

                if (filter.contains(entry)) {
                    filter.removeEntry(entry);
                }
                else {
                    filter.addEntry(entry);
                }

                this.show(viewer.getPlayer(), context, viewer.getCallback());
            })
            .handleResponse(ACTION_MODE, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                this.saveMode(filter, nbtHolder);
                this.show(player, context, viewer.getCallback());
            })
            .handleResponse(DialogActions.APPLY, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                rules.set(rule, filter);
                context.dirtyFlag().run();

                viewer.closeFully();
            })
            .build();
    }

    private void saveMode(FilteredSet<T> filter, NightNbtHolder nbtHolder) {
        FilterMode mode = nbtHolder.getText(JSON_MODE)
            .map(raw -> Enums.get(raw, FilterMode.class))
            .orElse(filter.getMode());
        filter.setMode(mode);
    }

    private WrappedActionButton itemButton(FilteredSet<T> filter, T element) {
        PlaceholderContext namePlaceholders = PlaceholderContext.builder()
            .with(ClaimsPlaceholders.GENERIC_ICON, () -> Optional.ofNullable(this.display.getSpriteTag(element))
                .map(
                    string -> string + " ").orElse(""))
            .with(CommonPlaceholders.GENERIC_VALUE, () -> display.getNameLocalized(element))
            .build();

        String name = adapter.serialize(element);
        boolean contains = filter.contains(element);
        ButtonLocale locale;
        FilterMode mode = filter.getMode();

        if (mode == FilterMode.BLACKLIST) {
            if (contains) {
                locale = RulesLang.UI_DIALOG_FILTER_BLACKLIST_ENTRY_IN;
            }
            else {
                locale = RulesLang.UI_DIALOG_FILTER_BLACKLIST_ENTRY_NOT_IN;
            }
        }
        else {
            if (contains) {
                locale = RulesLang.UI_DIALOG_FILTER_WHITELIST_ENTRY_IN;
            }
            else {
                locale = RulesLang.UI_DIALOG_FILTER_WHITELIST_ENTRY_NOT_IN;
            }
        }

        NightNbtHolder nbt = NightNbtHolder.builder().put(JSON_ELEMENT_NAME, name).build();

        return DialogButtons
            .action(locale)
            .placeholders(namePlaceholders)
            .action(DialogActions.customClick(ACTION_ELEMENT, nbt))
            .build();
    }

    private static <E> List<WrappedActionButton> modeButtons(FilterMode mode) {
        List<WrappedActionButton> buttons = new ArrayList<>();

        FilterMode next = Lists.next(mode);

        NightNbtHolder nbt = NightNbtHolder.builder().put(JSON_MODE, next.id()).build();

        PlaceholderContext namePlaceholders = PlaceholderContext.builder()
            .with(CommonPlaceholders.GENERIC_VALUE, () -> RulesLang.FILTER_MODE.getLocalized(mode))
            .with(ClaimsPlaceholders.GENERIC_NEXT, () -> RulesLang.FILTER_MODE.getLocalized(next))
            .build();

        WrappedActionButton button = DialogButtons.action(RulesLang.UI_DIALOG_FILTER_BUTTON_MODE)
            .action(DialogActions.customClick(JSON_MODE, nbt))
            .placeholders(namePlaceholders)
            .build();

        WrappedActionButton emptyButton = DialogButtons.action(TagWrappers.SPRITE_ITEM.apply(Material.REDSTONE))
            .width(30)
            .build();

        buttons.add(emptyButton);
        buttons.add(button);
        buttons.add(emptyButton);

        return buttons;
    }
}
