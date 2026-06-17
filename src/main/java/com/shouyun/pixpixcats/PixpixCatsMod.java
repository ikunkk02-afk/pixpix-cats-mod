package com.shouyun.pixpixcats;

import com.shouyun.pixpixcats.entity.ModEntities;
import com.shouyun.pixpixcats.entity.ZhangXuefengEntity;
import com.shouyun.pixpixcats.handler.CatAiHandler;
import com.shouyun.pixpixcats.handler.SuperSpeedHandler;
import com.shouyun.pixpixcats.network.CatCommandPayload;
import com.shouyun.pixpixcats.sound.ModSounds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PixpixCatsMod implements ModInitializer {
	public static final String MOD_ID = "pixpix-cats-mod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("PixPix Cats Mod 启动!");

		ModEntities.init();
		LOGGER.info("实体已注册");

		ModSounds.init();
		LOGGER.info("声音事件已注册");

		PayloadTypeRegistry.playC2S().register(CatCommandPayload.TYPE, CatCommandPayload.STREAM_CODEC);

		ServerPlayNetworking.registerGlobalReceiver(CatCommandPayload.TYPE, (payload, context) -> {
			context.server().execute(() -> {
				CatAiHandler.handle(context.player(), payload);
			});
		});

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				SuperSpeedHandler.tick(player);
			}

			// 巧乐兹/雪碧丢水里 → 雷劈召唤张雪峰
			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				ServerLevel level = player.serverLevel();
				AABB box = player.getBoundingBox().inflate(32);
				for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, box)) {
					ItemStack stack = item.getItem();
					if (!stack.is(ModEntities.QIAOLEZI) && !stack.is(ModEntities.XUEBI)) continue;
					if (!item.isInWater()) continue;

					LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
					bolt.setPos(item.getX(), item.getY(), item.getZ());
					level.addFreshEntity(bolt);

					// 炫酷粒子
					for (int i = 0; i < 30; i++) {
						level.sendParticles(net.minecraft.core.particles.ParticleTypes.FLASH,
								item.getX(), item.getY() + 1, item.getZ(),
								1, level.random.nextGaussian() * 2, level.random.nextDouble() * 3,
								level.random.nextGaussian() * 2, 0.1);
					}
					for (int i = 0; i < 20; i++) {
						level.sendParticles(net.minecraft.core.particles.ParticleTypes.END_ROD,
								item.getX(), item.getY() + 1, item.getZ(),
								1, level.random.nextGaussian() * 2, level.random.nextDouble() * 3,
								level.random.nextGaussian() * 2, 0.05);
					}
					for (int i = 0; i < 15; i++) {
						level.sendParticles(net.minecraft.core.particles.ParticleTypes.SOUL_FIRE_FLAME,
								item.getX(), item.getY() + 1, item.getZ(),
								1, level.random.nextGaussian() * 1.5, level.random.nextDouble() * 2,
								level.random.nextGaussian() * 1.5, 0.02);
					}

					ZhangXuefengEntity zhang = ModEntities.ZHANG_XUEFENG.create(level);
					if (zhang != null) {
						zhang.setPos(item.getX(), item.getY(), item.getZ());
						level.addFreshEntity(zhang);
					}

					item.discard();
					LOGGER.info("Lightning summoned ZhangXuefeng at {}", item.blockPosition());
				}
			}
		});

		LOGGER.info("网络包处理已注册");

		// 玩家退出时清除超速状态
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			SuperSpeedHandler.clearPlayer(handler.player);
		});
	}
}
