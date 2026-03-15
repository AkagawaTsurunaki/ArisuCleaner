package com.github.akagawatsurunaki.arisucleaner.task;


import net.minecraft.server.MinecraftServer;

public abstract class AbstractTask {
    protected int executePerTicks;
    private int elapsedTicks;

    public AbstractTask(int executePerTicks) {
        this.executePerTicks = executePerTicks;
        this.elapsedTicks = 0;
    }

    public void setExecutePerTicks(int executePerTicks) {
        this.executePerTicks = executePerTicks;
    }

    public void tick(MinecraftServer server) {
        elapsedTicks += 1;
        if (elapsedTicks >= executePerTicks) {
            execute(server);
            elapsedTicks = 0;
        }
    }

    public void execute(MinecraftServer server) {

    }
}
