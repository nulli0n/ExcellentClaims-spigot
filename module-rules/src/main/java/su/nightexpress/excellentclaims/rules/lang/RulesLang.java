package su.nightexpress.excellentclaims.rules.lang;

import org.bukkit.Material;

import su.nightexpress.excellentclaims.api.ClaimsPlaceholders;
import su.nightexpress.excellentclaims.rules.filter.FilterMode;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.ButtonLocale;
import su.nightexpress.nightcore.locale.entry.EnumLocale;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public final class RulesLang implements LangContainer {

    public static final EnumLocale<FilterMode> FILTER_MODE = LangEntry.builder("Rules.FilterMode")
        .enumeration(FilterMode.class);

    public static final MessageLocale PROTECTION_BLOCK_INTERACT = LangEntry
        .builder("Rules.Protection.Info.BlockInteract")
        .actionBarMessage(
            TagWrappers.RED.wrap("You can't use " +
                TagWrappers.YELLOW.wrap(CommonPlaceholders.GENERIC_VALUE) + " here!")
        );

    public static final MessageLocale PROTECTION_BLOCK_BREAK = LangEntry.builder("Protection.Info.BlockBreak")
        .actionBarMessage(
            TagWrappers.RED.wrap("You can't break " +
                TagWrappers.YELLOW.wrap(CommonPlaceholders.GENERIC_VALUE) + " here!")
        );

    public static final MessageLocale PROTECTION_BLOCK_PLACE = LangEntry.builder("Protection.Info.BlockPlace")
        .actionBarMessage(
            TagWrappers.RED.wrap("You can't place " +
                TagWrappers.YELLOW.wrap(CommonPlaceholders.GENERIC_VALUE) + " here!")
        );

    public static final MessageLocale PROTECTION_BLOCK_FERTILIZE = LangEntry.builder("Protection.Info.BlockFertilize")
        .actionBarMessage(
            TagWrappers.RED.wrap("You can't fertilize " +
                TagWrappers.YELLOW.wrap(CommonPlaceholders.GENERIC_VALUE) + " here!")
        );

    public static final MessageLocale PROTECTION_BLOCK_HARVEST = LangEntry.builder("Protection.Info.BlockHarvest")
        .actionBarMessage(
            TagWrappers.RED.wrap("You can't harvest " +
                TagWrappers.YELLOW.wrap(CommonPlaceholders.GENERIC_VALUE) + " here!")
        );

    public static final MessageLocale PROTECTION_BLOCK_TRAMP = LangEntry.builder("Protection.Info.BlockTramp")
        .actionBarMessage(
            TagWrappers.RED.wrap("You can't tramp " +
                TagWrappers.YELLOW.wrap(CommonPlaceholders.GENERIC_VALUE) + " here!")
        );


    public static final MessageLocale PROTECTION_ITEM_USE = LangEntry.builder("Protection.Info.ItemUse")
        .actionBarMessage(
            TagWrappers.RED.wrap("You can't use " +
                TagWrappers.YELLOW.wrap(CommonPlaceholders.GENERIC_VALUE) + " here!")
        );

    public static final MessageLocale PROTECTION_PROJECTILE_THROW = LangEntry.builder("Protection.Info.ProjectileThrow")
        .actionBarMessage(
            TagWrappers.RED.wrap("You can't throw " +
                TagWrappers.YELLOW.wrap(CommonPlaceholders.GENERIC_VALUE) + " here!")
        );

    public static final MessageLocale PROTECTION_PROJECTILE_SHOOT = LangEntry.builder("Protection.Info.ProjectileShoot")
        .actionBarMessage(
            TagWrappers.RED.wrap("You can't shoot " +
                TagWrappers.YELLOW.wrap(CommonPlaceholders.GENERIC_VALUE) + " here!")
        );

    public static final MessageLocale PROTECTION_ENTITY_INTERACT = LangEntry.builder("Protection.Info.EntityInteract")
        .actionBarMessage(
            TagWrappers.RED.wrap("You can't interact with " +
                TagWrappers.YELLOW.wrap(CommonPlaceholders.GENERIC_VALUE) + " here!")
        );

    public static final MessageLocale PROTECTION_PORTAL_USE = LangEntry.builder("Protection.Info.Portal")
        .actionBarMessage(
            TagWrappers.RED.wrap("You can't use portals here!")
        );

    public static final MessageLocale PROTECTION_DAMAGE_ENTITY = LangEntry.builder("Protection.Info.DamageEntity")
        .actionBarMessage(
            TagWrappers.RED.wrap("You can't harm " +
                TagWrappers.YELLOW.wrap(CommonPlaceholders.GENERIC_VALUE) + " here!")
        );

    public static final MessageLocale PROTECTION_COMMAND_USAGE = LangEntry.builder("Protection.Info.CommandUsage")
        .actionBarMessage(
            TagWrappers.RED.wrap("You can't use " +
                TagWrappers.YELLOW.wrap(CommonPlaceholders.GENERIC_VALUE) + " here!")
        );

    public static final MessageLocale PROTECTION_ITEM_DROP = LangEntry.builder("Protection.Info.ItemDrop")
        .actionBarMessage(
            TagWrappers.RED.wrap("You can't drop items here!")
        );

    public static final MessageLocale PROTECTION_ITEM_PICKUP = LangEntry.builder("Protection.Info.ItemPickup")
        .actionBarMessage(
            TagWrappers.RED.wrap("You can't pickup items here!")
        );

    public static final TextLocale FILTER_SUMMARY = LangEntry.builder("Rules.Other.FilterSummary")
        .text(ClaimsPlaceholders.GENERIC_TYPE + " " +
            TagWrappers.GRAY.wrap("(" + TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_AMOUNT) + " items)"));

    public static final ButtonLocale UI_DIALOG_FILTER_BUTTON_MODE = LangEntry
        .builder("Rules.UI.Dialog.Filter.Button.Mode")
        .button(
            TagWrappers.SPRITE_ITEM.apply(Material.COMPARATOR) + " Mode: " + CommonPlaceholders.GENERIC_VALUE,
            "Click to switch to " + TagWrappers.GOLD.wrap(ClaimsPlaceholders.GENERIC_NEXT) + " mode."
        );

    public static final ButtonLocale UI_DIALOG_FILTER_BLACKLIST_ENTRY_IN = LangEntry
        .builder("Rules.UI.Dialog.Filter.Blacklist.In")
        .button(
            ClaimsPlaceholders.GENERIC_ICON + TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_VALUE),
            "This item is " + TagWrappers.RED.and(TagWrappers.UNDERLINED).wrap("blacklisted") + "." +
                TagWrappers.BR +
                "Click to " + TagWrappers.GREEN.wrap("remove") + " it from blacklist."
        );

    public static final ButtonLocale UI_DIALOG_FILTER_BLACKLIST_ENTRY_NOT_IN = LangEntry
        .builder("Rules.UI.Dialog.Filter.Blacklist.NotIn")
        .button(
            ClaimsPlaceholders.GENERIC_ICON + TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_VALUE),
            "This item is " + TagWrappers.GREEN.and(TagWrappers.UNDERLINED).wrap("not blacklisted") + "." +
                TagWrappers.BR +
                "Click to " + TagWrappers.RED.wrap("blacklist") + " it."
        );

    public static final ButtonLocale UI_DIALOG_FILTER_WHITELIST_ENTRY_IN = LangEntry
        .builder("Rules.UI.Dialog.Filter.Whitelist.In")
        .button(
            ClaimsPlaceholders.GENERIC_ICON + TagWrappers.GREEN.wrap(CommonPlaceholders.GENERIC_VALUE),
            "This item is " + TagWrappers.GREEN.and(TagWrappers.UNDERLINED).wrap("whitelisted") + "." +
                TagWrappers.BR +
                "Click to " + TagWrappers.RED.wrap("remove") + " it from whitelist."
        );

    public static final ButtonLocale UI_DIALOG_FILTER_WHITELIST_ENTRY_NOT_IN = LangEntry
        .builder("Rules.UI.Dialog.Filter.Whitelist.NotIn")
        .button(
            ClaimsPlaceholders.GENERIC_ICON + TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_VALUE),
            "This item is " + TagWrappers.RED.and(TagWrappers.UNDERLINED).wrap("not whitelisted") + "." +
                TagWrappers.BR +
                "Click to " + TagWrappers.GREEN.wrap("whitelist") + " it."
        );

    private RulesLang() {
    }
}
