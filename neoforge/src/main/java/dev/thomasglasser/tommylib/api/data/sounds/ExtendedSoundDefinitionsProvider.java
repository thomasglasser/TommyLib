package dev.thomasglasser.tommylib.api.data.sounds;

import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import java.util.ArrayList;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

/**
 * Extension of {@link SoundDefinitionsProvider} that provides functionality for mod holders.
 */
public abstract class ExtendedSoundDefinitionsProvider extends SoundDefinitionsProvider {
    /**
     * Creates a new instance of this data provider.
     *
     * @param output The {@linkplain PackOutput} instance provided by the data generator.
     * @param modId  The mod ID of the current mod.
     */
    protected ExtendedSoundDefinitionsProvider(PackOutput output, String modId) {
        super(output, modId);
    }

    /**
     * Defines a {@link SoundDefinition} with a subtitle.
     * 
     * @param subtitle The subtitle to use.
     * @param sounds   The sounds to use.
     * @return The defined {@link SoundDefinition}.
     */
    private SoundDefinition define(String subtitle, SoundDefinition.Sound... sounds) {
        return SoundDefinition.definition().with(sounds).subtitle("subtitles." + subtitle);
    }

    /**
     * Adds a sound to the provider.
     * 
     * @param sound The sound to add.
     */
    protected void add(DeferredHolder<SoundEvent, ?> sound) {
        add(sound.get(), define(sound.get().location().getPath(), sound(sound.get().location())));
    }

    /**
     * Adds a sound to the provider with multiple variants.
     * 
     * @param sound    The sound to add.
     * @param variants The number of variants to add.
     */
    protected void add(DeferredHolder<SoundEvent, ?> sound, int variants) {
        add(sound.get(), defineVariants(sound.get().location().getPath(), sound.get().location(), variants));
    }

    /**
     * Adds a music sound to the provider.
     *
     * @param sound The sound to add.
     */
    protected void addMusic(DeferredHolder<SoundEvent, ?> sound) {
        add(sound.get(), SoundDefinition.definition().with(sound(sound.get().location()).stream()));
    }

    /**
     * Defines a {@link SoundDefinition} with multiple variants.
     * 
     * @param subtitle The subtitle to use.
     * @param sound    The sound to use.
     * @param variants The number of variants to add.
     * @return The defined {@link SoundDefinition}.
     */
    private SoundDefinition defineVariants(String subtitle, ResourceLocation sound, int variants) {
        if (variants == 1) {
            return define(subtitle, sound(sound));
        }
        ArrayList<SoundDefinition.Sound> sounds = new ArrayList<>();
        for (int i = 1; i < variants + 1; i++) {
            sounds.add(sound(ResourceLocation.fromNamespaceAndPath(sound.getNamespace(), sound.getPath() + i)));
        }
        return define(subtitle, sounds.toArray(new SoundDefinition.Sound[] {}));
    }

    /**
     * Gets a sound from a {@link ResourceLocation}.
     * 
     * @param location The location of the sound.
     * @return The sound.
     */
    protected static SoundDefinition.Sound sound(ResourceLocation location) {
        if (location.getPath().contains(".")) {
            return sound(ResourceLocation.fromNamespaceAndPath(location.getNamespace(), location.getPath().replace('.', '/')));
        }
        return SoundDefinitionsProvider.sound(location);
    }

    /**
     * Adds sounds for a {@link WoodSet}.
     *
     * @param set                      The set to add sounds for.
     * @param breakVariants            The number of break variants.
     * @param stepVariants             The number of step variants.
     * @param doorToggleVariants       The number of door toggle variants.
     * @param fenceGateToggleVariants  The number of fence gate toggle variants.
     * @param hangingSignBreakVariants The number of hanging sign break variants.
     * @param hangingSignStepVariants  The number of hanging sign step variants.
     * @param trapdoorToggleVariants   The number of trapdoor toggle variants.
     */
    protected void add(WoodSet set, int breakVariants, int stepVariants, int doorToggleVariants, int fenceGateToggleVariants, int hangingSignBreakVariants, int hangingSignStepVariants, int trapdoorToggleVariants) {
        ResourceLocation woodId = set.id().withPrefix("block.").withSuffix("_wood");
        ResourceLocation breakLoc = woodId.withSuffix(".break");
        ResourceLocation stepLoc = woodId.withSuffix(".step");
        ResourceLocation buttonClickLoc = woodId.withSuffix("_button.click");
        ResourceLocation doorLoc = woodId.withSuffix("_door");
        ResourceLocation doorToggleLoc = doorLoc.withSuffix(".toggle");
        ResourceLocation fenceGateLoc = woodId.withSuffix("_fence_gate");
        ResourceLocation fenceGateToggleLoc = fenceGateLoc.withSuffix(".toggle");
        ResourceLocation hangingSignLoc = woodId.withSuffix("_hanging_sign");
        ResourceLocation hangingSignBreakLoc = hangingSignLoc.withSuffix(".break");
        ResourceLocation hangingSignStepLoc = hangingSignLoc.withSuffix(".step");
        ResourceLocation trapdoorLoc = woodId.withSuffix("_trapdoor");
        ResourceLocation trapdoorToggleLoc = trapdoorLoc.withSuffix(".toggle");
        add(breakLoc, defineVariants("subtitles.block.generic.break", breakLoc, breakVariants));
        add(woodId.withSuffix(".fall"), defineVariants(null, stepLoc, stepVariants));
        add(woodId.withSuffix(".hit"), defineVariants("subtitles.block.generic.hit", stepLoc, stepVariants));
        add(woodId.withSuffix(".place"), defineVariants("subtitles.block.generic.place", breakLoc, breakVariants));
        add(stepLoc, defineVariants("subtitles.block.generic.footsteps", stepLoc, stepVariants));
        add(buttonClickLoc.withSuffix("_off"), define("subtitles.block.button.click", sound(buttonClickLoc).pitch(0.5).volume(0.4)));
        add(buttonClickLoc.withSuffix("_on"), define("subtitles.block.button.click", sound(buttonClickLoc).pitch(0.6).volume(0.4)));
        add(doorLoc.withSuffix(".close"), defineVariants("subtitles.block.door.toggle", doorToggleLoc, doorToggleVariants));
        add(doorLoc.withSuffix(".open"), defineVariants("subtitles.block.door.toggle", doorToggleLoc, doorToggleVariants));
        add(fenceGateLoc.withSuffix(".close"), defineVariants("subtitles.block.fence_gate.toggle", fenceGateToggleLoc, fenceGateToggleVariants));
        add(fenceGateLoc.withSuffix(".open"), defineVariants("subtitles.block.fence_gate.toggle", fenceGateToggleLoc, fenceGateToggleVariants));
        add(hangingSignBreakLoc, defineVariants("subtitles.block.generic.break", hangingSignBreakLoc, hangingSignBreakVariants));
        add(hangingSignLoc.withSuffix(".fall"), defineVariants(null, hangingSignStepLoc, hangingSignStepVariants));
        add(hangingSignLoc.withSuffix(".hit"), defineVariants("subtitles.block.generic.hit", hangingSignStepLoc, hangingSignStepVariants));
        add(hangingSignLoc.withSuffix(".place"), defineVariants("subtitles.block.generic.place", hangingSignBreakLoc, hangingSignBreakVariants));
        add(hangingSignStepLoc, defineVariants("subtitles.block.generic.footsteps", hangingSignStepLoc, hangingSignStepVariants));
        add(woodId.withSuffix("_pressure_plate.click_off"), define("subtitles.block.pressure_plate.click", sound("random/click").pitch(0.7).volume(0.3)));
        add(woodId.withSuffix("_pressure_plate.click_on"), define("subtitles.block.pressure_plate.click", sound("random/click").pitch(0.8).volume(0.3)));
        add(trapdoorLoc.withSuffix(".close"), defineVariants("subtitles.block.trapdoor.toggle", trapdoorToggleLoc, trapdoorToggleVariants));
        add(trapdoorLoc.withSuffix(".open"), defineVariants("subtitles.block.trapdoor.toggle", trapdoorToggleLoc, trapdoorToggleVariants));
    }
}
