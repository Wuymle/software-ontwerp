package application.test;

import java.lang.reflect.Field;
import application.Application;
import application.DatabaseAppContext;
import clutter.ApplicationWindow;

public class TestHelper {
    public static Object getPrivateField(Object obj, String fieldName) throws Exception {
        Class<?> clazz = obj.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true); // Bypass private access
                return field.get(obj);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass(); // Move to the superclass
            }
        }
        throw new NoSuchFieldException("Field '" + fieldName + "' not found in class hierarchy.");
    }

    public static void setPrivateField(Object obj, String fieldName, Object value)
            throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    public static ApplicationWindow window = new ApplicationWindow("SuperDBMS",
            (DatabaseAppContext appContext) -> new Application(appContext),
            (ApplicationWindow appWindow) -> new DatabaseAppContext(appWindow));

    public static DatabaseAppContext context = new DatabaseAppContext(window);
}
