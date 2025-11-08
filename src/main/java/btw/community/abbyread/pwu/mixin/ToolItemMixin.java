package btw.community.abbyread.pwu.mixin;

import btw.community.abbyread.pwu.util.TieredShovelDamage;
import btw.item.items.ShovelItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.src.Block;
import net.minecraft.src.EntityLivingBase;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import btw.item.items.ToolItem;
import btw.community.abbyread.pwu.util.UsefulnessHelper;

@Mixin(ToolItem.class)
public class ToolItemMixin {

    @Redirect(
            method = "onBlockDestroyed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/src/ItemStack;damageItem(ILnet/minecraft/src/EntityLivingBase;)V"
            )
    )
    private void redirectDamageItem(
            ItemStack itemStack,
            int damageAmount,
            EntityLivingBase entity,
            ItemStack stack,
            World world,
            int blockID,
            int x,
            int y,
            int z,
            EntityLivingBase usingEntity
    ) {
        Block block = Block.blocksList[blockID];
        if (block == null) {
            stack.damageItem(damageAmount, usingEntity);
            return;
        }

        // Determine actual damage
        int actualDamage = damageAmount;

        // If this is a shovel, use shovel-specific damage table
        if (stack.getItem() instanceof ShovelItem) {
            actualDamage = TieredShovelDamage.getDamageForBlock(block);
        }

        UsefulnessHelper.damageIfUseful(stack, world, block, x, y, z, actualDamage, usingEntity);
    }
}
