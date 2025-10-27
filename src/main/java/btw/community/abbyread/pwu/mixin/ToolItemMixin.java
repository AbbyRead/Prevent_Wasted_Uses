package btw.community.abbyread.pwu.mixin;

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
            UsefulnessHelper.damageIfUseful(
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

//    @Inject(method = "isEfficientVsBlock", at = @At("RETURN"))
//    private void printStats(ItemStack stack, World world, Block block, int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
//        System.out.println("block.blockMaterial.isToolNotRequired() returns " + block.blockMaterial.isToolNotRequired());
//        System.out.println("this.canHarvestBlock(stack, world, block, i, j, k) returns " + ((ToolItem)(Object)this).canHarvestBlock(stack, world, block, i, j, k));
//        System.out.println("this.isToolTypeEfficientVsBlockType(block) returns "+ ((ToolItem)(Object)this).isToolTypeEfficientVsBlockType(block));
//    }
}