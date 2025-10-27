package btw.community.abbyread.pwu.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.src.Block;
import net.minecraft.src.EntityLivingBase;
import net.minecraft.src.ItemShears;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import btw.community.abbyread.pwu.util.UsefulnessHelper;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemShears.class)
public class ItemShearsMixin {

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
            // Default to normal damageItem routine on invalid blockID
            stack.damageItem(damageAmount, usingEntity);
        }
    }

    @Inject(method = "isEfficientVsBlock", at = @At("RETURN"), cancellable = true)
    private void ar_pwu$efficientOnTallGrass(ItemStack stack, World world, Block block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        if (block == Block.tallGrass) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "canHarvestBlock", at = @At("RETURN"), cancellable = true)
    private void ar_pwu$canHarvestTallGrass(ItemStack stack, World world, Block block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        if (block == Block.tallGrass) {
            cir.setReturnValue(true);
        }
    }

}