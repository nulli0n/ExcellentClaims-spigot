package su.nightexpress.excellentclaims.wilderness.lang;

import org.bukkit.Sound;

import su.nightexpress.excellentclaims.api.ClaimsPlaceholders;
import su.nightexpress.excellentclaims.wilderness.WildernessPlaceholders;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public class WildernessLang implements LangContainer {

    public static final TextLocale COMMAND_ARGUMENT_NAME_WILDERNESS = LangEntry
        .builder("Wilderness.Command.Argument.Name.Wilderness")
        .text("wilderness");

    public static final TextLocale COMMAND_WILDERNESS_DESC = LangEntry
        .builder("Wilderness.Command.Wilderness.Desc")
        .text("Wilderness commands.");

    public static final TextLocale COMMAND_WILDERNESS_RULES_DESC = LangEntry
        .builder("Wilderness.Command.Wilderness.Rules.Desc")
        .text("Open Rules GUI.");

    public static final TextLocale COMMAND_WILDERNESS_MENU_DESC = LangEntry
        .builder("Wilderness.Command.Wilderness.Menu.Desc")
        .text("Open Region GUI.");

    public static final TextLocale COMMAND_WILDERNESS_LIST_DESC = LangEntry
        .builder("Wilderness.Command.Wilderness.List.Desc")
        .text("Open GUI with all wilderness regions.");

    public static final TextLocale COMMAND_WILDERNESS_SET_SPAWN_DESC = LangEntry
        .builder("Wilderness.Command.Wilderness.SetSpawn.Desc")
        .text("Set region spawn.");

    public static final TextLocale COMMAND_WILDERNESS_SET_NAME_DESC = LangEntry
        .builder("Wilderness.Command.Wilderness.Rename.Desc")
        .text("Rename region.");

    public static final TextLocale COMMAND_WILDERNESS_SET_DESCRIPTION_DESC = LangEntry
        .builder("Wilderness.Command.Wilderness.Description.Desc")
        .text("Set region description.");

    public static final TextLocale COMMAND_WILDERNESS_SET_PRIORITY_DESC = LangEntry
        .builder("Wilderness.Command.Wilderness.SetPriority.Desc")
        .text("Set region priority.");

    public static final MessageLocale COMMAND_SYNTAX_INVALID_WILDERNESS = LangEntry
        .builder("Wilderness.Command.Syntax.InvalidWilderness")
        .chatMessage(
            TagWrappers.GRAY.wrap(TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_INPUT) +
                " is not a valid wilderness region!")
        );

    public static final MessageLocale ERROR_NO_REGION_IN_LOCATION = LangEntry
        .builder("Wilderness.Error.NoRegionInLocation")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("There is no wilderness region(s) at your location.")
        );

    public static final MessageLocale ERROR_WILDERNESS_DISABLED = LangEntry.builder("Wilderness.Error.RegionDisabled")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("The wilderness region was disabled because the world it belongs to was unloaded.")
        );

    public static final MessageLocale WILDERNESS_TELEPORT_SUCCESS = LangEntry.builder("Wilderness.Teleport.Success")
        .chatMessage(
            TagWrappers.GRAY.wrap("You have been teleported to the spawn location of " +
                TagWrappers.GREEN.wrap(WildernessPlaceholders.WILDERNESS_NAME) + " wilderness region.")
        );

    public static final MessageLocale WILDERNESS_TELEPORT_CANCELLED = LangEntry.builder("Wilderness.Teleport.Cancelled")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("The teleport was interrupted.")
        );

    public static final MessageLocale EDITOR_SET_NAME_SUCCESS = LangEntry.builder("Wilderness.Editor.SetName.Success")
        .chatMessage(
            TagWrappers.GRAY.wrap("You have successfully changed name of wilderness region to " +
                TagWrappers.GREEN.wrap(WildernessPlaceholders.WILDERNESS_NAME) + "!")
        );

    public static final MessageLocale EDITOR_SET_NAME_TOO_LONG = LangEntry.builder("Wilderness.Editor.SetName.TooLong")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("Wilderness region name can not be longer than " +
                TagWrappers.RED.wrap(ClaimsPlaceholders.GENERIC_MAX) + " characters!")
        );


    public static final MessageLocale EDITOR_SET_DESCRIPTION_SUCCESS = LangEntry
        .builder("Wilderness.Editor.SetDescription.Success")
        .chatMessage(
            TagWrappers.GRAY.wrap("You have successfully changed description of " +
                TagWrappers.WHITE.wrap(WildernessPlaceholders.WILDERNESS_NAME) + " wilderness region to:"),
            TagWrappers.GRAY.wrap("\"" + TagWrappers.GREEN.wrap(WildernessPlaceholders.WILDERNESS_DESCRIPTION) + "\"")
        );

    public static final MessageLocale EDITOR_SET_DESCRIPTION_TOO_LONG = LangEntry
        .builder("Wilderness.Editor.SetDescription.TooLong")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("Wilderness region description can not be longer than " +
                TagWrappers.RED.wrap(ClaimsPlaceholders.GENERIC_MAX) + " characters!")
        );


    public static final MessageLocale EDITOR_SET_SPAWN_SUCCESS = LangEntry.builder("Wilderness.Editor.SetSpawn.Set")
        .chatMessage(
            Sound.BLOCK_WOODEN_DOOR_OPEN,
            TagWrappers.GRAY.wrap("You have successfully updated spawn location of " +
                TagWrappers.WHITE.wrap(WildernessPlaceholders.WILDERNESS_NAME) + " wilderness region!")
        );

    public static final MessageLocale EDITOR_SET_SPAWN_OUTSIDE = LangEntry.builder("Wilderness.Editor.SetSpawn.OutSide")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("Wilderness spawn location must be within region bounds!")
        );


    public static final MessageLocale EDITOR_SET_PRIORITY_SUCCESS = LangEntry.builder("Wilderness.Editor.Priority.Set")
        .chatMessage(
            TagWrappers.GRAY.wrap("You have successfully updated priority of " +
                TagWrappers.WHITE.wrap(WildernessPlaceholders.WILDERNESS_NAME) + " wilderness region to " +
                TagWrappers.GREEN.wrap(WildernessPlaceholders.WILDERNESS_PRIORITY) + "!")
        );

    public static final MessageLocale EDITOR_PRIORITY_NOT_IN_BOUNDS = LangEntry
        .builder("Wilderness.Editor.Priority.NotInBounds")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("Priority value must be between " +
                TagWrappers.RED.wrap(ClaimsPlaceholders.GENERIC_MIN) + " and " +
                TagWrappers.RED.wrap(ClaimsPlaceholders.GENERIC_MAX) + "!")
        );


    public static final MessageLocale EDITOR_SET_ICON_SUCCESS = LangEntry.builder("Wilderness.Editor.Icon.Set")
        .chatMessage(
            TagWrappers.GRAY.wrap("You have successfully updated icon of " +
                TagWrappers.WHITE.wrap(WildernessPlaceholders.WILDERNESS_NAME) + " wilderness region!")
        );

    public static final IconLocale UI_EDITOR_MENU_ICON_BUTTON = LangEntry.iconBuilder(
        "Wilderness.UI.Editor.Menu.IconButton")
        .accentColor(TagWrappers.GOLD)
        .name("Icon")
        .appendInfo("Represents the wilderness region in GUIs.")
        .br()
        .appendClick("Click to edit")
        .build();
}
