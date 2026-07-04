package su.nightexpress.excellentclaims.api.core;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.util.bukkit.NightTask;

@NullMarked
public abstract class AbstractController implements NightController, Listener {

    protected final NightCorePlugin plugin;
    protected final List<NightTask> tasks;

    private boolean isRegistered;

    public AbstractController(NightCorePlugin plugin) {
        this.plugin = plugin;
        this.tasks = new ArrayList<>();
    }

    protected abstract void onStart();

    protected abstract void onShutdown();

    protected abstract void onReload();

    @Override
    public void start() {
        if (this.isRegistered) return;

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);

        this.onStart();

        this.isRegistered = true;
    }

    @Override
    public void shutdown() {
        if (!this.isRegistered) return;

        // Automatically kill all tasks started by this controller
        this.stopTasks();

        // Automatically unregister all Bukkit events inside this class
        HandlerList.unregisterAll(this);

        // Fire an optional cleanup method for subclasses
        this.onShutdown();

        this.isRegistered = false;
    }

    @Override
    public void reload() {
        this.onReload();
    }

    public void stopTasks() {
        this.tasks.forEach(NightTask::stop);
        this.tasks.clear();
    }

    protected void addSecondsTask(Runnable runnable, int interval) {
        this.addTask(NightTask.create(plugin, runnable, interval));
    }

    protected void addTickTask(Runnable runnable, long interval) {
        this.addTask(NightTask.create(plugin, runnable, interval));
    }

    protected void addAsyncSecondsTask(Runnable runnable, int interval) {
        this.addTask(NightTask.createAsync(plugin, runnable, interval));
    }

    protected void addAsyncTickTask(Runnable runnable, long interval) {
        this.addTask(NightTask.createAsync(plugin, runnable, interval));
    }

    protected void addTask(NightTask task) {
        if (task.isValid()) {
            this.tasks.add(task);
        }
    }
}
