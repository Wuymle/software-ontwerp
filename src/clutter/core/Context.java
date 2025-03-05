package clutter.core;

import java.util.HashMap;
import java.util.Map;

public class Context {
    private final Map<Class<?>, Object> providers = new HashMap<>();

    public <T> void putProvider(Class<T> type, T provider) {
        providers.put(type, provider);
    }

    public <T> T getProvider(Class<T> type) {
        return type.cast(providers.get(type));
    }
}
