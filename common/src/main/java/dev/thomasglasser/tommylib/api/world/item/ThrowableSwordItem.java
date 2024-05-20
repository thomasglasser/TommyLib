package dev.thomasglasser.tommylib.api.world.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

/**
 * A {@link SwordItem} that can be thrown and enchanted with Loyalty.
 */
public abstract class ThrowableSwordItem extends SwordItem implements Enchantable
{
    public ThrowableSwordItem(Tier pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.SPEAR;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack pStack) {
        return 3600;
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1 && !itemstack.has(DataComponents.UNBREAKABLE)) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            pPlayer.startUsingItem(pHand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    @Override
    public abstract void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged);

    @Override
    public boolean canEnchant(Enchantment enchantment, ItemStack stack) {
        return enchantment == Enchantments.LOYALTY;
    }
}
