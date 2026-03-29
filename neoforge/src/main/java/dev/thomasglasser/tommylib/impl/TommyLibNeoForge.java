package dev.thomasglasser.tommylib.impl;

import dev.thomasglasser.tommylib.api.TommyLibConstants;
import net.neoforged.fml.common.Mod;

@Mod(TommyLibConstants.MOD_ID)
public class TommyLibNeoForge {
    public TommyLibNeoForge() {
        TommyLib.init();
    }
}
