package us.cyrien.minecordbot.instrumentation;

import shade.sun.tools.attach.AgentInitializationException;
import shade.sun.tools.attach.AgentLoadException;
import shade.sun.tools.attach.AttachNotSupportedException;
import shade.sun.tools.attach.spi.AttachProvider;
import shade.sun.tools.attach.BsdAttachProvider;
import shade.sun.tools.attach.LinuxAttachProvider;
import shade.sun.tools.attach.SolarisAttachProvider;
import shade.sun.tools.attach.WindowsAttachProvider;
import us.cyrien.minecordbot.Minecordbot;

import java.io.File;
import java.io.IOException;


public class Instrumentator {

    private String attachLibFolder;
    private Minecordbot minecordbot;

    public Instrumentator(Minecordbot plugin, String attachLibFolder) {
        this.minecordbot = plugin;
        this.attachLibFolder = attachLibFolder;
        System.out.println("[Minecordbot] Lib folder has been set to " + attachLibFolder);
    }

    public void instrumentate() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        Tools.addToLibPath(getLibraryPath(attachLibFolder));
        AttachProvider.setAttachProvider(getAttachProvider());
        AgentLoader.attachAgentToJVM(Tools.getCurrentPID(), MCBTransformAgent.class, AgentLoader.class);
    }

    private static String getLibraryPath(String parentDir) {
        String path = Tools.Platform.is64Bit() ? "64/" : "32/";
        switch (Tools.Platform.getPlatform()) {
            case LINUX:
                path += "linux/";
                break;
            case WINDOWS:
                path += "windows/";
                break;
            case MAC:
                path += "mac/";
                break;
            case SOLARIS:
                path += "solaris/";
                break;
            default:
                throw new UnsupportedOperationException("unsupported platform");
        }
        return new File(parentDir, path).getAbsolutePath();
    }

    private static AttachProvider getAttachProvider() {
        switch (Tools.Platform.getPlatform()) {
            case LINUX:
                return new LinuxAttachProvider();
            case WINDOWS:
                return new WindowsAttachProvider();
            case MAC:
                return new BsdAttachProvider();
            case SOLARIS:
                return new SolarisAttachProvider();
            default:
                throw new UnsupportedOperationException("unsupported platform");
        }
    }

}
