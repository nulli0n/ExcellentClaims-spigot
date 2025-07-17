package su.nightexpress.excellentclaims.flag.registry;

import su.nightexpress.excellentclaims.api.flag.FlagCategory;
import su.nightexpress.excellentclaims.flag.FlagTypes;
import su.nightexpress.excellentclaims.flag.impl.ClaimFlag;

import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class EntityFlags {

    public static final ClaimFlag<Boolean> ANIMAL_DAMAGE = ClaimFlag.builder("animal_damage", FlagTypes.BOOLEAN)
        .category(FlagCategory.ENTITY)
        .defaultValue(true)
        .icon("cefa6eeeebd3ea4bbed20ce601678f0d8636477275b8d2d93fc33ca57d5fcaa1")
        .description("Wheter " + LIGHT_YELLOW.wrap("animals") + " can get hurt.")
        .build();

    public static final ClaimFlag<Boolean> ANIMAL_GRIEF = ClaimFlag.builder("animal_grief", FlagTypes.BOOLEAN)
        .category(FlagCategory.ENTITY)
        .defaultValue(true)
        .icon("84e5cdb0edb362cb454586d1fd0ebe971423f015b0b1bfc95f8d5af8afe7e810")
        .description("Whether " + LIGHT_YELLOW.wrap("animals") + " can change blocks.")
        .build();

    public static final ClaimFlag<Boolean> ANIMAL_SPAWN = ClaimFlag.builder("animal_spawn", FlagTypes.BOOLEAN)
        .category(FlagCategory.ENTITY)
        .defaultValue(true)
        .icon("81359b8150b25cca5dc3a92ece02f2dfb6a5235d4f7bffacc3934880033a7")
        .description("Whether " + LIGHT_YELLOW.wrap("animals") + " can spawn.")
        .build();

    public static final ClaimFlag<Boolean> CREEPER_BLOCK_DAMAGE = ClaimFlag.builder("creeper_block_damage", FlagTypes.BOOLEAN)
        .category(FlagCategory.ENTITY)
        .defaultValue(false)
        .icon("1c26ec209756ff5d5b81f25ca4db2ee4dceb52874e5f35bb98ce82cace8ac506")
        .description("Whether " + LIGHT_YELLOW.wrap("creepers") + " can destroy blocks.")
        .build();

    public static final ClaimFlag<Boolean> END_CRYSTAL_BLOCK_DAMAGE = ClaimFlag.builder("end_crystal_block_damage", FlagTypes.BOOLEAN)
        .category(FlagCategory.ENTITY)
        .defaultValue(false)
        .icon("d65f9c5e595a159d86f8b3e5da98b2c5db7a2f7a7e3fc2303761acda409bc452")
        .description("Whether " + LIGHT_YELLOW.wrap("end crystals") + " can destroy blocks.")
        .build();

    public static final ClaimFlag<Boolean> ENDER_DRAGON_GRIEF = ClaimFlag.builder("ender_dragon_grief", FlagTypes.BOOLEAN)
        .category(FlagCategory.ENTITY)
        .defaultValue(false)
        .icon("7e4d35a2f6bd739605fa4eaa6e15d038009f3f20b1a204628016058736e7b95e")
        .description("Whether " + LIGHT_YELLOW.wrap("Ender Dragons") + " can destroy blocks.")
        .build();

    public static final ClaimFlag<Boolean> ENDERMAN_GRIEF = ClaimFlag.builder("enderman_grief", FlagTypes.BOOLEAN)
        .category(FlagCategory.ENTITY)
        .defaultValue(false)
        .icon("4f24767c8138b3dfec02f77bd151994d480d4e869664ce09a26b19289212162b")
        .description("Whether " + LIGHT_YELLOW.wrap("endermans") + " can pick up blocks.")
        .build();

    public static final ClaimFlag<Boolean> ENTITY_SPAWN = ClaimFlag.builder("entity_spawn", FlagTypes.BOOLEAN)
        .category(FlagCategory.ENTITY)
        .defaultValue(true)
        .icon("3d02e0e429a4a681a8686cae11554a73d4d0622c87f42d11cd9a87aa95611903")
        .description("Whether " + LIGHT_YELLOW.wrap("all entities") + " can spawn.")
        .build();

    public static final ClaimFlag<Boolean> FIREBALL_BLOCK_DAMAGE = ClaimFlag.builder("fireball_block_damage", FlagTypes.BOOLEAN)
        .category(FlagCategory.ENTITY)
        .defaultValue(false)
        .icon("64ab8a22e7687cc4c78f3b6ff5b1eb04917b51cd3cd7dbce36171160b3c77ced")
        .description("Whether " + LIGHT_YELLOW.wrap("fireballs") + " can destroy blocks.")
        .build();

    public static final ClaimFlag<Boolean> MONSTER_SPAWN = ClaimFlag.builder("monster_spawn", FlagTypes.BOOLEAN)
        .category(FlagCategory.ENTITY)
        .defaultValue(true)
        .icon("ca71506b17e71d7d3d16e50c391742d7993d1ad34a455860af5533076f11e2bc")
        .description("Whether " + LIGHT_YELLOW.wrap("monsters") + " can spawn.")
        .build();

    public static final ClaimFlag<Boolean> RAVAGER_GRIEF = ClaimFlag.builder("ravager_grief", FlagTypes.BOOLEAN)
        .category(FlagCategory.ENTITY)
        .defaultValue(false)
        .icon("cd20bf52ec390a0799299184fc678bf84cf732bb1bd78fd1c4b441858f0235a8")
        .description("Whether " + LIGHT_YELLOW.wrap("ravagers") + " can destroy blocks.")
        .build();

    public static final ClaimFlag<Boolean> SILVERFISH_INFEST = ClaimFlag.builder("silverfish_infest", FlagTypes.BOOLEAN)
        .category(FlagCategory.ENTITY)
        .defaultValue(false)
        .icon("da91dab8391af5fda54acd2c0b18fbd819b865e1a8f1d623813fa761e924540")
        .description("Whether " + LIGHT_YELLOW.wrap("silverfishes") + " can infest blocks.")
        .build();

    public static final ClaimFlag<Boolean> SNOWMAN_TRAIL = ClaimFlag.builder("snowman_trail", FlagTypes.BOOLEAN)
        .category(FlagCategory.ENTITY)
        .defaultValue(true)
        .icon("126ab3ed98ff470e4aa03fc69c745f61c0b614f3e1ecb42bac1c929223364789")
        .description("Whether " + LIGHT_YELLOW.wrap("snowmans") + " can form snow.")
        .build();

    public static final ClaimFlag<Boolean> VILLAGER_FARM = ClaimFlag.builder("villager_farm", FlagTypes.BOOLEAN)
        .category(FlagCategory.ENTITY)
        .defaultValue(true)
        .icon("ef15fe4b7623c753393524f557b36ec81258a75daba159b39a6f800f7171a475")
        .description("Whether " + LIGHT_YELLOW.wrap("villagers") + " can farm crops.")
        .build();

    public static final ClaimFlag<Boolean> WITHER_BLOCK_DAMAGE = ClaimFlag.builder("wither_block_damage", FlagTypes.BOOLEAN)
        .category(FlagCategory.ENTITY)
        .defaultValue(false)
        .icon("74f328f5044129b5d1f96affd1b8c05bcde6bd8e756aff5c5020585eef8a3daf")
        .description("Whether " + LIGHT_YELLOW.wrap("Withers") + " can destroy blocks.")
        .build();
}
