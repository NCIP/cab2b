package edu.wustl.cab2b.server.queryengine;

import java.util.HashMap;
import java.util.Map;

import edu.wustl.cab2b.common.queryengine.result.IFullyInitialializedRecord;
import edu.wustl.cab2b.common.queryengine.result.ILazyParams;
import edu.wustl.cab2b.common.queryengine.result.IPartiallyInitializedRecord;

public class LazyInitializer {
    private static Map<Integer, IFullyInitialializedRecord<?, ?>> map = new HashMap<Integer, IFullyInitialializedRecord<?, ?>>();

    private static int handle = 0;

    private static synchronized int nextHandle() {
        return handle++;
    }

    public static synchronized int register(IFullyInitialializedRecord<?, ?> fir) {
        int handle = nextHandle();
        map.put(handle, fir);
        return handle;
    }

    public static IPartiallyInitializedRecord<?, ?> getView(int handle, ILazyParams params) {
        return getFullyInitialializedRecord(handle).view(params, handle);
    }

    public static IFullyInitialializedRecord<?, ?> getFullyInitialializedRecord(int handle) {
        if (!map.containsKey(handle)) {
            throw new IllegalArgumentException("Invalid handle " + handle);
        }
        return map.get(handle);
    }

    public static synchronized void unregister(int handle) {
        if (!map.containsKey(handle)) {
            throw new IllegalArgumentException("Invalid handle " + handle);
        }
        map.remove(handle);
    }
}
