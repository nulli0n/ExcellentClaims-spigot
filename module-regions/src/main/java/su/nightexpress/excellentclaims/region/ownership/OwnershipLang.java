package su.nightexpress.excellentclaims.region.ownership;

import org.bukkit.Sound;

import su.nightexpress.excellentclaims.api.ClaimsPlaceholders;
import su.nightexpress.excellentclaims.region.RegionsPlaceholders;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.ButtonLocale;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public final class OwnershipLang implements LangContainer {

    public static final TextLocale COMMAND_LAND_TRANSFER_DESC = LangEntry
        .builder("Regions.Command.Region.Transfer.Desc")
        .text("Transfer region's ownership.");

    public static final MessageLocale TRANSFER_YOURSELF = LangEntry.builder("Regions.Ownership.Transfer.Yourself")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("You can not transfer region to yourself.")
        );

    public static final MessageLocale TRANSFER_NO_ELIGIBLES = LangEntry.builder(
        "Regions.Ownership.Transfer.NoEligibles")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("There are no eligible players online to whom you can transfer your region.")
        );

    public static final MessageLocale TRANSFER_TARGET_OFFLINE = LangEntry
        .builder("Regions.Ownership.Transfer.TargetOffline")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("Target player must be online.")
        );

    public static final MessageLocale TRANSFER_TARGET_OWNER = LangEntry
        .builder("Regions.Ownership.Transfer.TargetOwner")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap(TagWrappers.RED.wrap(CommonPlaceholders.PLAYER_NAME) +
                " is already owner of this region.")
        );

    public static final MessageLocale TRANSFER_TARGET_QUOTA_LIMIT = LangEntry
        .builder("Regions.Ownership.Transfer.TargetQuotaLimit")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap(TagWrappers.RED.wrap(CommonPlaceholders.PLAYER_NAME) +
                " is already owns too many regions.")
        );

    public static final MessageLocale TRANSFER_SUCCESS = LangEntry.builder("Regions.Ownership.Transfer.Success")
        .chatMessage(
            Sound.ENTITY_PLAYER_LEVELUP,
            TagWrappers.GRAY.wrap("You have successfully transferred your region to " +
                TagWrappers.GREEN.wrap(CommonPlaceholders.PLAYER_NAME) + ".")
        );

    public static final MessageLocale TRANSFER_NOTIFY = LangEntry.builder("Regions.Ownership.Transfer.Notify")
        .chatMessage(
            TagWrappers.GRAY.wrap(TagWrappers.GREEN.wrap(CommonPlaceholders.PLAYER_NAME) +
                " has transferred you ownership of the " +
                TagWrappers.GREEN.wrap(RegionsPlaceholders.REGION_NAME) + " region.")
        );

    public static final TextLocale UI_DIALOG_TRANSFER_SELECT_TARGET_TITLE = LangEntry
        .builder("Regions.UI.Ownership.Dialog.TransferSelectTarget.Title")
        .text(
            TagWrappers.YELLOW.and(TagWrappers.BOLD).wrap("TRANSFER CLAIM") +
                TagWrappers.DARK_GRAY.wrap(" » ") + "Select online player"
        );

    public static final DialogElementLocale UI_DIALOG_TRANSFER_SELECT_TARGET_BODY = LangEntry
        .builder("Regions.UI.Ownership.Dialog.TransferSelectTarget.Body")
        .dialogElement(
            "Click the desired player to transfer your region to."
        );

    public static final ButtonLocale UI_DIALOG_TRANSFER_SELECT_TARGET_PLAYER_BUTTON = LangEntry
        .builder("Regions.UI.Ownership.Dialog.TransferSelectTarget.PlayerButton")
        .button(ClaimsPlaceholders.GENERIC_ICON + " " + CommonPlaceholders.PLAYER_NAME,
            TagWrappers.WHITE.wrap("Click to " + TagWrappers.GREEN.wrap("transfer region") + " to this player.")
        );

    private OwnershipLang() {
    }
}
