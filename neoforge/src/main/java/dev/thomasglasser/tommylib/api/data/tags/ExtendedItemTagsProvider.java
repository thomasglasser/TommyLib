package dev.thomasglasser.tommylib.api.data.tags;

import com.mojang.datafixers.util.Pair;
import dev.thomasglasser.tommylib.api.registration.RegistryObject;
import dev.thomasglasser.tommylib.api.world.level.block.LeavesSet;
import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public abstract class ExtendedItemTagsProvider extends ItemTagsProvider
{
    private static final Pair<ResourceLocation, ResourceLocation> SWORDS = Pair.of(neoforgeLoc("tools/swords"), cLoc("swords"));
    private static final Pair<ResourceLocation, ResourceLocation> HELMETS = Pair.of(neoforgeLoc("armors/helmets"), cLoc("helmets"));
    private static final Pair<ResourceLocation, ResourceLocation> CHESTPLATES = Pair.of(neoforgeLoc("armors/chestplates"), cLoc("chestplates"));
    private static final Pair<ResourceLocation, ResourceLocation> LEGGINGS = Pair.of(neoforgeLoc("armors/leggings"), cLoc("leggings"));
    private static final Pair<ResourceLocation, ResourceLocation> BOOTS = Pair.of(neoforgeLoc("armors/boots"), cLoc("boots"));

    public ExtendedItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future, CompletableFuture<TagLookup<Block>> blockTagsProvider, String modId, ExistingFileHelper existingFileHelper) {
        super(output, future, blockTagsProvider, modId, existingFileHelper);
    }

    protected static ResourceLocation neoforgeLoc(String path)
    {
        return new ResourceLocation("neoforge", path);
    }

    protected static ResourceLocation cLoc(String path)
    {
        return new ResourceLocation("c", path);
    }

    protected void tagPair(Pair<ResourceLocation, ResourceLocation> tags, Item... items)
    {
        tag(TagKey.create(Registries.ITEM, tags.getFirst())).add(items);
        tag(TagKey.create(Registries.ITEM, tags.getSecond())).add(items);
    }

    protected void woodSet(WoodSet set)
    {
        copy(set.logsBlockTag().get(), set.logsItemTag().get());

        tag(ItemTags.PLANKS)
                .add(set.planks().get().asItem());

        tag(ItemTags.LOGS_THAT_BURN)
                .addTag(set.logsItemTag().get());
    }

    protected void leavesSet(LeavesSet set)
    {
        tag(ItemTags.LEAVES)
                .add(set.leaves().get().asItem());

        tag(ItemTags.SAPLINGS)
                .add(set.sapling().get().asItem());
    }

    protected void armorSet(RegistryObject<? extends Item> helmet, RegistryObject<? extends Item> chestplate, RegistryObject<? extends Item> leggings, RegistryObject<? extends Item> boots)
    {
        tagPair(HELMETS, helmet.get());
        tagPair(CHESTPLATES, chestplate.get());
        tagPair(LEGGINGS, leggings.get());
        tagPair(BOOTS, boots.get());
    }

    @SafeVarargs
    protected final void swords(RegistryObject<? extends Item>... swords)
    {
        for (RegistryObject<? extends Item> sword : swords)
        {
            tagPair(SWORDS, sword.get());
        }
    }

    @SafeVarargs
    protected final void helmets(RegistryObject<? extends Item>... helmets)
    {
        for (RegistryObject<? extends Item> helmet : helmets)
        {
            tagPair(HELMETS, helmet.get());
        }
    }

    @SafeVarargs
    protected final void chestplates(RegistryObject<? extends Item>... chestplates)
    {
        for (RegistryObject<? extends Item> chestplate : chestplates)
        {
            tagPair(CHESTPLATES, chestplate.get());
        }
    }

    @SafeVarargs
    protected final void leggings(RegistryObject<? extends Item>... leggings)
    {
        for (RegistryObject<? extends Item> legging : leggings)
        {
            tagPair(LEGGINGS, legging.get());
        }
    }

    @SafeVarargs
    protected final void boots(RegistryObject<? extends Item>... boots)
    {
        for (RegistryObject<? extends Item> boot : boots)
        {
            tagPair(BOOTS, boot.get());
        }
    }

    @Override
    public String getName()
    {
        return modId + " Item Tags";
    }
}
