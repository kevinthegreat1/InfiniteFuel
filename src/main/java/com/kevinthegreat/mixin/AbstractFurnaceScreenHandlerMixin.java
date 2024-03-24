package com.kevinthegreat.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(AbstractFurnaceScreenHandler.class)
public abstract class AbstractFurnaceScreenHandlerMixin extends AbstractRecipeScreenHandler<Inventory> {
    @Shadow
    @Final
    private Inventory inventory;

    public AbstractFurnaceScreenHandlerMixin(ScreenHandlerType<?> screenHandlerType, int i) {
        super(screenHandlerType, i);
    }

    /**
     * Allows a nether star to be quick-moved into the fuel slot of the furnace if there is not already a nether star.
     */
    @WrapOperation(method = "quickMove", slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/screen/AbstractFurnaceScreenHandler;isFuel(Lnet/minecraft/item/ItemStack;)Z")
    ), at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/AbstractFurnaceScreenHandler;insertItem(Lnet/minecraft/item/ItemStack;IIZ)Z", ordinal = 0))
    private boolean infinitefuel$modifyFuelSlotNetherStarQuickMove(AbstractFurnaceScreenHandler screenHandler, ItemStack stack, int startIndex, int endIndex, boolean fromLast, Operation<Boolean> original) {
        if (stack.isOf(Items.NETHER_STAR)) {
            if (!inventory.getStack(startIndex).isEmpty()) {
                return false;
            }
            boolean changed = original.call(screenHandler, stack.split(1), startIndex, endIndex, fromLast);
            if (!changed) {
                stack.increment(1);
            }
            return changed;
        } else {
            return original.call(screenHandler, stack, startIndex, endIndex, fromLast);
        }
    }
}
