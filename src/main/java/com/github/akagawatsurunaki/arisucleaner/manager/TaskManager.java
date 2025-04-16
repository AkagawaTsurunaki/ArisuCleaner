package com.github.akagawatsurunaki.arisucleaner.manager;

import com.github.akagawatsurunaki.arisucleaner.task.ClearItemsTask;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import static com.github.akagawatsurunaki.arisucleaner.ArisuCleaner.LOGGER;
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
}