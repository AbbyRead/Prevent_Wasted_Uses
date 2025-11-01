package btw.community.abbyread.pwu.util;

import btw.block.BTWBlocks;
import net.minecraft.src.*;
import java.util.WeakHashMap;
import java.util.Map;

public class UsefulnessHelper {
    private static final boolean DEBUG = false;

    // Track converted item stacks with their conversion context
    private static final Map<ItemStack, ConversionContext> conversionContexts = new WeakHashMap<>();
    private static final long CONVERSION_FLAG_TTL_MILLIS = 100L;

    /**
     * Stores information about a block conversion event.
     */
    public static class ConversionContext {
        public final int originalBlockID;
        public final Block originalBlock;
        public final long timestamp;

        public ConversionContext(int blockID, Block block) {
            this.originalBlockID = blockID;
            this.originalBlock = block;
            this.timestamp = System.currentTimeMillis();
        }
    }

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

        // Check if this block was just converted
        boolean wasJustConverted = isBlockJustConverted(itemStack);

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
            System.out.println("=== damageIfUseful DEBUG ===");
            System.out.println("Block: " + block.getClass().getSimpleName() + " (ID: " + block.blockID + ")");
            System.out.println("Block Material: " + block.blockMaterial);
            System.out.println("Tool: " + itemStack.getItem().getClass().getSimpleName());
            System.out.println("toolEfficiency:     " + toolEfficiency);
            System.out.println("bareHandsEfficiency: 1.0");
            System.out.println("moreEfficient:      " + moreEfficient);
            System.out.println("isEfficientVsBlock: " + isEfficientVsBlock);
            System.out.println("canHarvestBlock:    " + canHarvestBlock);
            System.out.println("canConvertBlock:    " + canConvertBlock);
            System.out.println("wasJustConverted:   " + wasJustConverted);
            System.out.println("betterThanNothing:  " + betterThanNothing);
        }

        // Only apply damage if tool is actually better than nothing
        if (betterThanNothing) {
            itemStack.damageItem(damageAmount, entity);

            // Clear the conversion flag AFTER we've made our decision and applied damage
            if (wasJustConverted && !world.isRemote) {
                clearConversionFlag(itemStack);
            }

            if (!world.isRemote && DEBUG) {
                System.out.println(">>> DAMAGE APPLIED: " + damageAmount);
                System.out.println();
            }
        } else {
            if (!world.isRemote && DEBUG) {
                System.out.println(">>> DAMAGE PREVENTED");
                System.out.println();
            }
        }
    }

    /**
     * Check if the itemstack was recently converted using server-side tracking.
     * Validates that the conversion was for a block type we care about (not stone/logs).
     */
    private static boolean isBlockJustConverted(ItemStack itemStack) {
        ConversionContext ctx = conversionContexts.get(itemStack);
        if (ctx != null) {
            long age = System.currentTimeMillis() - ctx.timestamp;

            // Check if still within TTL
            if (age < CONVERSION_FLAG_TTL_MILLIS) {
                // Reject false positives: if the original block was stone or log, don't count it
                if (ctx.originalBlock != null) {
                    if (ctx.originalBlock.blockMaterial == Material.rock) return false;
                    if (ctx.originalBlock.blockMaterial == BTWBlocks.logMaterial) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Clear the conversion flag for this itemstack.
     */
    private static void clearConversionFlag(ItemStack itemStack) {
        conversionContexts.remove(itemStack);
    }

    /**
     * Mark an itemstack as having just been converted from a specific block.
     * Called from ItemInWorldManagerMixin.
     */
    public static void markAsConverted(ItemStack itemStack, int originalBlockID) {
        if (itemStack != null) {
            Block originalBlock = Block.blocksList[originalBlockID];
            conversionContexts.put(itemStack, new ConversionContext(originalBlockID, originalBlock));
        }
    }
}
