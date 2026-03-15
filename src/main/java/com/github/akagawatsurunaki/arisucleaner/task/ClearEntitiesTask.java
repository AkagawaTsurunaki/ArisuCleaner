package com.github.akagawatsurunaki.arisucleaner.task;

import com.github.akagawatsurunaki.arisucleaner.util.EvictingList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.TypeFilter;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.github.akagawatsurunaki.arisucleaner.ArisuCleaner.MOD_ID;

public class ClearEntitiesTask extends AbstractTask {

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final int DEFAULT_CLEAR_ENTITIES_TICKS = 60;


    Map<RegistryKey<World>, EvictingList<Integer>> record = new HashMap<>();
    float MAX_RISE_RATE = 0.25F;

    public ClearEntitiesTask(int executePerTicks) {
        super(executePerTicks);
    }

    public void execute(MinecraftServer server) {
        server.getWorlds().forEach(world -> {
            // Get all living entities without using `NameTag`
            var livingEntities = world.getEntitiesByType(TypeFilter.instanceOf(LivingEntity.class),
                    livingEntity -> !livingEntity.hasCustomName());

            if (record.get(world.getRegistryKey()) == null) {
                record.put(world.getRegistryKey(), new EvictingList<>(10));
            }
            record.get(world.getRegistryKey()).add(livingEntities.size());
            LOGGER.info("Number of living entities: {}", livingEntities.size());
        });
        logRecord();
    }

    private void logRecord() {
        StringBuilder stringBuilder = new StringBuilder();
        record.forEach((world, list) -> {
            stringBuilder.append(world.toString()).append(": ");
            for (Integer i : list) {
                stringBuilder.append(i).append(", ");
            }
            stringBuilder.append("\n");
        });
        LOGGER.info("History record of number of living entities: {}", stringBuilder);
    }
}
