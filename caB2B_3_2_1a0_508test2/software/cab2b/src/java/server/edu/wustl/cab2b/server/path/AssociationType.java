package edu.wustl.cab2b.server.path;

/**
 * Enumeration to represents possible value which column "ASSOCIATION.ASSOCIATION_TYPE" can take.  
 * @author Chandrakant Talele
 */
public enum AssociationType {
    INTER_MODEL_ASSOCIATION(1), INTRA_MODEL_ASSOCIATION(2);
    private int value;

    AssociationType(int value) {
        this.value = value;
    }

    /**
     * @return integer value associated with enumeration
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value Get type based in integer value
     * @return Enumeration
     */
    public static AssociationType getType(int value) {
        if (value == 1) {
            return AssociationType.INTER_MODEL_ASSOCIATION;
        }
        if (value == 2) {
            return AssociationType.INTRA_MODEL_ASSOCIATION;
        }
        throw new IllegalArgumentException();
    }
}