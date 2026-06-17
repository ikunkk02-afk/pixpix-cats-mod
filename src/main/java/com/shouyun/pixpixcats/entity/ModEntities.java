package com.shouyun.pixpixcats.entity;

import com.shouyun.pixpixcats.PixpixCatsMod;
import com.shouyun.pixpixcats.item.KingSummonItem;
import com.shouyun.pixpixcats.item.SpecialFoodItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

public class ModEntities {

    // ====== King 实体 ======
    public static final EntityType<KingEntity> KING = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(PixpixCatsMod.MOD_ID, "king"),
            EntityType.Builder.of(KingEntity::new, MobCategory.MISC)
                    .sized(0.6f, 1.8f)
                    .clientTrackingRange(10)
                    .build("king")
    );

    public static final Item KING_SPAWN_EGG = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(PixpixCatsMod.MOD_ID, "king_spawn_egg"),
            new SpawnEggItem(KING, 0x1a1a1a, 0xffd700, new Item.Properties())
    );

    // ====== King 投掷物 ======
    public static final EntityType<KingSummonProjectile> KING_SUMMON_PROJECTILE = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(PixpixCatsMod.MOD_ID, "king_summon_projectile"),
            EntityType.Builder.<KingSummonProjectile>of(KingSummonProjectile::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("king_summon_projectile")
    );

    public static final Item KING_SUMMON_ITEM = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(PixpixCatsMod.MOD_ID, "king_summon_item"),
            new KingSummonItem(new Item.Properties())
    );

    // ====== 张雪峰实体 ======
    public static final EntityType<ZhangXuefengEntity> ZHANG_XUEFENG = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(PixpixCatsMod.MOD_ID, "zhang_xuefeng"),
            EntityType.Builder.of(ZhangXuefengEntity::new, MobCategory.CREATURE)
                    .sized(0.6f, 1.8f)
                    .clientTrackingRange(10)
                    .build("zhang_xuefeng")
    );

    public static final Item ZHANG_XUEFENG_SPAWN_EGG = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(PixpixCatsMod.MOD_ID, "zhang_xuefeng_spawn_egg"),
            new SpawnEggItem(ZHANG_XUEFENG, 0x333333, 0x00ff00, new Item.Properties())
    );

    // ====== 食物 ======
    public static final Item QIAOLEZI = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(PixpixCatsMod.MOD_ID, "qiaolezi"),
            new SpecialFoodItem(new Item.Properties().food(new net.minecraft.world.food.FoodProperties.Builder()
                    .nutrition(4).saturationModifier(0.3f).alwaysEdible().build()))
    );

    public static final Item XUEBI = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(PixpixCatsMod.MOD_ID, "xuebi"),
            new SpecialFoodItem(new Item.Properties().food(new net.minecraft.world.food.FoodProperties.Builder()
                    .nutrition(4).saturationModifier(0.3f).alwaysEdible().build()))
    );

    public static void init() {
        FabricDefaultAttributeRegistry.register(KING, KingEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ZHANG_XUEFENG, ZhangXuefengEntity.createAttributes());

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register(entries -> {
            entries.accept(KING_SPAWN_EGG);
            entries.accept(ZHANG_XUEFENG_SPAWN_EGG);
        });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
            entries.accept(KING_SUMMON_ITEM);
        });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(entries -> {
            entries.accept(QIAOLEZI);
            entries.accept(XUEBI);
        });
    }
}
