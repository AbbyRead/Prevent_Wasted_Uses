package btw.community.abbyread.pwu.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.src.Block;
import net.minecraft.src.EntityLivingBase;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ItemTool;
import net.minecraft.src.World;
import btw.community.abbyread.pwu.util.ToolEfficiencyChecker;

@Mixin(ItemTool.class)
public class ItemToolMixin {

    /**
     * Redirect the damageItem call in onBlockDestroyed to check efficiency first.
     * <p></p>
     * This intercepts: par1ItemStack.damageItem(1, par7EntityLivingBase);
     * In the method: onBlockDestroyed(ItemStack, World, int, int, int, int, EntityLivingBase)
     */
    @Redirect(
            method = "onBlockDestroyed(Lnet/minecraft/src/ItemStack;Lnet/minecraft/src/World;IIIILnet/minecraft/src/EntityLivingBase;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/src/ItemStack;damageItem(ILnet/minecraft/src/EntityLivingBase;)V"
            )
    )
    private static void redirectDamageItem(
            ItemStack itemStack,
            int damageAmount,
            EntityLivingBase entity,
            ItemStack stack,                // Capture the actual stack being used
            World world,                    // Capture world from method parameters
            int blockID,                    // Block ID parameter
            int x,                          // X coordinate
            int y,                          // Y coordinate
            int z,                          // Z coordinate
            EntityLivingBase usingEntity    // Entity using the tool
    ) {
        // Get the block from the ID
        Block block = Block.blocksList[blockID];

        // Only check efficiency if we have a valid block
        if (block != null) {
            ToolEfficiencyChecker.damageItemIfEfficient(
                    stack,
                    world,
                    block,
                    x, y, z,
                    damageAmount,
                    usingEntity
            );
        } else {
            // Fallback: apply damage anyway if block is null
            stack.damageItem(damageAmount, usingEntity);
        }
    }
}