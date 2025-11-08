package btw.community.abbyread.pwu.mixin;

import net.minecraft.src.*;
import btw.community.abbyread.pwu.util.UsefulnessHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Hooks ItemInWorldManager to track when blocks are converted.
 * This allows us to prevent double-damaging tools on converted blocks.
 */
@Mixin(ItemInWorldManager.class)
public abstract class ItemInWorldManagerMixin {

    @Unique
    private static final boolean DEBUG = false;

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

    @Inject(method = "activateBlockOrUseItem", at = @At("RETURN"))
    private void gimmeStats(EntityPlayer player, World world, ItemStack stack, int x, int y, int z, int side, float par8, float par9, float par10, CallbackInfoReturnable<Boolean> cir) {
        if (!DEBUG) return;

        int blockID = world.getBlockId(x, y, z);
        Block block = Block.blocksList[blockID];

        if (stack != null) System.out.print(stack.getMaxDamage() - stack.getItemDamage() + " durability remaining on " + stack.getDisplayName() + "\t|\t");
        if (block != null) System.out.print(block.getUnlocalizedName());
        System.out.println();
    }

}
