package su.nightexpress.excellentclaims.flag.list;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import su.nightexpress.excellentclaims.api.flag.FlagCategory;
import su.nightexpress.excellentclaims.flag.impl.list.*;
import su.nightexpress.excellentclaims.flag.type.DamageTypeList;
import su.nightexpress.excellentclaims.flag.type.EntityList;
import su.nightexpress.excellentclaims.flag.type.ListMode;
import su.nightexpress.excellentclaims.flag.type.MaterialList;
import su.nightexpress.nightcore.util.ItemUtil;

import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class PlayerFlags {

    public static final BooleanFlag ARMOR_STAND_USE = new BooleanFlag("armorstand_use", FlagCategory.PLAYER,
        false,
        new ItemStack(Material.ARMOR_STAND),
        "Allows " + LIGHT_RED.enclose("non-members") + " to use armor stands.",
        "Takes priority over " + LIGHT_YELLOW.enclose("Entity Interact") + " flag."
    );

    public static final BooleanFlag BLOCK_BREAK = new BooleanFlag("block_break", FlagCategory.PLAYER,
        false,
        "378d2631da7b372af294153818899ee0f43c9156c76c12c61a6e7f1ddb6db61a",
        "Allows " + LIGHT_RED.enclose("non-members") + " to break blocks."
    );

    public static final BooleanFlag BLOCK_FERTILIZE = new BooleanFlag("block_fertilize", FlagCategory.PLAYER,
        false,
        new ItemStack(Material.BONE_MEAL),
        "Allows " + LIGHT_RED.enclose("non-members") + " to fertilize crops."
    );

    public static final ListModeFlag BLOCK_INTERACT_MODE = new ListModeFlag("block_interact_mode", FlagCategory.PLAYER,
        ListMode.DISABLED,
        ItemUtil.getSkinHead("284e5de747b5adfb38743d95a3ab62c600bd6c370b99f8c2d1308d8330811518"),
        "Controls whether " + LIGHT_RED.enclose("non-members") + " can", "interact with blocks."
    );

    public static final MaterialListFlag BLOCK_INTERACT_LIST = new MaterialListFlag("block_interact_list", FlagCategory.PLAYER,
        new MaterialList(),
        Material::isBlock,
        ItemUtil.getSkinHead("ac62fd0d4123deeeb824a00a15b09b96039ed33599c077ef67821226a18f1f11"),
        "List of blocks with that " + LIGHT_RED.enclose("non-members") + " can",
        "interact depends on the " + LIGHT_YELLOW.enclose("Block Interact Mode") + " flag."
    );

    public static final BooleanFlag BLOCK_PLACE = new BooleanFlag("block_place", FlagCategory.PLAYER,
        false,
        "6c648324911d36a6767fd839dfdafa4cff6e2f640c7630d5b63aaa94d5239344",
        "Allows " + LIGHT_RED.enclose("non-members") + " to place blocks."
    );

    public static final BooleanFlag BLOCK_TRAMPLING = new BooleanFlag("block_trampling", FlagCategory.PLAYER,
        false,
        "9a656926adcd507ff079ce42f5177435c28ef369359cf7ca6f9d825f5767db",
        "Allows " + LIGHT_RED.enclose("non-members") + " to trample blocks:",
        LIGHT_YELLOW.enclose("●") + " Farmlands",
        LIGHT_YELLOW.enclose("●") + " Turtle Eggs"
    );

    public static final BooleanFlag CHEST_ACCESS = new BooleanFlag("chest_access", FlagCategory.PLAYER,
        false,
        "ef221b33f5b39e99ee6fd343abaaa9abdf66d93d4306cf01cca9f202e8773fd6",
        "Allows " + LIGHT_RED.enclose("non-members") + " to open chests."
    );

    public static final BooleanFlag CHORUS_FRUIT_USE = new BooleanFlag("chorus_fruit_use", FlagCategory.PLAYER,
        true,
        "30655d638e4b6219fe655f7bea34f9949541dd207764b36088ed1bffd1cc3e16",
        "Allows " + LIGHT_RED.enclose("non-members") + " to use chorus fruits."
    );

    public static final BooleanFlag CONTAINER_ACCESS = new BooleanFlag("container_access", FlagCategory.PLAYER,
        false,
        "54ff1f833731dfd3041f23e8fe2a90da6c112bfaed7a6c5d2ce806df641f434b",
        "Allows " + LIGHT_RED.enclose("non-members") + " to open containers:",
        LIGHT_YELLOW.enclose("●") + " Furnaces",
        LIGHT_YELLOW.enclose("●") + " Hoppers",
        LIGHT_YELLOW.enclose("●") + " Shulker Boxes",
        LIGHT_YELLOW.enclose("●") + " and other..."
    );

    public static final BooleanFlag END_PORTAL_USE = new BooleanFlag("end_portal_use", FlagCategory.PLAYER,
        false,
        "4db0c5d20168aaee45643cc4568ce2bb9a9f7618427ab5b5fc2115f5e07f828d",
        "Allows " + LIGHT_RED.enclose("non-members") + " to use end portals."
    );

    public static final BooleanFlag ENDER_PEARL_USE = new BooleanFlag("ender_pearl_use", FlagCategory.PLAYER,
        true,
        "5cb7c21cc43dc17678ee6f16591ffaab1f637c37f4f6bbd8cea497451d76db6d",
        "Allows " + LIGHT_RED.enclose("non-members") + " to use ender pearls."
    );

    public static final ListModeFlag ENTITY_INTERACT_MODE = new ListModeFlag("entity_interact_mode", FlagCategory.PLAYER,
        ListMode.DISABLED,
        ItemUtil.getSkinHead("37e838ccc26776a217c678386f6a65791fe8cdab8ce9ca4ac6b28397a4d81c22"),
        "Controls whether " + LIGHT_RED.enclose("non-members") + " can", "interact with entities."
    );

    public static final EntityListFlag ENTITY_INTERACT_LIST = new EntityListFlag("entity_interact_list", FlagCategory.PLAYER,
        new EntityList(),
        ItemUtil.getSkinHead("7de2181e5ecd9e3c0383039385e2d2b9d51de212cbe78af23e0a5ab546629f16"),
        "List of entities with that " + LIGHT_RED.enclose("non-members") + " can",
        "interact depends on the " + LIGHT_YELLOW.enclose("Entity Interact Mode") + " flag."
    );

    public static final MaterialListFlag ITEM_USE_LIST = new MaterialListFlag("item_use_list", FlagCategory.PLAYER,
        new MaterialList(),
        Material::isItem,
        ItemUtil.getSkinHead("ce67df8d715d1f0583c5955a83aa9ea0f13ed9826e1f70622e7a99cc8278e06d"),
        "List of items that " + LIGHT_RED.enclose("non-members") + " can use",
        "depends on the " + LIGHT_YELLOW.enclose("Item Use Mode") + " flag."
    );

    public static final ListModeFlag ITEM_USE_MODE = new ListModeFlag("item_use_mode", FlagCategory.PLAYER,
        ListMode.ENABLED,
        ItemUtil.getSkinHead("1aaefc0dce2e3d940b959341fa20269553111241d1de87e9f29957b7776a16ec"),
        "Controls whether " + LIGHT_RED.enclose("non-members") + " can use", "items in their hands."
    );

    public static final BooleanFlag NETHER_PORTAL_USE = new BooleanFlag("nether_portal_use", FlagCategory.PLAYER,
        true,
        "2c915db3fc40a79b63c2c453f0c490981e5227c5027501283272138533dea519",
        "Allows " + LIGHT_RED.enclose("non-members") + " to use nether portals."
    );

    public static final BooleanFlag PLAYER_DAMAGE_ANIMALS = new BooleanFlag("player_damage_animals", FlagCategory.PLAYER,
        false,
        "b667c0e107be79d7679bfe89bbc57c6bf198ecb529a3295fcfdfd2f24408dca3",
        "Allows " + LIGHT_RED.enclose("non-members") + " to damage animals.",
        "Takes priority over " + LIGHT_YELLOW.enclose("Animal Damage List") + " flag."
    );

    public static final ListModeFlag PLAYER_DAMAGE_MODE = new ListModeFlag("player_damage_mode", FlagCategory.PLAYER,
        ListMode.ENABLED,
        ItemUtil.getSkinHead("f3637961f8451a53b67d25312d350c620f32b5f608bd6ade06637be1712f364e"),
        "Controls whether " + LIGHT_GREEN.enclose("all players") + " can be", "damaged and by which damage types."
    );

    public static final DamageTypeListFlag PLAYER_DAMAGE_LIST = new DamageTypeListFlag("player_damage_list", FlagCategory.PLAYER,
        new DamageTypeList(),
        ItemUtil.getSkinHead("d492b7d89e4c1bede736233ddefa59aa86eb2916e56373ff83082ad5324c78f4"),
        "List of " + LIGHT_RED.enclose("damage types") + " applicable to",
        "players depends on the " + LIGHT_YELLOW.enclose("Player Damage Mode") + " flag."
    );

    public static final BooleanFlag PLAYER_DAMAGE_VILLAGERS = new BooleanFlag("player_damage_villagers", FlagCategory.PLAYER,
        false,
        "f5315ea877c3fe72a3d41ee167dff111ba08961b3a1aac51f47c94534b7acca4",
        "Allows " + LIGHT_RED.enclose("non-members") + " to damage villagers."
    );

    public static final BooleanFlag PLAYER_ITEM_DROP = new BooleanFlag("player_item_drop", FlagCategory.PLAYER,
        true,
        "1cb8be16d40c25ace64e09f6086d408ebc3d545cfb2990c5b6c25dabcedeacc",
        "Controls whether " + LIGHT_RED.enclose("non-members") + " can drop items."
    );

    public static final BooleanFlag PLAYER_ITEM_PICKUP = new BooleanFlag("player_item_pickup", FlagCategory.PLAYER,
        true,
        "45c588b9ec0a08a37e01a809ed0903cc34c3e3f176dc92230417da93b948f148",
        "Controls whether " + LIGHT_RED.enclose("non-members") + " can pickup items."
    );

    public static final BooleanFlag PLAYER_DAMAGE_PLAYERS = new BooleanFlag("pvp", FlagCategory.PLAYER,
        false,
        "d1d2b7dd66ffd86ad4709927b175e83f1a9e10fbc864b2390403708f39d8efd8",
        "Allows players to damage other players.",
        "Takes priority over " + LIGHT_YELLOW.enclose("Player Damage List") + " flag."
    );

    public static final BooleanFlag SPAWN_EGG_USE = new BooleanFlag("spawn_egg_use", FlagCategory.PLAYER,
        false,
        "e5d31559261b3e79024751fe07b711c8feef51d56c03635226955805bc42894e",
        "Allows " + LIGHT_RED.enclose("non-members") + " to use " + LIGHT_GREEN.enclose("spawn eggs") + ".",
        "Takes priority over " + LIGHT_YELLOW.enclose("Item Use List") + " flag."
    );

    public static final BooleanFlag USE_BUTTONS = new BooleanFlag("use_buttons", FlagCategory.PLAYER,
        false,
        new ItemStack(Material.STONE_BUTTON),
        "Allows " + LIGHT_RED.enclose("non-members") + " to use buttons and levers."
    );

    public static final BooleanFlag USE_DOORS = new BooleanFlag("use_doors", FlagCategory.PLAYER,
        false,
        new ItemStack(Material.OAK_DOOR),
        "Allows " + LIGHT_RED.enclose("non-members") + " to use doors, gates and trapdoors."
    );

    public static final BooleanFlag USE_PLATES = new BooleanFlag("use_plates", FlagCategory.PLAYER,
        false,
        new ItemStack(Material.STONE_PRESSURE_PLATE),
        "Allows " + LIGHT_RED.enclose("non-members") + " to use plates."
    );

    public static final BooleanFlag USE_TRIPWIRES = new BooleanFlag("use_tripwires", FlagCategory.PLAYER,
        false,
        new ItemStack(Material.TRIPWIRE_HOOK),
        "Allows " + LIGHT_RED.enclose("non-members") + " to trigger tripwires."
    );

    public static final BooleanFlag VEHICLE_USE = new BooleanFlag("vehicle_use", FlagCategory.PLAYER,
        false,
        new ItemStack(Material.MINECART),
        "Allows " + LIGHT_RED.enclose("non-members") + " to ride vehicles.",
        "Takes priority over " + LIGHT_YELLOW.enclose("Entity Interact List") + " flag."
    );

    public static final BooleanFlag VILLAGER_INTERACT = new BooleanFlag("villager_interact", FlagCategory.PLAYER,
        false,
        "35e799dbfaf98287dfbafce970612c8f075168977aacc30989d34a4a5fcdf429",
        "Allows " + LIGHT_RED.enclose("non-members") + " to interact with villagers."
    );
}
