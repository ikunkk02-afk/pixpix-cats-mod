package com.shouyun.pixpixcats.handler;

import com.shouyun.pixpixcats.PixpixCatsMod;
import com.shouyun.pixpixcats.network.CatCommandPayload;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CatAiHandler {

    private static final double RANGE = 16.0;
    private static final int MAX_JUMPS = 7;
    private static final int MAX_DURATION = 110;
    private static final int MIN_GAP = 14;

    public static void handle(ServerPlayer player, CatCommandPayload payload) {
        ServerLevel level = player.serverLevel();
        Vec3 playerPos = player.position();

        AABB box = new AABB(playerPos.subtract(RANGE, RANGE, RANGE),
                            playerPos.add(RANGE, RANGE, RANGE));
        List<Cat> cats = level.getEntitiesOfClass(Cat.class, box);

        int cmd = payload.command();
        for (Cat cat : cats) {
            switch (cmd) {
                case 0 -> handleGoAway(cat, player);
                case 1 -> handleComeHere(cat, player);
                case 2 -> handleDance(cat, level);
            }
        }
    }

    private static void handleGoAway(Cat cat, ServerPlayer player) {
        cat.setTarget(null);
        cat.getNavigation().stop();
        cat.setInSittingPose(false);

        Vec3 away = cat.position().subtract(player.position()).normalize();
        if (away.lengthSqr() < 0.01) {
            away = new Vec3(1, 0, 0);
        }
        Vec3 target = cat.position().add(away.scale(8.0));
        cat.getNavigation().moveTo(target.x, target.y, target.z, 1.0);
    }

    private static void handleComeHere(Cat cat, ServerPlayer player) {
        cat.setTarget(null);
        cat.setInSittingPose(false);
        cat.getNavigation().moveTo(player, 1.2);

        // 爱心粒子效果
        ServerLevel level = player.serverLevel();
        for (int i = 0; i < 5; i++) {
            level.sendParticles(ParticleTypes.HEART,
                    cat.getX() + (level.random.nextDouble() - 0.5),
                    cat.getY() + 1.0 + level.random.nextDouble(),
                    cat.getZ() + (level.random.nextDouble() - 0.5),
                    1, 0, 0.1, 0, 0.02);
        }
        level.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                cat.getX(), cat.getY() + 1.5, cat.getZ(),
                3, 0.2, 0.2, 0.2, 0);
    }

    private static void handleDance(Cat cat, ServerLevel level) {
        cat.setTarget(null);
        cat.getNavigation().stop();
        cat.setInSittingPose(false);

        MinecraftServer server = level.getServer();
        int startTick = server.getTickCount();
        int[] state = {0, 0};

        doJump(cat, level);
        state[0] = 1;
        state[1] = server.getTickCount();
        PixpixCatsMod.LOGGER.info("[CatAiHandler] Cat {} jump 1/{}", cat.getUUID(), MAX_JUMPS);

        scheduleNext(server, startTick, cat, level, state);
    }

    private static void scheduleNext(MinecraftServer server, int startTick, Cat cat,
                                      ServerLevel level, int[] state) {
        server.tell(new TickTask(server.getTickCount() + 3, () -> {
            int now = server.getTickCount();
            if (state[0] >= MAX_JUMPS || now - startTick > MAX_DURATION) return;
            if (!cat.onGround() || (state[1] > 0 && now - state[1] < MIN_GAP)) {
                scheduleNext(server, startTick, cat, level, state);
                return;
            }
            doJump(cat, level);
            state[0]++;
            state[1] = now;
            PixpixCatsMod.LOGGER.info("[CatAiHandler] Cat {} jump {}/{}", cat.getUUID(), state[0], MAX_JUMPS);
            scheduleNext(server, startTick, cat, level, state);
        }));
    }

    private static void doJump(Cat cat, ServerLevel level) {
        cat.setDeltaMovement(cat.getDeltaMovement().add(0, 0.30, 0));
        cat.hasImpulse = true;

        level.sendParticles(ParticleTypes.HEART,
                cat.getX(), cat.getY() + 1.0, cat.getZ(),
                6, 0.3, 0.3, 0.3, 0.03);
    }
}
