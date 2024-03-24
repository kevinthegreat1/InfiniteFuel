package com.kevinthegreat.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.FurnaceFuelSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FurnaceFuelSlot.class)
public class FurnaceFuelSlotMixin {
    /**
     * Allows only one nether star to be inserted into the fuel slot of the furnace from the player's cursor stack.
     */
    @ModifyExpressionValue(method = "getMaxItemCount", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/FurnaceFuelSlot;isBucket(Lnet/minecraft/item/ItemStack;)Z"))
    private boolean infinitefuel$modifyNetherStarMaxItemCount(boolean original, ItemStack stack) {
        return original || stack.isOf(Items.NETHER_STAR);
    }
}
