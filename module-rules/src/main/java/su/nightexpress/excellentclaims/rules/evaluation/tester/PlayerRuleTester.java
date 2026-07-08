package su.nightexpress.excellentclaims.rules.evaluation.tester;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.tester.PlayerTester;
import su.nightexpress.excellentclaims.rules.evaluation.EvaluatorEngine;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockBreakContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockInteractContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockPlaceContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityChangeBlockContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityDamageContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityInteractContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityPlaceContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityRemoveContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.ProjectileLaunchContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.item.ItemDropContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.item.ItemInteractContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.item.ItemPickupContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.player.CommandPreProcessContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.player.TeleportContext;

@NullMarked
public class PlayerRuleTester implements PlayerTester {

    private final EvaluatorEngine engine;

    public PlayerRuleTester(EvaluatorEngine engine) {
        this.engine = engine;
    }

    @Override
    public boolean canChange(Player player, Block block) {
        EntityChangeBlockContext context = new EntityChangeBlockContext(player, player, block);
        return this.engine.quickTest(context);
    }

    @Override
    public boolean canDamage(Player player, Entity entity, DamageSource source) {
        EntityDamageContext context = new EntityDamageContext(player, entity, source);
        return this.engine.quickTest(context);
    }

    @Override
    public boolean canDestroy(Player player, Block block) {
        BlockBreakContext context = new BlockBreakContext(player, block);
        return this.engine.quickTest(context);
    }

    @Override
    public boolean canDestroy(Player player, Entity entity) {
        EntityRemoveContext context = new EntityRemoveContext(player, entity);
        return this.engine.quickTest(context);
    }

    @Override
    public boolean canInteract(Player player, Block block, Action action) {
        BlockInteractContext context = new BlockInteractContext(player, block, action);
        return this.engine.quickTest(context);
    }

    @Override
    public boolean canInteract(Player player, Entity entity) {
        EntityInteractContext context = new EntityInteractContext(player, entity);
        return this.engine.quickTest(context);
    }

    @Override
    public boolean canInteract(Player player, ItemStack itemStack) {
        ItemInteractContext context = new ItemInteractContext(player, itemStack);
        return this.engine.quickTest(context);
    }

    @Override
    public boolean canDropItem(Player player, ItemStack itemStack) {
        ItemDropContext context = new ItemDropContext(player, itemStack);
        return this.engine.quickTest(context);
    }

    @Override
    public boolean canPickupItem(Player player, ItemStack itemStack) {
        ItemPickupContext context = new ItemPickupContext(player, itemStack);
        return this.engine.quickTest(context);
    }

    @Override
    public boolean canPlace(Player player, Block block, Material placedType) {
        BlockPlaceContext context = new BlockPlaceContext(player, block, placedType);
        return this.engine.quickTest(context);
    }

    @Override
    public boolean canPlace(Player player, Block block, Entity entity) {
        EntityPlaceContext context = new EntityPlaceContext(player, block, entity);
        return this.engine.quickTest(context);
    }

    @Override
    public boolean canShoot(Player player, Projectile projectile) {
        ProjectileLaunchContext context = new ProjectileLaunchContext(player, projectile);
        return this.engine.quickTest(context);
    }

    @Override
    public boolean canTeleport(Player player, Location from, Location to, TeleportCause cause) {
        TeleportContext context = new TeleportContext(player, from, to, cause);
        return this.engine.quickTest(context);
    }

    @Override
    public boolean canUseCommand(Player player, Location location, Command command) {
        CommandPreProcessContext context = new CommandPreProcessContext(player, location, command);
        return this.engine.quickTest(context);
    }
}
