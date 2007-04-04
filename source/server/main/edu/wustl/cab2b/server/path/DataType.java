package edu.wustl.cab2b.server.path;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.BooleanValueInterface;
import edu.common.dynamicextensions.domaininterface.DateValueInterface;
import edu.common.dynamicextensions.domaininterface.DoubleValueInterface;
import edu.common.dynamicextensions.domaininterface.FloatValueInterface;
import edu.common.dynamicextensions.domaininterface.IntegerValueInterface;
import edu.common.dynamicextensions.domaininterface.LongValueInterface;
import edu.common.dynamicextensions.domaininterface.StringValueInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.wustl.cab2b.server.util.DynamicExtensionUtility;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.cagrid.metadata.common.Enumeration;
import gov.nih.nci.cagrid.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.metadata.common.ValueDomain;

import java.util.Date;

/**
 * Enumration for DataType.
 * It also provides method to get this enumeration based on String Datatype
 * @author Chandrakant Talele
 */
enum DataType {
    ALPHANUMERIC("ALPHANUMERIC") {
        /**
         * @see DataType#createAttribute(UMLAttribute)
         */
        public AttributeInterface createAttribute(UMLAttribute umlAttribute) {
            AttributeInterface attribute = domainObjectFactory.createStringAttribute();
            ValueDomain valueDomain = umlAttribute.getValueDomain();

            Enumeration[] arr = valueDomain.getEnumerationCollection().getEnumeration();
            if (arr != null) {
                UserDefinedDEInterface userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
                for (Enumeration e : arr) {
                    StringValueInterface value = domainObjectFactory.createStringValue();
                    value.setValue(e.getPermissibleValue());
                    DynamicExtensionUtility.setSemanticMetadata(value, e.getSemanticMetadata());
                    userDefinedDE.addPermissibleValue(value);
                }
                attribute.getAttributeTypeInformation().setDataElement(userDefinedDE);
            }
            return attribute;
        }
    },
    STRING("java.lang.String") {
        /**
         * @see DataType#createAttribute(UMLAttribute)
         */
        public AttributeInterface createAttribute(UMLAttribute umlAttribute) {
            AttributeInterface attribute = domainObjectFactory.createStringAttribute();
            ValueDomain valueDomain = umlAttribute.getValueDomain();

            Enumeration[] arr = valueDomain.getEnumerationCollection().getEnumeration();
            if (arr != null) {
                UserDefinedDEInterface userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
                for (Enumeration e : arr) {
                    StringValueInterface value = domainObjectFactory.createStringValue();
                    value.setValue(e.getPermissibleValue());
                    DynamicExtensionUtility.setSemanticMetadata(value, e.getSemanticMetadata());
                    userDefinedDE.addPermissibleValue(value);
                }
                attribute.getAttributeTypeInformation().setDataElement(userDefinedDE);
            }
            return attribute;
        }
    },
    CHARACTER("CHARACTER") {
        /**
         * @see DataType#createAttribute(UMLAttribute)
         */
        public AttributeInterface createAttribute(UMLAttribute umlAttribute) {
            AttributeInterface attribute = domainObjectFactory.createStringAttribute();
            ValueDomain valueDomain = umlAttribute.getValueDomain();

            Enumeration[] arr = valueDomain.getEnumerationCollection().getEnumeration();
            if (arr != null) {
                UserDefinedDEInterface userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
                for (Enumeration e : arr) {
                    StringValueInterface value = domainObjectFactory.createStringValue();
                    value.setValue(e.getPermissibleValue());
                    DynamicExtensionUtility.setSemanticMetadata(value, e.getSemanticMetadata());
                    userDefinedDE.addPermissibleValue(value);
                }
                attribute.getAttributeTypeInformation().setDataElement(userDefinedDE);
            }
            return attribute;
        }
    },

    INTEGER("java.lang.Integer") {
        /**
         * @see DataType#createAttribute(UMLAttribute)
         */
        public AttributeInterface createAttribute(UMLAttribute umlAttribute) {
            AttributeInterface attribute = domainObjectFactory.createIntegerAttribute();
            ValueDomain valueDomain = umlAttribute.getValueDomain();

            Enumeration[] arr = valueDomain.getEnumerationCollection().getEnumeration();
            if (arr != null) {
                UserDefinedDEInterface userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
                for (Enumeration e : arr) {
                    IntegerValueInterface value = domainObjectFactory.createIntegerValue();
                    value.setValue(new Integer(e.getPermissibleValue()));
                    DynamicExtensionUtility.setSemanticMetadata(value, e.getSemanticMetadata());
                    userDefinedDE.addPermissibleValue(value);
                }
                attribute.getAttributeTypeInformation().setDataElement(userDefinedDE);
            }
            return attribute;
        }
    },

    DATE("java.util.Date") {
        /**
         * @see DataType#createAttribute(UMLAttribute)
         */
        public AttributeInterface createAttribute(UMLAttribute umlAttribute) {
            AttributeInterface attribute = domainObjectFactory.createDateAttribute();
            ValueDomain valueDomain = umlAttribute.getValueDomain();

            Enumeration[] arr = valueDomain.getEnumerationCollection().getEnumeration();
            if (arr != null) {
                UserDefinedDEInterface userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
                for (Enumeration e : arr) {
                    DateValueInterface value = domainObjectFactory.createDateValue();
                    //TODO what is meaning of permissible values for Date ??? 
                    //Not clear about date format string
                    Logger.out.info("Date Attribute has permissible value : " + e.getPermissibleValue());
                    value.setValue(new Date(e.getPermissibleValue()));
                    DynamicExtensionUtility.setSemanticMetadata(value, e.getSemanticMetadata());
                    userDefinedDE.addPermissibleValue(value);
                }
                attribute.getAttributeTypeInformation().setDataElement(userDefinedDE);
            }
            return attribute;
        }
    },
    FLOAT("java.lang.Float") {
        /**
         * @see DataType#createAttribute(UMLAttribute)
         */
        public AttributeInterface createAttribute(UMLAttribute umlAttribute) {
            AttributeInterface attribute = domainObjectFactory.createFloatAttribute();
            ValueDomain valueDomain = umlAttribute.getValueDomain();

            Enumeration[] arr = valueDomain.getEnumerationCollection().getEnumeration();
            if (arr != null) {
                UserDefinedDEInterface userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
                for (Enumeration e : arr) {
                    FloatValueInterface value = domainObjectFactory.createFloatValue();
                    value.setValue(new Float(e.getPermissibleValue()));
                    DynamicExtensionUtility.setSemanticMetadata(value, e.getSemanticMetadata());
                    userDefinedDE.addPermissibleValue(value);
                }
                attribute.getAttributeTypeInformation().setDataElement(userDefinedDE);
            }
            return attribute;
        }
    },
    BOOLEAN("java.lang.Boolean") {
        /**
         * @see DataType#createAttribute(UMLAttribute)
         */
        public AttributeInterface createAttribute(UMLAttribute umlAttribute) {
            AttributeInterface attribute = domainObjectFactory.createBooleanAttribute();
            ValueDomain valueDomain = umlAttribute.getValueDomain();

            Enumeration[] arr = valueDomain.getEnumerationCollection().getEnumeration();
            if (arr != null) {
                UserDefinedDEInterface userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
                for (Enumeration e : arr) {
                    BooleanValueInterface value = domainObjectFactory.createBooleanValue();
                    Logger.out.info("boolean Attribute has permissible value : " + e.getPermissibleValue());
                    //TODO what is meaning of permissible values for boolean???
                    //is any string processing needed??
                    value.setValue(new Boolean(e.getPermissibleValue()));
                    DynamicExtensionUtility.setSemanticMetadata(value, e.getSemanticMetadata());
                    userDefinedDE.addPermissibleValue(value);
                }
                attribute.getAttributeTypeInformation().setDataElement(userDefinedDE);
            }
            return attribute;
        }
    },
    LONG("java.lang.Long") {
        /**
         * @see DataType#createAttribute(UMLAttribute)
         */
        public AttributeInterface createAttribute(UMLAttribute umlAttribute) {
            AttributeInterface attribute = domainObjectFactory.createLongAttribute();
            ValueDomain valueDomain = umlAttribute.getValueDomain();

            Enumeration[] arr = valueDomain.getEnumerationCollection().getEnumeration();
            if (arr != null) {
                UserDefinedDEInterface userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
                for (Enumeration e : arr) {
                    LongValueInterface value = domainObjectFactory.createLongValue();
                    value.setValue(new Long(e.getPermissibleValue()));
                    DynamicExtensionUtility.setSemanticMetadata(value, e.getSemanticMetadata());
                    userDefinedDE.addPermissibleValue(value);
                }
                attribute.getAttributeTypeInformation().setDataElement(userDefinedDE);
            }
            return attribute;
        }
    },
    DOUBLE("java.lang.Double") {
        /**
         * @see DataType#createAttribute(UMLAttribute)
         */
        public AttributeInterface createAttribute(UMLAttribute umlAttribute) {
            AttributeInterface attribute = domainObjectFactory.createDoubleAttribute();
            ValueDomain valueDomain = umlAttribute.getValueDomain();

            Enumeration[] arr = valueDomain.getEnumerationCollection().getEnumeration();
            if (arr != null) {
                UserDefinedDEInterface userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
                for (Enumeration e : arr) {
                    DoubleValueInterface value = domainObjectFactory.createDoubleValue();
                    value.setValue(new Double(e.getPermissibleValue()));
                    DynamicExtensionUtility.setSemanticMetadata(value, e.getSemanticMetadata());
                    userDefinedDE.addPermissibleValue(value);
                }
                attribute.getAttributeTypeInformation().setDataElement(userDefinedDE);
            }
            return attribute;
        }

    },
    NUMBER("NUMBER") {
        /**
         * @see DataType#createAttribute(UMLAttribute)
         */
        public AttributeInterface createAttribute(UMLAttribute umlAttribute) {
            AttributeInterface attribute = domainObjectFactory.createDoubleAttribute();
            ValueDomain valueDomain = umlAttribute.getValueDomain();

            Enumeration[] arr = valueDomain.getEnumerationCollection().getEnumeration();
            if (arr != null) {
                UserDefinedDEInterface userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
                for (Enumeration e : arr) {
                    DoubleValueInterface value = domainObjectFactory.createDoubleValue();
                    value.setValue(new Double(e.getPermissibleValue()));
                    DynamicExtensionUtility.setSemanticMetadata(value, e.getSemanticMetadata());
                    userDefinedDE.addPermissibleValue(value);
                }
                attribute.getAttributeTypeInformation().setDataElement(userDefinedDE);
            }
            return attribute;
        }
    },
    OBJECT("java.lang.Object"), COLLECTION("java.util.Collection"), VECTOR("java.util.Vector"), ARRAY_LIST(
            "java.util.ArrayList"), HASH_SET("java.util.HashSet");

    String value;

    private static DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();

    /**
     * Method which creates dynamic extension attribute based on datatype. 
     * Each enumration must provide implement of this method.    
     * @param umlAttribute source UML attribute 
     * @return the newly created dynamic extension attribute.
     */
    public AttributeInterface createAttribute(UMLAttribute umlAttribute) {
        // TODO bypassing attributes, need to decide how to handle it.
        Logger.out.error("found attribute with type" + value + ". Not storing it");
        return null;
    }

    /**
     * Builds enumeration and assigns passed string as value
     * @param value
     */
    DataType(String value) {
        this.value = value;
    }

    /**
     * Returns the enumration for input String.
     * @param value
     * @return Returns the DataType
     */
    public static DataType get(String value) {
        DataType[] allDataypes = DataType.values();

        for (DataType dataType : allDataypes) {
            if (dataType.value.equals(value)) {
                return dataType;
            }
        }
        throw new RuntimeException("unknown datatype found : " + value);
    }

}