package su.nightexpress.excellentclaims.config;

import org.bukkit.Sound;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.command.impl.LandCommands;
import su.nightexpress.excellentclaims.command.impl.RegionCommands;
import su.nightexpress.excellentclaims.flag.type.ListMode;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.language.entry.LangEnum;
import su.nightexpress.nightcore.language.entry.LangString;
import su.nightexpress.nightcore.language.entry.LangText;
import su.nightexpress.nightcore.language.message.OutputType;

import static su.nightexpress.excellentclaims.Placeholders.*;
import static su.nightexpress.nightcore.language.tag.MessageTags.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class Lang extends CoreLang {

    public static final LangEnum<ClaimPermission> CLAIM_PERMISSION = LangEnum.of("Other.ClaimPermission", ClaimPermission.class);
    public static final LangEnum<ListMode>        LIST_MODE        = LangEnum.of("Other.ListMode", ListMode.class);

    public static final LangString OTHER_LIST_ENTRY_GOOD = LangString.of("Other.ListEntry.Good",
        LIGHT_GREEN.enclose("✔") + " " + LIGHT_GRAY.enclose(GENERIC_VALUE)
    );

    public static final LangString OTHER_LIST_ENTRY_BAD = LangString.of("Other.ListEntry.Bad",
        LIGHT_RED.enclose("✘") + " " + LIGHT_GRAY.enclose(GENERIC_VALUE)
    );

    public static final LangString OTHER_EMPTY_LIST = LangString.of("Other.EmptyList",
        LIGHT_GRAY.enclose("< No Entries >")
    );

    public static final LangString OTHER_NO_DESCRIPTION = LangString.of("Other.Claim.NoDescription",
        LIGHT_GRAY.enclose("<No Description>")
    );

    public static final LangString OTHER_UNSET = LangString.of("Other.Unset", "< Unset >");

    public static final LangString COMMAND_ARGUMENT_NAME_REGION = LangString.of("Command.Argument.Name.Region", "region");
    public static final LangString COMMAND_ARGUMENT_NAME_TEXT   = LangString.of("Command.Argument.Name.Text", "text");

    public static final LangString COMMAND_ADMIN_MODE_DESC = LangString.of("Command.AdminMode.Desc", "Toggle Admin Mode.");

    public static final LangString COMMAND_LAND_DESC             = LangString.of("Command.Land.Desc", "Land commands.");
    public static final LangString COMMAND_LAND_CLAIM_DESC       = LangString.of("Command.Land.Claim.Desc", "Claim chunk.");
    public static final LangString COMMAND_LAND_UNCLAIM_DESC     = LangString.of("Command.Land.Unclaim.Desc", "Unclaim chunk.");
    public static final LangString COMMAND_LAND_FLAGS_DESC       = LangString.of("Command.Land.Flags.Desc", "Manage claim flags.");
    public static final LangString COMMAND_LAND_MEMBERS_DESC     = LangString.of("Command.Land.Members.Desc", "Manage claim members.");
    public static final LangString COMMAND_LAND_SETTINGS_DESC    = LangString.of("Command.Land.Settings.Desc", "Manage claim settings.");
    public static final LangString COMMAND_LAND_LIST_DESC        = LangString.of("Command.Land.List.Desc", "View your claims.");
    public static final LangString COMMAND_LAND_LIST_ALL_DESC    = LangString.of("Command.Land.ListAll.Desc", "View all claims.");
    public static final LangString COMMAND_LAND_MERGE_DESC       = LangString.of("Command.Land.Merge.Desc", "Merge chnuk to a claim.");
    public static final LangString COMMAND_LAND_SEPARATE_DESC    = LangString.of("Command.Land.Separate.Desc", "Separate claim chunk.");
    public static final LangString COMMAND_LAND_SET_SPAWN_DESC   = LangString.of("Command.Land.SetSpawn.Desc", "Set claim spawn.");
    public static final LangString COMMAND_LAND_BOUNDS_DESC      = LangString.of("Command.Land.Bounds.Desc", "Toggle claim bounds.");
    public static final LangString COMMAND_LAND_RENAME_DESC      = LangString.of("Command.Land.Rename.Desc", "Rename claim.");
    public static final LangString COMMAND_LAND_DESCRIPTION_DESC = LangString.of("Command.Land.Description.Desc", "Set claim description.");
    public static final LangString COMMAND_LAND_TRANSFER_DESC    = LangString.of("Command.Land.Transfer.Desc", "Transfer claim's ownership.");

    public static final LangString COMMAND_REGION_DESC             = LangString.of("Command.Region.Desc", "Region commands.");
    public static final LangString COMMAND_REGION_WAND_DESC        = LangString.of("Command.Region.Wand.Desc", "Get region wand tool.");
    public static final LangString COMMAND_REGION_CLAIM_DESC       = LangString.of("Command.Region.Claim.Desc", "Claim region.");
    public static final LangString COMMAND_REGION_REMOVE_DESC      = LangString.of("Command.Region.Remove.Desc", "Remove region.");
    public static final LangString COMMAND_REGION_FLAGS_DESC       = LangString.of("Command.Region.Flags.Desc", "Manage region flags.");
    public static final LangString COMMAND_REGION_MEMBERS_DESC     = LangString.of("Command.Region.Members.Desc", "Manage region members.");
    public static final LangString COMMAND_REGION_SETTINGS_DESC    = LangString.of("Command.Region.Settings.Desc", "Manage region settings.");
    public static final LangString COMMAND_REGION_LIST_DESC        = LangString.of("Command.Region.List.Desc", "View your regions.");
    public static final LangString COMMAND_REGION_LIST_ALL_DESC    = LangString.of("Command.Region.ListAll.Desc", "View all regions.");
    public static final LangString COMMAND_REGION_SET_SPAWN_DESC   = LangString.of("Command.Region.SetSpawn.Desc", "Set region spawn.");
    public static final LangString COMMAND_REGION_BOUNDS_DESC      = LangString.of("Command.Region.Bounds.Desc", "Toggle region bounds.");
    public static final LangString COMMAND_REGION_RENAME_DESC      = LangString.of("Command.Region.Rename.Desc", "Rename region.");
    public static final LangString COMMAND_REGION_DESCRIPTION_DESC = LangString.of("Command.Region.Description.Desc", "Set region description.");
    public static final LangString COMMAND_REGION_TRANSFER_DESC    = LangString.of("Command.Region.Transfer.Desc", "Transfer region's ownership.");

    public static final LangString COMMAND_WILDERNESS_DESC             = LangString.of("Command.Wilderness.Desc", "Wilderness commands.");
    public static final LangString COMMAND_WILDERNESS_FLAGS_DESC       = LangString.of("Command.Wilderness.Flags.Desc", "Manage global world flags.");
    public static final LangString COMMAND_WILDERNESS_RENAME_DESC      = LangString.of("Command.Wilderness.Rename.Desc", "Rename wilderness.");
    public static final LangString COMMAND_WILDERNESS_DESCRIPTION_DESC = LangString.of("Command.Wilderness.Description.Desc", "Set wilderness description.");


    public static final LangText ADMIN_MODE_TOGGLE = LangText.of("AdminMode.Toggle",
        LIGHT_GRAY.enclose("Admin Mode: " + LIGHT_YELLOW.enclose(GENERIC_VALUE))
    );

    public static final LangString WILDERNESS_DISPLAY_NAME = LangString.of("Wilderness.DisplayName", "Wilderness");

    public static final LangString WILDERNESS_OWNER_NAME = LangString.of("Wilderness.OwnerName", "Server");

    public static final LangText REGION_SELECTION_INFO = LangText.of("Region.Selection.Info",
        LIGHT_GRAY.enclose("Set " + LIGHT_YELLOW.enclose("#" + GENERIC_VALUE) + " region position.")
    );

    public static final LangText LAND_BOUNDS_TOGGLE = LangText.of("Land.Bounds.Toggle",
        LIGHT_GRAY.enclose("Chunk claim bounds: " + LIGHT_YELLOW.enclose(GENERIC_VALUE))
    );

    public static final LangText LAND_CLAIM_SUCCESS = LangText.of("Land.Claim.Success",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_PLAYER_LEVELUP),
        LIGHT_GREEN.enclose(BOLD.enclose("Chunk Claimed!")),
        LIGHT_GRAY.enclose("Use " + LIGHT_GREEN.enclose("/" + Config.getLandAlias() + " " + LandCommands.DEF_SETTINGS) + " to manage your claim.")
    );

    public static final LangText LAND_CLAIM_ERROR_INVALID_WORLD = LangText.of("Land.Claim.Error.InvalidWorld",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Invalid Location!")),
        LIGHT_GRAY.enclose("Could not claim land at your location.")
    );

    public static final LangText LAND_CLAIM_ERROR_BAD_WORLD = LangText.of("Land.Claim.Error.DisabledWorld",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Forbidden World!")),
        LIGHT_GRAY.enclose("You can't claim chunks in this world.")
    );

    public static final LangText LAND_CLAIM_ERROR_BAD_NAME = LangText.of("Land.Claim.Error.BadName",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Invalid Name!")),
        LIGHT_GRAY.enclose("Use only latin letters and numbers.")
    );

    public static final LangText LAND_CLAIM_ERROR_OCCUPIED = LangText.of("Land.Claim.Error.Occupied",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Occupied Chunk!")),
        LIGHT_GRAY.enclose("The chunk is already claimed by someone.")
    );

    public static final LangText LAND_CLAIM_ERROR_ALREADY_CLAIMED = LangText.of("Land.Claim.Error.AlreadyClaimed",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_AMBIENT),
        LIGHT_RED.enclose(BOLD.enclose("Already Claimed!")),
        LIGHT_GRAY.enclose("You already claimed this chunk.")
    );

    public static final LangText LAND_CLAIM_ERROR_MAX_AMOUNT = LangText.of("Land.Claim.Error.MaxAmount",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Too Many Claims!")),
        LIGHT_GRAY.enclose("You already claimed maximum of " + LIGHT_RED.enclose(GENERIC_AMOUNT) + " chunks.")
    );

    public static final LangText LAND_CLAIM_ERROR_OVERLAP_DISABLED = LangText.of("Land.Claim.Error.OverlapDisabled",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Region Overlap!")),
        LIGHT_GRAY.enclose("The chunk overlaps with claimed regions.")
    );

    public static final LangText LAND_CLAIM_ERROR_OVERLAP_FOREIGN = LangText.of("Land.Claim.Error.OverlapForeign",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Region Overlap!")),
        LIGHT_GRAY.enclose("The chunk overlaps with foreign regions.")
    );

    public static final LangText LAND_CLAIM_ERROR_INSUFFICIENT_FUNDS = LangText.of("Land.Claim.Error.InsufficientFunds",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Insufficient Funds!")),
        LIGHT_GRAY.enclose("You need " + LIGHT_RED.enclose(GENERIC_AMOUNT) + " to claim a chunk.")
    );

    public static final LangText LAND_UNCLAIM_SUCCESS = LangText.of("Land.Unlaim.Success",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.BLOCK_ANVIL_BREAK),
        LIGHT_GREEN.enclose(BOLD.enclose("Chunk Unclaimed!")),
        LIGHT_GRAY.enclose("The chunk is no longer protected.")
    );

    public static final LangText LAND_UNCLAIM_ERROR_NOTHING = LangText.of("Land.Unclaim.Error.Nothing",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("No Claim!")),
        LIGHT_GRAY.enclose("The chunk is not claimed.")
    );

//    public static final LangText LAND_ERROR_NOT_OWNER = LangText.of("Land.Error.NotOwner",
//        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
//        LIGHT_RED.enclose(BOLD.enclose("Not Your Claim!")),
//        LIGHT_GRAY.enclose("You do not own this chunk.")
//    );

    public static final LangText LAND_MERGE_ERROR_FOREIGN = LangText.of("Land.Merge.Error.Foreign",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Foreign Claim!")),
        LIGHT_GRAY.enclose("You can't merge chunks with different owners.")
    );

    public static final LangText LAND_MERGE_ERROR_SAME = LangText.of("Land.Merge.Error.Same",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Same Claim!")),
        LIGHT_GRAY.enclose("You can't merge chunks of the same claim.")
    );

    public static final LangText LAND_MERGE_ERROR_DIFFERENT_WORLD = LangText.of("Land.Merge.Error.DifferentWorld",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Different Worlds!")),
        LIGHT_GRAY.enclose("You can't merge claims of different worlds.")
    );

    public static final LangText LAND_MERGE_ERROR_INACTIVE = LangText.of("Land.Merge.Error.Inactive",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Invalid Claim!")),
        LIGHT_GRAY.enclose("Claim selected for merge does not exist anymore.")
    );

    public static final LangText LAND_MERGE_ERROR_NOTHING = LangText.of("Land.Merge.Error.Nothing",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("No Claim!")),
        LIGHT_GRAY.enclose("There is no claimed chunk to merge into.")
    );

    public static final LangText LAND_MERGE_ERROR_MAX_CHUNKS = LangText.of("Land.Merge.Error.MaxChunks",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Too Many Chunks!")),
        LIGHT_GRAY.enclose("This claim has maximum of " + LIGHT_RED.enclose(GENERIC_AMOUNT) + " merged chunks.")
    );

    public static final LangText LAND_MERGE_SUCCESS = LangText.of("Land.Merge.Success",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_ENDERMAN_TELEPORT),
        LIGHT_GREEN.enclose(BOLD.enclose("Claims Merged!")),
        LIGHT_GRAY.enclose("You successfully merged the claimed chunks.")
    );

    public static final LangText LAND_MERGE_INFO = LangText.of("Land.Merge.Info",
        LIGHT_GRAY.enclose("Select a claim where you want to merge this claim chunks by using the " + LIGHT_YELLOW.enclose("Merge Tool") + ".")
    );

    public static final LangText LAND_SEPARATE_INFO = LangText.of("Land.Separate.Info",
        LIGHT_GRAY.enclose("Select a chunk you want to separate from this claim by using the " + LIGHT_YELLOW.enclose("Separate Tool") + ".")
    );

    public static final LangText LAND_SEPARATE_ERROR_NOTHING = LangText.of("Land.Separate.Error.Nothing",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("No Claim!")),
        LIGHT_GRAY.enclose("There is no claimed chunk to separate.")
    );

    public static final LangText LAND_SEPARATE_ERROR_DIFFERENT = LangText.of("Land.Separate.Error.Different",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Different Claim!")),
        LIGHT_GRAY.enclose("This chunk is not from current claim.")
    );

    public static final LangText LAND_SEPARATE_ERROR_NOT_MERGED = LangText.of("Land.Separate.Error.NotMerged",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Not Merged!")),
        LIGHT_GRAY.enclose("This claim has single chunk only.")
    );

    public static final LangText LAND_SEPARATE_SUCCESS = LangText.of("Land.Separate.Success",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_ENDERMAN_TELEPORT),
        LIGHT_GREEN.enclose(BOLD.enclose("Chunk Separated!")),
        LIGHT_GRAY.enclose("You successfully separated chunk into the new claim.")
    );


    public static final LangText REGION_CREATE_SUCCESS = LangText.of("Region.Create.Success",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.BLOCK_WOOD_PLACE),
        LIGHT_GREEN.enclose(BOLD.enclose("Region Created!")),
        LIGHT_GRAY.enclose("Manage region using " + LIGHT_GREEN.enclose("/" + Config.getRegionAlias() + " " + RegionCommands.DEF_SETTINGS))
    );

    public static final LangText REGION_CREATE_ERROR_BAD_WORLD = LangText.of("Region.Create.Error.DisabledWorld",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Forbidden World!")),
        LIGHT_GRAY.enclose("You can't create regions in this world.")
    );

    public static final LangText REGION_CREATE_ERROR_NO_SELECTION = LangText.of("Region.Create.Error.NoSelection",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("No Selection!")),
        LIGHT_GRAY.enclose("You must select a cuboid to define a region.")
    );

    public static final LangText REGION_CREATE_ERROR_INCOMPLETE_SELECTION = LangText.of("Region.Create.Error.IncompleteSelection",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Incomplete Selection!")),
        LIGHT_GRAY.enclose("You must select two cuboid corners.")
    );

    public static final LangText REGION_CREATE_ERROR_BAD_NAME = LangText.of("Region.Create.Error.BadName",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Invalid Name!")),
        LIGHT_GRAY.enclose("Use only latin letters and numbers.")
    );

    public static final LangText REGION_CREATE_ERROR_MAX_AMOUNT = LangText.of("Region.Create.Error.MaxAmount",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Too Many Regions!")),
        LIGHT_GRAY.enclose("You already claimed maximum of " + LIGHT_RED.enclose(GENERIC_AMOUNT) + " regions.")
    );

    public static final LangText REGION_CREATE_ERROR_MAX_BLOCKS = LangText.of("Region.Create.Error.MaxBlocks",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Too Big Region!")),
        LIGHT_GRAY.enclose("You can claim regions with max. of " + LIGHT_RED.enclose(GENERIC_AMOUNT) + " blocks.")
    );

    public static final LangText REGION_CREATE_ERROR_ALREADY_EXISTS = LangText.of("Region.Create.Error.AlreadyExists",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Already Exists!")),
        LIGHT_GRAY.enclose("Region with such name already exists.")
    );

    public static final LangText REGION_CREATE_ERROR_OVERLAP_CHUNK = LangText.of("Region.Create.Error.ChunkOverlap",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Chunk Overlap!")),
        LIGHT_GRAY.enclose("Region cuboid overlaps with claimed chunks.")
    );

    public static final LangText REGION_CREATE_ERROR_OVERLAP_FOREIGN_CHUNK = LangText.of("Region.Create.Error.ForeignChunkOverlap",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Chunk Overlap!")),
        LIGHT_GRAY.enclose("Region cuboid overlaps with foreign chunks.")
    );

    public static final LangText REGION_CREATE_ERROR_OVERLAP_FOREIGN_REGION = LangText.of("Region.Create.Error.ForeignRegionOverlap",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Region Overlap!")),
        LIGHT_GRAY.enclose("Region cuboid overlaps with foreign regions.")
    );

    public static final LangText REGION_CREATE_ERROR_INSUFFICIENT_FUNDS = LangText.of("Region.Create.Error.InsufficientFunds",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Insufficient Funds!")),
        LIGHT_GRAY.enclose("You need " + LIGHT_RED.enclose(GENERIC_AMOUNT) + " to claim a region.")
    );

    public static final LangText REGION_REMOVE_SUCCESS = LangText.of("Region.Remove.Success",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.BLOCK_ANVIL_BREAK),
        LIGHT_GREEN.enclose(BOLD.enclose("Region Removed!")),
        LIGHT_GRAY.enclose("You removed region " + LIGHT_GREEN.enclose(CLAIM_ID) + ".")
    );

    public static final LangText REGION_REMOVE_ERROR_NO_REGION = LangText.of("Region.Remove.Error.NoRegion",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Invalid Region!")),
        LIGHT_GRAY.enclose("Region with such name does not exists.")
    );

//    public static final LangText REGION_ERROR_NOT_OWNER = LangText.of("Region.Error.NotOwner",
//        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
//        LIGHT_RED.enclose(BOLD.enclose("Not Your Region!")),
//        LIGHT_GRAY.enclose("You're not the region owner.")
//    );


    public static final LangText MEMBER_ADD_ERROR_OWNER = LangText.of("Member.Add.Error.Owner",
        LIGHT_GRAY.enclose(LIGHT_RED.enclose(PLAYER_NAME) + " is the claim owner. You can't add it as a member.")
    );

//    public static final LangText MEMBER_ADD_ERROR_SELF = LangText.of("Member.Add.Error.Self",
//        LIGHT_GRAY.enclose("You can't add yourself to members.")
//    );

    public static final LangText MEMBER_ADD_ERROR_ALREADY = LangText.of("Member.Add.Error.AlreadyAdded",
        LIGHT_GRAY.enclose(LIGHT_RED.enclose(PLAYER_NAME) + " is already a member of the " + LIGHT_YELLOW.enclose(CLAIM_NAME) + " claim.")
    );

    public static final LangText MEMBER_ADD_SUCCESS = LangText.of("Member.Add.Success",
        LIGHT_GRAY.enclose("You added " + LIGHT_YELLOW.enclose(PLAYER_NAME) + " as " + LIGHT_YELLOW.enclose(RANK_NAME) + " to the " + LIGHT_YELLOW.enclose(CLAIM_NAME) + " claim.")
    );

//    public static final LangText MEMBER_ADD_PROMPT = LangText.of("Member.Add.Prompt",
//        OUTPUT.enclose(10, -1) + SOUND.enclose(Sound.BLOCK_LAVA_POP),
//        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Player Name]")),
//        LIGHT_GRAY.enclose("To add it as a claim member.")
//    );

    public static final LangString MEMBER_ADD_PROMPT = LangString.of("Member.Add.Dialog",
        //OUTPUT.enclose(10, -1) + SOUND.enclose(Sound.BLOCK_LAVA_POP),
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Player Name]"))
        //LIGHT_GRAY.enclose("To add it as a claim member.")
    );


    public static final LangText GREETING_CLAIM = LangText.of("Greeting.Claim",
        OUTPUT.enclose(10, 40),
        LIGHT_YELLOW.enclose(CLAIM_NAME),
        LIGHT_GRAY.enclose(CLAIM_DESCRIPTION)
    );

    public static final LangText GREETING_WILDERNESS = LangText.of("Greeting.Wilderness",
        OUTPUT.enclose(10, 40),
        LIGHT_GREEN.enclose(BOLD.enclose("Wilderness")),
        LIGHT_GRAY.enclose("")
    );


//    public static final LangText CLAIM_RENAME_PROMPT = LangText.of("Claim.Rename.Prompt",
//        OUTPUT.enclose(10, -1) + SOUND.enclose(Sound.BLOCK_LAVA_POP),
//        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Name]")),
//        LIGHT_GRAY.enclose("To rename your claim.")
//    );

    public static final LangString CLAIM_RENAME_PROMPT = LangString.of("Claim.Rename.Dialog",
        //OUTPUT.enclose(10, -1) + SOUND.enclose(Sound.BLOCK_LAVA_POP),
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Claim Name]"))
        //LIGHT_GRAY.enclose("To rename your claim.")
    );

    public static final LangText CLAIM_RENAME_SUCCESS = LangText.of("Claim.Rename.Success",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_CELEBRATE),
        LIGHT_GREEN.enclose(BOLD.enclose("Claim Renamed!")),
        LIGHT_GRAY.enclose("New name: " + LIGHT_GREEN.enclose(CLAIM_NAME))
    );

    public static final LangText CLAIM_RENAME_ERROR_TOO_LONG = LangText.of("Claim.Rename.Error.TooLong",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Long Name!")),
        LIGHT_GRAY.enclose("Name must be up to " + LIGHT_RED.enclose(GENERIC_AMOUNT) + " characters.")
    );

//    public static final LangText CLAIM_DESCRIPTION_PROMPT = LangText.of("Claim.Description.Prompt",
//        OUTPUT.enclose(10, -1) + SOUND.enclose(Sound.BLOCK_LAVA_POP),
//        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Description Text]")),
//        LIGHT_GRAY.enclose("To add claim description.")
//    );

    public static final LangString CLAIM_DESCRIPTION_PROMPT = LangString.of("Claim.Description.Dialog",
        //.enclose(10, -1) + SOUND.enclose(Sound.BLOCK_LAVA_POP),
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Description Text]"))
        //LIGHT_GRAY.enclose("To add claim description.")
    );

    public static final LangText CLAIM_DESCRIPTION_SUCCESS = LangText.of("Claim.Description.Success",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_CELEBRATE),
        LIGHT_GREEN.enclose(BOLD.enclose("Description Changed!")),
        LIGHT_GRAY.enclose("New description: " + LIGHT_GREEN.enclose(CLAIM_DESCRIPTION))
    );

    public static final LangText CLAIM_DESCRIPTION_ERROR_TOO_LONG = LangText.of("Claim.Description.Error.TooLong",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Long Description!")),
        LIGHT_GRAY.enclose("Description must be up to " + LIGHT_RED.enclose(GENERIC_AMOUNT) + " characters.")
    );

//    public static final LangText CLAIM_PRIORITY_PROMPT = LangText.of("Claim.Priority.Prompt",
//        OUTPUT.enclose(10, -1) + SOUND.enclose(Sound.BLOCK_LAVA_POP),
//        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Priority Value]")),
//        LIGHT_GRAY.enclose("To change claim priority.")
//    );

    public static final LangString CLAIM_PRIORITY_PROMPT = LangString.of("Claim.Priority.Dialog",
        //OUTPUT.enclose(10, -1) + SOUND.enclose(Sound.BLOCK_LAVA_POP),
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Priority Value]"))
        //LIGHT_GRAY.enclose("To change claim priority.")
    );


    public static final LangText CLAIM_SET_SPAWN_ERROR_OUTSIDE = LangText.of("Claim.SetSpawn.Error.OutSide",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Not in Claim!")),
        LIGHT_GRAY.enclose("Spawn location must be inside the claim.")
    );

    public static final LangText CLAIM_SET_SPAWN_ERROR_UNSAFE = LangText.of("Claim.SetSpawn.Error.Unsafe",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Unsafe Location!")),
        LIGHT_GRAY.enclose("Spawn location must be on solid, harmless block.")
    );

    public static final LangText CLAIM_SET_SPAWN_SUCCESS = LangText.of("Claim.SetSpawn.Success",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.BLOCK_WOODEN_DOOR_OPEN),
        LIGHT_GREEN.enclose(BOLD.enclose("Spawn Set!")),
        LIGHT_GRAY.enclose("Successfully set spawn for " + LIGHT_GREEN.enclose(CLAIM_NAME) + ".")
    );


    public static final LangText CLAIM_TELEPORT_ERROR_UNSAFE = LangText.of("Claim.Teleport.Error.Unsafe",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Unsafe Location!")),
        LIGHT_GRAY.enclose("There is a hole or lava at claim spawn point.")
    );

    public static final LangText CLAIM_TELEPORT_SUCCESS = LangText.of("Claim.Teleport.Success",
        LIGHT_GRAY.enclose("You teleported to the " + LIGHT_GREEN.enclose(CLAIM_NAME) + " spawn point.")
    );


    public static final LangText CLAIM_TRANSFER_ERROR_YOURSELF = LangText.of("Claim.TransferOwnership.Error.Yourself",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Already Yours!")),
        LIGHT_GRAY.enclose("You're already claim owner.")
    );

    public static final LangText CLAIM_TRANSFER_ERROR_TOO_MANY = LangText.of("Claim.TransferOwnership.Error.TooMany",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Too Many Claims!")),
        LIGHT_GRAY.enclose(LIGHT_RED.enclose(PLAYER_NAME) + " already owns too many claims.")
    );

    public static final LangText CLAIM_TRANSFER_SUCCESS = LangText.of("Claim.TransferOwnership.Success",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_CELEBRATE),
        LIGHT_GREEN.enclose(BOLD.enclose("Ownership Transferred!")),
        LIGHT_GRAY.enclose(LIGHT_GREEN.enclose(PLAYER_NAME) + " now owns " + LIGHT_GREEN.enclose(CLAIM_NAME) + ".")
    );

    public static final LangText CLAIM_TRANSFER_NOTIFY = LangText.of("Claim.TransferOwnership.Notify",
        LIGHT_GRAY.enclose(LIGHT_GREEN.enclose(PLAYER_NAME) + " transferred you ownership of the " + LIGHT_GREEN.enclose(CLAIM_NAME) + ".")
    );



    public static final LangText SELECTION_REGION_INFO = LangText.of("Selection.Region.Info",
        OUTPUT.enclose(OutputType.ACTION_BAR),
        LIGHT_CYAN.enclose("Selected " + WHITE.enclose(GENERIC_VOLUME + "/" + GENERIC_MAX) + " blocks.")
    );



    public static final LangText PROTECTION_BLOCK_BREAK = LangText.of("Protection.Info.BlockBreak",
        OUTPUT.enclose(OutputType.ACTION_BAR),
        LIGHT_RED.enclose("You can't break " + LIGHT_YELLOW.enclose(GENERIC_VALUE) + " here!")
    );

    public static final LangText PROTECTION_BLOCK_PLACE = LangText.of("Protection.Info.BlockPlace",
        OUTPUT.enclose(OutputType.ACTION_BAR),
        LIGHT_RED.enclose("You can't place " + LIGHT_YELLOW.enclose(GENERIC_VALUE) + " here!")
    );

    public static final LangText PROTECTION_BLOCK_FERTILIZE = LangText.of("Protection.Info.BlockFertilize",
        OUTPUT.enclose(OutputType.ACTION_BAR),
        LIGHT_RED.enclose("You can't fertilize " + LIGHT_YELLOW.enclose(GENERIC_VALUE) + " here!")
    );

    public static final LangText PROTECTION_BLOCK_INTERACT = LangText.of("Protection.Info.BlockInteract",
        OUTPUT.enclose(OutputType.ACTION_BAR),
        LIGHT_RED.enclose("You can't use " + LIGHT_YELLOW.enclose(GENERIC_VALUE) + " here!")
    );

    public static final LangText PROTECTION_ITEM_USE = LangText.of("Protection.Info.ItemUse",
        OUTPUT.enclose(OutputType.ACTION_BAR),
        LIGHT_RED.enclose("You can't use " + LIGHT_YELLOW.enclose(GENERIC_VALUE) + " here!")
    );

    public static final LangText PROTECTION_ENTITY_INTERACT = LangText.of("Protection.Info.EntityInteract",
        OUTPUT.enclose(OutputType.ACTION_BAR),
        LIGHT_RED.enclose("You can't use " + LIGHT_YELLOW.enclose(GENERIC_VALUE) + " here!")
    );

    public static final LangText PROTECTION_PORTAL_USE = LangText.of("Protection.Info.Portal",
        OUTPUT.enclose(OutputType.ACTION_BAR),
        LIGHT_RED.enclose("You can't use portals here!")
    );

    public static final LangText PROTECTION_DAMAGE_ENTITY = LangText.of("Protection.Info.DamageEntity",
        OUTPUT.enclose(OutputType.ACTION_BAR),
        LIGHT_RED.enclose("You can't harm " + LIGHT_YELLOW.enclose(GENERIC_VALUE) + " here!")
    );

    public static final LangText PROTECTION_ITEM_DROP = LangText.of("Protection.Info.ItemDrop",
        OUTPUT.enclose(OutputType.ACTION_BAR),
        LIGHT_RED.enclose("You can't drop items here!")
    );

    public static final LangText PROTECTION_ITEM_PICKUP = LangText.of("Protection.Info.ItemPickup",
        OUTPUT.enclose(OutputType.ACTION_BAR),
        LIGHT_RED.enclose("You can't pickup items here!")
    );


//    public static final LangText FLAG_PROMPT_ENTITY_TYPE = LangText.of("Flag.Prompt.EntityType",
//        OUTPUT.enclose(10, -1) + SOUND.enclose(Sound.BLOCK_LAVA_POP),
//        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Entity Type]")),
//        LIGHT_GRAY.enclose("Look in chat for a list of available values.")
//    );
//
//    public static final LangText FLAG_PROMPT_DAMAGE_TYPE = LangText.of("Flag.Prompt.DamageType",
//        OUTPUT.enclose(10, -1) + SOUND.enclose(Sound.BLOCK_LAVA_POP),
//        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Damage Type]")),
//        LIGHT_GRAY.enclose("Look in chat for a list of available values.")
//    );
//
//    public static final LangText FLAG_PROMPT_ITEM_TYPE = LangText.of("Flag.Prompt.ItemType",
//        OUTPUT.enclose(10, -1) + SOUND.enclose(Sound.BLOCK_LAVA_POP),
//        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Item Name]")),
//        LIGHT_GRAY.enclose(" ")
//    );
//
//    public static final LangText FLAG_PROMPT_BLOCK_TYPE = LangText.of("Flag.Prompt.BlockType",
//        OUTPUT.enclose(10, -1) + SOUND.enclose(Sound.BLOCK_LAVA_POP),
//        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Block Name]")),
//        LIGHT_GRAY.enclose(" ")
//    );

    public static final LangString FLAG_PROMPT_ENTITY_TYPE = LangString.of("Flag.Dialog.EntityType",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Entity Type]"))
    );

    public static final LangString FLAG_PROMPT_DAMAGE_TYPE = LangString.of("Flag.Dialog.DamageType",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Damage Type]"))
    );

    public static final LangString FLAG_PROMPT_ITEM_TYPE = LangString.of("Flag.Dialog.ItemType",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Item Name]"))
    );

    public static final LangString FLAG_PROMPT_BLOCK_TYPE = LangString.of("Flag.Dialog.BlockType",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Block Name]"))
    );


    public static final LangText ERROR_NOT_OWNER = LangText.of("Error.NotOwner",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Not Your Claim!")),
        LIGHT_GRAY.enclose("Only claim owner can do that.")
    );

    public static final LangText ERROR_NO_CLAIM_PERMISSION = LangText.of("Error.NoClaimPermission",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("No Permission!")),
        LIGHT_GRAY.enclose("You can't do that in this claim.")
    );

    public static final LangText ERROR_NO_CLAIM = LangText.of("Error.NoClaim",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("No Claims!")),
        LIGHT_GRAY.enclose("There are no claimed chunks/regions at your location.")
    );

    public static final LangText ERROR_NO_CHUNK = LangText.of("Error.NoChunk",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("No Claim!")),
        LIGHT_GRAY.enclose("There are no claimed chunks at your location.")
    );

    public static final LangText ERROR_NO_REGION = LangText.of("Error.NoRegion",
        OUTPUT.enclose(20, 60) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("No Region!")),
        LIGHT_GRAY.enclose("There are no regions at your location.")
    );

    public static final LangText ERROR_COMMAND_INVALID_REGION_ARGUMENT = LangText.of("Error.Command.Argument.InvalidRegion",
        LIGHT_GRAY.enclose(LIGHT_RED.enclose(GENERIC_VALUE) + " is not a valid region!")
    );
}
