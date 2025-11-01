package btw.community.abbyread.pwu.util;

import btw.block.BTWBlocks;
import net.minecraft.src.*;

public class UsefulnessHelper {
    private static final boolean DEBUG = false;

    /**
     * Damages an item only if it's more efficient than bare hands at mining the target block.
     * Bare hands are assumed to have 1.0x efficiency multiplier.
     *
     * @param itemStack    The tool being used
     * @param world        The world
     * @param block        The block being mined
     * @param x            Block X coordinate
     * @param y            Block Y coordinate
     * @param z            Block Z coordinate
     * @param damageAmount How much damage to apply
     * @param entity       The entity using the tool
     */
    public static void damageIfUseful(
            ItemStack itemStack,
            World world,
            Block block,
            int x,
            int y,
            int z,
            int damageAmount,
            EntityLivingBase entity
    ) {
        // Safety checks
        if (itemStack == null || world == null || block == null || entity == null) {
            return;
        }

        if (!itemStack.isItemStackDamageable()) return;

        // Check and consume the conversion flag from the itemstack
        boolean wasJustConverted = false;
        if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("ar_pwu$converted")) {
            wasJustConverted = itemStack.getTagCompound().getBoolean("ar_pwu$converted");
            itemStack.getTagCompound().removeTag("ar_pwu$converted");  // Consume it
        }

        // Get the efficiency of the current tool on this block
        float toolEfficiency = itemStack.getStrVsBlock(world, block, x, y, z);

        // Get bare hands efficiency (1.0x - no bonus)
        float bareHandsEfficiency = 1.0f;

        boolean moreEfficient = toolEfficiency > bareHandsEfficiency;
        boolean isEfficientVsBlock = itemStack.getItem().isEfficientVsBlock(itemStack, world, block, x, y, z);
        boolean canHarvestBlock = itemStack.getItem().canHarvestBlock(itemStack, world, block, x, y, z);
        boolean canConvertBlock = block.canConvertBlock(itemStack, world, x, y, z);

        // Disregard false positives
        if (block.blockMaterial == Material.rock) canConvertBlock = false;
        if (block.blockMaterial == BTWBlocks.logMaterial) canConvertBlock = false;

        boolean betterThanNothing = moreEfficient || isEfficientVsBlock || canHarvestBlock || canConvertBlock || wasJustConverted;

        if (!world.isRemote && DEBUG) {
            System.out.println("-------------------------");
            System.out.println("toolEfficiency:     " + toolEfficiency);
            System.out.println("isEfficientVsBlock: " + isEfficientVsBlock);
            System.out.println("canHarvestBlock:    " + canHarvestBlock);
            System.out.println("canConvertBlock:    " + canConvertBlock);
            System.out.println("wasJustConverted:   " + wasJustConverted);
        }

        // Only apply damage if tool is actually better than nothing
        if (betterThanNothing) {
            itemStack.damageItem(damageAmount, entity);
            if (!world.isRemote && DEBUG) {
                System.out.println("Damaged item by " + damageAmount);
            }
        } else {
            if (!world.isRemote && DEBUG) {
                System.out.println("Prevented damage.");
            }
        }
    }
}