package su.nightexpress.excellentclaims.rules.impl.player.block;

import java.util.Set;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.behavior.base.StandardPlayerInteractBlockHandler;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockInteractContext;
import su.nightexpress.excellentclaims.rules.filter.FilterMode;
import su.nightexpress.excellentclaims.rules.filter.FilteredSet;
import su.nightexpress.excellentclaims.rules.spec.AbstractFilterSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class BlockInteractFilterRule extends AbstractFilterSpec<BlockInteractContext, Material> {

    private static final Set<Material> GENERIC_BLOCKS = Lists.newSet(
        Material.BEACON, Material.BREWING_STAND,
        Material.CARTOGRAPHY_TABLE, Material.CRAFTING_TABLE, Material.ENCHANTING_TABLE,
        Material.FURNACE, Material.BLAST_FURNACE, Material.SMOKER,
        Material.GRINDSTONE, Material.LECTERN, Material.LOOM,
        Material.SMITHING_TABLE, Material.STONECUTTER, Material.BEEHIVE,
        Material.BELL, Material.CAMPFIRE,
        Material.SOUL_CAMPFIRE, Material.CHISELED_BOOKSHELF, Material.COMPOSTER,
        Material.DECORATED_POT, Material.END_PORTAL_FRAME, Material.FLETCHING_TABLE,
        Material.FLOWER_POT, Material.JUKEBOX, Material.LODESTONE,
        Material.SPAWNER, Material.RESPAWN_ANCHOR, Material.SUSPICIOUS_GRAVEL,
        Material.SUSPICIOUS_SAND, Material.TNT, Material.TRIAL_SPAWNER,
        Material.VAULT, Material.CRAFTER, Material.DAYLIGHT_DETECTOR,
        Material.DISPENSER, Material.DROPPER, Material.HOPPER,
        Material.NOTE_BLOCK, Material.OBSERVER, Material.COMPARATOR, Material.REPEATER,
        Material.TARGET, Material.TRIPWIRE_HOOK
    );

    private final ClaimPermissionAPI permissions;

    public BlockInteractFilterRule(ClaimPermissionAPI permissions) {
        super(BlockInteractContext.class, RuleTypes.MATERIALS, RuleCategory.PLAYER);
        this.permissions = permissions;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Block Interactions")
            .description(
                "Controls which blocks can",
                "use outsiders and members",
                "without the " + TagWrappers.WHITE.wrap("Block Interact"),
                "permission.",
                "",
                "More specific rules will",
                "override this one."
            )
            .icon(Material.CRAFTING_TABLE)
            .build();
    }

    @Override
    public FilteredSet<Material> getDefaultValue() {
        return FilteredSet.empty(FilterMode.WHITELIST);
    }

    @Override
    public RuleBehavior<BlockInteractContext, FilteredSet<Material>> createBehavior() {
        return this.behaviorBuilder()
            .weight(10)
            .allValues(() -> GENERIC_BLOCKS)
            .shouldHandle(context -> GENERIC_BLOCKS.contains(context.block().getType()))
            .process(
                new StandardPlayerInteractBlockHandler<FilteredSet<Material>>(this.permissions, ClaimPermission.BLOCK_INTERACT) {

                    @Override
                    protected boolean isBlockAllowed(Material type, FilteredSet<Material> blockList) {
                        return blockList.isAllowed(type);
                    }

                })
            .build();
    }
}
