package com.shouyun.pixpixcats.handler;

import com.shouyun.pixpixcats.PixpixCatsMod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.*;

public class SuperSpeedHandler {

    private static final Map<UUID, Long> activePlayers = new HashMap<>();
    private static final long DURATION_TICKS = 20 * 60;

    public static void activate(ServerPlayer player) {
        activePlayers.put(player.getUUID(), player.serverLevel().getGameTime() + DURATION_TICKS);
        PixpixCatsMod.LOGGER.info("SuperSpeed activated for {}", player.getName().getString());
    }

    public static boolean isActive(Player player) {
        return activePlayers.containsKey(player.getUUID());
    }

    public static void clearPlayer(Player player) {
        activePlayers.remove(player.getUUID());
    }

    public static void tick(ServerPlayer player) {
        UUID uuid = player.getUUID();
        Long expireAt = activePlayers.get(uuid);
        if (expireAt == null) return;

        long now = player.serverLevel().getGameTime();
        if (now >= expireAt) {
            activePlayers.remove(uuid);
            player.removeEffect(MobEffects.MOVEMENT_SPEED);
            return;
        }

        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,
                40, 9, false, false, true));

        Level level = player.level();
        BlockPos playerPos = player.blockPosition();

        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = 0; dy <= 3; dy++) {
                for (int dz = -2; dz <= 2; dz++) {
                    BlockPos pos = playerPos.offset(dx, dy, dz);
                    if (pos.distSqr(playerPos) > 9) continue;
                    // 绝不碰脚下方块
                    if (pos.getY() < playerPos.getY()) continue;
                    var state = level.getBlockState(pos);
                    if (state.isAir() || state.is(Blocks.BEDROCK) || state.is(Blocks.BARRIER)) continue;
                    if (state.getDestroySpeed(level, pos) < 0) continue;
                    level.destroyBlock(pos, true);
                }
            }
        }

        // 撞飞周围生物
        for (Entity entity : level.getEntities(player, player.getBoundingBox().inflate(3.0))) {
            if (entity == player) continue;
            if (entity instanceof EnderDragon) continue;
            entity.hurt(player.damageSources().playerAttack(player), 20.0f);
            double dx = entity.getX() - player.getX();
            double dz = entity.getZ() - player.getZ();
            double len = Math.sqrt(dx * dx + dz * dz);
            if (len > 0) {
                entity.setDeltaMovement(entity.getDeltaMovement().add(
                        dx / len * 3, 0.8, dz / len * 3));
                entity.hasImpulse = true;
            }
        }
    }
}
