package dev.thomasglasser.tommylib;

import net.neoforged.fml.common.Mod;

@Mod(TommyLib.MOD_ID)
public class TommyLibNeoForge {
    public TommyLibNeoForge() {
        TommyLib.init();
    }
}
