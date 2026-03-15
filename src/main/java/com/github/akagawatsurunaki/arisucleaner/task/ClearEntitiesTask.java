package com.github.akagawatsurunaki.arisucleaner.task;

import com.github.akagawatsurunaki.arisucleaner.util.EvictingList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.TypeFilter;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.akagawatsurunaki.arisucleaner.ArisuCleaner.ARISU_STYLE;
import static com.github.akagawatsurunaki.arisucleaner.ArisuCleaner.LOGGER;

public class ClearEntitiesTask extends AbstractTask {

    public static final int DEFAULT_EXECUTE_TICKS = 20 * 60 * 5;
    public static final int DEFAULT_MAX_ENTITIES = 3000;
    public static final float DEFAULT_REMOVE_RATIO = 0.8f;

    Map<RegistryKey<World>, EvictingList<Integer>> record = new HashMap<>();
    private int maxEntities = DEFAULT_MAX_ENTITIES;
    private float removeRatio = DEFAULT_REMOVE_RATIO;

    public void setMaxEntities(int maxEntities) {
        this.maxEntities = maxEntities;
    }

    public void setRemoveRatio(float removeRatio) {
        if (removeRatio > 1) {
            this.removeRatio = 1;
        } else if (removeRatio < 0) {
            this.removeRatio = 0;
        } else {
            this.removeRatio = removeRatio;
        }
    }

    public ClearEntitiesTask(int executePerTicks) {
        super(executePerTicks);
    }

    public void execute(MinecraftServer server) {
        int totalEntities = 0;
        for (ServerWorld world : server.getWorlds()) {// Get all living entities without using `NameTag`
            var livingEntities = world.getEntitiesByType(TypeFilter.instanceOf(LivingEntity.class),
                    livingEntity -> !livingEntity.hasCustomName());

            if (record.get(world.getRegistryKey()) == null) {
                record.put(world.getRegistryKey(), new EvictingList<>(10));
            }
            record.get(world.getRegistryKey()).add(livingEntities.size());
            totalEntities += livingEntities.size();
        }
        logRecord();
        LOGGER.info("Number of Entities in total: {}", totalEntities);

        // Kill entities
        if (totalEntities >= maxEntities) {
            killEntitiesWithMostPopulation(server);
        }
    }

    /**
     * Find the living entity type with the highest count and eliminate a portion based on the given ratio.
     * @param server MinecraftServer
     */
    private void killEntitiesWithMostPopulation(MinecraftServer server) {
        var livingEntities = new ArrayList<LivingEntity>();
        for (ServerWorld world : server.getWorlds()) {
            livingEntities.addAll(world.getEntitiesByType(TypeFilter.instanceOf(LivingEntity.class),
                    livingEntity -> !livingEntity.hasCustomName()));
        }
        var candidateEntities = livingEntities.stream()
                .collect(Collectors.groupingBy(LivingEntity::getType, Collectors.counting()));
        var maxPopulationEntities = candidateEntities.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
        if (maxPopulationEntities != null) {
            var entityType = maxPopulationEntities.getKey();
            var killEntities = livingEntities.stream().filter(livingEntity -> livingEntity.getType() == entityType).toList();
            var numKillableEntities = 0;
            try {
                for (int i = 0; i < killEntities.size() * removeRatio; i++) {
                    var killEntity = killEntities.get(i);
                    killEntity.remove(Entity.RemovalReason.DISCARDED);
                    numKillableEntities+=1;
                }
            } catch (Exception e) {
                LOGGER.error("Encounter an exception during cleaning entities", e);
            }
            LOGGER.info("Since {} has the largest population {}, remove {}% of them.", entityType, numKillableEntities, removeRatio * 100);
            server.getPlayerManager().broadcast(
                    Text.literal("[ArisuCleaner] %d 个有生命实体已被清除！".formatted(numKillableEntities))
                            .setStyle(ARISU_STYLE),
                    false);
        }

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
