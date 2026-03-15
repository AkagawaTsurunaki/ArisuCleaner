package com.github.akagawatsurunaki.arisucleaner.command;

import com.github.akagawatsurunaki.arisucleaner.manager.TaskManager;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import static com.github.akagawatsurunaki.arisucleaner.ArisuCleaner.ARISU_STYLE;

public class ClearEntitiesCommand {
    public static int executeWithOneArg(CommandContext<ServerCommandSource> context) {
        Integer clearTicks = context.getArgument("clearTicks", Integer.class);
        if (clearTicks == null || clearTicks < 1) {
            context.getSource().sendFeedback(() ->
                    Text.literal("[ArisuCleaner] 参数 clearTicks 必须为正整数")
                            .withColor(Colors.RED), false);
            return 0;
        }
        var tipsTicks = (int) (clearTicks * 0.1); // Default to 10% * clearTicks
        execute(clearTicks, context);
        return 1;
    }

    private static void execute(int clearPerTicks, CommandContext<ServerCommandSource> context) {
        TaskManager.INSTANCE.startClearEntitiesTask(clearPerTicks);
        context.getSource().sendFeedback(() ->
                Text.literal("[ArisuCleaner] 清理实体任务更新为每 %d Ticks 清理。"
                                .formatted(clearPerTicks))
                        .setStyle(ARISU_STYLE), true);
    }
}
