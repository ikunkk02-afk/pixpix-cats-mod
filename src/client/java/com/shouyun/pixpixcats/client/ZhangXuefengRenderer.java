package com.shouyun.pixpixcats.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.shouyun.pixpixcats.PixpixCatsMod;
import com.shouyun.pixpixcats.entity.ZhangXuefengEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class ZhangXuefengRenderer extends EntityRenderer<ZhangXuefengEntity> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            PixpixCatsMod.MOD_ID, "textures/entity/zhangxuefeng.png"
    );

    public ZhangXuefengRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(ZhangXuefengEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(ZhangXuefengEntity entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0, 1.5, 0.0);
        poseStack.mulPose(Minecraft.getInstance().gameRenderer.getMainCamera().rotation());
        poseStack.scale(-2.0f, 2.0f, 2.0f);

        Matrix4f matrix = poseStack.last().pose();
        VertexConsumer vc = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        int light = 0xF000F0;

        vc.addVertex(matrix, -0.5f, -0.9f, 0).setColor(0xFFFFFFFF).setUv(0, 1)
                .setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 0, 1);
        vc.addVertex(matrix,  0.5f, -0.9f, 0).setColor(0xFFFFFFFF).setUv(1, 1)
                .setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 0, 1);
        vc.addVertex(matrix,  0.5f,  0.9f, 0).setColor(0xFFFFFFFF).setUv(1, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 0, 1);
        vc.addVertex(matrix, -0.5f,  0.9f, 0).setColor(0xFFFFFFFF).setUv(0, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 0, 1);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }
}
