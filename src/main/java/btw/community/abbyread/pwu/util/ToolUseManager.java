package btw.community.abbyread.pwu.util;

import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;

public class ToolUseManager {
    public static boolean shouldConsumeDurability(EntityPlayer player, ItemStack tool, Block block, int x, int y, int z) {
        // Return false if the block shouldn't cost durability
        return false;
    }

    public static boolean shouldConsumeDurability(EntityPlayer player, ItemStack tool, Entity target) {
        // Return false if target doesn’t justify durability loss
        return false;
    }
}
