package dev.thomasglasser.tommylib.api.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public abstract class ExtendedKeyMapping extends KeyMapping {
    public ExtendedKeyMapping(String name, InputConstants.Type type, int keyCode, String category) {
        super(name, type, keyCode, category);
    }

    public ExtendedKeyMapping(String name, int keyCode, String category) {
        this(name, InputConstants.Type.KEYSYM, keyCode, category);
    }

    public abstract void onClick();
}
