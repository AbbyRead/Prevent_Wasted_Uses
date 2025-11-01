package btw.community.abbyread.pwu.mixin;

import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Hooks ItemInWorldManager to track when blocks are converted.
 * This allows us to prevent double-damaging tools on converted blocks.
 */
@Mixin(ItemInWorldManager.class)
public abstract class ItemInWorldManagerMixin {
    /**
     * Redirect the convertBlock call to capture its return value.
     * If conversion succeeds, mark the itemstack with a conversion flag.
     */
    @Redirect(
            method = "survivalTryHarvestBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/src/Block;convertBlock(Lnet/minecraft/src/ItemStack;Lnet/minecraft/src/World;IIII)Z"
            )
    )
    private boolean recordConversionIfSuccessful(
            Block block,
            ItemStack stack,
            World world,
            int x,
            int y,
            int z,
            int fromSide
    ) {
        // Call the original convertBlock method
        boolean convertedSuccessfully = block.convertBlock(stack, world, x, y, z, fromSide);

        // If conversion succeeded, mark the itemstack
        if (convertedSuccessfully && stack != null) {
            // Create NBT tag if it doesn't exist
            if (!stack.hasTagCompound()) {
                stack.setTagCompound(new NBTTagCompound());
            }
            // Set the conversion flag
            stack.getTagCompound().setBoolean("ar_pwu$converted", true);
        }

        // Return the original result
        return convertedSuccessfully;
    }
}