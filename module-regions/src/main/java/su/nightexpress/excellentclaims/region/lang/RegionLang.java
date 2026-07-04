package su.nightexpress.excellentclaims.region.lang;

import org.bukkit.Sound;

import su.nightexpress.excellentclaims.api.ClaimsPlaceholders;
import su.nightexpress.excellentclaims.region.RegionsPlaceholders;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.locale.message.MessageData;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public class RegionLang implements LangContainer {

    public static final TextLocale COMMAND_ARGUMENT_NAME_REGION = LangEntry
        .builder("Regions.Command.Argument.Name.Region")
        .text("region");

    public static final TextLocale COMMAND_REGION_DESC = LangEntry
        .builder("Regions.Command.Region.Desc")
        .text("Region commands.");

    public static final TextLocale COMMAND_REGION_WAND_DESC = LangEntry
        .builder("Regions.Command.Region.Wand.Desc")
        .text("Get region wand tool.");

    public static final TextLocale COMMAND_REGION_CLAIM_DESC = LangEntry
        .builder("Regions.Command.Region.Claim.Desc")
        .text("Claim a region.");

    public static final TextLocale COMMAND_REGION_UNCLAIM_DESC = LangEntry
        .builder("Regions.Command.Region.Unclaim.Desc")
        .text("Unclaim region.");

    public static final TextLocale COMMAND_REGION_RULES_DESC = LangEntry
        .builder("Regions.Command.Region.Rules.Desc")
        .text("Open Rules GUI.");

    public static final TextLocale COMMAND_REGION_MEMBERS_DESC = LangEntry
        .builder("Regions.Command.Region.Members.Desc")
        .text("Open Members GUI.");

    public static final TextLocale COMMAND_REGION_MENU_DESC = LangEntry
        .builder("Regions.Command.Region.Menu.Desc")
        .text("Open Region GUI.");

    public static final TextLocale COMMAND_REGION_LIST_DESC = LangEntry
        .builder("Regions.Command.Region.List.Desc")
        .text("Open GUI with your regions.");

    public static final TextLocale COMMAND_REGION_INSPECT_DESC = LangEntry
        .builder("Regions.Command.Region.Inspect.Desc")
        .text("Inspect player's regions.");

    public static final TextLocale COMMAND_REGION_SET_SPAWN_DESC = LangEntry
        .builder("Regions.Command.Region.SetSpawn.Desc")
        .text("Set region spawn.");

    public static final TextLocale COMMAND_REGION_BORDERS_DESC = LangEntry
        .builder("Regions.Command.Region.Borders.Desc")
        .text("Toggle region borders.");

    public static final TextLocale COMMAND_REGION_SET_NAME_DESC = LangEntry
        .builder("Regions.Command.Region.Rename.Desc")
        .text("Rename region.");

    public static final TextLocale COMMAND_REGION_SET_DESCRIPTION_DESC = LangEntry
        .builder("Regions.Command.Region.Description.Desc")
        .text("Set region description.");

    public static final TextLocale COMMAND_REGION_SET_PRIORITY_DESC = LangEntry
        .builder("Regions.Command.Region.SetPriority.Desc")
        .text("Set region priority.");

    public static final MessageLocale COMMAND_SYNTAX_INVALID_REGION_CLAIM = LangEntry
        .builder("Regions.Command.Syntax.InvalidRegion")
        .chatMessage(
            TagWrappers.GRAY.wrap(TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_INPUT) +
                " is not a valid region!")
        );

    public static final MessageLocale ERROR_NO_REGION_IN_LOCATION = LangEntry
        .builder("Regions.Error.NoRegionInLocation")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("There is no region(s) at your location.")
        );

    public static final MessageLocale ERROR_REGION_DISABLED = LangEntry.builder("Regions.Error.RegionDisabled")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("The region was disabled because the world it belongs to was unloaded.")
        );

    public static final MessageLocale REGION_INSPECT_EMPTY = LangEntry.builder("Regions.Inspect.Empty")
        .chatMessage(
            TagWrappers.GRAY.wrap("Player " + TagWrappers.RED.wrap(CommonPlaceholders.PLAYER_NAME) +
                " is not involved in any region as an owner or a member.")
        );

    public static final MessageLocale REGION_TELEPORT_SUCCESS = LangEntry.builder("Regions.Teleport.Success")
        .chatMessage(
            TagWrappers.GRAY.wrap("You have been teleported to the home location of " +
                TagWrappers.GREEN.wrap(RegionsPlaceholders.REGION_NAME) + " region.")
        );

    public static final MessageLocale REGION_TELEPORT_CANCELLED = LangEntry.builder("Regions.Teleport.Cancelled")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("The teleport was interrupted.")
        );

    public static final MessageLocale REGION_BORDERS_ENABLED = LangEntry.builder("Regions.Borders.Enabled")
        .chatMessage(
            TagWrappers.GRAY.wrap("You have toggled region borders " + TagWrappers.GREEN.wrap("ON") + ".")
        );

    public static final MessageLocale REGION_BORDERS_DISABLED = LangEntry.builder("Regions.Borders.Disabled")
        .chatMessage(
            TagWrappers.GRAY.wrap("You have toggled region borders " + TagWrappers.RED.wrap("OFF") + ".")
        );

    public static final MessageLocale CLAIMING_SUCCESS = LangEntry.builder("Regions.Claim.Success")
        .chatMessage(
            Sound.ENTITY_PLAYER_LEVELUP,
            TagWrappers.GRAY.wrap("You have successfully claimed " +
                TagWrappers.GREEN.wrap(RegionsPlaceholders.REGION_NAME) + " region!")
        );

    public static final MessageLocale CLAIMING_BAD_WORLD = LangEntry.builder("Regions.Claim.BadWorld")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("You can't claim regions here.")
        );

    public static final MessageLocale CLAIMING_INVALID_NAME = LangEntry.builder("Regions.Claim.InvalidName")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("You can't use this name for your region.")
        );

    public static final MessageLocale CLAIMING_ALREADY_EXISTS = LangEntry
        .builder("Regions.Claim.AlreadyExists")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("A region with such name already exists.")
        );

    public static final MessageLocale CLAIMING_REGION_QOUTA = LangEntry.builder("Regions.Claim.RegionQuota")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("You already has maximum of " +
                TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_AMOUNT) + " regions claimed.")
        );

    public static final MessageLocale CLAIMING_OVERLAP_FORBIDDEN = LangEntry
        .builder("Regions.Claim.Overlap.Forbidden")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("You can't claim regions that overlaps with other claims.")
        );

    public static final MessageLocale CLAIMING_OVERLAP_INCOMPATIBLE_CLAIM = LangEntry
        .builder("Regions.Claim.Overlap.IncompatibleClaim")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("This region contains " + TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_NAME) +
                " claim that you are not allowed to overlap with.")
        );

    public static final MessageLocale CLAIMING_INSUFFICIENT_FUNDS = LangEntry
        .builder("Regions.Claim.InsufficientFunds")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("You need " + TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_AMOUNT) +
                " to claim this region.")
        );

    public static final MessageLocale UNCLAIMING_SUCCESS = LangEntry.builder("Regions.Unclaim.Success")
        .chatMessage(
            Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR,
            TagWrappers.GRAY.wrap("You have successfully unclaimed " +
                TagWrappers.GREEN.wrap(RegionsPlaceholders.REGION_NAME) + " region.")
        );

    public static final MessageLocale SELECTION_START = LangEntry.builder("Regions.Selection.Start")
        .message(MessageData.CHAT_NO_PREFIX,
            " ",
            TagWrappers.GRAY.wrap("You have entered " + TagWrappers.GOLD.wrap("Region Selection") + " mode."),
            TagWrappers.GRAY.wrap("Use " +
                TagWrappers.GOLD.wrap(TagWrappers.KEY.apply("key.mouse.left")) + " and " +
                TagWrappers.GOLD.wrap(TagWrappers.KEY.apply("key.mouse.right")) + " to select " +
                TagWrappers.RED.wrap("Lowest") + " and " +
                TagWrappers.GREEN.wrap("Highest") + " positions."),
            " "
        );

    public static final MessageLocale SELECTION_QUIT = LangEntry.builder("Regions.Selection.Quit")
        .chatMessage(
            TagWrappers.GRAY.wrap("You have left " + TagWrappers.GOLD.wrap("Region Selection") + " mode.")
        );

    public static final MessageLocale SELECTION_PAUSED = LangEntry.builder("Regions.Selection.Paused")
        .message(MessageData.CHAT_NO_PREFIX,
            TagWrappers.GRAY.wrap("You have " + TagWrappers.RED.wrap("Paused") + " selection mode. "),
            TagWrappers.GRAY.wrap("Block interactions are " + TagWrappers.GREEN.wrap("enabled") + " now.")
        );

    public static final MessageLocale SELECTION_RESUMED = LangEntry.builder("Regions.Selection.Resumed")
        .message(MessageData.CHAT_NO_PREFIX,
            TagWrappers.GRAY.wrap("You have " + TagWrappers.GREEN.wrap("Resumed") + " selection mode. "),
            TagWrappers.GRAY.wrap("Block interactions are " + TagWrappers.RED.wrap("disabled") + " now.")
        );

    public static final MessageLocale SELECTION_SET_FIRST = LangEntry.builder("Regions.Selection.SetFirst")
        .chatMessage(
            TagWrappers.GRAY.wrap("You have set " + TagWrappers.GOLD.wrap("first") + " region position.")
        );

    public static final MessageLocale SELECTION_SET_SECOND = LangEntry.builder("Regions.Selection.SetSecond")
        .chatMessage(
            TagWrappers.GRAY.wrap("You have set " + TagWrappers.GOLD.wrap("second") + " region position.")
        );

    public static final MessageLocale SELECTION_SIZE_QUOTA = LangEntry
        .builder("Regions.Selection.SizeQuota")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("The region you selected is too big. Your limit is " +
                TagWrappers.RED.wrap(ClaimsPlaceholders.GENERIC_MAX) + " blocks.")
        );

    public static final MessageLocale SELECTION_CREATE_NO_SELECTION = LangEntry
        .builder("Regions.Selection.Create.NoSelection")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("You must selection region area to do that.")
        );

    public static final MessageLocale SELECTION_CREATE_INCOMPLETE = LangEntry
        .builder("Regions.Selection.Create.Incomplete")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("You must select two corners to create a region.")
        );

    public static final MessageLocale EDITOR_SET_NAME_SUCCESS = LangEntry.builder("Regions.Editor.SetName.Success")
        .chatMessage(
            TagWrappers.GRAY.wrap("You have successfully changed name of your region to " +
                TagWrappers.GREEN.wrap(RegionsPlaceholders.REGION_NAME) + "!")
        );

    public static final MessageLocale EDITOR_SET_NAME_TOO_LONG = LangEntry.builder("Regions.Editor.SetName.TooLong")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("Region name can not be longer than " +
                TagWrappers.RED.wrap(ClaimsPlaceholders.GENERIC_MAX) + " characters!")
        );


    public static final MessageLocale EDITOR_SET_DESCRIPTION_SUCCESS = LangEntry
        .builder("Regions.Editor.SetDescription.Success")
        .chatMessage(
            TagWrappers.GRAY.wrap("You have successfully changed description of your " +
                TagWrappers.WHITE.wrap(RegionsPlaceholders.REGION_NAME) + " region to:"),
            TagWrappers.GRAY.wrap("\"" + TagWrappers.GREEN.wrap(RegionsPlaceholders.REGION_DESCRIPTION) + "\"")
        );

    public static final MessageLocale EDITOR_SET_DESCRIPTION_TOO_LONG = LangEntry
        .builder("Regions.Editor.SetDescription.TooLong")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("Region description can not be longer than " +
                TagWrappers.RED.wrap(ClaimsPlaceholders.GENERIC_MAX) + " characters!")
        );


    public static final MessageLocale EDITOR_SET_SPAWN_SUCCESS = LangEntry.builder("Regions.Editor.SetSpawn.Set")
        .chatMessage(
            Sound.BLOCK_WOODEN_DOOR_OPEN,
            TagWrappers.GRAY.wrap("You have successfully updated home location of your " +
                TagWrappers.WHITE.wrap(RegionsPlaceholders.REGION_NAME) + " region!")
        );

    public static final MessageLocale EDITOR_SET_SPAWN_OUTSIDE = LangEntry.builder("Regions.Editor.SetSpawn.OutSide")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("Region spawn location must be within region bounds!")
        );


    public static final MessageLocale EDITOR_SET_PRIORITY_SUCCESS = LangEntry.builder("Regions.Editor.Priority.Set")
        .chatMessage(
            TagWrappers.GRAY.wrap("You have successfully updated priority of your " +
                TagWrappers.WHITE.wrap(RegionsPlaceholders.REGION_NAME) + " region to " +
                TagWrappers.GREEN.wrap(RegionsPlaceholders.REGION_PRIORITY) + "!")
        );

    public static final MessageLocale EDITOR_PRIORITY_NOT_IN_BOUNDS = LangEntry
        .builder("Regions.Editor.Priority.NotInBounds")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("Priority value must be between " +
                TagWrappers.RED.wrap(ClaimsPlaceholders.GENERIC_MIN) + " and " +
                TagWrappers.RED.wrap(ClaimsPlaceholders.GENERIC_MAX) + "!")
        );


    public static final MessageLocale EDITOR_SET_ICON_SUCCESS = LangEntry.builder("Regions.Editor.Icon.Set")
        .chatMessage(
            TagWrappers.GRAY.wrap("You have successfully updated icon of your " +
                TagWrappers.WHITE.wrap(RegionsPlaceholders.REGION_NAME) + " region!")
        );

    public static final IconLocale UI_EDITOR_MENU_ICON_BUTTON = LangEntry.iconBuilder(
        "Regions.UI.Editor.Menu.IconButton")
        .accentColor(TagWrappers.GOLD)
        .name("Icon")
        .appendInfo("Represents the region in GUIs.")
        .br()
        .appendClick("Click to edit")
        .build();
}
