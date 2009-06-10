package edu.wustl.cab2b.server.queryengine;

import java.util.HashMap;
import java.util.Map;

import edu.wustl.cab2b.common.queryengine.result.IFullyInitialializedRecord;
import edu.wustl.cab2b.common.queryengine.result.ILazyParams;
import edu.wustl.cab2b.common.queryengine.result.IPartiallyInitializedRecord;

/**
 * Server-side repository for the object that need lazy initialization at the
 * client. Currently the full object is stored in memory in a map.
 * 
 * @author srinath_k
 * 
 */
public class LazyInitializer {
    private static Map<Integer, IFullyInitialializedRecord<?, ?>> map = new HashMap<Integer, IFullyInitialializedRecord<?, ?>>();

    private static int handle = 0;

    private static synchronized int nextHandle() {
        return handle++;
    }

    /**
     * Registers the given full record and provides a handle for it.
     * 
     * @param fir the record to be registered.
     * @return the handle to the record.
     */
    public static synchronized int register(IFullyInitialializedRecord<?, ?> fir) {
        int handle = nextHandle();
        map.put(handle, fir);
        return handle;
    }

    /**
     * Asks the full record corresponding to the handle to provide the view
     * corresponding to the lazy params given.
     * 
     * @param handle the handle to the full record.
     * @param params the lazy params
     * @return the partially initialized record corresponding to the lazy params
     *         provided.
     * @see IFullyInitialializedRecord#view(ILazyParams, int)
     */
    public static IPartiallyInitializedRecord<?, ?> getView(int handle, ILazyParams params) {
        return getFullyInitialializedRecord(handle).view(params, handle);
    }

    public static IFullyInitialializedRecord<?, ?> getFullyInitialializedRecord(int handle) {
        if (!map.containsKey(handle)) {
            throw new IllegalArgumentException("Invalid handle " + handle);
        }
        return map.get(handle);
    }

    /**
     * Unregisters the record indicated by the handle provided.
     * 
     * @param handle the handle to the record to be unregisterd.
     */
    public static synchronized void unregister(int handle) {
        if (!map.containsKey(handle)) {
            throw new IllegalArgumentException("Invalid handle " + handle);
        }
        map.remove(handle);
    }
}
