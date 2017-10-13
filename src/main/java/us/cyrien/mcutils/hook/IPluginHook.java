package us.cyrien.mcutils.hook;

/**
 * Hooks are specialised objects designed to be used to hook into other plugins.
 * To create a hook, you'll want a class that implements {@link IPluginHook}, like so:
 * <pre>
 * public class MyHook implements IPluginHook {
 *  public boolean available() {
 *      return false;
 *  }
 * }
 * </pre>
 * <p>
 * You'll want to register your hook with Frame in your onEnable() like so:
 * <pre>
 * Frame.addHook(MyHook.class);
 * </pre>
 */
public interface IPluginHook {

    /**
     * @return whether the hook is available or not; if false, the hook will never be injected, and null will be passed instead.
     */
    boolean available();

    String getPluginName();
}
