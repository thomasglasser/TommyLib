package dev.thomasglasser.tommylib.api.data.advancements;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Consumer;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of {@link AdvancementProvider.AdvancementGenerator} that provides helpers.
 */
public abstract class ExtendedAdvancementGenerator implements AdvancementProvider.AdvancementGenerator {
    private final String modId;
    private final String category;
    private final LanguageProvider enUs;

    private Consumer<AdvancementHolder> saver;
    private ExistingFileHelper existingFileHelper;

    public ExtendedAdvancementGenerator(String modId, String category, LanguageProvider enUs) {
        this.modId = modId;
        this.category = category;
        this.enUs = enUs;
    }

    /**
     * Generates advancements, storing the saver and existing file helper for later use.
     * 
     * @param provider           The {@link HolderLookup.Provider} to use for registries
     * @param consumer           The consumer to save advancements to
     * @param existingFileHelper The existing file helper to use for saving advancements
     */
    @Override
    public final void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer, ExistingFileHelper existingFileHelper) {
        this.saver = consumer;
        this.existingFileHelper = existingFileHelper;

        generate(provider);
    }

    /**
     * Generates advancements.
     * 
     * @param provider The {@link HolderLookup.Provider} to use for registries
     */
    public abstract void generate(HolderLookup.Provider provider);

    /**
     * Creates a translation key for the title of an advancement.
     * 
     * @param category The category of the advancement.
     * @param path     The path of the advancement.
     * @return The translation key for the title of the advancement.
     */
    protected Component title(String category, String path) {
        return Component.translatable("advancement." + modId + "." + category + "." + path + ".title");
    }

    /**
     * Creates a translation key for the description of an advancement.
     * 
     * @param category The category of the advancement.
     * @param path     The path of the advancement.
     * @return The translation key for the description of the advancement.
     */
    protected Component desc(String category, String path) {
        return Component.translatable("advancement." + modId + "." + category + "." + path + ".desc");
    }

    /**
     * Creates a root advancement.
     * 
     * @param displayItem The item to display in the advancement tab.
     * @param id          The ID of the advancement.
     * @param background  The background texture of the advancement.
     * @param frameType   The type of frame to use for the advancement.
     * @param toast       Whether to display a toast notification for the advancement.
     * @param announce    Whether to announce the advancement in chat.
     * @param hidden      Whether the advancement is hidden.
     * @param rewards     The rewards for the advancement.
     * @param triggers    The triggers for the advancement.
     * @param title       The title of the advancement.
     * @param desc        The description of the advancement.
     * @return The root advancement.
     */
    protected AdvancementHolder root(ItemLike displayItem, String id, ResourceLocation background, AdvancementType frameType, boolean toast, boolean announce, boolean hidden, @Nullable AdvancementRewards rewards, AdvancementRequirements.Strategy strategy, Map<String, Criterion<?>> triggers, String title, String desc) {
        Component titleKey = title(category, id);
        Component descKey = desc(category, id);

        add(titleKey, title);
        add(descKey, desc);

        Advancement.Builder builder = Advancement.Builder.advancement()
                .display(displayItem, titleKey, descKey, background, frameType, toast, announce, hidden);

        return makeInternal(builder, id, rewards, strategy, triggers);
    }

    /**
     * Creates an advancement.
     * 
     * @param root        The parent advancement.
     * @param displayItem The item to display in the advancement tab.
     * @param id          The ID of the advancement.
     * @param frameType   The type of frame to use for the advancement.
     * @param toast       Whether to display a toast notification for the advancement.
     * @param announce    Whether to announce the advancement in chat.
     * @param hidden      Whether the advancement is hidden.
     * @param rewards     The rewards for the advancement.
     * @param triggers    The triggers for the advancement.
     * @param title       The title of the advancement.
     * @param desc        The description of the advancement.
     * @return The advancement.
     */
    protected AdvancementHolder create(AdvancementHolder root, ItemLike displayItem, String id, AdvancementType frameType, boolean toast, boolean announce, boolean hidden, @Nullable AdvancementRewards rewards, AdvancementRequirements.Strategy strategy, Map<String, Criterion<?>> triggers, String title, String desc) {
        Component titleKey = title(category, id);
        Component descKey = desc(category, id);

        add(titleKey, title);
        add(descKey, desc);

        Advancement.Builder builder = Advancement.Builder.advancement()
                .parent(root)
                .display(displayItem, titleKey, descKey, null, frameType, toast, announce, hidden);

        return makeInternal(builder, id, rewards, strategy, triggers);
    }

    /**
     * Creates an advancement.
     * 
     * @param root        The parent advancement.
     * @param displayItem The item to display in the advancement tab.
     * @param id          The ID of the advancement.
     * @param frameType   The type of frame to use for the advancement.
     * @param toast       Whether to display a toast notification for the advancement.
     * @param announce    Whether to announce the advancement in chat.
     * @param hidden      Whether the advancement is hidden.
     * @param rewards     The rewards for the advancement.
     * @param triggers    The triggers for the advancement.
     * @param title       The title of the advancement.
     * @param desc        The description of the advancement.
     * @return The advancement.
     */
    protected AdvancementHolder create(AdvancementHolder root, ItemStack displayItem, String id, AdvancementType frameType, boolean toast, boolean announce, boolean hidden, @Nullable AdvancementRewards rewards, AdvancementRequirements.Strategy strategy, Map<String, Criterion<?>> triggers, String title, String desc) {
        Component titleKey = title(category, id);
        Component descKey = desc(category, id);

        add(titleKey, title);
        add(descKey, desc);

        Advancement.Builder builder = Advancement.Builder.advancement()
                .parent(root)
                .display(displayItem, titleKey, descKey, null, frameType, toast, announce, hidden);

        return makeInternal(builder, id, rewards, strategy, triggers);
    }

    /**
     * Creates an advancement.
     * 
     * @param root        The parent advancement.
     * @param displayItem The item to display in the advancement tab.
     * @param id          The ID of the advancement.
     * @param frameType   The type of frame to use for the advancement.
     * @param toast       Whether to display a toast notification for the advancement.
     * @param announce    Whether to announce the advancement in chat.
     * @param hidden      Whether the advancement is hidden.
     * @param rewards     The rewards for the advancement.
     * @param triggers    The triggers for the advancement.
     * @param title       The title of the advancement.
     * @param desc        The description of the advancement.
     * @return The advancement.
     */
    protected AdvancementHolder create(ResourceLocation root, ItemLike displayItem, String id, AdvancementType frameType, boolean toast, boolean announce, boolean hidden, @Nullable AdvancementRewards rewards, AdvancementRequirements.Strategy strategy, Map<String, Criterion<?>> triggers, String title, String desc) {
        Component titleKey = title(category, id);
        Component descKey = desc(category, id);

        add(titleKey, title);
        add(descKey, desc);

        Advancement.Builder builder = Advancement.Builder.advancement()
                .parent(new AdvancementHolder(root, null))
                .display(displayItem, titleKey, descKey, null, frameType, toast, announce, hidden);

        return makeInternal(builder, id, rewards, strategy, triggers);
    }

    /**
     * Creates an advancement.
     * 
     * @param root        The parent advancement.
     * @param displayItem The item to display in the advancement tab.
     * @param id          The ID of the advancement.
     * @param frameType   The type of frame to use for the advancement.
     * @param toast       Whether to display a toast notification for the advancement.
     * @param announce    Whether to announce the advancement in chat.
     * @param hidden      Whether the advancement is hidden.
     * @param rewards     The rewards for the advancement.
     * @param triggers    The triggers for the advancement.
     * @param title       The title of the advancement.
     * @param desc        The description of the advancement.
     * @return The advancement.
     */
    protected AdvancementHolder create(ResourceLocation root, ItemStack displayItem, String id, AdvancementType frameType, boolean toast, boolean announce, boolean hidden, @Nullable AdvancementRewards rewards, AdvancementRequirements.Strategy strategy, Map<String, Criterion<?>> triggers, String title, String desc) {
        Component titleKey = title(category, id);
        Component descKey = desc(category, id);

        add(titleKey, title);
        add(descKey, desc);

        Advancement.Builder builder = Advancement.Builder.advancement()
                .parent(new AdvancementHolder(root, null))
                .display(displayItem, titleKey, descKey, null, frameType, toast, announce, hidden);

        return makeInternal(builder, id, rewards, strategy, triggers);
    }

    /**
     * Adds rewards and criteria to an advancement.
     * 
     * @param builder  The builder for the advancement.
     * @param id       The ID of the advancement.
     * @param rewards  The rewards for the advancement.
     * @param triggers The triggers for the advancement.
     * @return The advancement.
     */
    private AdvancementHolder makeInternal(Advancement.Builder builder, String id, @Nullable AdvancementRewards rewards, AdvancementRequirements.Strategy strategy, Map<String, Criterion<?>> triggers) {
        if (rewards != null)
            builder.rewards(rewards);

        SortedMap<String, Criterion<?>> sm = new TreeMap<>(triggers);
        sm.forEach(builder::addCriterion);

        return builder.requirements(strategy).save(saver, modLoc(category + "/" + id), existingFileHelper);
    }

    /**
     * Adds a translation key to the language provider.
     * 
     * @param component The translation key.
     * @param name      The name of the translation.
     */
    protected void add(Component component, String name) {
        enUs.add(((TranslatableContents) component.getContents()).getKey(), name);
    }

    /**
     * Creates a resource location with the mod ID as the namespace.
     * 
     * @param path The path of the resource location.
     * @return The resource location.
     */
    protected ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(modId, path);
    }
}
