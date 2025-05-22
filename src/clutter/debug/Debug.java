package clutter.debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.function.Supplier;

/**
 * A class for debugging.
 */
public class Debug {
    private static Set<DebugMode> debugModes = new HashSet<DebugMode>();
    private static Stack<Debuggable> debuggables = new Stack<Debuggable>();
    private static Debuggable lastDebuggable = null;
    private static Map<DebugMode, Integer> debugCounts = new HashMap<DebugMode, Integer>();

    public static void log(Debuggable o, DebugMode mode, Object... message) {
        if (!o.hasDebugMode(mode) && !debugModes.contains(mode))
            return;
        printWithName(o, message);
    }

    public static void run(Debuggable o, DebugMode mode, Runnable runnable) {
        if (!o.hasDebugMode(mode) && !debugModes.contains(mode))
            return;
        runnable.run();
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



    public static <T> T nest(Debuggable o, DebugMode mode, Supplier<T> runnable) {
        lastDebuggable = o;
        count(mode);
        return runIndented(o, runnable, mode);
    }

    public static void nest(Debuggable o, DebugMode mode, Runnable runnable) {
        lastDebuggable = o;
        count(mode);
        runIndented(o, runnable, mode);
    }

    private static void printWithName(Debuggable o, Object... message) {
        if (lastDebuggable == o)
            printIndented(message);
        else
            printIndented(o.getClass().getSimpleName(), ":", message);
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

    private static <T> T runIndented(Debuggable o, Supplier<T> runnable, DebugMode mode) {
        T result = null;
        debuggables.push(o);
        try {
            result = runnable.get();
        } catch (Throwable throwable) {
            onError(throwable, mode);
        }
        debuggables.pop();
        return result;
    }

    private static void runIndented(Debuggable o, Runnable runnable, DebugMode mode) {
        debuggables.push(o);
        try {
            runnable.run();
        } catch (Throwable throwable) {
            onError(throwable, mode);
        }
        debuggables.pop();
    }

    private static void onError(Throwable t, DebugMode mode) {
        System.out.println("While in mode: " + mode);
        System.out.println("Error: " + t.getMessage() + " in");
        int debuggableCount = debuggables.size();
        for (int i = 0; i < debuggableCount; i++) {
            System.out.print("  ".repeat(i));
            System.out.print("> ");
            System.out.println(debuggables.removeFirst().getClass().getSimpleName());
        }
        System.exit(-1);
    }

    public static void debug(DebugMode mode, Runnable runnable) {
        if (mode == DebugMode.NONE) {
            runnable.run();
            return;
        }
        debugCounts.put(mode, 0);
        boolean newMode = debugModes.add(mode);
        printIndented("DEBUG START", mode);
        runnable.run();
        printIndented("DEBUG END", mode);
        if (newMode)
            debugModes.remove(mode);
        System.out.println("DEBUG COUNT: " + mode + ": " + debugCounts.get(mode));
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

    private static void count(DebugMode mode) {
        if (!debugModes.contains(mode))
            return;
        debugCounts.compute(mode, (k, v) -> v + 1);
    }
}
