package clutter.debug;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * A class for debugging.
 */
public class Debug {
    private static Set<DebugMode> debugModes = new HashSet<DebugMode>();
    private static Stack<Debuggable> debuggables = new Stack<Debuggable>();
    private static Debuggable lastDebuggable = null;

    public static void log(Debuggable o, DebugMode mode, Object... message) {
        if (!o.hasDebugMode(mode) && !debugModes.contains(mode))
            return;
        printIndented(o.getClass().getSimpleName(), ":", message);
    }

    private static void printIndented(Object... message) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < debuggables.size() - 1; i++)
            sb.append("  ");
        if (!debuggables.empty())
            sb.append(" |");
        for (Object msg : message) {
            if (msg == null) {
                sb.append("null").append(" ");
            } else if (msg.getClass().isArray()) {
                Object[] array = (Object[]) msg;
                for (Object element : array) {
                    sb.append(element == null ? "null" : element.toString()).append(" ");
                }
            } else {
                sb.append(msg.toString()).append(" ");
            }
        }
        System.out.println(sb.toString());
    }

    public static void log(Debuggable o, DebugMode mode, Object messageBefore, Runnable runnable,
            Object messageAfter) {
        lastDebuggable = o;
        printName(o);
        printIndented("->", messageBefore);
        runIndented(o, runnable);
        printIndented("<-", messageAfter);
    }

    public static void log(Debuggable o, DebugMode mode, Runnable runnable) {
        lastDebuggable = o;
        printName(o);
        runIndented(o, runnable);
    }

    private static void printName(Debuggable o) {
        if (lastDebuggable == o)
            return;
        printIndented(o.getClass().getSimpleName(), ":");
    }

    /**
     * warn about a message
     * 
     * @param o the object
     * @param message the message
     */
    public static void warn(Debuggable o, DebugMode mode, Object... message) {
        if (!o.hasDebugMode(mode) && !debugModes.contains(mode))
            return;
        lastDebuggable = o;
        printIndented("WARNING: ", o.getClass().getSimpleName(), ":");
    }

    private static void runIndented(Debuggable o, Runnable runnable) {
        debuggables.push(o);
        try {
            runnable.run();
        } catch (Exception e) {
            System.out.println("Debugmodes: " + debugModes);
            System.out.println("Error: " + e.getMessage() + " in");
            for (int i = 0; i < debuggables.size(); i++) {
                for (int j = 0; j < i-1; j++)
                    System.out.print("  ");
                if (i != 0)
                    System.out.print("> ");
                System.out.println(debuggables.removeFirst().getClass().getSimpleName());
            }
            System.exit(-1);            
        }
        debuggables.pop();
    }

    public static void debug(DebugMode mode, Runnable runnable) {
        boolean newMode = debugModes.add(mode);
        printIndented("DEBUG START", mode);
        runIndented(null, runnable);
        printIndented("DEBUG END", mode);
        if (newMode)
            debugModes.remove(mode);
    }
}
