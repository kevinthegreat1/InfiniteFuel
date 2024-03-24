package com.kevinthegreat.mixin;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.SmithingTransformRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SmithingTransformRecipe.class)
public class SmithingTransformRecipeMixin {
    @ModifyArg(method = "craft", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;setNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
    private NbtCompound infinitefuel$removeDepletedNbt(NbtCompound nbt) {
        if (nbt.getBoolean("depleted")) {
            nbt.remove("depleted");
        }
        return nbt.isEmpty() ? null : nbt;
    }
}
