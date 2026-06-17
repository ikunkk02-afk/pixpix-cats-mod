package com.shouyun.pixpixcats.entity;

import com.shouyun.pixpixcats.sound.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ZhangXuefengEntity extends PathfinderMob {

    private int talkTimer;

    public ZhangXuefengEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.talkTimer = 200 + this.random.nextInt(200);
    }

    public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0)
                .add(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.level().isClientSide()) return InteractionResult.SUCCESS;

        ItemStack held = player.getItemInHand(hand);
        if (held.isEmpty()) return InteractionResult.FAIL;

        // 收走玩家手中一个物品
        held.shrink(1);

        // 掉食物
        ItemStack reward;
        if (this.random.nextBoolean()) {
            reward = new ItemStack(ModEntities.QIAOLEZI);
        } else {
            reward = new ItemStack(ModEntities.XUEBI);
        }
        ItemEntity drop = new ItemEntity(this.level(),
                this.getX(), this.getY() + 1.0, this.getZ(), reward);
        drop.setPickUpDelay(0);
        this.level().addFreshEntity(drop);

        return InteractionResult.SUCCESS;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) return;

        if (talkTimer > 0) {
            talkTimer--;
        } else {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    ModSounds.ZHANG_TALK, SoundSource.VOICE, 1.0f, 1.0f);
            talkTimer = 200 + this.random.nextInt(400);
        }
    }
}
