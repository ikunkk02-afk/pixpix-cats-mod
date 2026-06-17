package com.shouyun.pixpixcats.entity;

import com.shouyun.pixpixcats.PixpixCatsMod;
import com.shouyun.pixpixcats.sound.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class KingSummonProjectile extends ThrowableItemProjectile {

    public KingSummonProjectile(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    public KingSummonProjectile(Level level, LivingEntity owner) {
        super(ModEntities.KING_SUMMON_PROJECTILE, owner, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModEntities.KING_SUMMON_ITEM;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.level().addParticle(ParticleTypes.PORTAL,
                    this.getX(), this.getY(), this.getZ(),
                    0, 0, 0);
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) this.level();

            // 炫酷粒子爆发
            for (int i = 0; i < 40; i++) {
                double dx = (this.random.nextDouble() - 0.5) * 2;
                double dy = this.random.nextDouble() * 2;
                double dz = (this.random.nextDouble() - 0.5) * 2;
                serverLevel.sendParticles(ParticleTypes.FLASH,
                        this.getX(), this.getY() + 1, this.getZ(),
                        1, dx, dy, dz, 0.1);
            }
            for (int i = 0; i < 15; i++) {
                double dx = (this.random.nextDouble() - 0.5) * 3;
                double dy = this.random.nextDouble() * 2;
                double dz = (this.random.nextDouble() - 0.5) * 3;
                serverLevel.sendParticles(ParticleTypes.FIREWORK,
                        this.getX(), this.getY() + 1, this.getZ(),
                        1, dx, dy, dz, 0.05);
            }
            for (int i = 0; i < 20; i++) {
                double dx = (this.random.nextDouble() - 0.5) * 2;
                double dy = this.random.nextDouble() * 2;
                double dz = (this.random.nextDouble() - 0.5) * 2;
                serverLevel.sendParticles(ParticleTypes.ENCHANT,
                        this.getX(), this.getY() + 1, this.getZ(),
                        1, dx, dy, dz, 0.5);
            }

            // 召唤 King
            KingEntity king = ModEntities.KING.create(this.level());
            if (king != null) {
                king.setPos(this.getX(), this.getY(), this.getZ());
                this.level().addFreshEntity(king);

                // 播放 BGM，围绕 King 播放
                this.level().playSound(null, king.getX(), king.getY(), king.getZ(),
                        ModSounds.BABY_DONT_CRY, SoundSource.RECORDS, 1.0f, 1.0f);
            }

            PixpixCatsMod.LOGGER.info("King summoned at {}, {}, {}", this.getX(), this.getY(), this.getZ());
            this.discard();
        }
    }
}
