package edu.wustl.common.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

/**
 * A <tt>UserType</tt> that can be used to persist canonical representations
 * of objects.<br>
 * <b>Mapping example:</b> <br>
 * Suppose a field <tt>foo</tt> is of type <tt>com.foobar.FooClass</tt>.
 * Also suppose that there exists a canonical form of
 * <tt>com.foobar.FooClass</tt>; this canonical form is provided by a class
 * <tt>com.foobar.FooCanonicalFormProvider</tt>. Note that
 * <tt>com.foobar.FooCanonicalFormProvider</tt> should implement
 * {@link CanonicalFormProvider}.<br>
 * The field <tt>foo</tt> can then be mapped as follows:
 * 
 * <pre>
 *              &lt;property name=&quot;foo&quot; column=&quot;FOO_COL&quot;&gt;
 *                  &lt;type name=&quot;edu.wustl.common.hibernate.CanonicalFormType&quot;&gt;
 *                      &lt;param name=&quot;canonical-form-provider&quot;&gt;
 *                          com.foobar.FooCanonicalFormProvider
 *                      &lt;/param&gt;
 *                  &lt;/type&gt;
 *              &lt;/property&gt;
 * </pre>
 * 
 * <b><tt>canonical-form-provider</tt></b> is a mandatory parameter that
 * specifies the implementation of {@link CanonicalFormProvider} for this field.
 * <p>
 * <tt>CanonicalFormType</tt> persists the canonical form of the object (as
 * specified by the canonical-form-provider) into the database, and reads back
 * the object based on the canonical form (again, using the
 * canonical-form-provider).
 * 
 * @author srinath_k
 * @see CanonicalFormProvider
 */
public class CanonicalFormType implements UserType, ParameterizedType {

    private int[] sqlTypes = new int[1];

    private CanonicalFormProvider<Object, Serializable> canonicalFormProvider;

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return canonicalFormProvider.nullSafeFromCanonicalForm(cached);
    }

    // TODO assumes value is immutable
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return canonicalFormProvider.nullSafeToCanonicalForm(value);
    }

    public boolean equals(Object x, Object y) throws HibernateException {
        return canonicalFormProvider.equals(x, y);
    }

    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    public boolean isMutable() {
        return false;
    }

    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        Serializable canonicalForm = (Serializable) canonicalFormProvider.canonicalFormType().get(rs, names[0]);
        return canonicalFormProvider.nullSafeFromCanonicalForm(canonicalForm);
    }

    public void nullSafeSet(PreparedStatement statement, Object value, int index) throws HibernateException,
            SQLException {
        Serializable canonicalForm = canonicalFormProvider.nullSafeToCanonicalForm(value);
        if (canonicalForm == null) {
            statement.setNull(index, sqlTypes[0]);
        } else {
            canonicalFormProvider.canonicalFormType().set(statement, canonicalForm, index);
        }
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    public Class<?> returnedClass() {
        return canonicalFormProvider.objectClass();
    }

    public int[] sqlTypes() {
        return sqlTypes;
    }

    @SuppressWarnings("unchecked")
    public void setParameterValues(Properties properties) {
        String className = properties.getProperty("canonical-form-provider");
        if (className == null) {
            throw new IllegalArgumentException("canonical-form-provider not specified for CanonicalFormType.");
        }
        try {
            canonicalFormProvider = (CanonicalFormProvider<Object, Serializable>) Class.forName(className)
                    .newInstance();
            sqlTypes[0] = canonicalFormProvider.canonicalFormType().sqlType();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}