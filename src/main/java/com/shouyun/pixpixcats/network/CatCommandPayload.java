package com.shouyun.pixpixcats.network;

import com.shouyun.pixpixcats.PixpixCatsMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CatCommandPayload(int command) implements CustomPacketPayload {
    // 0 = 音频一 (H键) — 猫远离
    // 1 = 音频二 (J键) — 猫奔向
    // 2 = 音频三 (Z键) — 猫跳舞

    public static final CustomPacketPayload.Type<CatCommandPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PixpixCatsMod.MOD_ID, "cat_command"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CatCommandPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,
                    CatCommandPayload::command,
                    CatCommandPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
