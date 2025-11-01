package btw.community.abbyread.pwu.mixin;

import btw.community.abbyread.pwu.util.UsefulnessHelper;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemSword.class)
public class ItemSwordMixin {

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
            stack.damageItem(damageAmount, usingEntity);
        }
    }
}
