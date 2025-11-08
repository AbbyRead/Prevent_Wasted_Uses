package btw.community.abbyread.pwu.mixin;

import btw.item.items.ShovelItem;
import net.minecraft.src.EnumToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShovelItem.class)
public class ShovelItemMixin {

    // Quadruple durability prior to multiplying damage for tiered damage system
    @Inject(method = "<init>(ILnet/minecraft/src/EnumToolMaterial;)V", at = @At("RETURN"))
    private void modifyConstructor(int iItemID, EnumToolMaterial material, CallbackInfo ci) {
        ((ItemAccessor)this).setMaxDamage(((ItemAccessor)this).getMaxDamage() * 4);
    }



}
