package dev.thomasglasser.tommylib.api.client.animation;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.IdentityHashMap;
import java.util.Map;

public class AnimationUtils
{
	/**
	 * This will map player objects and the animation containers. To retrieve the animation for the player, you'll need that exact player object.
	 */
	public static final Map<AbstractClientPlayer, ModifierLayer<IAnimation>> animationData = new IdentityHashMap<>();

	//This method will set your mods animation into the library.
	public static void registerPlayerAnimation(AbstractClientPlayer player, AnimationStack stack) {
		//This will be invoked for every new player
		var layer = new ModifierLayer<>();
		stack.addAnimLayer(1000, layer); //Register the layer with a priority
		//The priority will tell, how important is this animation compared to other mods. Higher number means higher priority
		//Mods with higher priority will override the lower priority mods (if they want to animation anything)

		//If you want to map with Players, you have to use IdentityHashMap. that doesn't require hashCode function but does require the exact same object.
		animationData.put(player, layer);

		//Alternative solution is to Mixin the mod animation container into the player class. But that requires knowing how to use Mixins.
	}

	public static void startAnimation(KeyframeAnimation startAnim, @Nullable KeyframeAnimation goAnim, Player player, FirstPersonMode firstPersonMode)
	{
		var animation = animationData.get(player);
		//Get the animation for that player
		if (animation != null) {
			//You can set an animation from anywhere ON THE CLIENT
			//Do not attempt to do this on a server, that will only fail
			animation.setAnimation(new KeyframeAnimationPlayer(startAnim).setFirstPersonMode(firstPersonMode));
			if (goAnim != null)
				animation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(20, Ease.CONSTANT), new KeyframeAnimationPlayer(goAnim).setFirstPersonMode(firstPersonMode));
		}

	}

	public static void startAnimation(KeyframeAnimation anim, Player player, FirstPersonMode firstPersonMode)
	{
		startAnimation(anim, null, player, firstPersonMode);
	}

	public static void stopAnimation(AbstractClientPlayer player)
	{
		var animation = animationData.get(player);
		animation.setAnimation(null);
	}
}
