package su.nightexpress.excellentclaims.region.member;

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

public final class MemberLang implements LangContainer {

    public static final MessageLocale MEMBER_PURGE_SUCCESS = LangEntry.builder("Regions.Member.Purge.Success")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("You have successfully purged all region members.")
        );

    public static final MessageLocale MEMBER_PURGE_ALREADY_EMPTY = LangEntry.builder(
        "Regions.Member.Purge.AlreadyEmpty")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("There is already no members in the region.")
        );

    public static final MessageLocale MEMBER_ADD_ONLINE_NOTHING = LangEntry.builder("Regions.Member.AddOnline.Nothing")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("There are no players online that could be members of your region.")
        );

    public static final MessageLocale MEMBER_ADD_TARGET_IS_OWNER = LangEntry.builder("Regions.Member.Add.TargetIsOwner")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap(TagWrappers.RED.wrap(CommonPlaceholders.PLAYER_NAME) +
                " is the region owner. You can't add it as a member.")
        );

    public static final MessageLocale MEMBER_ADD_TARGET_IS_MEMBER = LangEntry.builder(
        "Regions.Member.Add.TargetIsMember")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap(TagWrappers.RED.wrap(CommonPlaceholders.PLAYER_NAME) +
                " is already a member of the " + TagWrappers.YELLOW.wrap(RegionsPlaceholders.REGION_NAME) + " region.")
        );

    public static final MessageLocale MEMBER_ADD_SUCCESS = LangEntry.builder("Regions.Member.Add.Success")
        .chatMessage(
            TagWrappers.GRAY.wrap("You added " + TagWrappers.YELLOW.wrap(CommonPlaceholders.PLAYER_NAME) +
                " as " + TagWrappers.YELLOW.wrap(ClaimsPlaceholders.RANK_NAME) +
                " to the " + TagWrappers.YELLOW.wrap(RegionsPlaceholders.REGION_NAME) + " region.")
        );

    public static final MessageLocale MEMBER_KICK_TARGET_IS_ABOVE = LangEntry.builder(
        "Regions.Member.Kick.TargetIsAbove")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("You can't kick " + TagWrappers.RED.wrap(CommonPlaceholders.PLAYER_NAME) +
                " because it has a higher rank than yours.")
        );

    public static final MessageLocale MEMBER_KICK_TARGET_IS_NOT_MEMBER = LangEntry
        .builder("Member.Kick.TargetIsNotMember")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("You can't kick " + TagWrappers.RED.wrap(CommonPlaceholders.PLAYER_NAME) +
                " because it is not a member of your region.")
        );

    public static final MessageLocale MEMBER_KICK_SUCCESS = LangEntry.builder("Regions.Member.Kick.Success")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("You have successfully kicked " +
                TagWrappers.GREEN.wrap(CommonPlaceholders.PLAYER_NAME) + " from region members.")
        );

    public static final MessageLocale MEMBER_PROMOTION_SUCCESS = LangEntry
        .builder("Regions.Member.Promotion.Success")
        .chatMessage(
            TagWrappers.GRAY.wrap("You have successfuly promoted " +
                TagWrappers.WHITE.wrap(CommonPlaceholders.PLAYER_NAME) + " to " +
                TagWrappers.GREEN.wrap(ClaimsPlaceholders.RANK_NAME) + "!")
        );

    public static final MessageLocale MEMBER_PROMOTION_NOTHING_TO = LangEntry
        .builder("Regions.Member.Promotion.NothingTo")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap(TagWrappers.RED.wrap(CommonPlaceholders.PLAYER_NAME) +
                " can not be promoted further!")
        );

    public static final MessageLocale MEMBER_DEMOTION_SUCCESS = LangEntry
        .builder("Regions.Member.Demotion.Success")
        .chatMessage(
            TagWrappers.GRAY.wrap("You have successfuly demoted " +
                TagWrappers.WHITE.wrap(CommonPlaceholders.PLAYER_NAME) + " to " +
                TagWrappers.RED.wrap(ClaimsPlaceholders.RANK_NAME) + "!")
        );

    public static final MessageLocale MEMBER_DEMOTION_NOTHING_TO = LangEntry
        .builder("Regions.Member.Demotion.NothingTo")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap(TagWrappers.RED.wrap(CommonPlaceholders.PLAYER_NAME) +
                " can not be demoted further!")
        );

    public static final TextLocale UI_DIALOG_ADD_MEMBER_BY_ONLINE_TITLE = LangEntry
        .builder("Regions.UI.Member.Dialog.AddMember.Title")
        .text(
            TagWrappers.YELLOW.and(TagWrappers.BOLD).wrap("ADD MEMBER") +
                TagWrappers.DARK_GRAY.wrap(" » ") + "Select online player"
        );

    public static final DialogElementLocale UI_DIALOG_ADD_MEMBER_BY_ONLINE_BODY = LangEntry
        .builder("Regions.UI.Member.Dialog.AddMember.Body")
        .dialogElement(
            "Click the desired player to add it to region members."
        );

    public static final ButtonLocale UI_DIALOG_ADD_MEMBER_BY_ONLINE_PLAYER_BUTTON = LangEntry
        .builder("Regions.UI.Member.Dialog.AddMember.PlayerButton")
        .button(ClaimsPlaceholders.GENERIC_ICON + " " + CommonPlaceholders.PLAYER_NAME,
            TagWrappers.WHITE.wrap("Click to " + TagWrappers.GREEN.wrap("add") + " this player.")
        );

    public static final TextLocale UI_DIALOG_PURGE_MEMBERS_TITLE = LangEntry
        .builder("Regions.UI.Member.Dialog.PurgeMembers.Title")
        .text(
            TagWrappers.YELLOW.and(TagWrappers.BOLD).wrap("PURGE MEMBERS") +
                TagWrappers.DARK_GRAY.wrap(" » ") + "Are you sure?"
        );

    public static final DialogElementLocale UI_DIALOG_PURGE_MEMBERS_BODY = LangEntry
        .builder("Regions.UI.Member.Dialog.PurgeMembers.Body")
        .dialogElement(
            "Are you sure you want to kick " + TagWrappers.RED.wrap("ALL MEMBERS") +
                " from the " + TagWrappers.YELLOW.wrap(RegionsPlaceholders.REGION_NAME) + " region?"
        );

    public static final TextLocale UI_DIALOG_KICK_MEMBER_TITLE = LangEntry
        .builder("Regions.UI.Member.Dialog.KickMember.Title")
        .text(
            TagWrappers.YELLOW.and(TagWrappers.BOLD).wrap("KICK MEMBER") +
                TagWrappers.DARK_GRAY.wrap(" » ") + "Are you sure?"
        );

    public static final DialogElementLocale UI_DIALOG_KICK_MEMBER_BODY = LangEntry
        .builder("Regions.UI.Member.Dialog.KickMember.Body")
        .dialogElement(
            "Are you sure you want to kick " + TagWrappers.RED.wrap(CommonPlaceholders.PLAYER_NAME) +
                " from the " + TagWrappers.YELLOW.wrap(RegionsPlaceholders.REGION_NAME) + " region?"
        );

    public static final TextLocale UI_DIALOG_RANK_PROMOTION_TITLE = LangEntry
        .builder("Regions.UI.Member.Dialog.RankPromotion.Title")
        .text(
            TagWrappers.YELLOW.and(TagWrappers.BOLD).wrap("MEMBER PROMOTION") +
                TagWrappers.DARK_GRAY.wrap(" » ") + "Select new rank"
        );

    public static final DialogElementLocale UI_DIALOG_RANK_PROMOTION_BODY = LangEntry
        .builder("Regions.UI.Member.Dialog.RankPromotion.Body")
        .dialogElement(
            "Select the new rank for " + TagWrappers.WHITE.wrap(CommonPlaceholders.PLAYER_NAME) +
                " in the " + TagWrappers.YELLOW.wrap(RegionsPlaceholders.REGION_NAME) + " region."
        );

    public static final ButtonLocale UI_DIALOG_RANK_PROMOTION_RANK_BUTTON = LangEntry
        .builder("Regions.UI.Member.Dialog.RankPromotion.RankButton")
        .button(ClaimsPlaceholders.RANK_NAME,
            TagWrappers.WHITE.wrap("Click to " + TagWrappers.GREEN.wrap("promote") + " player to this rank.")
        );

    public static final TextLocale UI_DIALOG_RANK_DEMOTION_TITLE = LangEntry
        .builder("Regions.UI.Member.Dialog.RankDemotion.Title")
        .text(
            TagWrappers.YELLOW.and(TagWrappers.BOLD).wrap("MEMBER DEMOTION") +
                TagWrappers.DARK_GRAY.wrap(" » ") + "Select new rank"
        );

    public static final DialogElementLocale UI_DIALOG_RANK_DEMOTION_BODY = LangEntry
        .builder("Regions.UI.Member.Dialog.RankDemotion.Body")
        .dialogElement(
            "Select the new rank for " + TagWrappers.WHITE.wrap(CommonPlaceholders.PLAYER_NAME) +
                " in the " + TagWrappers.YELLOW.wrap(RegionsPlaceholders.REGION_NAME) + " region."
        );

    public static final ButtonLocale UI_DIALOG_RANK_DEMOTION_RANK_BUTTON = LangEntry
        .builder("Regions.UI.Member.Dialog.RankDemotion.RankButton")
        .button(ClaimsPlaceholders.RANK_NAME,
            TagWrappers.WHITE.wrap("Click to " + TagWrappers.RED.wrap("demote") + " player to this rank.")
        );

    /* public static final ButtonLocale UI_DIALOG_ADD_MEMBER_PLAYER_BUTTON_UNSELECTED = LangEntry
        .builder("Regions.UI.Member.Dialog.AddMember.PlayerButton.Unselected")
        .button(ClaimsPlaceholders.GENERIC_ICON + CommonPlaceholders.PLAYER_NAME,
            TagWrappers.WHITE.wrap("Click to " + TagWrappers.GREEN.wrap("select") + " this player.")
        );
    
    public static final ButtonLocale UI_DIALOG_ADD_MEMBER_PLAYER_BUTTON_SELECTED = LangEntry
        .builder("Regions.UI.Member.Dialog.AddMember.PlayerButton.Selected")
        .button(
            ClaimsPlaceholders.GENERIC_ICON + TagWrappers.GREEN.and(TagWrappers.UNDERLINED).wrap(
                CommonPlaceholders.PLAYER_NAME),
            TagWrappers.WHITE.wrap("Click to " + TagWrappers.RED.wrap("deselect") + " this player.")
        ); */

    private MemberLang() {
    }
}
