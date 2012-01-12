package edu.wustl.common.util;

import java.io.Serializable;

import edu.wustl.common.hibernate.EnumType;

/**
 * Represents a compound enumeration formed primitive <tt>enum</tt>s. In the
 * following description, the word (primitive) <tt>enum</tt> refers to java's
 * <b><tt>enum</tt></b> type. <br>
 * A compound enum (<tt>compoundEnum</tt>) logically contains a list of
 * <tt>enum</tt> values that may belong to different <tt>enum</tt>s, but
 * are, in some context, meaningful when grouped together. For example, there
 * can exist two <tt>enum</tt>s called <tt>NumericTypes</tt> and
 * <tt>DateTypes</tt>; but we may need to group together these enumerated
 * values under a <tt>compoundEnum</tt> called <tt>AllDataTypes</tt>. Each
 * instance (value) of a <tt>compoundEnum</tt> has a corresponding
 * <tt>enum</tt> value and an ordinal in the context of this
 * <tt>compoundEnum</tt>.
 * <p>
 * It is recommended that the <tt>enum</tt>s that have some logical relation
 * share a (possibly marker) interface. For example, we could have an interface
 * <tt>IDataType</tt> that is implemented by <tt>NumericTypes</tt> and
 * <tt>DateTypes</tt>. We can the declare the
 * <tt>compoundEnum AllDataTypes</tt> as
 * 
 * <pre>
 * public class AllDataTypes&lt;T extends Enum&lt;?&gt; &amp; IDataType&gt;
 *   extends CompoundEnum&lt;AllDataTypes&lt;T&gt;, T&gt;
 * </pre>
 * 
 * <p>
 * <b>Compound enum implementation mandates:</b>
 * <ol>
 * <li> It is mandatory that a concrete <tt>compoundEnum</tt> implements
 * static methods with the following signatures:
 * <ul>
 * <li>
 * 
 * <pre>
 * // return all compound enum values
 * public static CompoundEnum[] values();
 * </pre>
 * 
 * </li>
 * <li>
 * 
 * <pre>
 * // return compound enum with given name
 * public static CompoundEnum valueOf(String name);
 * </pre>
 * 
 * <br>
 * The above static methods will help reduce the difference between
 * <tt>compoundEnum</tt>s and <tt>enum</tt>s.<br>
 * </li>
 * </ul>
 * <li> It is mandatory for a <tt>compoundEnum</tt> to implement the
 * <tt>readResolve()</tt> method to enusure unique instances (see example
 * below).</li>
 * </ol>
 * <p>
 * <b>Compound enum example:</b><br>
 * Following is an example implementation of <tt>AllDataTypes</tt>:<br>
 * 
 * <pre>
 * // type params specify that the compound enum is of type AllDataTypes and that
 * // the primitive enum must be of type IDataType
 * 
 * public class AllDataTypes&lt;T extends Enum&lt;?&gt; &amp; IDataType&gt; extends CompoundEnum&lt;AllDataTypes&lt;T&gt;, T&gt; {
 *     private static final long serialVersionUID = 7794336702657368626L;
 * 
 *     // list of the primitive enums.
 *     private static final List&lt;IDataType&gt; primitiveValues = new ArrayList&lt;IDataType&gt;();
 * 
 *     // list of compound enums; ordering is same as the &quot;values&quot; list
 *     private static final List&lt;AllDataTypes&lt;?&gt;&gt; values = new ArrayList&lt;AllDataTypes&lt;?&gt;&gt;();
 * 
 *     // ordinal tracker
 *     private static int nextOrdinal = 0;
 * 
 *     // private constructor because this is an enum
 *     private AllDataTypes(T primitiveEnum) {
 *         super(primitiveEnum, nextOrdinal++, primitiveEnum.name());
 *         primitiveValues.add(primitiveEnum);
 *         values.add(this);
 *     }
 * 
 *     // the compound enum values
 *     public static final AllDataTypes&lt;NumericTypes&gt; CInteger = new AllDataTypes&lt;NumericTypes&gt;(NumericTypes.Integer);
 * 
 *     public static final AllDataTypes&lt;NumericTypes&gt; CFloat = new AllDataTypes&lt;NumericTypes&gt;(NumericTypes.Float);
 * 
 *     public static final AllDataTypes&lt;DateTypes&gt; CDate = new AllDataTypes&lt;DateTypes&gt;(DateTypes.Date);
 * 
 *     public static final AllDataTypes&lt;DateTypes&gt; CTimestamp = new AllDataTypes&lt;DateTypes&gt;(DateTypes.Timestamp);
 * 
 *     // all the compound enum values in this compound enum.
 *     // this method is mandated by the CompoundEnum contract.
 *     public static AllDataTypes&lt;?&gt;[] values() {
 *         return values.toArray(new AllDataTypes&lt;?&gt;[0]);
 *     }
 * 
 *     // find compound enum with specified name.
 *     // this method is mandated by the CompoundEnum contract.
 *     public static AllDataTypes&lt;?&gt; valueOf(String name) {
 *         if (name == null) {
 *             throw new NullPointerException(&quot;name is null.&quot;);
 *         }
 *         for (AllDataTypes&lt;?&gt; dataType : values) {
 *             if (dataType.name().equals(name)) {
 *                 return dataType;
 *             }
 *         }
 *         throw new IllegalArgumentException(&quot;no compound enum AllDataTypes.&quot; + name);
 *     }
 * 
 *     // returns all the primitive enum values in this compound enum.
 *     public static IDataType[] primitiveValues() {
 *         return primitiveValues.toArray(new IDataType[0]);
 *     }
 * 
 *     // serialization; ensure unique instance.
 *     private Object readResolve() throws ObjectStreamException {
 *         return values.get(ordinal());
 *     }
 * }
 * </pre>
 * 
 * Note that the typesafe-enum pattern mentioned in Item 21 of "Effective Java"
 * by Joshua Bloch is to be followed to the maximum possible extent while
 * defining compound enums. For example, compound enums can also have
 * constant-specific class bodies.
 * <p>
 * <b>Hibernate note: </b> A <tt>compound enum</tt> field can be persisted
 * using {@link EnumType}.
 * 
 * @author srinath_k
 * 
 * @param <E> the specific type of the compound enum; should be same as that of
 *            the compound enum.
 * @param <T> the type of primitive enum that the compound enum corresponds to.
 */
public abstract class CompoundEnum<E extends CompoundEnum<E, T>, T extends Enum<?>>
        implements
            Comparable<E>,
            Serializable {
    private T primitiveEnum;

    private int ordinal;

    private String name;

    /**
     * Creates a compound enum with specified name, ordinal and corresponding
     * primitive enum.
     */
    protected CompoundEnum(T primitiveEnum, int ordinal, String name) {
        if (primitiveEnum == null || name == null) {
            throw new NullPointerException();
        }
        if (name.equals("")) {
            throw new IllegalArgumentException("enum name should not be empty.");
        }
        this.primitiveEnum = primitiveEnum;
        this.ordinal = ordinal;
        this.name = name;
    }

    /**
     * @return the primitive enum corresponding to this compound enum.
     */
    public T primitiveEnum() {
        return primitiveEnum;
    }

    /**
     * @return the ordinal of this compound enum.
     */
    public int ordinal() {
        return ordinal;
    }

    /**
     * @return the name of this compound name.
     */
    public String name() {
        return name;
    }

    /**
     * Compares this enum with the specified object for order based on the
     * ordinal. Compound enum constants are only comparable to other compound
     * enum constants of the same compound enum type.
     * 
     * @return a negative integer, zero, or a positive integer as this object is
     *         less than, equal to, or greater than the specified object.
     * @see Comparable
     */
    public final int compareTo(E o) {
        CompoundEnum other = (CompoundEnum) o;
        CompoundEnum self = this;
        if (self.getClass() != other.getClass() && // optimization
                self.getDeclaringClass() != other.getDeclaringClass())
            throw new ClassCastException();
        return self.ordinal - other.ordinal;
    }

    /**
     * Returns the Class object corresponding to this enum constant's enum type.
     * Two enum constants e1 and e2 are of the same enum type if and only if
     * e1.getDeclaringClass() == e2.getDeclaringClass(). (The value returned by
     * this method may differ from the one returned by the
     * {@link Object#getClass} method for enum constants with constant-specific
     * class bodies.)
     * 
     * @return the Class object corresponding to this enum constant's enum type
     */
    @SuppressWarnings("unchecked")
    public final Class<E> getDeclaringClass() {
        Class clazz = getClass();
        Class zuper = clazz.getSuperclass();
        return (zuper == CompoundEnum.class) ? clazz : zuper;
    }

    @Override
    public final boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }

    /**
     * Throws <tt>CloneNotSupportedException</tt>. This guarantees that enums
     * are never cloned, which is necessary to preserve their "singleton"
     * status.
     * 
     * @return (never returns)
     */
    protected final Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public String toString() {
        return primitiveEnum.toString();
    }
}
