package edu.wustl.common.querysuite.queryobject;

/**
 * An enum of the possible data types of attributes.
 * @version 1.0
 * @updated 11-Oct-2006 02:55:25 PM
 */
public enum DataType {

    String(java.lang.String.class) {
        public Object convertValue(String value) {
            return value;
        }

    },
    Date(java.util.Date.class) {
        public Object convertValue(String value) {
            return value;
        }
    },
    Integer(java.lang.Integer.class) {
        public Integer convertToType(String value) {
            return java.lang.Integer.valueOf(value);
        }
    },
    Long(java.lang.Long.class) {

        public Long convertToType(String value) {
            return java.lang.Long.valueOf(value);
        }
    },
    Double(java.lang.Double.class) {

        public Double convertToType(String value) {
            return java.lang.Double.valueOf(value);
        }
    },
    Boolean(java.lang.Boolean.class) {

        public Boolean convertToType(String value) {
            return java.lang.Boolean.valueOf(value);
        }
    },
    Float(java.lang.Float.class) {

        public Float convertToType(String value) {
            return java.lang.Float.valueOf(value);
        }
    };
    private Class javaType;

    private DataType(Class javaType) {
        this.javaType = javaType;
    }

    /**
     * @return Returns the javaType.
     */
    public Class getJavaType() {
        return javaType;
    }

    /**
     * This method returns 
     * @param value
     * @return
     */
    public Object convertValue(String value) {
        if (isValid(value)) {
            return convertToType(value);
        }
        return null;
    }

    protected Object convertToType(String value) {
        return value;
    }

    protected boolean isValid(String value) {
        return (value != null && !value.equalsIgnoreCase(""));
    }
}