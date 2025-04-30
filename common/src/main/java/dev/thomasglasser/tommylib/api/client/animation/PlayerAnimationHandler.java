package dev.thomasglasser.tommylib.api.client.animation;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import java.util.Map;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

/**
 * Handles registration and execution for player animation.
 */
public class PlayerAnimationHandler {
    private static final Map<Player, ModifierLayer<IAnimation>> ANIMATION_DATA = new Reference2ReferenceOpenHashMap<>();

    /**
     * Registers client for player animation.
     * <p>
     * Note: This method should be called during client init.
     */
    public static void init() {
        PlayerAnimationAccess.REGISTER_ANIMATION_EVENT.register(PlayerAnimationHandler::internalRegister);
    }

    /**
     * Registers a player for animation.
     * 
     * @param player The player to register
     * @param stack  The animation stack to register to
     */
    private static void internalRegister(Player player, AnimationStack stack) {
        ModifierLayer<IAnimation> layer = new ModifierLayer<>();
        stack.addAnimLayer(1000, layer);
        ANIMATION_DATA.put(player, layer);
    }

    /**
     * Starts an animation for a player with an optional transition and specified first-person mode and configuration.
     * 
     * @param start                    The animation to start with
     * @param next                     The optional animation to transition to
     * @param player                   The player to start the animation for
     * @param firstPersonMode          The first-person mode to use
     * @param firstPersonConfiguration The first-person configuration to use
     */
    public static void startAnimation(KeyframeAnimation start, @Nullable KeyframeAnimation next, Player player, FirstPersonMode firstPersonMode, FirstPersonConfiguration firstPersonConfiguration) {
        ModifierLayer<IAnimation> animation = ANIMATION_DATA.get(player);
        if (animation != null) {
            animation.setAnimation(new KeyframeAnimationPlayer(start).setFirstPersonMode(firstPersonMode).setFirstPersonConfiguration(firstPersonConfiguration));
            if (next != null)
                animation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(20, Ease.CONSTANT), new KeyframeAnimationPlayer(next).setFirstPersonMode(firstPersonMode).setFirstPersonConfiguration(firstPersonConfiguration));
        }
    }

    /**
     * Starts an animation for a player with an optional transition with the default first-person mode and configuration.
     *
     * @param start  The animation to start with
     * @param next   The optional animation to transition to
     * @param player The player to start the animation for
     */
    public static void startAnimation(KeyframeAnimation start, @Nullable KeyframeAnimation next, Player player) {
        startAnimation(start, next, player, FirstPersonMode.THIRD_PERSON_MODEL, getDefaultFirstPersonConfiguration(player));
    }

    /**
     * Starts an animation for a player with specified first-person mode and configuration.
     *
     * @param animation                The animation to start
     * @param player                   The player to start the animation for
     * @param firstPersonMode          The first-person mode to use
     * @param firstPersonConfiguration The first-person configuration to use
     */
    public static void startAnimation(KeyframeAnimation animation, Player player, FirstPersonMode firstPersonMode, FirstPersonConfiguration firstPersonConfiguration) {
        startAnimation(animation, null, player, firstPersonMode, firstPersonConfiguration);
    }

    /**
     * Starts an animation for a player with the default first-person mode and configuration.
     *
     * @param animation The animation to start
     * @param player    The player to start the animation for
     */
    public static void startAnimation(KeyframeAnimation animation, Player player) {
        startAnimation(animation, null, player, FirstPersonMode.THIRD_PERSON_MODEL, getDefaultFirstPersonConfiguration(player));
    }

    /**
     * Stops the animation for a player.
     * 
     * @param player The player to stop the animation for
     */
    public static void stopAnimation(Player player) {
        ModifierLayer<IAnimation> animation = ANIMATION_DATA.get(player);
        animation.setAnimation(null);
    }

    /**
     * Creates a {@link FirstPersonConfiguration} for a player with the default arm rendering settings.
     * 
     * @param player The player to create the configuration for
     * @return The {@link FirstPersonConfiguration} with default arm rendering settings
     */
    public static FirstPersonConfiguration getDefaultFirstPersonConfiguration(Player player) {
        boolean hasMainHandItem = !player.getMainHandItem().isEmpty();
        boolean hasOffHandItem = !player.getOffhandItem().isEmpty();
        boolean hasLeftItem = player.getMainArm() == HumanoidArm.LEFT ? hasMainHandItem : hasOffHandItem;
        boolean hasRightItem = player.getMainArm() == HumanoidArm.RIGHT ? hasMainHandItem : hasOffHandItem;
        return new FirstPersonConfiguration().setShowLeftArm(!hasLeftItem).setShowRightArm(!hasRightItem).setShowLeftItem(hasLeftItem).setShowRightItem(hasRightItem);
    }
}
