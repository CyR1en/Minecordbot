package us.cyrien.minecordbot.instrumentation;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class MCBTransformAgent implements ClassFileTransformer {

    private static Instrumentation instrumentation = null;
    private static MCBTransformAgent transformer;

    public void agentmain(String args, Instrumentation instrument) {
        System.out.println("[Agent] Load agent into running JVM using Attach API");

        transformer = new MCBTransformAgent();
        instrumentation = instrument;
        instrumentation.addTransformer(transformer);
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        String instrumentedClassName = "CraftServer";
        String instrumentedMethodName = "broadcast";
        byte[] bytecode = classfileBuffer;
        try {
            ClassPool cPool = ClassPool.getDefault();
            CtClass ctClass = cPool.makeClass(new ByteArrayInputStream(bytecode));
            CtMethod[] ctClassMethods = ctClass.getDeclaredMethods();
            for (CtMethod ctClassMethod : ctClassMethods) {
                String xlassName = ctClassMethod.getDeclaringClass().getName();
                xlassName = xlassName.substring(xlassName.lastIndexOf("."));
                if (xlassName.equals(instrumentedClassName) && ctClassMethod.getName().equals(instrumentedMethodName)) {
                    ctClassMethod.insertAfter(
                            "BroadcastMessageEvent broadcastMessageEvent = new BroadcastMessageEvent(message, recipients);\n" +
                            "getPluginManager().callEvent(broadcastMessageEvent);\n\n" +
                            "if (broadcastMessageEvent.isCancelled()) {\n" +
                            "   return 0;\n" +
                            "}\n" +
                            "\n" +
                            "message = broadcastMessageEvent.getMessage();\n" +
                            "\n" +
                            "for (CommandSender recipient : recipients) {\n" +
                            "   recipient.sendMessage(message);\n" +
                            "}\n" +
                            "\n");
                    bytecode = ctClass.toBytecode();
                }
            }
        } catch (IOException e) {
            throw new IllegalClassFormatException(e.getMessage());
        } catch (RuntimeException e) {
            throw new IllegalClassFormatException(e.getMessage());
        } catch (CannotCompileException e) {
            throw new IllegalClassFormatException(e.getMessage());
        }
        return bytecode;
    }
}
