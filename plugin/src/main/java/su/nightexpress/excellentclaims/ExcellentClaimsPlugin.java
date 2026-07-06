package su.nightexpress.excellentclaims;

import java.util.logging.Logger;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.admin.AdminBypassBoostrap;
import su.nightexpress.excellentclaims.admin.AdminBypassModule;
import su.nightexpress.excellentclaims.api.APIConfiguration;
import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.core.ComponentCore;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.core.EventPublisher;
import su.nightexpress.excellentclaims.api.core.SimpleDependencies;
import su.nightexpress.excellentclaims.core.Lang;
import su.nightexpress.excellentclaims.engine.ClaimEngine;
import su.nightexpress.excellentclaims.engine.EngineBootstrap;
import su.nightexpress.excellentclaims.highlighter.HighlightConfiguration;
import su.nightexpress.excellentclaims.permission.PermissionConfiguration;
import su.nightexpress.excellentclaims.rank.RanksBootstrap;
import su.nightexpress.excellentclaims.rank.RanksModule;
import su.nightexpress.excellentclaims.rules.RulesBootstrap;
import su.nightexpress.excellentclaims.rules.RulesModule;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.config.PluginDetails;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogRegistry;
import su.nightexpress.nightcore.userdata.UserDataManager;

@NullMarked
public class ExcellentClaimsPlugin extends NightPlugin implements ClaimPlugin {

    private final ComponentCore core = new ComponentCore();

    @Override
    protected PluginDetails getDefaultDetails() {
        return PluginDetails.create("Claims", new String[]{"eclaims", "excellentclaims"});
    }

    @Override
    protected boolean disableCommandManager() {
        return true;
    }

    @Override
    protected void addRegistries() {
        super.addRegistries();
        this.registerLang(Lang.class);
    }

    @Override
    public void enable() {
        DependencyContainer dependencies = new SimpleDependencies();

        ClaimRegistry claims = new ClaimRegistry();

        dependencies.register(ClaimPlugin.class, this);
        dependencies.register(Logger.class, this.getLogger());
        dependencies.register(EventPublisher.class, this);
        dependencies.register(DialogRegistry.class, this.dialogRegistry);
        dependencies.register(UserDataManager.class, this.getUserDataManager());
        dependencies.register(ClaimRegistry.class, claims);

        AdminBypassModule bypassModule = AdminBypassBoostrap.bootstrap(dependencies);
        RanksModule ranksModule = RanksBootstrap.bootstrap(dependencies);

        HighlightConfiguration.configure(dependencies);
        PermissionConfiguration.configure(dependencies);

        RulesModule ruleModule = RulesBootstrap.bootstrap(dependencies);
        ClaimEngine engine = EngineBootstrap.bootstrap(dependencies);

        APIConfiguration.configure(dependencies);

        this.core.register(bypassModule);
        this.core.register(ranksModule);
        this.core.register(ruleModule);
        this.core.register(engine);

        this.core.start();
    }

    @Override
    public void disable() {
        if (this.core != null) {
            this.core.shutdown();
        }
    }

    @Override
    protected void onShutdown() {
        super.onShutdown();
    }

    @Override
    public boolean fireEvent(Event event) {
        this.getPluginManager().callEvent(event);
        if (event instanceof Cancellable cancellable) {
            return !cancellable.isCancelled();
        }
        return true;
    }

    @Override
    public void reloadPlugin() {
        this.core.reload();
    }
}