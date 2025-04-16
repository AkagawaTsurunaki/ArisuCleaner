package com.github.akagawatsurunaki.arisucleaner.command;

import com.github.akagawatsurunaki.arisucleaner.manager.TaskManager;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import static com.github.akagawatsurunaki.arisucleaner.ArisuCleaner.ARISU_STYLE;

public class ClearItemsCommand {

    public static int executeWithOneArg(CommandContext<ServerCommandSource> context) {
        Integer clearTicks = context.getArgument("clearTicks", Integer.class);
        if (clearTicks == null || clearTicks < 1) {
            context.getSource().sendFeedback(() ->
                    Text.literal("[ArisuCleaner] 参数 clearTicks 必须为正整数")
                            .withColor(Colors.RED), false);
            return 0;
        }
        var tipsTicks = (int) (clearTicks * 0.1); // Default to 10% * clearTicks
        execute(clearTicks, tipsTicks, context);
        return 1;
    }

    public static int executeWithTwoArg(CommandContext<ServerCommandSource> context) {
        Integer clearTicks = context.getArgument("clearTicks", Integer.class);
        Integer tipsTicks = context.getArgument("tipsTicks", Integer.class);
        if (tipsTicks == null || tipsTicks < 1) {
            context.getSource().sendFeedback(() ->
                    Text.literal("[ArisuCleaner] 参数 tipsTicks 必须为正整数")
                            .withColor(Colors.RED), false);
            return 0;
        }
        if (clearTicks == null || clearTicks < 1) {
            context.getSource().sendFeedback(() ->
                    Text.literal("[ArisuCleaner] 参数 clearTicks 必须为正整数")
                            .withColor(Colors.RED), false);
            return 0;
        }
        if (tipsTicks > clearTicks) {
            context.getSource().sendFeedback(() ->
                    Text.literal("[ArisuCleaner] 参数 tipsTicks 必须小于或等于 clearTicks")
                            .withColor(Colors.RED), false);
            return 0;
        }
        execute(clearTicks, tipsTicks, context);
        return 1;
    }

    private static void execute(int clearPerTicks, int tipsTicks, CommandContext<ServerCommandSource> context) {
        TaskManager.INSTANCE.startClearItemsTask(clearPerTicks, tipsTicks);
        context.getSource().sendFeedback(() ->
                Text.literal("[ArisuCleaner] 清理掉落物任务更新为每 %d Ticks 清理，清理前 %d Ticks 给予提醒。"
                                .formatted(clearPerTicks, tipsTicks))
                        .setStyle(ARISU_STYLE), true);
    }
}
