package clutter.core;

public class Debug {
    public static void log(Object o, Object... message) {
        StringBuilder sb = new StringBuilder();
        for (Object msg : message) {
            sb.append(msg.toString()).append(" ");
        }
        System.out.println(o.getClass().getSimpleName() + ": " + sb.toString().trim());
    }
}
