package dev.thomasglasser.tommylib.impl.network;

import dev.thomasglasser.tommylib.TommyLib;
import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public record ServerboundRequestDataSyncPacketPayload(int entity) implements ExtendedPacketPayload
{
	public static final Type<ServerboundRequestDataSyncPacketPayload> TYPE = new Type<>(TommyLib.modLoc("request_data_sync"));
	public static final StreamCodec<ByteBuf, ServerboundRequestDataSyncPacketPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.INT, ServerboundRequestDataSyncPacketPayload::entity,
			ServerboundRequestDataSyncPacketPayload::new
	);

	// ON SERVER
	@Override
	public void handle(Player player)
	{
		Entity target = player.level().getEntity(entity);
		TommyLibServices.NETWORK.sendToTrackingClients(new ClientboundSyncDataPacketPayload(TommyLibServices.ENTITY.getPersistentData(target), entity), player.level().getServer(), target);
	}

	private void write(ByteBuf buffer)
	{
		buffer.writeInt(entity);
	}

	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return TYPE;
	}
}
