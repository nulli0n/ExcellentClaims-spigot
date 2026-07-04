package su.nightexpress.excellentclaims.land.merge;

import java.nio.file.Path;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.core.SimpleDependencies;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.land.LandsModule;
import su.nightexpress.excellentclaims.land.LandsRepository;
import su.nightexpress.excellentclaims.land.data.LandDataService;
import su.nightexpress.excellentclaims.land.merge.command.MergeCommandConfiguration;
import su.nightexpress.excellentclaims.land.merge.session.SessionCache;
import su.nightexpress.excellentclaims.land.merge.session.SessionOrchestrator;
import su.nightexpress.excellentclaims.land.merge.session.SessionService;
import su.nightexpress.excellentclaims.land.merge.settings.MergeSettings;
import su.nightexpress.excellentclaims.land.merge.tool.MergeTool;
import su.nightexpress.excellentclaims.land.merge.tool.MergeToolController;
import su.nightexpress.excellentclaims.land.merge.tool.MergeToolService;
import su.nightexpress.excellentclaims.land.settings.LandSettings;
import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

public final class MergeConfiguration {

    private static final String FILE_SETTINGS = "lands.merge.yml";

    private MergeConfiguration() {
    }

    public static void configure(LandsModule landsModule, DependencyContainer container) {
        DependencyContainer mergeContainer = new SimpleDependencies(container);

        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        ClaimPermissionAPI permissions = container.get(ClaimPermissionAPI.class);
        Path settingsFile = plugin.dataPath().resolve(FILE_SETTINGS);
        // TODO UI Buttons

        LandsRepository repository = container.get(LandsRepository.class);
        LandDataService dataService = container.get(LandDataService.class);
        MessageDispatcher dispatcher = container.get(MessageDispatcher.class);

        MergeModule module = new MergeModule();

        plugin.injectLang(MergeLang.class);

        LandSettings landSettings = container.get(LandSettings.class); // TODO Settings
        MergeSettings mergeSettings = new MergeSettings(settingsFile);

        MergeToolService toolService = createToolService(plugin, mergeSettings);
        MergeService mergeService = new MergeService(plugin, landSettings, dataService, permissions);

        SessionCache sessionCache = new SessionCache();
        SessionService sessionService = new SessionService(sessionCache, toolService, mergeSettings);
        SessionOrchestrator sessionOrchestrator = new SessionOrchestrator(repository, sessionService, mergeService);

        mergeContainer.register(SessionOrchestrator.class, sessionOrchestrator);

        MergeCommandConfiguration.configure(module, mergeContainer);

        MergeToolController toolController = new MergeToolController(plugin, toolService, sessionOrchestrator, dispatcher);

        module.addComponent(mergeSettings);
        module.addComponent(toolController);

        landsModule.addComponent(module);
    }

    private static MergeToolService createToolService(ClaimPlugin plugin, MergeSettings settings) {
        AdaptedKey toolDomain = BukkitKeys.create(plugin, "lands.merge_tools");
        MergeToolbox toolbox = createTools(settings);
        return new MergeToolService(toolDomain, toolbox);
    }

    private static MergeToolbox createTools(MergeSettings settings) {
        MergeTool mergeTool = new MergeTool(Identifier.of("merge_tool"), settings);
        MergeTool splitTool = new MergeTool(Identifier.of("split_tool"), settings);

        return new MergeToolbox(mergeTool, splitTool);
    }
}
