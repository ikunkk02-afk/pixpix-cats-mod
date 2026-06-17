package com.shouyun.pixpixcats.item;

import com.shouyun.pixpixcats.entity.KingSummonProjectile;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class KingSummonItem extends Item {

    public KingSummonItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // 粒子效果 - 投掷时
        if (level.isClientSide()) {
            level.addParticle(ParticleTypes.FLASH,
                    player.getX(), player.getEyeY(), player.getZ(),
                    0, 0, 0);
            level.addParticle(ParticleTypes.ENCHANT,
                    player.getX(), player.getEyeY(), player.getZ(),
                    0, 0, 0);
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.5f, 0.4f / (level.random.nextFloat() * 0.4f + 0.8f));

        if (!level.isClientSide()) {
            KingSummonProjectile projectile = new KingSummonProjectile(level, player);
            projectile.setItem(stack);
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.5f, 1.0f);
            level.addFreshEntity(projectile);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        stack.consume(1, player);

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
