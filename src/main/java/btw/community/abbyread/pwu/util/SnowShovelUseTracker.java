package btw.community.abbyread.pwu.util;

public class SnowShovelUseTracker {
    private static final boolean DEBUG = true;

    public static boolean incrementAndCheck(PWUPlayerDataExtension data) {
        int count = data.getShovelUseCounter() + 1;
        boolean shouldDamage = (count >= 4);
        data.setShovelUseCounter(shouldDamage ? 0 : count);

        if (DEBUG) {
            System.out.println("[PWU DEBUG] incrementAndCheck: count=" + count
                    + " -> shouldDamage=" + shouldDamage
                    + " | stored=" + data.getShovelUseCounter());
        }

        return shouldDamage;
    }
}
