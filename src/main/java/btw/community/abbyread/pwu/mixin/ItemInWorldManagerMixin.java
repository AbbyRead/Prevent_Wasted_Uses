package btw.community.abbyread.pwu.mixin;

import net.minecraft.src.*;
import btw.community.abbyread.pwu.util.UsefulnessHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemInWorldManager.class)
public abstract class ItemInWorldManagerMixin {

    @Redirect(
            method = "survivalTryHarvestBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/src/Block;convertBlock(Lnet/minecraft/src/ItemStack;Lnet/minecraft/src/World;IIII)Z"
            )
    )
    private boolean recordConversionIfSuccessful(
            Block block,
            ItemStack stack,
            World world,
            int x,
            int y,
            int z,
            int fromSide
    ) {
        boolean convertedSuccessfully = block.convertBlock(stack, world, x, y, z, fromSide);

        if (convertedSuccessfully && stack != null && !world.isRemote) {
            UsefulnessHelper.markAsConverted(stack, block.blockID);
        }

        return convertedSuccessfully;
    }
}
