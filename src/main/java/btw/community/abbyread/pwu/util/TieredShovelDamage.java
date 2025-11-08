package btw.community.abbyread.pwu.util;

import net.minecraft.src.Block;
import java.util.HashMap;
import java.util.Map;

public class TieredShovelDamage {
    private static final Map<Block, Integer> blockDamageMap = new HashMap<>();

    static {
        // Example values
        blockDamageMap.put(Block.dirt, 4);
        blockDamageMap.put(Block.grass, 4);
        blockDamageMap.put(Block.sand, 2);
        blockDamageMap.put(Block.gravel, 4);
        blockDamageMap.put(Block.snow, 1);
        // Add more blocks as needed
    }

    public static int getDamageForBlock(Block block) {
        return blockDamageMap.getOrDefault(block, 4); // default to 4
    }
}
