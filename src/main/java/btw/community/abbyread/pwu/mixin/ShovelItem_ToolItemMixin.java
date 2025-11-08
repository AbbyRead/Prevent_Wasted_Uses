package btw.community.abbyread.pwu.mixin;

import btw.community.abbyread.pwu.util.PWUPlayerDataExtension;
import btw.community.abbyread.pwu.util.SnowShovelUseTracker;
import btw.item.items.ShovelItem;
import btw.item.items.ToolItem;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ToolItem.class)
public class ShovelItem_ToolItemMixin {

    @Inject(
            method = "onBlockDestroyed",
            at = @At("HEAD"),
            cancellable = true)
    private void redirectSnowDamageControl(
            ItemStack stack,
            World world,
            int blockID,
            int x, int y, int z,
            EntityLivingBase usingEntity,
            CallbackInfoReturnable<Boolean> cir) {

        Block block = Block.blocksList[blockID];
        if (stack == null) return;
        if (block != Block.snow) return;
        if (!(stack.getItem() instanceof ShovelItem)) return;
        if (!(usingEntity instanceof EntityPlayer player)) return;
        if (!(player instanceof PWUPlayerDataExtension data)) return;

        // --- only run this logic server-side ---
        if (!world.isRemote) {
            boolean shouldDamage = SnowShovelUseTracker.incrementAndCheck(data);
            if (!shouldDamage) {
                cir.setReturnValue(true);
                cir.cancel();
            }
        } else {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
