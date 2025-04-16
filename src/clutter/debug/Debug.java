package clutter.debug;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
        sb.append("  ".repeat(debuggables.size()));
        sb.append(" |");
        for (Object msg : message) {
            if (msg == null)
                sb.append("null").append(" ");
            else if (msg.getClass().isArray()) {
                for (Object element : (Object[]) msg)
                    sb.append(element == null ? "null" : element.toString()).append(" ");
            } else
                sb.append(msg.toString()).append(" ");
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
            Throwable filteredTrace = filterStackTrace(filterStackTrace(e, "debug"), "java");
            filteredTrace.printStackTrace();
            System.out.println("Debugmodes: " + debugModes);
            System.out.println("Error: " + e.getMessage() + " in");
            int debuggableCount = debuggables.size();
            for (int i = 0; i < debuggableCount; i++) {
                System.out.print("  ".repeat(i));
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
        runnable.run();
        printIndented("DEBUG END", mode);
        if (newMode)
            debugModes.remove(mode);
    }

    private static Throwable filterStackTrace(Throwable t, String excludePattern) {
        StackTraceElement[] original = t.getStackTrace();
        List<StackTraceElement> filtered = new ArrayList<>();
        for (StackTraceElement element : original) {
            if (!element.getClassName().contains(excludePattern)) {
                filtered.add(element);
            }
        }
        t.setStackTrace(filtered.toArray(new StackTraceElement[0]));
        return t;
    }
}
