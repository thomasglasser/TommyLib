package dev.thomasglasser.tommylib.api.util;

import com.mojang.serialization.Codec;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;

public class TommyLibExtraCodecs {
    public static final Codec<Optional<Holder<SoundEvent>>> OPTIONAL_SOUND_EVENT_CODEC = TommyLibExtraCodecs.optionalCodec(SoundEvent.CODEC);

    public static <T> Codec<Optional<T>> optionalCodec(Codec<T> codec) {
        return codec.optionalFieldOf("value").codec();
    }
}
