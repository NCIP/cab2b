/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject.impl;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.IBinaryOperator;
import edu.wustl.common.querysuite.queryobject.IConnector;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.util.CompoundEnum;

/**
 * @author Mandar Shidhore
 * @author chetan_patil
 * @version 1.0
 * @created 12-Oct-2006 15.04.04 AM
 * 
 * @hibernate.class table="QUERY_LOGICAL_CONNECTOR"
 * @hibernate.cache usage="read-write"
 */
// TODO check hbm etc...
public class Connector<P extends Enum<?> & IBinaryOperator> extends BaseQueryObject implements IConnector<P> {
    private static final long serialVersionUID = 3065606993455242889L;

    private static class BinaryOperatorCompoundEnum<T extends Enum<?> & IBinaryOperator>
            extends
                CompoundEnum<BinaryOperatorCompoundEnum<T>, T> implements Serializable {
        private static final long serialVersionUID = 7794336702657368626L;

        private static final List<IBinaryOperator> primitiveValues = new ArrayList<IBinaryOperator>();

        private static final List<BinaryOperatorCompoundEnum<?>> values = new ArrayList<BinaryOperatorCompoundEnum<?>>();

        private static int nextOrdinal = 0;

        // STATIC INIT
        static {
            addEnums(LogicalOperator.class);
            addEnums(ArithmeticOperator.class);
        }

        private static int nextOrdinal() {
            return nextOrdinal++;
        }

        private static <T extends Enum<?> & IBinaryOperator> void addEnums(Class<T> klass) {
            for (T e : klass.getEnumConstants()) {
                primitiveValues.add(e);
                values.add(newCompoundEnum(e));
            }
        }

        private static <T extends Enum<?> & IBinaryOperator> BinaryOperatorCompoundEnum<T> newCompoundEnum(T e) {
            BinaryOperatorCompoundEnum<T> compoundEnum = new BinaryOperatorCompoundEnum<T>(e, nextOrdinal());
            return compoundEnum;
        }

        // END STATIC INIT

        @SuppressWarnings("unchecked")
        public static <T extends Enum<?> & IBinaryOperator> BinaryOperatorCompoundEnum<T> compoundEnum(T primitiveEnum) {
            if (primitiveEnum == null) {
                throw new IllegalArgumentException();
            }
            int index = primitiveValues.indexOf(primitiveEnum);
            return (BinaryOperatorCompoundEnum<T>) values.get(index);
        }

        public static BinaryOperatorCompoundEnum<?>[] values() {
            return values.toArray(new BinaryOperatorCompoundEnum<?>[0]);
        }

        public static BinaryOperatorCompoundEnum<?> valueOf(String name) {
            if (name == null) {
                throw new NullPointerException("name is null.");
            }
            for (BinaryOperatorCompoundEnum<?> oper : values) {
                if (oper.name().equals(name)) {
                    return oper;
                }
            }
            throw new IllegalArgumentException("no compound enum BinaryOperatorCompoundEnum." + name);
        }

        public static IBinaryOperator[] primitiveValues() {
            return primitiveValues.toArray(new IBinaryOperator[0]);
        }

        private BinaryOperatorCompoundEnum(T primitiveEnum, int ordinal) {
            super(primitiveEnum, ordinal, primitiveEnum.name());
        }

        // serialization; ensure unique instance
        private Object readResolve() throws ObjectStreamException {
            return values.get(ordinal());
        }
    }

    private BinaryOperatorCompoundEnum<P> compoundOperator;

    private int nestingNumber = 0;

    /**
     * Default Constructor
     */
    public Connector() {

    }

    /**
     * Parameterized Constructor
     * 
     * @param logicalOperator The reference to the Logical operator.
     */
    public Connector(P operator) {
        setOperator(operator);
    }

    /**
     * The constructor to instantiate the Logical connector object with the
     * given logical operator & nesting number.
     * 
     * @param operator The reference to the Logical operator.
     * @param nestingNumber The integer value which will decide no. of
     *            parenthesis surrounded by this operator.
     */
    public Connector(P operator, int nestingNumber) {
        setOperator(operator);
        this.nestingNumber = nestingNumber;
    }

    /**
     * Returns the identifier assigned to BaseQueryObject.
     * 
     * @return a unique id assigned to the Condition.
     * 
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     *               unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="LOGICAL_CONNECTOR_SEQ"
     */
    public Long getId() {
        return id;
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IConnector#getOperator()
     */
    public P getOperator() {
        if (compoundOperator == null) {
            return null;
        }
        return compoundOperator.primitiveEnum();
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IConnector#setOperator(edu.wustl.common.querysuite.queryobject.IBinaryOperator)
     */
    public void setOperator(P operator) {
        this.compoundOperator = BinaryOperatorCompoundEnum.compoundEnum(operator);
    }

    /**
     * @return integer value, that represents no. of parantheses sorrounding
     *         this connector.
     * @see edu.wustl.common.querysuite.queryobject.ILogicalConnector#getNestingNumber()
     * 
     * @hibernate.property name="nestingNumber" column="NESTING_NUMBER"
     *                     type="integer" update="true" insert="true"
     */
    public int getNestingNumber() {
        return nestingNumber;
    }

    /**
     * To set the nesting numbet for this Logical connector.
     * 
     * @param nestingNumber the nesting number.
     */
    public void setNestingNumber(int nestingNumber) {
        this.nestingNumber = nestingNumber;

    }

    /**
     * to increment nesting numeber by 1.
     * 
     */
    public void incrementNestingNumber() {
        nestingNumber++;
    }

    /**
     * 
     * @return hash code value for this object. It uses logicalOperator &
     *         nestingNumber for the hash code calculation.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(compoundOperator).append(nestingNumber).toHashCode();
    }

    /**
     * @param obj The reference of object to be compared.
     * @return true if object specified is of same class, and locial operator &
     *         nesting number of two classes are equal.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;

        if (this == obj) {
            isEqual = true;
        }

        if (!isEqual && (obj != null && this.getClass() == obj.getClass())) {
            Connector<?> logicalConnector = (Connector<?>) obj;
            if (this.compoundOperator != null && this.compoundOperator.equals(logicalConnector.compoundOperator)
                    && this.nestingNumber == logicalConnector.nestingNumber) {
                isEqual = true;
            }
        }

        return isEqual;
    }

    /**
     * @return String representation for this object as Logicaloperator:nesting
     *         number.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[" + compoundOperator + ":" + nestingNumber + "]";
    }

    // for hibernate
    @SuppressWarnings("unused")
    private BinaryOperatorCompoundEnum<P> getCompoundOperator() {
        return compoundOperator;
    }

    @SuppressWarnings("unused")
    private void setCompoundOperator(BinaryOperatorCompoundEnum<P> compoundOperator) {
        this.compoundOperator = compoundOperator;
    }

}