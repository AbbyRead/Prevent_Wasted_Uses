package btw.community.abbyread.pwu;

import btw.AddonHandler;
import btw.BTWAddon;

public class PreventWastedUsesAddon extends BTWAddon {
    private static PreventWastedUsesAddon instance;

    public PreventWastedUsesAddon() {
        super();
    }

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
    }
}