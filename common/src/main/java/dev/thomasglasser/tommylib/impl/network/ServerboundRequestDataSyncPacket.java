package dev.thomasglasser.tommylib.impl.network;

import dev.thomasglasser.tommylib.TommyLib;
import dev.thomasglasser.tommylib.api.network.CustomPacket;
import dev.thomasglasser.tommylib.api.network.PacketUtils;
import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class ServerboundRequestDataSyncPacket implements CustomPacket
{
	public static final ResourceLocation ID = TommyLib.modLoc("request_data_sync");

	private final int entity;

	public ServerboundRequestDataSyncPacket(FriendlyByteBuf buf)
	{
		this.entity = buf.readInt();
	}

	// ON SERVER
	@Override
	public void handle(Player player)
	{
		Entity target = player.level().getEntity(entity);
		TommyLibServices.NETWORK.sendToTrackingClients(ID, ClientboundSyncDataPacket::new, ClientboundSyncDataPacket.write(TommyLibServices.ENTITY.getPersistentData(target), entity), player.level().getServer(), target);
	}

	@Override
	public CustomPacket.Direction direction()
	{
		return CustomPacket.Direction.CLIENT_TO_SERVER;
	}

	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeInt(entity);
	}

	public static FriendlyByteBuf write(int entity)
	{
		FriendlyByteBuf buf = PacketUtils.create();
		buf.writeInt(entity);
		return buf;
	}

	@Override
	public ResourceLocation id()
	{
		return ID;
	}
}
