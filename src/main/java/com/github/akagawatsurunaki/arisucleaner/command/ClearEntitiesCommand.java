package com.github.akagawatsurunaki.arisucleaner.command;

import com.github.akagawatsurunaki.arisucleaner.manager.TaskManager;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Pair;

import static com.github.akagawatsurunaki.arisucleaner.ArisuCleaner.ARISU_STYLE;
import static com.github.akagawatsurunaki.arisucleaner.task.ClearEntitiesTask.DEFAULT_MAX_ENTITIES;
import static com.github.akagawatsurunaki.arisucleaner.task.ClearEntitiesTask.DEFAULT_REMOVE_RATIO;

public class ClearEntitiesCommand {
    public static int executeWithOneArg(CommandContext<ServerCommandSource> context) {
        Pair<Boolean, Integer> clearTicks = getPositiveIntArg(context, "executeTicks");
        if (!clearTicks.getLeft()) {
            return 0;
        }
        execute(clearTicks.getRight(), DEFAULT_MAX_ENTITIES, DEFAULT_REMOVE_RATIO, context);
        return 1;
    }

    private static Pair<Boolean, Integer> getPositiveIntArg(CommandContext<ServerCommandSource> context, String arg) {
        var value = context.getArgument(arg, Integer.class);
        if (value == null || value < 1) {
            context.getSource().sendFeedback(() ->
                    Text.literal("[ArisuCleaner] 参数 " + arg + " 必须为正整数")
                            .withColor(Colors.RED), false);
            return new Pair<>(false, null);
        }
        return new Pair<>(true, value);
    }

    public static int executeWithTwoArgs(CommandContext<ServerCommandSource> context) {
        Pair<Boolean, Integer> clearTicks = getPositiveIntArg(context, "executeTicks");
        if (!clearTicks.getLeft()) {
            return 0;
        }
        Pair<Boolean, Integer> maxEntities = getPositiveIntArg(context, "maxEntities");
        if (!maxEntities.getLeft()) {
            return 0;
        }
        execute(clearTicks.getRight(), maxEntities.getRight(), DEFAULT_REMOVE_RATIO, context);
        return 1;
    }

    public static int executeWithThreeArgs(CommandContext<ServerCommandSource> context) {
        Pair<Boolean, Integer> clearTicks = getPositiveIntArg(context, "executeTicks");
        if (!clearTicks.getLeft()) {
            return 0;
        }
        Pair<Boolean, Integer> maxEntities = getPositiveIntArg(context, "maxEntities");
        if (!maxEntities.getLeft()) {
            return 0;
        }

        var removeRatio = context.getArgument("removeRatio", Float.class);
        if (removeRatio == null || removeRatio < 0 || removeRatio > 1) {
            context.getSource().sendFeedback(() ->
                    Text.literal("[ArisuCleaner] 参数 removeRatio 必须为 0 ~ 1 的浮点数")
                            .withColor(Colors.RED), false);
            return 0;
        }

        execute(clearTicks.getRight(), maxEntities.getRight(), removeRatio, context);
        return 1;
    }

    private static void execute(int executeTicks, int maxEntities, float removeRatio, CommandContext<ServerCommandSource> context) {
        TaskManager.INSTANCE.startClearEntitiesTask(executeTicks, maxEntities, removeRatio);
        context.getSource().sendFeedback(() ->
                Text.literal("[ArisuCleaner] 清理实体任务更新为每 %d Ticks 清理，最大实体数量 %d，清除实体比例 %.2f。"
                                .formatted(executeTicks, maxEntities, removeRatio))
                        .setStyle(ARISU_STYLE), true);
    }
}
