package dev.thomasglasser.tommylib.api.network;

import com.mojang.datafixers.util.Function10;
import com.mojang.datafixers.util.Function11;
import com.mojang.datafixers.util.Function12;
import com.mojang.datafixers.util.Function13;
import com.mojang.datafixers.util.Function14;
import com.mojang.datafixers.util.Function15;
import com.mojang.datafixers.util.Function16;
import com.mojang.datafixers.util.Function7;
import com.mojang.datafixers.util.Function8;
import com.mojang.datafixers.util.Function9;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * Provides utils for creating {@link ByteBuf}s and {@link StreamCodec}s
 */
public class NetworkUtils {
    /**
     * Creates an empty {@link FriendlyByteBuf}.
     * 
     * @return An empty {@link FriendlyByteBuf}
     */
    public static FriendlyByteBuf empty() {
        return new FriendlyByteBuf(Unpooled.EMPTY_BUFFER);
    }

    /**
     * Creates a new {@link FriendlyByteBuf}.
     * 
     * @return A new {@link FriendlyByteBuf}
     */
    public static FriendlyByteBuf create() {
        return new FriendlyByteBuf(Unpooled.buffer());
    }

    /**
     * Creates a {@link StreamCodec} for an enum.
     * 
     * @param enumClass The class of the enum
     * @return A {@link StreamCodec} for the enum
     * @param <B> The type of the {@link FriendlyByteBuf}
     * @param <V> The type of the enum
     */
    public static <B extends FriendlyByteBuf, V extends Enum<V>> StreamCodec<B, V> enumCodec(Class<V> enumClass) {
        return new StreamCodec<>() {
            @Override
            public V decode(B buf) {
                return buf.readEnum(enumClass);
            }

            @Override
            public void encode(B buf, V value) {
                buf.writeEnum(value);
            }
        };
    }

    /**
     * Creates a {@link StreamCodec} for more generics than the base class provides helpers for.
     * 
     * @param streamCodec The {@link StreamCodec} to use for the object parameter
     * @param function    The getter {@link Function} to use for the object parameter
     * @param initializer The initializer {@link Function} for the object
     * @return A {@link StreamCodec}
     * @param <B> The type of the {@link FriendlyByteBuf}
     * @param <C> The type of the object
     */

    // Overload for 7 generics
    public static <B, C, T1, T2, T3, T4, T5, T6, T7> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> streamCodec, final Function<C, T1> function, final StreamCodec<? super B, T2> streamCodec2, final Function<C, T2> function2, final StreamCodec<? super B, T3> streamCodec3, final Function<C, T3> function3, final StreamCodec<? super B, T4> streamCodec4, final Function<C, T4> function4, final StreamCodec<? super B, T5> streamCodec5, final Function<C, T5> function5, final StreamCodec<? super B, T6> streamCodec6, final Function<C, T6> function6, final StreamCodec<? super B, T7> streamCodec7, final Function<C, T7> function7, final Function7<T1, T2, T3, T4, T5, T6, T7, C> initializer) {
        return new StreamCodec<>() {
            public C decode(B object) {
                T1 object2 = streamCodec.decode(object);
                T2 object3 = streamCodec2.decode(object);
                T3 object4 = streamCodec3.decode(object);
                T4 object5 = streamCodec4.decode(object);
                T5 object6 = streamCodec5.decode(object);
                T6 object7 = streamCodec6.decode(object);
                T7 object8 = streamCodec7.decode(object);
                return initializer.apply(object2, object3, object4, object5, object6, object7, object8);
            }

            public void encode(B object, C object2) {
                streamCodec.encode(object, function.apply(object2));
                streamCodec2.encode(object, function2.apply(object2));
                streamCodec3.encode(object, function3.apply(object2));
                streamCodec4.encode(object, function4.apply(object2));
                streamCodec5.encode(object, function5.apply(object2));
                streamCodec6.encode(object, function6.apply(object2));
                streamCodec7.encode(object, function7.apply(object2));
            }
        };
    }

    // Overload for 8 generics
    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> streamCodec1, final Function<C, T1> function1,
            final StreamCodec<? super B, T2> streamCodec2, final Function<C, T2> function2,
            final StreamCodec<? super B, T3> streamCodec3, final Function<C, T3> function3,
            final StreamCodec<? super B, T4> streamCodec4, final Function<C, T4> function4,
            final StreamCodec<? super B, T5> streamCodec5, final Function<C, T5> function5,
            final StreamCodec<? super B, T6> streamCodec6, final Function<C, T6> function6,
            final StreamCodec<? super B, T7> streamCodec7, final Function<C, T7> function7,
            final StreamCodec<? super B, T8> streamCodec8, final Function<C, T8> function8,
            final Function8<T1, T2, T3, T4, T5, T6, T7, T8, C> initializer) {
        return new StreamCodec<>() {
            public C decode(B object) {
                T1 object2 = streamCodec1.decode(object);
                T2 object3 = streamCodec2.decode(object);
                T3 object4 = streamCodec3.decode(object);
                T4 object5 = streamCodec4.decode(object);
                T5 object6 = streamCodec5.decode(object);
                T6 object7 = streamCodec6.decode(object);
                T7 object8 = streamCodec7.decode(object);
                T8 object9 = streamCodec8.decode(object);
                return initializer.apply(object2, object3, object4, object5, object6, object7, object8, object9);
            }

            public void encode(B object, C object2) {
                streamCodec1.encode(object, function1.apply(object2));
                streamCodec2.encode(object, function2.apply(object2));
                streamCodec3.encode(object, function3.apply(object2));
                streamCodec4.encode(object, function4.apply(object2));
                streamCodec5.encode(object, function5.apply(object2));
                streamCodec6.encode(object, function6.apply(object2));
                streamCodec7.encode(object, function7.apply(object2));
                streamCodec8.encode(object, function8.apply(object2));
            }
        };
    }

    // Overload for 9 generics
    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> streamCodec1, final Function<C, T1> function1,
            final StreamCodec<? super B, T2> streamCodec2, final Function<C, T2> function2,
            final StreamCodec<? super B, T3> streamCodec3, final Function<C, T3> function3,
            final StreamCodec<? super B, T4> streamCodec4, final Function<C, T4> function4,
            final StreamCodec<? super B, T5> streamCodec5, final Function<C, T5> function5,
            final StreamCodec<? super B, T6> streamCodec6, final Function<C, T6> function6,
            final StreamCodec<? super B, T7> streamCodec7, final Function<C, T7> function7,
            final StreamCodec<? super B, T8> streamCodec8, final Function<C, T8> function8,
            final StreamCodec<? super B, T9> streamCodec9, final Function<C, T9> function9,
            final Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, C> initializer) {
        return new StreamCodec<>() {
            public C decode(B object) {
                T1 object2 = streamCodec1.decode(object);
                T2 object3 = streamCodec2.decode(object);
                T3 object4 = streamCodec3.decode(object);
                T4 object5 = streamCodec4.decode(object);
                T5 object6 = streamCodec5.decode(object);
                T6 object7 = streamCodec6.decode(object);
                T7 object8 = streamCodec7.decode(object);
                T8 object9 = streamCodec8.decode(object);
                T9 object10 = streamCodec9.decode(object);
                return initializer.apply(object2, object3, object4, object5, object6, object7, object8, object9, object10);
            }

            public void encode(B object, C object2) {
                streamCodec1.encode(object, function1.apply(object2));
                streamCodec2.encode(object, function2.apply(object2));
                streamCodec3.encode(object, function3.apply(object2));
                streamCodec4.encode(object, function4.apply(object2));
                streamCodec5.encode(object, function5.apply(object2));
                streamCodec6.encode(object, function6.apply(object2));
                streamCodec7.encode(object, function7.apply(object2));
                streamCodec8.encode(object, function8.apply(object2));
                streamCodec9.encode(object, function9.apply(object2));
            }
        };
    }

    // Overload for 10 generics
    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> streamCodec1, final Function<C, T1> function1,
            final StreamCodec<? super B, T2> streamCodec2, final Function<C, T2> function2,
            final StreamCodec<? super B, T3> streamCodec3, final Function<C, T3> function3,
            final StreamCodec<? super B, T4> streamCodec4, final Function<C, T4> function4,
            final StreamCodec<? super B, T5> streamCodec5, final Function<C, T5> function5,
            final StreamCodec<? super B, T6> streamCodec6, final Function<C, T6> function6,
            final StreamCodec<? super B, T7> streamCodec7, final Function<C, T7> function7,
            final StreamCodec<? super B, T8> streamCodec8, final Function<C, T8> function8,
            final StreamCodec<? super B, T9> streamCodec9, final Function<C, T9> function9,
            final StreamCodec<? super B, T10> streamCodec10, final Function<C, T10> function10,
            final Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, C> initializer) {
        return new StreamCodec<>() {
            public C decode(B object) {
                T1 object2 = streamCodec1.decode(object);
                T2 object3 = streamCodec2.decode(object);
                T3 object4 = streamCodec3.decode(object);
                T4 object5 = streamCodec4.decode(object);
                T5 object6 = streamCodec5.decode(object);
                T6 object7 = streamCodec6.decode(object);
                T7 object8 = streamCodec7.decode(object);
                T8 object9 = streamCodec8.decode(object);
                T9 object10 = streamCodec9.decode(object);
                T10 object11 = streamCodec10.decode(object);
                return initializer.apply(object2, object3, object4, object5, object6, object7, object8, object9, object10, object11);
            }

            public void encode(B object, C object2) {
                streamCodec1.encode(object, function1.apply(object2));
                streamCodec2.encode(object, function2.apply(object2));
                streamCodec3.encode(object, function3.apply(object2));
                streamCodec4.encode(object, function4.apply(object2));
                streamCodec5.encode(object, function5.apply(object2));
                streamCodec6.encode(object, function6.apply(object2));
                streamCodec7.encode(object, function7.apply(object2));
                streamCodec8.encode(object, function8.apply(object2));
                streamCodec9.encode(object, function9.apply(object2));
                streamCodec10.encode(object, function10.apply(object2));
            }
        };
    }

    // Overload for 11 generics
    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> streamCodec1, final Function<C, T1> function1,
            final StreamCodec<? super B, T2> streamCodec2, final Function<C, T2> function2,
            final StreamCodec<? super B, T3> streamCodec3, final Function<C, T3> function3,
            final StreamCodec<? super B, T4> streamCodec4, final Function<C, T4> function4,
            final StreamCodec<? super B, T5> streamCodec5, final Function<C, T5> function5,
            final StreamCodec<? super B, T6> streamCodec6, final Function<C, T6> function6,
            final StreamCodec<? super B, T7> streamCodec7, final Function<C, T7> function7,
            final StreamCodec<? super B, T8> streamCodec8, final Function<C, T8> function8,
            final StreamCodec<? super B, T9> streamCodec9, final Function<C, T9> function9,
            final StreamCodec<? super B, T10> streamCodec10, final Function<C, T10> function10,
            final StreamCodec<? super B, T11> streamCodec11, final Function<C, T11> function11,
            final Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, C> initializer) {
        return new StreamCodec<>() {
            public C decode(B object) {
                T1 object2 = streamCodec1.decode(object);
                T2 object3 = streamCodec2.decode(object);
                T3 object4 = streamCodec3.decode(object);
                T4 object5 = streamCodec4.decode(object);
                T5 object6 = streamCodec5.decode(object);
                T6 object7 = streamCodec6.decode(object);
                T7 object8 = streamCodec7.decode(object);
                T8 object9 = streamCodec8.decode(object);
                T9 object10 = streamCodec9.decode(object);
                T10 object11 = streamCodec10.decode(object);
                T11 object12 = streamCodec11.decode(object);
                return initializer.apply(object2, object3, object4, object5, object6, object7, object8, object9, object10, object11, object12);
            }

            public void encode(B object, C object2) {
                streamCodec1.encode(object, function1.apply(object2));
                streamCodec2.encode(object, function2.apply(object2));
                streamCodec3.encode(object, function3.apply(object2));
                streamCodec4.encode(object, function4.apply(object2));
                streamCodec5.encode(object, function5.apply(object2));
                streamCodec6.encode(object, function6.apply(object2));
                streamCodec7.encode(object, function7.apply(object2));
                streamCodec8.encode(object, function8.apply(object2));
                streamCodec9.encode(object, function9.apply(object2));
                streamCodec10.encode(object, function10.apply(object2));
                streamCodec11.encode(object, function11.apply(object2));
            }
        };
    }

    // Overload for 12 generics
    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> streamCodec1, final Function<C, T1> function1,
            final StreamCodec<? super B, T2> streamCodec2, final Function<C, T2> function2,
            final StreamCodec<? super B, T3> streamCodec3, final Function<C, T3> function3,
            final StreamCodec<? super B, T4> streamCodec4, final Function<C, T4> function4,
            final StreamCodec<? super B, T5> streamCodec5, final Function<C, T5> function5,
            final StreamCodec<? super B, T6> streamCodec6, final Function<C, T6> function6,
            final StreamCodec<? super B, T7> streamCodec7, final Function<C, T7> function7,
            final StreamCodec<? super B, T8> streamCodec8, final Function<C, T8> function8,
            final StreamCodec<? super B, T9> streamCodec9, final Function<C, T9> function9,
            final StreamCodec<? super B, T10> streamCodec10, final Function<C, T10> function10,
            final StreamCodec<? super B, T11> streamCodec11, final Function<C, T11> function11,
            final StreamCodec<? super B, T12> streamCodec12, final Function<C, T12> function12,
            final Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, C> initializer) {
        return new StreamCodec<>() {
            public C decode(B object) {
                T1 object2 = streamCodec1.decode(object);
                T2 object3 = streamCodec2.decode(object);
                T3 object4 = streamCodec3.decode(object);
                T4 object5 = streamCodec4.decode(object);
                T5 object6 = streamCodec5.decode(object);
                T6 object7 = streamCodec6.decode(object);
                T7 object8 = streamCodec7.decode(object);
                T8 object9 = streamCodec8.decode(object);
                T9 object10 = streamCodec9.decode(object);
                T10 object11 = streamCodec10.decode(object);
                T11 object12 = streamCodec11.decode(object);
                T12 object13 = streamCodec12.decode(object);
                return initializer.apply(object2, object3, object4, object5, object6, object7, object8, object9, object10, object11, object12, object13);
            }

            public void encode(B object, C object2) {
                streamCodec1.encode(object, function1.apply(object2));
                streamCodec2.encode(object, function2.apply(object2));
                streamCodec3.encode(object, function3.apply(object2));
                streamCodec4.encode(object, function4.apply(object2));
                streamCodec5.encode(object, function5.apply(object2));
                streamCodec6.encode(object, function6.apply(object2));
                streamCodec7.encode(object, function7.apply(object2));
                streamCodec8.encode(object, function8.apply(object2));
                streamCodec9.encode(object, function9.apply(object2));
                streamCodec10.encode(object, function10.apply(object2));
                streamCodec11.encode(object, function11.apply(object2));
                streamCodec12.encode(object, function12.apply(object2));
            }
        };
    }

    // Overload for 13 generics
    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> streamCodec1, final Function<C, T1> function1,
            final StreamCodec<? super B, T2> streamCodec2, final Function<C, T2> function2,
            final StreamCodec<? super B, T3> streamCodec3, final Function<C, T3> function3,
            final StreamCodec<? super B, T4> streamCodec4, final Function<C, T4> function4,
            final StreamCodec<? super B, T5> streamCodec5, final Function<C, T5> function5,
            final StreamCodec<? super B, T6> streamCodec6, final Function<C, T6> function6,
            final StreamCodec<? super B, T7> streamCodec7, final Function<C, T7> function7,
            final StreamCodec<? super B, T8> streamCodec8, final Function<C, T8> function8,
            final StreamCodec<? super B, T9> streamCodec9, final Function<C, T9> function9,
            final StreamCodec<? super B, T10> streamCodec10, final Function<C, T10> function10,
            final StreamCodec<? super B, T11> streamCodec11, final Function<C, T11> function11,
            final StreamCodec<? super B, T12> streamCodec12, final Function<C, T12> function12,
            final StreamCodec<? super B, T13> streamCodec13, final Function<C, T13> function13,
            final Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, C> initializer) {
        return new StreamCodec<>() {
            public C decode(B object) {
                T1 object2 = streamCodec1.decode(object);
                T2 object3 = streamCodec2.decode(object);
                T3 object4 = streamCodec3.decode(object);
                T4 object5 = streamCodec4.decode(object);
                T5 object6 = streamCodec5.decode(object);
                T6 object7 = streamCodec6.decode(object);
                T7 object8 = streamCodec7.decode(object);
                T8 object9 = streamCodec8.decode(object);
                T9 object10 = streamCodec9.decode(object);
                T10 object11 = streamCodec10.decode(object);
                T11 object12 = streamCodec11.decode(object);
                T12 object13 = streamCodec12.decode(object);
                T13 object14 = streamCodec13.decode(object);
                return initializer.apply(object2, object3, object4, object5, object6, object7, object8, object9, object10, object11, object12, object13, object14);
            }

            public void encode(B object, C object2) {
                streamCodec1.encode(object, function1.apply(object2));
                streamCodec2.encode(object, function2.apply(object2));
                streamCodec3.encode(object, function3.apply(object2));
                streamCodec4.encode(object, function4.apply(object2));
                streamCodec5.encode(object, function5.apply(object2));
                streamCodec6.encode(object, function6.apply(object2));
                streamCodec7.encode(object, function7.apply(object2));
                streamCodec8.encode(object, function8.apply(object2));
                streamCodec9.encode(object, function9.apply(object2));
                streamCodec10.encode(object, function10.apply(object2));
                streamCodec11.encode(object, function11.apply(object2));
                streamCodec12.encode(object, function12.apply(object2));
                streamCodec13.encode(object, function13.apply(object2));
            }
        };
    }

    // Overload for 14 generics
    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> streamCodec1, final Function<C, T1> function1,
            final StreamCodec<? super B, T2> streamCodec2, final Function<C, T2> function2,
            final StreamCodec<? super B, T3> streamCodec3, final Function<C, T3> function3,
            final StreamCodec<? super B, T4> streamCodec4, final Function<C, T4> function4,
            final StreamCodec<? super B, T5> streamCodec5, final Function<C, T5> function5,
            final StreamCodec<? super B, T6> streamCodec6, final Function<C, T6> function6,
            final StreamCodec<? super B, T7> streamCodec7, final Function<C, T7> function7,
            final StreamCodec<? super B, T8> streamCodec8, final Function<C, T8> function8,
            final StreamCodec<? super B, T9> streamCodec9, final Function<C, T9> function9,
            final StreamCodec<? super B, T10> streamCodec10, final Function<C, T10> function10,
            final StreamCodec<? super B, T11> streamCodec11, final Function<C, T11> function11,
            final StreamCodec<? super B, T12> streamCodec12, final Function<C, T12> function12,
            final StreamCodec<? super B, T13> streamCodec13, final Function<C, T13> function13,
            final StreamCodec<? super B, T14> streamCodec14, final Function<C, T14> function14,
            final Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, C> initializer) {
        return new StreamCodec<>() {
            public C decode(B object) {
                T1 object2 = streamCodec1.decode(object);
                T2 object3 = streamCodec2.decode(object);
                T3 object4 = streamCodec3.decode(object);
                T4 object5 = streamCodec4.decode(object);
                T5 object6 = streamCodec5.decode(object);
                T6 object7 = streamCodec6.decode(object);
                T7 object8 = streamCodec7.decode(object);
                T8 object9 = streamCodec8.decode(object);
                T9 object10 = streamCodec9.decode(object);
                T10 object11 = streamCodec10.decode(object);
                T11 object12 = streamCodec11.decode(object);
                T12 object13 = streamCodec12.decode(object);
                T13 object14 = streamCodec13.decode(object);
                T14 object15 = streamCodec14.decode(object);
                return initializer.apply(object2, object3, object4, object5, object6, object7, object8, object9, object10, object11, object12, object13, object14, object15);
            }

            public void encode(B object, C object2) {
                streamCodec1.encode(object, function1.apply(object2));
                streamCodec2.encode(object, function2.apply(object2));
                streamCodec3.encode(object, function3.apply(object2));
                streamCodec4.encode(object, function4.apply(object2));
                streamCodec5.encode(object, function5.apply(object2));
                streamCodec6.encode(object, function6.apply(object2));
                streamCodec7.encode(object, function7.apply(object2));
                streamCodec8.encode(object, function8.apply(object2));
                streamCodec9.encode(object, function9.apply(object2));
                streamCodec10.encode(object, function10.apply(object2));
                streamCodec11.encode(object, function11.apply(object2));
                streamCodec12.encode(object, function12.apply(object2));
                streamCodec13.encode(object, function13.apply(object2));
                streamCodec14.encode(object, function14.apply(object2));
            }
        };
    }

    // Overload for 15 generics
    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> streamCodec1, final Function<C, T1> function1,
            final StreamCodec<? super B, T2> streamCodec2, final Function<C, T2> function2,
            final StreamCodec<? super B, T3> streamCodec3, final Function<C, T3> function3,
            final StreamCodec<? super B, T4> streamCodec4, final Function<C, T4> function4,
            final StreamCodec<? super B, T5> streamCodec5, final Function<C, T5> function5,
            final StreamCodec<? super B, T6> streamCodec6, final Function<C, T6> function6,
            final StreamCodec<? super B, T7> streamCodec7, final Function<C, T7> function7,
            final StreamCodec<? super B, T8> streamCodec8, final Function<C, T8> function8,
            final StreamCodec<? super B, T9> streamCodec9, final Function<C, T9> function9,
            final StreamCodec<? super B, T10> streamCodec10, final Function<C, T10> function10,
            final StreamCodec<? super B, T11> streamCodec11, final Function<C, T11> function11,
            final StreamCodec<? super B, T12> streamCodec12, final Function<C, T12> function12,
            final StreamCodec<? super B, T13> streamCodec13, final Function<C, T13> function13,
            final StreamCodec<? super B, T14> streamCodec14, final Function<C, T14> function14,
            final StreamCodec<? super B, T15> streamCodec15, final Function<C, T15> function15,
            final Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, C> initializer) {
        return new StreamCodec<>() {
            public C decode(B object) {
                T1 object2 = streamCodec1.decode(object);
                T2 object3 = streamCodec2.decode(object);
                T3 object4 = streamCodec3.decode(object);
                T4 object5 = streamCodec4.decode(object);
                T5 object6 = streamCodec5.decode(object);
                T6 object7 = streamCodec6.decode(object);
                T7 object8 = streamCodec7.decode(object);
                T8 object9 = streamCodec8.decode(object);
                T9 object10 = streamCodec9.decode(object);
                T10 object11 = streamCodec10.decode(object);
                T11 object12 = streamCodec11.decode(object);
                T12 object13 = streamCodec12.decode(object);
                T13 object14 = streamCodec13.decode(object);
                T14 object15 = streamCodec14.decode(object);
                T15 object16 = streamCodec15.decode(object);
                return initializer.apply(object2, object3, object4, object5, object6, object7, object8, object9, object10, object11, object12, object13, object14, object15, object16);
            }

            public void encode(B object, C object2) {
                streamCodec1.encode(object, function1.apply(object2));
                streamCodec2.encode(object, function2.apply(object2));
                streamCodec3.encode(object, function3.apply(object2));
                streamCodec4.encode(object, function4.apply(object2));
                streamCodec5.encode(object, function5.apply(object2));
                streamCodec6.encode(object, function6.apply(object2));
                streamCodec7.encode(object, function7.apply(object2));
                streamCodec8.encode(object, function8.apply(object2));
                streamCodec9.encode(object, function9.apply(object2));
                streamCodec10.encode(object, function10.apply(object2));
                streamCodec11.encode(object, function11.apply(object2));
                streamCodec12.encode(object, function12.apply(object2));
                streamCodec13.encode(object, function13.apply(object2));
                streamCodec14.encode(object, function14.apply(object2));
                streamCodec15.encode(object, function15.apply(object2));
            }
        };
    }

    // Overload for 16 generics
    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> streamCodec1, final Function<C, T1> function1,
            final StreamCodec<? super B, T2> streamCodec2, final Function<C, T2> function2,
            final StreamCodec<? super B, T3> streamCodec3, final Function<C, T3> function3,
            final StreamCodec<? super B, T4> streamCodec4, final Function<C, T4> function4,
            final StreamCodec<? super B, T5> streamCodec5, final Function<C, T5> function5,
            final StreamCodec<? super B, T6> streamCodec6, final Function<C, T6> function6,
            final StreamCodec<? super B, T7> streamCodec7, final Function<C, T7> function7,
            final StreamCodec<? super B, T8> streamCodec8, final Function<C, T8> function8,
            final StreamCodec<? super B, T9> streamCodec9, final Function<C, T9> function9,
            final StreamCodec<? super B, T10> streamCodec10, final Function<C, T10> function10,
            final StreamCodec<? super B, T11> streamCodec11, final Function<C, T11> function11,
            final StreamCodec<? super B, T12> streamCodec12, final Function<C, T12> function12,
            final StreamCodec<? super B, T13> streamCodec13, final Function<C, T13> function13,
            final StreamCodec<? super B, T14> streamCodec14, final Function<C, T14> function14,
            final StreamCodec<? super B, T15> streamCodec15, final Function<C, T15> function15,
            final StreamCodec<? super B, T16> streamCodec16, final Function<C, T16> function16,
            final Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, C> initializer) {
        return new StreamCodec<>() {
            @Override
            public C decode(B object) {
                T1 object2 = streamCodec1.decode(object);
                T2 object3 = streamCodec2.decode(object);
                T3 object4 = streamCodec3.decode(object);
                T4 object5 = streamCodec4.decode(object);
                T5 object6 = streamCodec5.decode(object);
                T6 object7 = streamCodec6.decode(object);
                T7 object8 = streamCodec7.decode(object);
                T8 object9 = streamCodec8.decode(object);
                T9 object10 = streamCodec9.decode(object);
                T10 object11 = streamCodec10.decode(object);
                T11 object12 = streamCodec11.decode(object);
                T12 object13 = streamCodec12.decode(object);
                T13 object14 = streamCodec13.decode(object);
                T14 object15 = streamCodec14.decode(object);
                T15 object16 = streamCodec15.decode(object);
                T16 object17 = streamCodec16.decode(object);
                return initializer.apply(object2, object3, object4, object5, object6, object7, object8, object9, object10, object11, object12, object13, object14, object15, object16, object17);
            }

            @Override
            public void encode(B object, C object2) {
                streamCodec1.encode(object, function1.apply(object2));
                streamCodec2.encode(object, function2.apply(object2));
                streamCodec3.encode(object, function3.apply(object2));
                streamCodec4.encode(object, function4.apply(object2));
                streamCodec5.encode(object, function5.apply(object2));
                streamCodec6.encode(object, function6.apply(object2));
                streamCodec7.encode(object, function7.apply(object2));
                streamCodec8.encode(object, function8.apply(object2));
                streamCodec9.encode(object, function9.apply(object2));
                streamCodec10.encode(object, function10.apply(object2));
                streamCodec11.encode(object, function11.apply(object2));
                streamCodec12.encode(object, function12.apply(object2));
                streamCodec13.encode(object, function13.apply(object2));
                streamCodec14.encode(object, function14.apply(object2));
                streamCodec15.encode(object, function15.apply(object2));
                streamCodec16.encode(object, function16.apply(object2));
            }
        };
    }
}
