package edu.wustl.common.util;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Hash set implementation that is backed by an <tt>IdentityHashMap</tt>. The
 * implementation code is copied from <tt>HashSet</tt>; only change is that
 * references are to <tt>IdentityHashMap</tt> instead of <tt>HashMap</tt>.
 * 
 * @author srinath_k
 */
public class IdentityHashSet<E> extends AbstractSet<E> implements Set<E>, Cloneable, Serializable {
    private static final long serialVersionUID = -9161619520802182726L;

    private transient IdentityHashMap<E, Object> map;

    // Dummy value to associate with an Object in the backing Map
    private static final Object PRESENT = new Object();

    /**
     * Constructs a new, empty set; the backing <tt>IdentityHashMap</tt>
     * instance has default initial capacity (16) and load factor (0.75).
     */
    public IdentityHashSet() {
        map = new IdentityHashMap<E, Object>();
    }

    /**
     * Constructs a new set containing the elements in the specified collection.
     * 
     * @param c the collection whose elements are to be placed into this set.
     * @throws NullPointerException if the specified collection is null.
     */
    public IdentityHashSet(Collection<? extends E> c) {
        map = new IdentityHashMap<E, Object>();
        addAll(c);
    }

    /**
     * Returns an iterator over the elements in this set. The elements are
     * returned in no particular order.
     * 
     * @return an Iterator over the elements in this set.
     * @see ConcurrentModificationException
     */
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    /**
     * Returns the number of elements in this set (its cardinality).
     * 
     * @return the number of elements in this set (its cardinality).
     */
    public int size() {
        return map.size();
    }

    /**
     * Returns <tt>true</tt> if this set contains no elements.
     * 
     * @return <tt>true</tt> if this set contains no elements.
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Returns <tt>true</tt> if this set contains the specified element.
     * 
     * @param o element whose presence in this set is to be tested.
     * @return <tt>true</tt> if this set contains the specified element.
     */
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    /**
     * Adds the specified element to this set if it is not already present.
     * 
     * @param o element to be added to this set.
     * @return <tt>true</tt> if the set did not already contain the specified
     *         element.
     */
    public boolean add(E o) {
        return map.put(o, PRESENT) == null;
    }

    /**
     * Removes the specified element from this set if it is present.
     * 
     * @param o object to be removed from this set, if present.
     * @return <tt>true</tt> if the set contained the specified element.
     */
    public boolean remove(Object o) {
        return map.remove(o) == PRESENT;
    }

    /**
     * Removes all of the elements from this set.
     */
    public void clear() {
        map.clear();
    }

    /**
     * Returns a shallow copy of this <tt>IdentityHashSet</tt> instance: the
     * elements themselves are not cloned.
     * 
     * @return a shallow copy of this set.
     */
    @SuppressWarnings("unchecked")
    public Object clone() {
        try {
            IdentityHashSet<E> newSet = (IdentityHashSet<E>) super.clone();
            newSet.map = (IdentityHashMap<E, Object>) map.clone();
            return newSet;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    /**
     * Save the state of this <tt>IdentityHashSet</tt> instance to a stream
     * (that is, serialize this set).
     * 
     * @serialData Tthe size of the set (the number of elements it contains)
     *             (int) is emitted, followed by all of its elements (each an
     *             Object) in no particular order.
     */
    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
        // Write out any hidden serialization magic
        s.defaultWriteObject();

        // Write out size
        s.writeInt(map.size());

        // Write out all elements in the proper order.
        for (Iterator i = map.keySet().iterator(); i.hasNext();)
            s.writeObject(i.next());
    }

    /**
     * Reconstitute the <tt>IdentityHashSet</tt> instance from a stream (that
     * is, deserialize it).
     */
    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
        // Read in any hidden serialization magic
        s.defaultReadObject();

        // create backing IdentityHashMap
        map = new IdentityHashMap<E, Object>();

        // Read in size
        int size = s.readInt();

        // Read in all elements in the proper order.
        for (int i = 0; i < size; i++) {
            E e = (E) s.readObject();
            map.put(e, PRESENT);
        }
    }
}
