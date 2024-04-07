package dev.thomasglasser.tommylib.impl.network;

import dev.thomasglasser.tommylib.TommyLib;
import dev.thomasglasser.tommylib.api.network.CustomPacket;
import dev.thomasglasser.tommylib.api.network.PacketUtils;
import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class ClientboundSyncDataPacket implements CustomPacket
{
	public static final ResourceLocation ID = TommyLib.modLoc("sync_data");

	private final CompoundTag data;
	private final int entity;

	public ClientboundSyncDataPacket(FriendlyByteBuf buffer)
	{
		this.data = buffer.readWithCodecTrusted(NbtOps.INSTANCE, CompoundTag.CODEC);
		this.entity = buffer.readInt();
	}

	// ON CLIENT
	@Override
	public void handle(Player player)
	{
		Entity target = player.level().getEntity(entity);
		TommyLibServices.ENTITY.setPersistentData(target, data, false);
	}

	@Override
	public Direction direction()
	{
		return Direction.SERVER_TO_CLIENT;
	}

	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeWithCodec(NbtOps.INSTANCE, CompoundTag.CODEC, data);
		buffer.writeInt(entity);
	}

	public static FriendlyByteBuf write(CompoundTag data, int entity)
	{
		FriendlyByteBuf buffer = PacketUtils.create();
		buffer.writeWithCodec(NbtOps.INSTANCE, CompoundTag.CODEC, data);
		buffer.writeInt(entity);
		return buffer;
	}

	public static FriendlyByteBuf write(CompoundTag data, Entity entity)
	{
		return write(data, entity.getId());
	}

	@Override
	public ResourceLocation id()
	{
		return ID;
	}
}
