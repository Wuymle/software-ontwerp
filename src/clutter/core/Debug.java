package clutter.core;

import clutter.widgetinterfaces.Debuggable;

public class Debug {
    private static int indentations = 0;

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

    public static void warn(Debuggable o, Object... message) {
        StringBuilder sb = new StringBuilder();
        for (Object msg : message) {
            sb.append(msg.toString()).append(" ");
        }
        System.out.println("WARNING: " + o.getClass().getSimpleName() + ": " + sb.toString().trim());
    }

    public static void beginDebug(Debuggable o) {
        if (!o.isDebug())
            return;
        System.out.println(o.getClass().getSimpleName() + ": {");
        indentations++;
    }

    public static void endDebug(Debuggable o) {
        if (!o.isDebug())
            return;
        indentations--;
        System.out.println("}");
    }
}
