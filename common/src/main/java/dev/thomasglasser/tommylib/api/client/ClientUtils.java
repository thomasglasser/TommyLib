package dev.thomasglasser.tommylib.api.client;

import dev.thomasglasser.tommylib.api.world.item.ItemUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ClientUtils
{
    private static final ArrayList<KeyMapping> KEY_MAPPINGS = new ArrayList<>();

    public static AbstractClientPlayer getClientPlayerByUUID(UUID uuid) {
        return (AbstractClientPlayer) Minecraft.getInstance().level.getPlayerByUUID(uuid);
    }

    public static void setScreen(Screen screen)
    {
        Minecraft.getInstance().setScreen(screen);
    }

    public static Entity getEntityById(int id)
    {
        return Minecraft.getInstance().level == null ? null : Minecraft.getInstance().level.getEntity(id);
    }

    public static Player getMainClientPlayer()
    {
        return Minecraft.getInstance().player;
    }

    public static Level getLevel()
    {
        return Minecraft.getInstance().level;
    }

    public static Minecraft getMinecraft()
    {
        return Minecraft.getInstance();
    }

    public static List<ItemStack> getItemsForTab(ResourceKey<CreativeModeTab> tab)
    {
        List<ItemStack> items = new ArrayList<>();

        ItemUtils.getItemTabs().forEach((itemTab, itemLikes) -> {
            if (tab == itemTab)
            {
                itemLikes.forEach((itemLike) -> items.add(Objects.requireNonNull(BuiltInRegistries.ITEM.get(itemLike)).getDefaultInstance()));
            }
        });

        return items;
    }

    public static ArrayList<KeyMapping> getKeyMappings()
    {
        return KEY_MAPPINGS;
    }

    public static KeyMapping registerKeyMapping(ResourceLocation name, int key, String category)
    {
        KeyMapping mapping = new KeyMapping(name.toLanguageKey("key"), key, category);
        KEY_MAPPINGS.add(mapping);
        return mapping;
    }
}
