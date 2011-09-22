package edu.wustl.common.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import org.hibernate.type.BagType;
import org.hibernate.type.ListType;
import org.hibernate.type.SetType;
import org.hibernate.type.Type;

import edu.wustl.common.util.IdentityHashSet;

/**
 * Removes all hibernate references from a fully initialized object graph. If
 * the object graph is not fully initialized (i.e. contains some lazy
 * uninitialized associations/collections), then DO NOT use this class.
 * 
 * TODO maps are not cleansed
 * 
 * @author srinath_k
 * 
 */
public class HibernateCleanser {
    private final Object obj;

    private final Map<Collection<Object>, Collection<Object>> oldToNew;

    private final Set<Object> cleanedObjects;

    private final Metadata metadata;

    public HibernateCleanser(Object obj) {
        this(obj, new IdentityHashMap<Collection<Object>, Collection<Object>>(), new IdentityHashSet<Object>());
    }

    private HibernateCleanser(Object obj, Map<Collection<Object>, Collection<Object>> oldToNew,
            Set<Object> cleanedObjects) {
        this.obj = obj;
        this.oldToNew = oldToNew;
        this.cleanedObjects = cleanedObjects;
        this.metadata = new Metadata(obj);
    }

    /**
     * @param nullifyIds if <tt>true</tt> ids of the objects are set to
     *            <tt>null</tt>; otherwise the ids are left untouched.
     */
    public void clean(boolean nullifyIds) {
        if (!metadata.present()) {
            return;
        }
        if (obj instanceof Collection) {
            throw new IllegalArgumentException("can't clean a collection.");
        }
        if (cleanedObjects.contains(obj)) {
            return;
        }
        cleanedObjects.add(obj);
        if (nullifyIds) {
            processIdentifier();
        }
        processCollections();
        processAssociations();
    }

    /**
     * Nullifies ids of all the objects.
     * 
     * @see #clean(boolean)
     */
    public void clean() {
        clean(true);
    }

    private void processIdentifier() {
        metadata.nullifyId();
    }

    private void processAssociations() {
        for (String name : metadata.getAssociations()) {
            // TODO check proxies??
            recursiveClean(metadata.getValue(name));
        }
    }

    private void processCollections() {
        for (String name : metadata.getCollections()) {
            Collection<Object> newColl = newCollection(name);
            metadata.setValue(name, newColl);
        }
    }

    @SuppressWarnings("unchecked")
    private Collection<Object> newCollection(String name) {
        Collection<Object> old = (Collection<Object>) metadata.getValue(name);
        if (oldToNew.containsKey(old)) {
            return oldToNew.get(old);
        }
        Collection<Object> res = createCollectionForType(metadata.getType(name));
        oldToNew.put(old, res);
        for (Object e : old) {
            recursiveClean(e);
            res.add(e);
        }
        return res;
    }

    private Collection<Object> createCollectionForType(Type collType) {
        if (collType instanceof SetType) {
            return new HashSet<Object>();
        } else if (collType instanceof ListType || collType instanceof BagType) {
            return new ArrayList<Object>();
        }
        throw new UnsupportedOperationException("Collections of type " + collType + " cannot be cleansed.");
    }

    private void recursiveClean(Object childObj) {
        new HibernateCleanser(childObj, oldToNew, cleanedObjects).clean();
    }

}
