package dev.thomasglasser.tommylib.api.network;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;

public class PacketUtils
{
    public static FriendlyByteBuf empty() {
        return new FriendlyByteBuf(Unpooled.EMPTY_BUFFER);
    }

    public static FriendlyByteBuf create() {
        return new FriendlyByteBuf(Unpooled.buffer());
    }
}
