package com.github.akagawatsurunaki.arisucleaner.manager;

import com.github.akagawatsurunaki.arisucleaner.task.ClearEntitiesTask;
import com.github.akagawatsurunaki.arisucleaner.task.ClearItemsTask;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import static com.github.akagawatsurunaki.arisucleaner.ArisuCleaner.LOGGER;
import static com.github.akagawatsurunaki.arisucleaner.task.ClearEntitiesTask.DEFAULT_EXECUTE_TICKS;
import static com.github.akagawatsurunaki.arisucleaner.task.ClearItemsTask.DEFAULT_CLEAR_TICKS;
import static com.github.akagawatsurunaki.arisucleaner.task.ClearItemsTask.DEFAULT_TIPS_TICKS;

public class TaskManager {

    public static final TaskManager INSTANCE = new TaskManager();

    private final ClearItemsTask clearItemsTask = new ClearItemsTask(DEFAULT_CLEAR_TICKS, DEFAULT_TIPS_TICKS);
    private boolean isClearItemsTaskRegistered = false;

    public void startClearItemsTask(int clearPerTicks, int tipsTicks) {
        clearItemsTask.setClearTicks(clearPerTicks);
        clearItemsTask.setTipsTicks(tipsTicks);
        if (!isClearItemsTaskRegistered) {
            ServerTickEvents.END_SERVER_TICK.register(clearItemsTask::execute);
            isClearItemsTaskRegistered = true;
            LOGGER.info("ClearItemsTask is registered");
        }
    }

    private final ClearEntitiesTask clearEntitiesTask = new ClearEntitiesTask(DEFAULT_EXECUTE_TICKS);
    private boolean isClearEntitiesTaskRegistered = false;

    public void startClearEntitiesTask(int clearTicks, int maxEntities, float removeRatio) {
        clearEntitiesTask.setExecutePerTicks(clearTicks);
        clearEntitiesTask.setMaxEntities(maxEntities);
        clearEntitiesTask.setRemoveRatio(removeRatio);
        if (!isClearEntitiesTaskRegistered) {
            ServerTickEvents.END_SERVER_TICK.register(clearEntitiesTask::tick);
            isClearEntitiesTaskRegistered = true;
            LOGGER.info("ClearEntitiesTask is registered");
        }
    }
}