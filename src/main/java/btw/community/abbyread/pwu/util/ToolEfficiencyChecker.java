package btw.community.abbyread.pwu.util;

import net.minecraft.src.*;

public class ToolEfficiencyChecker {
    private static final boolean DEBUG = true;

    /**
     * Damages an item only if it's more efficient than bare hands at mining the target block.
     * Bare hands are assumed to have 1.0x efficiency multiplier.
     *
     * @param itemStack The tool being used
     * @param world The world
     * @param block The block being mined
     * @param x Block X coordinate
     * @param y Block Y coordinate
     * @param z Block Z coordinate
     * @param damageAmount How much damage to apply
     * @param entity The entity using the tool
     * @return true if damage was applied, false if prevented due to inefficiency
     */
    public static boolean damageItemIfEfficient(
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
            return false;
        }

        if (!itemStack.isItemStackDamageable()) {
            return false;
        }

        // Get the efficiency of the current tool on this block
        float toolEfficiency = itemStack.getStrVsBlock(world, block, x, y, z);

        // Get bare hands efficiency (1.0x - no bonus)
        float bareHandsEfficiency = 1.0f;

        boolean moreEfficient = toolEfficiency > bareHandsEfficiency;
        boolean isEfficientVsBlock = itemStack.getItem().isEfficientVsBlock(itemStack, world, block, x, y, z);
        boolean canHarvestBlock = itemStack.getItem().canHarvestBlock(itemStack, world, block, x, y, z);
        boolean canConvertBlock = block.canConvertBlock(itemStack, world, x, y, z);

        boolean betterThanNothing = moreEfficient || isEfficientVsBlock || canHarvestBlock || canConvertBlock;

        if (!world.isRemote && DEBUG) {
            System.out.println("-------------------------");
            System.out.println("toolEfficiency: " + toolEfficiency);
            System.out.println("isEfficientVsBlock: " + isEfficientVsBlock);
            System.out.println("canHarvestBlock: " + canHarvestBlock);
            System.out.println("canConvertBlock: " + canConvertBlock);
        }

        // Only apply damage if tool is actually better than nothing
        if (betterThanNothing) {
            itemStack.damageItem(damageAmount, entity);
            if (!world.isRemote && DEBUG) {
                System.out.println("Damaged item by " + damageAmount);
            }
            return true;
        }

        return false;
    }

}