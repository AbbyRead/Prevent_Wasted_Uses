package btw.community.abbyread.pwu.mixin;

import net.minecraft.src.*;
import btw.community.abbyread.pwu.util.UsefulnessHelper;
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
     * If conversion succeeds, mark the itemstack as converted via UsefulnessHelper.
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

        // If conversion succeeded, mark the itemstack with the original block info (server-side only)
        if (convertedSuccessfully && stack != null && !world.isRemote) {
            UsefulnessHelper.markAsConverted(stack, block.blockID);
        }

        // Return the original result
        return convertedSuccessfully;
    }
}
