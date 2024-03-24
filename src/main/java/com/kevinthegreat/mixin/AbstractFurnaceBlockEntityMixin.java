package com.kevinthegreat.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtByte;
import net.minecraft.util.math.MathHelper;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Priority is set to 1200 so that this mixin is applied after Fabric API's stack remainder mixin, which replaces the stack outright.
 */
@Mixin(value = AbstractFurnaceBlockEntity.class, priority = 1200)
public abstract class AbstractFurnaceBlockEntityMixin {
    @Unique
    private static final ThreadLocal<ItemStack> REMAINDER_STACK = new ThreadLocal<>();

    @ModifyExpressionValue(method = "writeNbt", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/block/entity/AbstractFurnaceBlockEntity;burnTime:I"))
    private int infinitefuel$clampNetherStarBurnTime(int burnTime) {
        return MathHelper.clamp(burnTime, Short.MIN_VALUE, Short.MAX_VALUE);
    }

    @ModifyVariable(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
    private static ItemStack infinitefuel$saveNetherStarRecipeRemainder(ItemStack stack) {
        if (stack.isOf(Items.NETHER_STAR)) {
            ItemStack depletedNetherStar = new ItemStack(Items.NETHER_STAR);
            depletedNetherStar.setSubNbt("depleted", NbtByte.of(true));
            REMAINDER_STACK.set(depletedNetherStar);
        }
        return stack;
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;set(ILjava/lang/Object;)Ljava/lang/Object;"))
    private static Object infinitefuel$modifyNetherStarRecipeRemainder(Object recipeRemainder) {
        ItemStack remainderStack = REMAINDER_STACK.get();
        if (remainderStack != null) {
            REMAINDER_STACK.remove();
            return remainderStack;
        } else {
            return recipeRemainder;
        }
    }

    /**
     * Prevents depleted nether stars from being used as fuel.
     */
    @Inject(method = "canUseAsFuel", at = @At("HEAD"), cancellable = true)
    private static void infinitefuel$modifyNetherStarCanUseAsFuel(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getNbt() != null && stack.getNbt().getBoolean("depleted")) {
            cir.setReturnValue(false);
        }
    }

    @ModifyReturnValue(method = "canExtract", at = @At(value = "RETURN", ordinal = 0))
    private boolean infinitefuel$modifyNetherStarCanExtract(boolean canExtract, @Local(argsOnly = true) ItemStack stack) {
        return canExtract || (stack.isOf(Items.NETHER_STAR) && stack.getNbt() != null && stack.getNbt().getBoolean("depleted"));
    }

    /**
     * Allows a nether star to be inserted into the fuel slot of the furnace by non-players if it is not depleted and there is not already a nether star.
     * For nether starts, {@code isValid} is only true if it is not depleted, due to {@link #infinitefuel$modifyNetherStarCanUseAsFuel(ItemStack, CallbackInfoReturnable)}.
     */
    @ModifyExpressionValue(method = "isValid", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/AbstractFurnaceBlockEntity;canUseAsFuel(Lnet/minecraft/item/ItemStack;)Z"))
    private boolean infinitefuel$modifyNetherStarIsValid(boolean isValid, @Local(ordinal = 0, argsOnly = true) ItemStack stack, @Local(ordinal = 1) ItemStack previousStack) {
        return stack.isOf(Items.NETHER_STAR) ? isValid && !previousStack.isOf(Items.NETHER_STAR) : isValid;
    }
}
