package com.shouyun.pixpixcats.client;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

public class FollowingSoundInstance extends AbstractTickableSoundInstance {

    private final Entity entity;

    public FollowingSoundInstance(SoundEvent sound, Entity entity) {
        super(sound, SoundSource.RECORDS, RandomSource.create());
        this.entity = entity;
        this.looping = false;
        this.volume = 1.0f;
        this.pitch = 1.0f;
        this.x = entity.getX();
        this.y = entity.getY();
        this.z = entity.getZ();
        this.attenuation = Attenuation.LINEAR;
    }

    @Override
    public void tick() {
        if (!this.entity.isAlive()) {
            this.stop();
            return;
        }
        this.x = this.entity.getX();
        this.y = this.entity.getY();
        this.z = this.entity.getZ();
    }
}
