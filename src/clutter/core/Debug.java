package clutter.core;

import clutter.widgetinterfaces.Debuggable;

/**
 * A class for debugging.
 */
public class Debug {
    private static int indentations = 0;

    /**
     * @param o       the object
     * @param message the message
     */
    public static void log(Debuggable o, Object... message) {
        if (!o.isDebug())
            return;
        StringBuilder sb = new StringBuilder();
        for (Object msg : message) {
            if (msg == null) {
                sb.append("null").append(" ");
                continue;
            }
            sb.append(msg.toString()).append(" ");
        }
        for (int i = 0; i < indentations; i++) {
            sb.insert(0, "    ");
        }
        System.out.println(o.getClass().getSimpleName() + ": " + sb.toString().trim());
    }

    /**
     * @param o       the object
     * @param message the message
     */
    public static void warn(Debuggable o, Object... message) {
        StringBuilder sb = new StringBuilder();
        for (Object msg : message) {
            sb.append(msg.toString()).append(" ");
        }
        System.out.println("WARNING: " + o.getClass().getSimpleName() + ": " + sb.toString().trim());
    }

    /**
     * @param o       the object
     * @param message the message
     */
    public static void beginDebug(Debuggable o) {
        if (!o.isDebug())
            return;
        System.out.println(o.getClass().getSimpleName() + ": {");
        indentations++;
    }

    /**
     * @param o the object
     */
    public static void endDebug(Debuggable o) {
        if (!o.isDebug())
            return;
        indentations--;
        System.out.println("}");
    }
}
