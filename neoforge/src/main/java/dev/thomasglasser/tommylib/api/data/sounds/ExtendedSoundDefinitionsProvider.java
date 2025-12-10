package dev.thomasglasser.tommylib.api.data.sounds;

import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import java.util.ArrayList;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
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
    private SoundDefinition defineVariants(String subtitle, Identifier sound, int variants) {
        if (variants == 1) {
            return define(subtitle, sound(sound));
        }
        ArrayList<SoundDefinition.Sound> sounds = new ArrayList<>();
        for (int i = 1; i < variants + 1; i++) {
            sounds.add(sound(Identifier.fromNamespaceAndPath(sound.getNamespace(), sound.getPath() + i)));
        }
        return define(subtitle, sounds.toArray(new SoundDefinition.Sound[] {}));
    }

    /**
     * Gets a sound from a {@link Identifier}.
     *
     * @param id The location of the sound.
     * @return The sound.
     */
    protected static SoundDefinition.Sound sound(Identifier id) {
        if (id.getPath().contains(".")) {
            return sound(Identifier.fromNamespaceAndPath(id.getNamespace(), id.getPath().replace('.', '/')));
        }
        return SoundDefinitionsProvider.sound(id);
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
        Identifier woodId = set.id().withPrefix("block.").withSuffix("_wood");
        Identifier breakId = woodId.withSuffix(".break");
        Identifier stepId = woodId.withSuffix(".step");
        Identifier buttonClickId = woodId.withSuffix("_button.click");
        Identifier doorId = woodId.withSuffix("_door");
        Identifier doorToggleId = doorId.withSuffix(".toggle");
        Identifier fenceGateId = woodId.withSuffix("_fence_gate");
        Identifier fenceGateToggleId = fenceGateId.withSuffix(".toggle");
        Identifier hangingSignId = woodId.withSuffix("_hanging_sign");
        Identifier hangingSignBreakId = hangingSignId.withSuffix(".break");
        Identifier hangingSignStepId = hangingSignId.withSuffix(".step");
        Identifier trapdoorId = woodId.withSuffix("_trapdoor");
        Identifier trapdoorToggleId = trapdoorId.withSuffix(".toggle");
        add(breakId, defineVariants("subtitles.block.generic.break", breakId, breakVariants));
        add(woodId.withSuffix(".fall"), defineVariants(null, stepId, stepVariants));
        add(woodId.withSuffix(".hit"), defineVariants("subtitles.block.generic.hit", stepId, stepVariants));
        add(woodId.withSuffix(".place"), defineVariants("subtitles.block.generic.place", breakId, breakVariants));
        add(stepId, defineVariants("subtitles.block.generic.footsteps", stepId, stepVariants));
        add(buttonClickId.withSuffix("_off"), define("subtitles.block.button.click", sound(buttonClickId).pitch(0.5).volume(0.4)));
        add(buttonClickId.withSuffix("_on"), define("subtitles.block.button.click", sound(buttonClickId).pitch(0.6).volume(0.4)));
        add(doorId.withSuffix(".close"), defineVariants("subtitles.block.door.toggle", doorToggleId, doorToggleVariants));
        add(doorId.withSuffix(".open"), defineVariants("subtitles.block.door.toggle", doorToggleId, doorToggleVariants));
        add(fenceGateId.withSuffix(".close"), defineVariants("subtitles.block.fence_gate.toggle", fenceGateToggleId, fenceGateToggleVariants));
        add(fenceGateId.withSuffix(".open"), defineVariants("subtitles.block.fence_gate.toggle", fenceGateToggleId, fenceGateToggleVariants));
        add(hangingSignBreakId, defineVariants("subtitles.block.generic.break", hangingSignBreakId, hangingSignBreakVariants));
        add(hangingSignId.withSuffix(".fall"), defineVariants(null, hangingSignStepId, hangingSignStepVariants));
        add(hangingSignId.withSuffix(".hit"), defineVariants("subtitles.block.generic.hit", hangingSignStepId, hangingSignStepVariants));
        add(hangingSignId.withSuffix(".place"), defineVariants("subtitles.block.generic.place", hangingSignBreakId, hangingSignBreakVariants));
        add(hangingSignStepId, defineVariants("subtitles.block.generic.footsteps", hangingSignStepId, hangingSignStepVariants));
        add(woodId.withSuffix("_pressure_plate.click_off"), define("subtitles.block.pressure_plate.click", sound("random/click").pitch(0.7).volume(0.3)));
        add(woodId.withSuffix("_pressure_plate.click_on"), define("subtitles.block.pressure_plate.click", sound("random/click").pitch(0.8).volume(0.3)));
        add(trapdoorId.withSuffix(".close"), defineVariants("subtitles.block.trapdoor.toggle", trapdoorToggleId, trapdoorToggleVariants));
        add(trapdoorId.withSuffix(".open"), defineVariants("subtitles.block.trapdoor.toggle", trapdoorToggleId, trapdoorToggleVariants));
    }
}
