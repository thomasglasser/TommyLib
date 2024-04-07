package dev.thomasglasser.tommylib.impl.platform;

import dev.thomasglasser.tommylib.TommyLib;
import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import dev.thomasglasser.tommylib.impl.network.ClientboundSyncDataPacket;
import dev.thomasglasser.tommylib.impl.platform.services.EntityHelper;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

@SuppressWarnings("UnstableApiUsage")
public class FabricEntityHelper implements EntityHelper
{
	private static final AttachmentType<CompoundTag> DATA = AttachmentRegistry.<CompoundTag>builder().initializer(CompoundTag::new).persistent(CompoundTag.CODEC).buildAndRegister(TommyLib.modLoc("data"));

	@Override
	public CompoundTag getPersistentData(Entity entity)
	{
		return entity.getAttachedOrCreate(DATA);
	}

	@Override
	public void setPersistentData(Entity entity, CompoundTag data, boolean syncToClient)
	{
		entity.setAttached(DATA, data);
		if (syncToClient) TommyLibServices.NETWORK.sendToAllClients(ClientboundSyncDataPacket.ID, ClientboundSyncDataPacket::new, ClientboundSyncDataPacket.write(data, entity), entity.level().getServer());
	}
}
