package us.cyrien.minecordbot;

import us.cyrien.mcutils.annotations.Hook;
import us.cyrien.minecordbot.hooks.*;

public class HookContainer {

    @Hook
    private static EssentialsHook essentialsHook;
    @Hook
    private static GriefPreventionHook griefPreventionHook;
    @Hook
    private static MCBHook mcbHook;
    @Hook
    private static mcMMOHook mcMMOHook;
    @Hook
    private static MVHook mvHook;
    @Hook
    private static PermissionsExHook permissionsExHook;
    @Hook
    private static VaultHook vaultHook;
    @Hook
    private static SuperVanishHook superVanishHook;

    public static SuperVanishHook getSuperVanishHook() {
        return superVanishHook;
    }

    public static GriefPreventionHook getGriefPreventionHook() {
        return griefPreventionHook;
    }

    public static MCBHook getMcbHook() {
        return mcbHook;
    }

    public static mcMMOHook getMcMMOHook() {
        return mcMMOHook;
    }

    public static MVHook getMvHook() {
        return mvHook;
    }

    public static PermissionsExHook getPermissionsExHook() {
        return permissionsExHook;
    }

    public static VaultHook getVaultHook() {
        return vaultHook;
    }

    public static EssentialsHook getEssentialsHook() {
        return essentialsHook;
    }
}
