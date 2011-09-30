package edu.wustl.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Clones a <tt>Serializable</tt> object by writing out to a stream and
 * reading it back into a new object.
 * 
 * <p>
 * Subclasses can override following methods to customize the
 * serialization/deserialization process.
 * <ol>
 * <li><tt>createObjectOutputStream()</tt> and
 * <tt>createObjectInputStream</tt> to provide customized streams. If not
 * overridden, <tt>CloneOutputStream</tt> and <tt>CloneInputStream</tt> are
 * used. </li>
 * <li><tt>createOutputStream()</tt> and <tt>createOutputStream()</tt> to
 * provide the underlying streams to which the serialized form is to be written
 * to. If not overridden, <tt>ByteArrayOutputStream</tt> and
 * <tt>ByteArrayInputStream</tt> are used.</li>
 * </ol>
 * 
 * @author srinath_k
 * 
 */
public class ObjectCloner {
    /**
     * The output stream used while serializing an object in the cloning
     * process. Overrides the <tt>annotateClass()</tt> and
     * <tt>annotateProxyClass()</tt> methods of <tt>ObjectOutputStream</tt>
     * to keep track of the classes whose objects are serialized.<br>
     * See
     * http://weblogs.java.net/blog/emcmanus/archive/2007/04/cloning_java_ob.html
     * for details.
     * 
     * @author srinath_k
     */
    public static class CloneOutputStream extends ObjectOutputStream {
        private Queue<Class<?>> classQueue;

        private OutputStream out;

        protected CloneOutputStream(OutputStream out) throws IOException {
            super(out);
            this.out = out;
        }

        @Override
        protected void annotateClass(Class<?> c) {
            classQueue.add(c);
        }

        @Override
        protected void annotateProxyClass(Class<?> c) {
            classQueue.add(c);
        }

        protected final OutputStream underlyingStream() {
            return out;
        }
    }

    /**
     * The input stream used while deserializing in the cloning process.
     * Overrides the <tt>resolveClass()</tt> and <tt>resolveProxyClass()</tt>
     * methods of <tt>ObjectInputStream</tt> to provide the <tt>Class</tt>
     * to be used in deserialization.<br>
     * See
     * http://weblogs.java.net/blog/emcmanus/archive/2007/04/cloning_java_ob.html
     * for details.
     * 
     * @author srinath_k
     */
    public static class CloneInputStream extends ObjectInputStream {
        private Queue<Class<?>> classQueue;

        protected CloneInputStream(InputStream in) throws IOException {
            super(in);
        }

        @Override
        protected Class<?> resolveClass(ObjectStreamClass osc) throws IOException, ClassNotFoundException {
            Class<?> c = classQueue.poll();
            String expected = osc.getName();
            String found = (c == null) ? null : c.getName();
            if (!expected.equals(found)) {
                throw new InvalidClassException("Classes desynchronized: " + "found " + found + " when expecting "
                        + expected);
            }
            return c;
        }

        @Override
        protected Class<?> resolveProxyClass(String[] interfaceNames) throws IOException, ClassNotFoundException {
            return classQueue.poll();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T clone(T obj) {
        if (!(obj instanceof Serializable)) {
            throw new IllegalArgumentException("Can't clone object of type " + obj.getClass()
                    + " since the class is not serializable.");
        }
        classQueue.clear();
        try {
            objOut = createObjectOutputStream();
            objOut.classQueue = classQueue;
            objOut.writeObject(obj);
            objOut.flush();

            CloneInputStream objIn = createObjectInputStream();
            objIn.classQueue = classQueue;
            T res = (T) objIn.readObject();
            objOut.close();
            objIn.close();
            return res;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            // won't occur.
            throw new RuntimeException("Can't occur.");
        }
    }

    protected CloneOutputStream createObjectOutputStream() throws IOException {
        return new CloneOutputStream(createOutputStream());
    }

    protected CloneInputStream createObjectInputStream() throws IOException {
        return new CloneInputStream(createInputStream());
    }

    protected InputStream createInputStream() {
        return new ByteArrayInputStream(((ByteArrayOutputStream) objOut.underlyingStream()).toByteArray());
    }

    protected OutputStream createOutputStream() {
        return new ByteArrayOutputStream();
    }

    private CloneOutputStream objOut;

    private Queue<Class<?>> classQueue = new LinkedList<Class<?>>();
}
