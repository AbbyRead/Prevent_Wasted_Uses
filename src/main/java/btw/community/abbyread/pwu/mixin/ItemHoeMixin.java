package btw.community.abbyread.pwu.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemHoe;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import btw.community.abbyread.pwu.util.ToolEfficiencyChecker;

@Mixin(ItemHoe.class)
public class ItemHoeMixin {

    /**
     * For ItemHoe, we need to inject logic rather than redirect.
     * We check efficiency BEFORE damaging, and return false if not efficient.
     */
    @Inject(
            method = "onItemUse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/src/ItemStack;damageItem(ILnet/minecraft/src/EntityLivingBase;)V"
            ),
            cancellable = true
    )
    private void injectEfficiencyCheck(
            ItemStack itemStack,
            EntityPlayer entityPlayer,
            World world,
            int i,
            int j,
            int k,
            int l,
            float f,
            float g,
            float h,
            CallbackInfoReturnable<Boolean> cir
    ) {
        // Get the block being tilled
        int blockID = world.getBlockId(i, j, k);
        Block block = Block.blocksList[blockID];

        // Check efficiency - if not efficient, cancel and return false
        if (block != null) {
            boolean wasEfficient = ToolEfficiencyChecker.damageItemIfEfficient(
                    itemStack,
                    world,
                    block,
                    i, j, k,
                    1,
                    entityPlayer
            );

            // If not efficient, cancel the method (don't till the ground)
            if (!wasEfficient) {
                cir.setReturnValue(false);
            }
        }
    }
}