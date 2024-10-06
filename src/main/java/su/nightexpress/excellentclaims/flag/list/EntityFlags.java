package su.nightexpress.excellentclaims.flag.list;

import su.nightexpress.excellentclaims.api.flag.FlagCategory;
import su.nightexpress.excellentclaims.flag.impl.list.BooleanFlag;
import su.nightexpress.excellentclaims.flag.impl.list.DamageTypeListFlag;
import su.nightexpress.excellentclaims.flag.impl.list.EntityListFlag;
import su.nightexpress.excellentclaims.flag.impl.list.ListModeFlag;
import su.nightexpress.excellentclaims.flag.type.DamageTypeList;
import su.nightexpress.excellentclaims.flag.type.EntityList;
import su.nightexpress.excellentclaims.flag.type.ListMode;
import su.nightexpress.nightcore.util.ItemUtil;

import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class EntityFlags {

    public static final DamageTypeListFlag ANIMAL_DAMAGE_LIST = new DamageTypeListFlag("animal_damage_list", FlagCategory.ENTITY,
        new DamageTypeList(),
        ItemUtil.getSkinHead("cefa6eeeebd3ea4bbed20ce601678f0d8636477275b8d2d93fc33ca57d5fcaa1"),
        "List of " + LIGHT_RED.enclose("damage types") + " applicable to",
        "animals depends on the " + LIGHT_YELLOW.enclose("Animal Damage Mode") + " flag."
    );

    public static final ListModeFlag ANIMAL_DAMAGE_MODE = new ListModeFlag("animal_damage_mode", FlagCategory.ENTITY,
        ListMode.ENABLED,
        ItemUtil.getSkinHead("7c7f0ad90bf60aa36645f2a63ef5bd8a7035bce5ca46ca3b605842624449052f"),
        "Controls wheter " + LIGHT_YELLOW.enclose("animals") + " can take", "damage and which type of it."
    );

    public static final BooleanFlag ANIMAL_GRIEF = new BooleanFlag("animal_grief", FlagCategory.ENTITY,
        true,
        "84e5cdb0edb362cb454586d1fd0ebe971423f015b0b1bfc95f8d5af8afe7e810",
        "Controls whether animals can", "modify environment:",
        LIGHT_YELLOW.enclose("●") + " Sheeps eating grass",
        LIGHT_YELLOW.enclose("●") + " Rabbits eating carrots",
        LIGHT_YELLOW.enclose("●") + " Foxes picking berries"
    );

    public static final BooleanFlag ANIMAL_SPAWN = new BooleanFlag("animal_spawn", FlagCategory.ENTITY,
        true,
        "81359b8150b25cca5dc3a92ece02f2dfb6a5235d4f7bffacc3934880033a7",
        "Controls whether " + LIGHT_GREEN.enclose("animals") + " can spawn.",
        "Takes priority over " + LIGHT_YELLOW.enclose("Entity Spawn List") + " flag."
    );

    public static final BooleanFlag CREEPER_BLOCK_DAMAGE = new BooleanFlag("creeper_block_damage", FlagCategory.ENTITY,
        false,
        "1c26ec209756ff5d5b81f25ca4db2ee4dceb52874e5f35bb98ce82cace8ac506",
        "Allows creepers to " + LIGHT_RED.enclose("destroy") + " blocks."
    );

    public static final BooleanFlag END_CRYSTAL_BLOCK_DAMAGE = new BooleanFlag("end_crystal_block_damage", FlagCategory.ENTITY,
        false,
        "d65f9c5e595a159d86f8b3e5da98b2c5db7a2f7a7e3fc2303761acda409bc452",
        "Allows End Crystals to " + LIGHT_RED.enclose("destroy") + " blocks."
    );

    public static final BooleanFlag ENDER_DRAGON_GRIEF = new BooleanFlag("ender_dragon_grief", FlagCategory.ENTITY,
        false,
        "7e4d35a2f6bd739605fa4eaa6e15d038009f3f20b1a204628016058736e7b95e",
        "Allows Ender Dragons to " + LIGHT_RED.enclose("destroy") + " blocks."
    );

    public static final BooleanFlag    ENDERMAN_GRIEF    = new BooleanFlag("enderman_grief", FlagCategory.ENTITY,
        false,
        "4f24767c8138b3dfec02f77bd151994d480d4e869664ce09a26b19289212162b",
        "Allows endermans to pick up blocks."
    );

    public static final ListModeFlag ENTITY_SPAWN_MODE = new ListModeFlag("entity_spawn_mode", FlagCategory.ENTITY,
        ListMode.ENABLED,
        ItemUtil.getSkinHead("3d02e0e429a4a681a8686cae11554a73d4d0622c87f42d11cd9a87aa95611903"),
        "Controls whether entities can spawn."
    );

    public static final EntityListFlag ENTITY_SPAWN_LIST = new EntityListFlag("entity_spawn_list", FlagCategory.ENTITY,
        new EntityList(),
        ItemUtil.getSkinHead("647e2e5d55b6d04943519bed2557c6329e33b60b909dee8923cd88b115210"),
        "Controls which " + LIGHT_GREEN.enclose("entities") + " can spawn",
        "depends on the " + LIGHT_YELLOW.enclose("Entity Spawn Mode") + " flag."
    );


    public static final BooleanFlag EXPLOSION_BLOCK_DAMAGE = new BooleanFlag("explosion_block_damage", FlagCategory.ENTITY,
        false,
        "6c92e3f45b49e405670224892f93ebc84fa7f8c96c36aab24a8854f2cbf0b8",
        "Allows other explosions to " + LIGHT_RED.enclose("destroy") + " blocks."
    );

    public static final BooleanFlag FIREBALL_BLOCK_DAMAGE = new BooleanFlag("fireball_block_damage", FlagCategory.ENTITY,
        false,
        "64ab8a22e7687cc4c78f3b6ff5b1eb04917b51cd3cd7dbce36171160b3c77ced",
        "Allows Fireballs to " + LIGHT_RED.enclose("destroy") + " blocks."
    );

    public static final BooleanFlag MONSTER_DAMAGE_PLAYERS = new BooleanFlag("monster_damage_players", FlagCategory.ENTITY,
        true,
        "1c232d0683ba1e362593fa1a97d744c290b1655d72b2277988a6c85a93854255",
        "Allows monsters to damage players.",
        "Takes priority over " + LIGHT_YELLOW.enclose("Player Damage List") + " flag."
    );

    public static final BooleanFlag MONSTER_SPAWN = new BooleanFlag("monster_spawn", FlagCategory.ENTITY,
        true,
        "ca71506b17e71d7d3d16e50c391742d7993d1ad34a455860af5533076f11e2bc",
            "Controls whether " + LIGHT_RED.enclose("monsters") + " can spawn.",
            "Takes priority over " + LIGHT_YELLOW.enclose("Entity Spawn List") + " flag."
    );

    public static final BooleanFlag RAVAGER_GRIEF = new BooleanFlag("ravager_grief", FlagCategory.ENTITY,
        false,
        "cd20bf52ec390a0799299184fc678bf84cf732bb1bd78fd1c4b441858f0235a8",
        "Allows " + LIGHT_YELLOW.enclose("ravagers") + " to destroy blocks."
    );

    public static final BooleanFlag SILVERFISH_INFEST = new BooleanFlag("silverfish_infest", FlagCategory.ENTITY,
        false,
        "da91dab8391af5fda54acd2c0b18fbd819b865e1a8f1d623813fa761e924540",
        "Allows " + LIGHT_YELLOW.enclose("silverfishes") + " to infest blocks."
    );

    public static final BooleanFlag SNOWMAN_TRAIL = new BooleanFlag("snowman_trail", FlagCategory.ENTITY,
        true,
        "126ab3ed98ff470e4aa03fc69c745f61c0b614f3e1ecb42bac1c929223364789",
        "Allows " + LIGHT_YELLOW.enclose("snowmans") + " to form snow."
    );

    public static final BooleanFlag VILLAGER_FARM = new BooleanFlag("villager_farm", FlagCategory.ENTITY,
        true,
        "ef15fe4b7623c753393524f557b36ec81258a75daba159b39a6f800f7171a475",
        "Allows " + LIGHT_YELLOW.enclose("villagers") + " to farm crops."
    );

    public static final BooleanFlag WITHER_BLOCK_DAMAGE = new BooleanFlag("wither_block_damage", FlagCategory.ENTITY,
        false,
        "74f328f5044129b5d1f96affd1b8c05bcde6bd8e756aff5c5020585eef8a3daf",
        "Allows Wither to " + LIGHT_RED.enclose("destroy") + " blocks."
    );
}
