package us.cyrien.mcutils;


import us.cyrien.mcutils.config.ConfigurationBuilder;
import us.cyrien.mcutils.config.ConfigurationInjector;
import us.cyrien.mcutils.diagnosis.ReportLoader;
import us.cyrien.mcutils.dispatcher.help.HelpTopicUtil;
import us.cyrien.mcutils.events.BukkitEventsInjector;
import us.cyrien.mcutils.hook.HookInjector;
import us.cyrien.mcutils.hook.HookLoader;
import us.cyrien.mcutils.hook.IPluginHook;
import us.cyrien.mcutils.inject.FrameInjector;
import us.cyrien.mcutils.loader.CommandInjector;
import us.cyrien.mcutils.logger.Logger;
import us.cyrien.mcutils.module.ModuleInjector;
import us.cyrien.mcutils.module.ModuleLoader;

public class Frame {
    public static void main() {
        Logger.info("- Starting plugin framework initialization.");

        // Build configurations
        try {
            ConfigurationBuilder.buildAwaiting();
        } catch (Exception e) {
            Logger.err("- Error building configurations!");
            Logger.err(e.getMessage());
            e.printStackTrace();
        }

        new FrameInjector()
                .injector(new CommandInjector())
                .injector(new HookInjector())
                .injector(new ModuleInjector())
                .injector(new ConfigurationInjector())
                .injector(new BukkitEventsInjector())
                .injectAll();
        HelpTopicUtil.index();

        Logger.info("- Finished plugin framework initialization.");
    }

    /*
     * Util functions for adding modules, configurations and hooks.
     */

    /**
     * Register and load your module into Frame
     *
     * @param clazz Class containing your module
     */
    public static void addModule(Class clazz) {
        ModuleLoader.add(clazz);
    }

    /**
     * Register and load diagnostics reporter into Frame
     *
     * @param clazz Class that implements {@link IPluginHook}
     */
    public static void addReporter(Class clazz) {
        ReportLoader.addReporter(clazz);
    }

    public static void addConfiguration(Class clazz) {
        ConfigurationBuilder.add(clazz);
    }

    /**
     * Register specified hook in Frame
     *
     * @param clazz Class that implements {@link IPluginHook}
     */
    public static void addHook(Class<? extends IPluginHook> clazz) {
        HookLoader.addHook(clazz);
    }

    public static Object getConfig(Class clazz) {
        return ConfigurationBuilder.get(clazz);
    }
}
