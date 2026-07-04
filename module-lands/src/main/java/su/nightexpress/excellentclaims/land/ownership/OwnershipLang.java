package su.nightexpress.excellentclaims.land.ownership;

import org.bukkit.Sound;

import su.nightexpress.excellentclaims.api.ClaimsPlaceholders;
import su.nightexpress.excellentclaims.land.LandsPlaceholders;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.ButtonLocale;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public final class OwnershipLang implements LangContainer {

    public static final TextLocale COMMAND_LAND_TRANSFER_DESC = LangEntry.builder("Command.Land.Transfer.Desc")
        .text("Transfer claim's ownership.");

    public static final MessageLocale TRANSFER_YOURSELF = LangEntry.builder("Lands.Ownership.Transfer.Yourself")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("You can not transfer claim to yourself.")
        );

    public static final MessageLocale TRANSFER_NO_ELIGIBLES = LangEntry.builder("Lands.Ownership.Transfer.NoEligibles")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("There are no eligible players online to whom you can transfer your claim.")
        );

    public static final MessageLocale TRANSFER_TARGET_OFFLINE = LangEntry
        .builder("Lands.Ownership.Transfer.TargetOffline")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("Target player must be online.")
        );

    public static final MessageLocale TRANSFER_TARGET_OWNER = LangEntry
        .builder("Lands.Ownership.Transfer.TargetOwner")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap(TagWrappers.RED.wrap(CommonPlaceholders.PLAYER_NAME) +
                " is already owner of this claim.")
        );

    public static final MessageLocale TRANSFER_TARGET_QUOTA_LIMIT = LangEntry
        .builder("Lands.Ownership.Transfer.TargetQuotaLimit")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap(TagWrappers.RED.wrap(CommonPlaceholders.PLAYER_NAME) +
                " is already owns too many claims.")
        );

    public static final MessageLocale TRANSFER_SUCCESS = LangEntry.builder("Lands.Ownership.Transfer.Success")
        .chatMessage(
            Sound.ENTITY_PLAYER_LEVELUP,
            TagWrappers.GRAY.wrap("You have successfully transferred your claim to " +
                TagWrappers.GREEN.wrap(CommonPlaceholders.PLAYER_NAME) + ".")
        );

    public static final MessageLocale TRANSFER_NOTIFY = LangEntry.builder("Lands.Ownership.Transfer.Notify")
        .chatMessage(
            TagWrappers.GRAY.wrap(TagWrappers.GREEN.wrap(CommonPlaceholders.PLAYER_NAME) +
                " has transferred you ownership of the " +
                TagWrappers.GREEN.wrap(LandsPlaceholders.LAND_NAME) + " claim.")
        );

    public static final TextLocale UI_DIALOG_TRANSFER_SELECT_TARGET_TITLE = LangEntry
        .builder("Lands.UI.Ownership.Dialog.TransferSelectTarget.Title")
        .text(
            TagWrappers.YELLOW.and(TagWrappers.BOLD).wrap("TRANSFER CLAIM") +
                TagWrappers.DARK_GRAY.wrap(" » ") + "Select online player"
        );

    public static final DialogElementLocale UI_DIALOG_TRANSFER_SELECT_TARGET_BODY = LangEntry
        .builder("Lands.UI.Ownership.Dialog.TransferSelectTarget.Body")
        .dialogElement(
            "Click the desired player to transfer your claim to."
        );

    public static final ButtonLocale UI_DIALOG_TRANSFER_SELECT_TARGET_PLAYER_BUTTON = LangEntry
        .builder("Lands.UI.Ownership.Dialog.TransferSelectTarget.PlayerButton")
        .button(ClaimsPlaceholders.GENERIC_ICON + " " + CommonPlaceholders.PLAYER_NAME,
            TagWrappers.WHITE.wrap("Click to " + TagWrappers.GREEN.wrap("transfer claim") + " to this player.")
        );

    private OwnershipLang() {
    }
}
