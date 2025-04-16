package com.github.akagawatsurunaki.arisucleaner;

import com.github.akagawatsurunaki.arisucleaner.command.ClearItemsCommand;
import com.github.akagawatsurunaki.arisucleaner.manager.TaskManager;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.akagawatsurunaki.arisucleaner.task.ClearItemsTask.DEFAULT_CLEAR_TICKS;
import static com.github.akagawatsurunaki.arisucleaner.task.ClearItemsTask.DEFAULT_TIPS_TICKS;

public class ArisuCleaner implements ModInitializer {
    public static final String MOD_ID = "arisucleaner";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Style ARISU_STYLE = Style.EMPTY
            .withColor(TextColor.parse("#33ddff").getOrThrow());

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        LOGGER.info("Initializing ArisuCleaner...");
        TaskManager.INSTANCE.startClearItemsTask(DEFAULT_CLEAR_TICKS, DEFAULT_TIPS_TICKS);
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
            commandDispatcher.register(
                    CommandManager.literal("clearItems")
                            .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(4))
                            .then(CommandManager.argument("clearTicks", IntegerArgumentType.integer(1))
                                    .executes(ClearItemsCommand::executeWithOneArg)
                                    .then(CommandManager.argument("tipsTicks", IntegerArgumentType.integer(1))
                                            .executes(ClearItemsCommand::executeWithTwoArg))
                            )

            );

        });
        LOGGER.info("Initialized ArisuCleaner!");
    }
}