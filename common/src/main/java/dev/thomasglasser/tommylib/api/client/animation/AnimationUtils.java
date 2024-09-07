package dev.thomasglasser.tommylib.api.client.animation;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;

public class AnimationUtils {
    public static final Map<Player, ModifierLayer<IAnimation>> animationData = new IdentityHashMap<>();

    public static void registerPlayerForAnimation() {
        PlayerAnimationAccess.REGISTER_ANIMATION_EVENT.register(AnimationUtils::registerPlayerInternal);
    }

    /**
     * Registers a player for animation
     * 
     * @param player The player to register
     * @param stack  The animation stack to register to
     */
    private static void registerPlayerInternal(Player player, AnimationStack stack) {
        var layer = new ModifierLayer<>();
        stack.addAnimLayer(1000, layer);
        animationData.put(player, layer);
    }

    /**
     * Starts an animation for a player with an optional transition
     * 
     * @param startAnim       The animation to start with
     * @param goAnim          The optional animation to transition to
     * @param player          The player to start the animation for
     * @param firstPersonMode The first person mode to use
     */
    public static void startAnimation(KeyframeAnimation startAnim, @Nullable KeyframeAnimation goAnim, Player player, FirstPersonMode firstPersonMode) {
        var animation = animationData.get(player);
        if (animation != null) {
            animation.setAnimation(new KeyframeAnimationPlayer(startAnim).setFirstPersonMode(firstPersonMode));
            if (goAnim != null)
                animation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(20, Ease.CONSTANT), new KeyframeAnimationPlayer(goAnim).setFirstPersonMode(firstPersonMode));
        }
    }

    /**
     * Starts an animation for a player with no transition
     * 
     * @param anim            The animation to start
     * @param player          The player to start the animation for
     * @param firstPersonMode The first person mode to use
     */
    public static void startAnimation(KeyframeAnimation anim, Player player, FirstPersonMode firstPersonMode) {
        startAnimation(anim, null, player, firstPersonMode);
    }

    /**
     * Stops the animation for a player
     * 
     * @param player The player to stop the animation for
     */
    public static void stopAnimation(Player player) {
        var animation = animationData.get(player);
        animation.setAnimation(null);
    }
}
