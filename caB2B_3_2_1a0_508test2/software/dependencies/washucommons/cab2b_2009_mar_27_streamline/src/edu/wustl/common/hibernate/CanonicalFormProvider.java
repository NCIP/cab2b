package edu.wustl.common.hibernate;

import java.io.Serializable;

import org.hibernate.type.NullableType;

/**
 * Provide a canonical representation of objects of a certain type. Such a
 * canonical representation can be used while storing the object in a database
 * column. <br>
 * <b>Note:</b>It is required that implementing classes provide a default
 * no-arg constructor in order to be used in conjunction with
 * {@link CanonicalFormProvider}.
 * 
 * @author srinath_k
 * 
 * @param <O> the object whose canonical representation is needed.
 * @param <C> the type of the canonical representation.
 * 
 * @see CanonicalFormType
 */
public interface CanonicalFormProvider<O, C extends Serializable> {
    /**
     * Returns canonical form of specified object. Implementors should handle
     * possibility of null values.
     * 
     * @param object the object whose canonical representation is needed.
     * @return the canonical form of specified object.
     */
    C nullSafeToCanonicalForm(O object);

    /**
     * Returns object corresponding to specified canonical form. Implementors
     * should handle possibility of null values.
     * 
     * @param canonicalForm the canonical form whose corresponding object is
     *            needed.
     * @return the object corresponding to specified canonical form.
     */
    O nullSafeFromCanonicalForm(C canonicalForm);

    /**
     * @return the class of the object whose canonical form is needed.
     */
    Class<O> objectClass();

    /**
     * Checks if the specified objects are equal (persistent state equality).
     */
    boolean equals(O o1, O o2);

    /**
     * Returns the hibernate type of the canonical form; this would be a
     * constant from <tt>org.hibernate.Hibernate</tt>.
     * 
     * @return the hibernate type of the canonical form.
     */
    NullableType canonicalFormType();
}
