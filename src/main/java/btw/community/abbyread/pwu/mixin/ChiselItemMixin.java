package btw.community.abbyread.pwu.mixin;

import btw.item.items.ChiselItem;
import btw.item.items.ToolItem;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChiselItem.class)
public class ChiselItemMixin {
//    @Inject(method = "isEfficientVsBlock", at = @At("RETURN"))
//    private void printStats(ItemStack stack, World world, Block block, int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
//        System.out.println("block.blockMaterial.isToolNotRequired() returns " + block.blockMaterial.isToolNotRequired());
//        System.out.println("this.canHarvestBlock(stack, world, block, i, j, k) returns " + ((ToolItem)(Object)this).canHarvestBlock(stack, world, block, i, j, k));
//        System.out.println("this.isToolTypeEfficientVsBlockType(block) returns "+ ((ToolItem)(Object)this).isToolTypeEfficientVsBlockType(block));
//    }
}
