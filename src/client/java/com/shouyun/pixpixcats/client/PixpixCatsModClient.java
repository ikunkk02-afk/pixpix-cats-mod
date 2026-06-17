package com.shouyun.pixpixcats.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.shouyun.pixpixcats.entity.ModEntities;
import com.shouyun.pixpixcats.network.CatCommandPayload;
import com.shouyun.pixpixcats.sound.ModSounds;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.world.effect.MobEffects;
import org.lwjgl.glfw.GLFW;

public class PixpixCatsModClient implements ClientModInitializer {

	private static KeyMapping keyAudioOne;
	private static KeyMapping keyAudioTwo;
	private static KeyMapping keyAudioThree;
	private static AbstractTickableSoundInstance currentBgm;

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(ModEntities.KING, KingEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.KING_SUMMON_PROJECTILE, ThrownItemRenderer::new);
		EntityRendererRegistry.register(ModEntities.ZHANG_XUEFENG, ZhangXuefengRenderer::new);

		keyAudioOne = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.pixpix-cats-mod.audio_one",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_H,
				"category.pixpix-cats-mod"
		));
		keyAudioTwo = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.pixpix-cats-mod.audio_two",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_J,
				"category.pixpix-cats-mod"
		));
		keyAudioThree = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.pixpix-cats-mod.audio_three",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_Z,
				"category.pixpix-cats-mod"
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;

			if (keyAudioOne.consumeClick()) {
				handleKeyPress(client, 0, ModSounds.AUDIO_ONE);
			}
			if (keyAudioTwo.consumeClick()) {
				handleKeyPress(client, 1, ModSounds.AUDIO_TWO);
			}
			if (keyAudioThree.consumeClick()) {
				handleKeyPress(client, 2, ModSounds.AUDIO_THREE);
			}

			// 超速模式 BGM 跟随玩家
			handleSuperSpeedBgm(client);
		});
	}

	private static void handleSuperSpeedBgm(Minecraft client) {
		boolean hasSuperSpeed = false;
		var effect = client.player.getEffect(MobEffects.MOVEMENT_SPEED);
		if (effect != null && effect.getAmplifier() >= 9) {
			hasSuperSpeed = true;
		}

		if (hasSuperSpeed && (currentBgm == null || currentBgm.isStopped())) {
			currentBgm = new FollowingSoundInstance(ModSounds.NIAN_ZHANG_SHI, client.player);
			client.getSoundManager().play(currentBgm);
		} else if (!hasSuperSpeed && currentBgm != null) {
			client.getSoundManager().stop(currentBgm);
			currentBgm = null;
		}
	}

	private static void handleKeyPress(Minecraft client, int commandId, net.minecraft.sounds.SoundEvent sound) {
		if (client.player != null) {
			client.player.playSound(sound, 1.0F, 1.0F);
		}
		ClientPlayNetworking.send(new CatCommandPayload(commandId));
	}
}
