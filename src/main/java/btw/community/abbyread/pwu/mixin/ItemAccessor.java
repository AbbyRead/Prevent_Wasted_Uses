package btw.community.abbyread.pwu.mixin;

import net.minecraft.src.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Item.class)
public interface ItemAccessor {

    @Accessor("maxDamage")
    int getMaxDamage();

    @Accessor("maxDamage")
    void setMaxDamage(int maxDamage);
}
