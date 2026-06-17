package com.shouyun.pixpixcats.sound;

import com.shouyun.pixpixcats.PixpixCatsMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
    public static final SoundEvent AUDIO_ONE = register("audio_one");
    public static final SoundEvent AUDIO_TWO = register("audio_two");
    public static final SoundEvent AUDIO_THREE = register("audio_three");
    public static final SoundEvent BABY_DONT_CRY = register("baby_dont_cry");
    public static final SoundEvent NIAN_ZHANG_SHI = register("nian_zhang_shi");
    public static final SoundEvent ZHANG_TALK = register("zhang_talk");

    private static SoundEvent register(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(PixpixCatsMod.MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void init() {
        // 静态初始化器已触发注册，这里留空作为显式入口
    }
}
