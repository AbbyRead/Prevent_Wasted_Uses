package btw.community.abbyread.pwu.util;

import btw.block.BTWBlocks;
import net.minecraft.src.*;
import java.util.WeakHashMap;
import java.util.Map;

public class UsefulnessHelper {
    private static final boolean DEBUG = true;

    private static final Map<ItemStack, ConversionContext> conversionContexts = new WeakHashMap<>();
    private static final long CONVERSION_FLAG_TTL_MILLIS = 100L;

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
        if (itemStack == null || world == null || block == null || entity == null) {
            if (DEBUG) System.out.println("[PWU DEBUG] damageIfUseful: null parameter(s)");
            return;
        }

        if (!itemStack.isItemStackDamageable()) {
            if (DEBUG) System.out.println("[PWU DEBUG] Non-damageable item: " + itemStack);
            return;
        }

        boolean wasJustConverted = isBlockJustConverted(itemStack);

        if (DEBUG) {
            System.out.println("[PWU DEBUG] Checking usefulness for block " + block.getClass().getSimpleName()
                    + " id=" + block.blockID
                    + " | tool=" + itemStack.getItem().getClass().getSimpleName()
                    + " | converted=" + wasJustConverted);
        }

        float toolEfficiency = itemStack.getStrVsBlock(world, block, x, y, z);
        float bareHandsEfficiency = 1.0f;

        boolean moreEfficient = toolEfficiency > bareHandsEfficiency;
        boolean isEfficientVsBlock = itemStack.getItem().isEfficientVsBlock(itemStack, world, block, x, y, z);
        boolean canHarvestBlock = itemStack.getItem().canHarvestBlock(itemStack, world, block, x, y, z);
        boolean canConvertBlock = block.canConvertBlock(itemStack, world, x, y, z);

        if (block.blockMaterial == Material.rock) canConvertBlock = false;
        if (block.blockMaterial == BTWBlocks.logMaterial) canConvertBlock = false;

        boolean betterThanNothing = moreEfficient || isEfficientVsBlock || canHarvestBlock || canConvertBlock || wasJustConverted;

        if (!world.isRemote && DEBUG) {
            System.out.println("  toolEfficiency=" + toolEfficiency);
            System.out.println("  moreEfficient=" + moreEfficient);
            System.out.println("  isEfficientVsBlock=" + isEfficientVsBlock);
            System.out.println("  canHarvestBlock=" + canHarvestBlock);
            System.out.println("  canConvertBlock=" + canConvertBlock);
            System.out.println("  betterThanNothing=" + betterThanNothing);
        }

        if (betterThanNothing) {
            itemStack.damageItem(damageAmount, entity);
            if (wasJustConverted && !world.isRemote) {
                clearConversionFlag(itemStack);
            }
            if (DEBUG && !world.isRemote) System.out.println("[PWU DEBUG] >>> DAMAGE APPLIED");
        } else {
            if (DEBUG && !world.isRemote) System.out.println("[PWU DEBUG] >>> DAMAGE PREVENTED");
        }
    }

    private static boolean isBlockJustConverted(ItemStack itemStack) {
        ConversionContext ctx = conversionContexts.get(itemStack);
        if (ctx != null) {
            long age = System.currentTimeMillis() - ctx.timestamp;
            if (age < CONVERSION_FLAG_TTL_MILLIS) {
                if (ctx.originalBlock != null) {
                    if (ctx.originalBlock.blockMaterial == Material.rock) return false;
                    if (ctx.originalBlock.blockMaterial == BTWBlocks.logMaterial) return false;
                }
                if (DEBUG) System.out.println("[PWU DEBUG] isBlockJustConverted=true for " + itemStack);
                return true;
            }
        }
        return false;
    }

    private static void clearConversionFlag(ItemStack itemStack) {
        conversionContexts.remove(itemStack);
        if (DEBUG) System.out.println("[PWU DEBUG] Cleared conversion flag for " + itemStack);
    }

    public static void markAsConverted(ItemStack itemStack, int originalBlockID) {
        if (itemStack != null) {
            Block originalBlock = Block.blocksList[originalBlockID];
            conversionContexts.put(itemStack, new ConversionContext(originalBlockID, originalBlock));
            if (DEBUG) System.out.println("[PWU DEBUG] Marked as converted: " + itemStack + " from blockID " + originalBlockID);
        }
    }
}
