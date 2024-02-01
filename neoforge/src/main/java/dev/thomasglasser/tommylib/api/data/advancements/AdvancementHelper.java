package dev.thomasglasser.tommylib.api.data.advancements;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.Criterion;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.LanguageProvider;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Consumer;

public class AdvancementHelper
{
    private final Consumer<AdvancementHolder> saver;
    private final ExistingFileHelper existingFileHelper;
    private final LanguageProvider enUs;
    String modId;
    private final String category;

    public AdvancementHelper(Consumer<AdvancementHolder> saver, String modId, ExistingFileHelper existingFileHelper, LanguageProvider enUs, String category)
    {
        this.saver = saver;
        this.modId = modId;
        this.existingFileHelper = existingFileHelper;
        this.enUs = enUs;
        this.category = category;
    }

    public Component title(String category, String path)
    {
        return Component.translatable("advancement." + modId + "." + category + "." + path + ".title");
    }

    public Component desc(String category, String path)
    {
        return Component.translatable("advancement." + modId + "." + category + "." + path + ".desc");
    }

    public AdvancementHolder root(ItemLike displayItem, String id, ResourceLocation background, AdvancementType frameType, boolean toast, boolean announce, boolean hidden, @Nullable AdvancementRewards rewards, Map<String, Criterion<?>> triggers, String title, String desc)
    {
        Component titleKey = title(category, id);
        Component descKey = desc(category, id);

        add(titleKey, title);
        add(descKey, desc);

        Advancement.Builder builder = Advancement.Builder.advancement()
                .display(displayItem, titleKey, descKey, background, frameType, toast, announce, hidden);

        return makeInternal(builder, id, rewards, triggers);
    }

    public AdvancementHolder make(AdvancementHolder root, ItemLike displayItem, String id, AdvancementType frameType, boolean toast, boolean announce, boolean hidden, @Nullable AdvancementRewards rewards, Map<String, Criterion<?>> triggers, String title, String desc)
    {
        Component titleKey = title(category, id);
        Component descKey = desc(category, id);

        add(titleKey, title);
        add(descKey, desc);

        Advancement.Builder builder = Advancement.Builder.advancement()
                .parent(root)
                .display(displayItem, titleKey, descKey, null, frameType, toast, announce, hidden);

        return makeInternal(builder, id, rewards, triggers);
    }

    public AdvancementHolder make(AdvancementHolder root, ItemStack displayItem, String id, AdvancementType frameType, boolean toast, boolean announce, boolean hidden, @Nullable AdvancementRewards rewards, Map<String, Criterion<?>> triggers, String title, String desc)
    {
        Component titleKey = title(category, id);
        Component descKey = desc(category, id);

        add(titleKey, title);
        add(descKey, desc);

        Advancement.Builder builder = Advancement.Builder.advancement()
                .parent(root)
                .display(displayItem, titleKey, descKey, null, frameType, toast, announce, hidden);

        return makeInternal(builder, id, rewards, triggers);
    }

    public AdvancementHolder make(ResourceLocation root, ItemLike displayItem, String id, AdvancementType frameType, boolean toast, boolean announce, boolean hidden, @Nullable AdvancementRewards rewards, Map<String, Criterion<?>> triggers, String title, String desc)
    {
        Component titleKey = title(category, id);
        Component descKey = desc(category, id);

        add(titleKey, title);
        add(descKey, desc);

        Advancement.Builder builder = Advancement.Builder.advancement()
                .parent(new AdvancementHolder(root, null))
                .display(displayItem, titleKey, descKey, null, frameType, toast, announce, hidden);

        return makeInternal(builder, id, rewards, triggers);
    }

    public AdvancementHolder make(ResourceLocation root, ItemStack displayItem, String id, AdvancementType frameType, boolean toast, boolean announce, boolean hidden, @Nullable AdvancementRewards rewards, Map<String, Criterion<?>> triggers, String title, String desc)
    {
        Component titleKey = title(category, id);
        Component descKey = desc(category, id);

        add(titleKey, title);
        add(descKey, desc);

        Advancement.Builder builder = Advancement.Builder.advancement()
                .parent(new AdvancementHolder(root, null))
                .display(displayItem, titleKey, descKey, null, frameType, toast, announce, hidden);

        return makeInternal(builder, id, rewards, triggers);
    }

    private AdvancementHolder makeInternal(Advancement.Builder builder, String id, @Nullable AdvancementRewards rewards, Map<String, Criterion<?>> triggers)
    {
        if (rewards != null)
            builder.rewards(rewards);

        SortedMap<String, Criterion<?>> sm = new TreeMap<>(triggers);
        sm.forEach(builder::addCriterion);

        return builder.save(saver, modLoc(category + "/" + id), existingFileHelper);
    }

    public void add(Component component, String name)
    {
        enUs.add(((TranslatableContents)component.getContents()).getKey(), name);
    }

    private ResourceLocation modLoc(String path)
    {
        return new ResourceLocation(modId, path);
    }
}
