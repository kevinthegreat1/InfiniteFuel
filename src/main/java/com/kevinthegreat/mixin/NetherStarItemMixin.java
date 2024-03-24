package com.kevinthegreat.mixin;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.NetherStarItem;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(NetherStarItem.class)
public abstract class NetherStarItemMixin extends Item {
    public NetherStarItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (stack.getNbt() != null && stack.getNbt().getBoolean("depleted")) {
            tooltip.add(Text.translatable("item.infinitefuel.nether_star.depleted").formatted(Formatting.GRAY, Formatting.ITALIC));
            tooltip.add(Text.translatable("item.infinitefuel.nether_star.depleted.tooltip").formatted(Formatting.GRAY));
        }
    }

    @Inject(method = "hasGlint", at = @At("HEAD"), cancellable = true)
    private void infinitefuel$removeDepletedGlint(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getNbt() != null && stack.getNbt().getBoolean("depleted")) {
            cir.setReturnValue(false);
        }
    }
}
