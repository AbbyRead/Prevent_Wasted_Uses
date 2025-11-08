package btw.community.abbyread.pwu.mixin;

import btw.community.abbyread.pwu.util.PWUPlayerDataExtension;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NBTTagCompound;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
@Implements(@Interface(iface = PWUPlayerDataExtension.class, prefix = "pwu$"))
public class EntityPlayerMixin {

    @Unique
    private static final String NBT_TAG = "PWUPlayerData";
    @Unique
    private static final boolean DEBUG = true;

    @Unique
    private int playerShovelUseCounter = 0;

    public int pwu$getShovelUseCounter() {
        return playerShovelUseCounter;
    }

    public void pwu$setShovelUseCounter(int value) {
        this.playerShovelUseCounter = value;
    }

    @Inject(method = "writeModDataToNBT", at = @At("TAIL"))
    private void onWriteModDataToNBT(NBTTagCompound tag, CallbackInfo ci) {
        NBTTagCompound myData = new NBTTagCompound();
        myData.setInteger("playerShovelUseCounter", playerShovelUseCounter);
        tag.setTag(NBT_TAG, myData);
        if (DEBUG) System.out.println("[PWU DEBUG] Saving shovel counter: " + playerShovelUseCounter);
    }

    @Inject(method = "readModDataFromNBT", at = @At("TAIL"))
    private void onReadModDataFromNBT(NBTTagCompound tag, CallbackInfo ci) {
        if (tag.hasKey(NBT_TAG)) {
            NBTTagCompound myData = tag.getCompoundTag(NBT_TAG);
            playerShovelUseCounter = myData.hasKey("playerShovelUseCounter")
                    ? myData.getInteger("playerShovelUseCounter")
                    : 0;
            if (DEBUG) System.out.println("[PWU DEBUG] Loaded shovel counter: " + playerShovelUseCounter);
        }
    }
}
