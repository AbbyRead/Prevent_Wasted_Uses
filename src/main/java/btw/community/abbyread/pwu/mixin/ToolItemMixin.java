package btw.community.abbyread.pwu.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.src.Block;
import net.minecraft.src.EntityLivingBase;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import btw.item.items.ToolItem;
import btw.community.abbyread.pwu.util.ToolEfficiencyChecker;

@Mixin(ToolItem.class)
public class ToolItemMixin {

    /**
     * Redirect the damageItem call in BTW's ToolItem.onBlockDestroyed
     */
    @Redirect(
            method = "onBlockDestroyed(Lnet/minecraft/src/ItemStack;Lnet/minecraft/src/World;IIIILnet/minecraft/src/EntityLivingBase;)Z",
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
            stack.damageItem(damageAmount, usingEntity);
        }
    }
}