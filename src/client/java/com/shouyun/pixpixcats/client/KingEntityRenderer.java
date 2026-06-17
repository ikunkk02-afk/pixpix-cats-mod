package com.shouyun.pixpixcats.client;

import com.shouyun.pixpixcats.PixpixCatsMod;
import com.shouyun.pixpixcats.entity.KingEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public class KingEntityRenderer extends HumanoidMobRenderer<KingEntity, HumanoidModel<KingEntity>> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            PixpixCatsMod.MOD_ID, "textures/entity/king.png"
    );

    public KingEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(KingEntity entity) {
        return TEXTURE;
    }
}
