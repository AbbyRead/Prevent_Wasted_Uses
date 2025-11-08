package btw.community.abbyread.pwu.util;

public class SnowShovelUseTracker {

    public static boolean incrementAndCheck(PWUPlayerDataExtension data) {
        int count = data.getShovelUseCounter() + 1;
        boolean shouldDamage = (count >= 4);
        data.setShovelUseCounter(shouldDamage ? 0 : count);
        return shouldDamage;
    }
}
