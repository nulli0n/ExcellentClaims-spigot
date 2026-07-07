package su.nightexpress.excellentclaims.rules.evaluation;

import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.rule.EventController;
import su.nightexpress.excellentclaims.rules.evaluation.controller.block.BlockBurnController;
import su.nightexpress.excellentclaims.rules.evaluation.controller.block.BlockExplodeController;
import su.nightexpress.excellentclaims.rules.evaluation.controller.block.BlockFadeController;
import su.nightexpress.excellentclaims.rules.evaluation.controller.block.BlockFormController;
import su.nightexpress.excellentclaims.rules.evaluation.controller.block.BlockFromToController;
import su.nightexpress.excellentclaims.rules.evaluation.controller.block.BlockGrowSpreadController;
import su.nightexpress.excellentclaims.rules.evaluation.controller.block.StructureGrowController;
import su.nightexpress.excellentclaims.rules.evaluation.controller.entity.EntityChangeBlockController;
import su.nightexpress.excellentclaims.rules.evaluation.controller.entity.EntitySpawnController;
import su.nightexpress.excellentclaims.rules.evaluation.controller.player.BlockEntityBreakController;
import su.nightexpress.excellentclaims.rules.evaluation.controller.player.BlockEntityPlaceController;
import su.nightexpress.excellentclaims.rules.evaluation.controller.player.BlockFertilizeController;
import su.nightexpress.excellentclaims.rules.evaluation.controller.player.BlockHarvestController;
import su.nightexpress.excellentclaims.rules.evaluation.controller.player.ItemDropPickupController;
import su.nightexpress.excellentclaims.rules.evaluation.controller.player.PlayerCommandController;
import su.nightexpress.excellentclaims.rules.evaluation.controller.player.PlayerDamageController;
import su.nightexpress.excellentclaims.rules.evaluation.controller.player.PlayerInteractController;
import su.nightexpress.excellentclaims.rules.evaluation.controller.player.PlayerTeleportController;
import su.nightexpress.excellentclaims.rules.evaluation.controller.player.ProjectiveLaunchController;
import su.nightexpress.excellentclaims.rules.settings.RulesSettings;

@NullMarked
public class EventControllerService {

    private final EvaluatorEngine   engine;
    private final RulesSettings     settings;
    private final MessageDispatcher dispatcher;

    public EventControllerService(EvaluatorEngine engine, RulesSettings settings, MessageDispatcher dispatcher) {
        this.engine = engine;
        this.settings = settings;
        this.dispatcher = dispatcher;
    }

    public List<EventController> createControllers() {
        List<EventController> controllers = new ArrayList<>();

        controllers.add(new BlockGrowSpreadController(this.engine, this.settings.isResetBlockAgeInBlockGrowEvent()));
        controllers.add(new BlockFormController(this.engine));
        controllers.add(new BlockFadeController(this.engine));
        controllers.add(new BlockExplodeController(this.engine));
        controllers.add(new BlockBurnController(this.engine));
        //controllers.add(new LeavesDecayController(this.engine));
        controllers.add(new StructureGrowController(this.engine));

        if (this.settings.isAllowHighFrequencyRules()) {
            controllers.add(new BlockFromToController(this.engine));
        }

        controllers.add(new BlockEntityBreakController(this.engine, this.dispatcher));
        controllers.add(new BlockEntityPlaceController(this.engine, this.dispatcher));
        controllers.add(new BlockFertilizeController(this.engine, this.dispatcher));
        controllers.add(new BlockHarvestController(this.engine, this.dispatcher));

        controllers.add(new PlayerInteractController(this.engine, this.dispatcher));

        controllers.add(new ItemDropPickupController(this.engine, this.dispatcher));
        controllers.add(new ProjectiveLaunchController(this.engine, this.dispatcher));
        controllers.add(new PlayerDamageController(this.engine, this.dispatcher));
        controllers.add(new PlayerCommandController(this.engine, this.dispatcher));
        controllers.add(new PlayerTeleportController(this.engine, this.dispatcher));

        controllers.add(new EntityChangeBlockController(this.engine, this.dispatcher));
        controllers.add(new EntitySpawnController(this.engine));

        return controllers;
    }
}
