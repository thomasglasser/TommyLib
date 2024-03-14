package dev.thomasglasser.tommylib.impl.mixin.minecraft.client;

import dev.thomasglasser.tommylib.api.client.ClientUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(Options.class)
public abstract class OptionsMixin
{
    @Shadow @Final @Mutable
    public KeyMapping[] keyMappings;

    @Inject(method = "<init>", at = @At(value = "TAIL", shift = At.Shift.BEFORE))
    private void Options(Minecraft minecraft, File file, CallbackInfo ci)
    {
        keyMappings = ArrayUtils.addAll(keyMappings, ClientUtils.getKeyMappings().toArray(new KeyMapping[0]));
    }
}
