package clutter.core;

public class Debug {
    public static void log(Object o, String message) {
        System.out.println(o.getClass().getSimpleName() + ": " + message);
    }
}
