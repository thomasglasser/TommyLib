package dev.thomasglasser.tommylib.api.data.advancements;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceLinkedOpenHashMap;
import java.util.SortedMap;
import java.util.function.Consumer;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of {@link AdvancementProvider.AdvancementGenerator} that provides a {@link Builder}.
 */
public abstract class ExtendedAdvancementGenerator implements AdvancementProvider.AdvancementGenerator {
    private final String modId;
    private final String category;
    private final LanguageProvider lang;

    private Consumer<AdvancementHolder> saver;
    private ExistingFileHelper existingFileHelper;

    protected ExtendedAdvancementGenerator(String modId, String category, LanguageProvider lang) {
        this.modId = modId;
        this.category = category;
        this.lang = lang;
    }

    /**
     * Generates advancements, storing the saver and existing file helper for use.
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
    protected abstract void generate(HolderLookup.Provider provider);

    /**
     * Creates a new {@link Builder} with the given parameters.
     * 
     * @param id          The ID of the advancement
     * @param displayItem The item to display in the advancement
     * @param title       The title of the advancement in the provided language file
     * @param desc        The description of the advancement in the provided language file
     * @return The {@link Builder} with the given parameters
     */
    protected Builder builder(String id, ItemStack displayItem, String title, String desc) {
        return new Builder(id, displayItem, title, desc);
    }

    /**
     * Creates a resource location with the mod ID as the namespace.
     * 
     * @param path The path of the resource location
     * @return The resource location
     */
    protected ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(modId, path);
    }

    /**
     * Creates a resource location with "minecraft" as the namespace.
     * 
     * @param path The path of the resource location
     * @return The resource location
     */
    protected ResourceLocation mcLoc(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }

    protected class Builder {
        private final String id;
        private final ItemStack displayItem;
        private final String title;
        private final String desc;
        private final SortedMap<String, Criterion<?>> triggers;
        @Nullable
        private AdvancementHolder parent;
        @Nullable
        private ResourceLocation background;
        private AdvancementType frameType = AdvancementType.TASK;
        private boolean toast = true;
        private boolean announce = true;
        private boolean hidden = false;
        @Nullable
        private AdvancementRewards rewards;
        private AdvancementRequirements.Strategy strategy = AdvancementRequirements.Strategy.AND;

        public Builder(String id, ItemStack displayItem, String title, String desc) {
            this.id = id;
            this.displayItem = displayItem;
            this.title = title;
            this.desc = desc;
            this.triggers = new Reference2ReferenceLinkedOpenHashMap<>();
        }

        public Builder trigger(String key, Criterion<?> criterion) {
            triggers.put(key, criterion);
            return this;
        }

        public Builder parent(AdvancementHolder parent) {
            this.parent = parent;
            return this;
        }

        public Builder parent(ResourceLocation parent) {
            return parent(new AdvancementHolder(parent, null));
        }

        public Builder background(ResourceLocation background) {
            this.background = background;
            return this;
        }

        public Builder frameType(AdvancementType frameType) {
            this.frameType = frameType;
            return this;
        }

        public Builder toast(boolean toast) {
            this.toast = toast;
            return this;
        }

        public Builder announce(boolean announce) {
            this.announce = announce;
            return this;
        }

        public Builder hidden(boolean hidden) {
            this.hidden = hidden;
            return this;
        }

        public Builder rewards(AdvancementRewards rewards) {
            this.rewards = rewards;
            return this;
        }

        public Builder strategy(AdvancementRequirements.Strategy strategy) {
            this.strategy = strategy;
            return this;
        }

        public AdvancementHolder build() {
            String title = "advancement." + modId + "." + category + "." + id + ".title";
            String desc = "advancement." + modId + "." + category + "." + id + ".desc";
            lang.add(title, this.title);
            lang.add(desc, this.desc);
            Advancement.Builder builder = Advancement.Builder.advancement();
            if (parent != null)
                builder.parent(parent);
            builder.display(displayItem, Component.translatable(title), Component.translatable(desc), background, frameType, toast, announce, hidden);
            if (rewards != null)
                builder.rewards(rewards);
            triggers.forEach(builder::addCriterion);
            return builder.requirements(strategy).save(saver, modLoc(category + "/" + id), existingFileHelper);
        }
    }
}
