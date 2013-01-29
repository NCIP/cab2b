/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.xmi.importer;

import java.util.Date;

import org.omg.uml.foundation.core.Attribute;

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
import edu.wustl.common.util.logger.Logger;


/**
 * Enumration for DataType.
 * It also provides method to get this enumeration based on String Datatype
 * @author Chandrakant Talele
 */
enum DataType {
    ALPHANUMERIC("ALPHANUMERIC") {
        /**
         * @see DataType#createAttribute(Attribute)
         */
        public AttributeInterface createAttribute(Attribute umlAttribute) {
            AttributeInterface attribute = domainObjectFactory.createStringAttribute();
            attribute.setName(umlAttribute.getName());
//            ValueDomain valueDomain = umlAttribute.getValueDomain();
//
//            Enumeration[] arr = valueDomain.getEnumerationCollection().getEnumeration();
//            if (arr != null) {
//                UserDefinedDEInterface userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
//                for (Enumeration e : arr) {
//                    StringValueInterface value = domainObjectFactory.createStringValue();
//                    value.setValue(e.getPermissibleValue());
//                    XMIImporterUtil.setSemanticMetadata(value, e.getSemanticMetadata());
//                    userDefinedDE.addPermissibleValue(value);
//                }
//                attribute.getAttributeTypeInformation().setDataElement(userDefinedDE);
//            }
            return attribute;
        }
    },
    STRING("String") {
        /**
         * @see DataType#createAttribute(Attribute)
         */
        public AttributeInterface createAttribute(Attribute umlAttribute) {
            AttributeInterface attribute = domainObjectFactory.createStringAttribute();
            attribute.setName(umlAttribute.getName());
//            ValueDomain valueDomain = umlAttribute.getValueDomain();
//
//            Enumeration[] arr = valueDomain.getEnumerationCollection().getEnumeration();
//            if (arr != null) {
//                UserDefinedDEInterface userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
//                for (Enumeration e : arr) {
//                    StringValueInterface value = domainObjectFactory.createStringValue();
//                    value.setValue(e.getPermissibleValue());
//                    XMIImporterUtil.setSemanticMetadata(value, e.getSemanticMetadata());
//                    userDefinedDE.addPermissibleValue(value);
//                }
//                attribute.getAttributeTypeInformation().setDataElement(userDefinedDE);
//            }
            return attribute;
        }
    },
    CHARACTER("CHARACTER") {
        /**
         * @see DataType#createAttribute(Attribute)
         */
        public AttributeInterface createAttribute(Attribute umlAttribute) {
            AttributeInterface attribute = domainObjectFactory.createStringAttribute();
            attribute.setName(umlAttribute.getName());
            
//            ValueDomain valueDomain = umlAttribute.getValueDomain();
//            Enumeration[] arr = valueDomain.getEnumerationCollection().getEnumeration();
//            if (arr != null) {
//                UserDefinedDEInterface userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
//                for (Enumeration e : arr) {
//                    StringValueInterface value = domainObjectFactory.createStringValue();
//                    value.setValue(e.getPermissibleValue());
//                    XMIImporterUtil.setSemanticMetadata(value, e.getSemanticMetadata());
//                    userDefinedDE.addPermissibleValue(value);
//                }
//                attribute.getAttributeTypeInformation().setDataElement(userDefinedDE);
//            }
            return attribute;
        }
    },

    INTEGER("Integer") {
        /**
         * @see DataType#createAttribute(Attribute)
         */
        public AttributeInterface createAttribute(Attribute umlAttribute) {
            AttributeInterface attribute = domainObjectFactory.createIntegerAttribute();
            attribute.setName(umlAttribute.getName());
//            ValueDomain valueDomain = umlAttribute.getValueDomain();
//
//            Enumeration[] arr = valueDomain.getEnumerationCollection().getEnumeration();
//            if (arr != null) {
//                UserDefinedDEInterface userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
//                for (Enumeration e : arr) {
//                    IntegerValueInterface value = domainObjectFactory.createIntegerValue();
//                    value.setValue(new Integer(e.getPermissibleValue()));
//                    XMIImporterUtil.setSemanticMetadata(value, e.getSemanticMetadata());
//                    userDefinedDE.addPermissibleValue(value);
//                }
//                attribute.getAttributeTypeInformation().setDataElement(userDefinedDE);
//            }
            return attribute;
        }
    },

    DATE("Date") {
        /**
         * @see DataType#createAttribute(Attribute)
         */
        public AttributeInterface createAttribute(Attribute umlAttribute) {
            AttributeInterface attribute = domainObjectFactory.createDateAttribute();
            attribute.setName(umlAttribute.getName());
//            ValueDomain valueDomain = umlAttribute.getValueDomain();
//
//            Enumeration[] arr = valueDomain.getEnumerationCollection().getEnumeration();
//            if (arr != null) {
//                UserDefinedDEInterface userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
//                for (Enumeration e : arr) {
//                    DateValueInterface value = domainObjectFactory.createDateValue();
//                    //TODO what is meaning of permissible values for Date ??? 
//                    //Not clear about date format string
//                    Logger.out.info("Date Attribute has permissible value : " + e.getPermissibleValue());
//                    value.setValue(new Date(e.getPermissibleValue()));
//                    XMIImporterUtil.setSemanticMetadata(value, e.getSemanticMetadata());
//                    userDefinedDE.addPermissibleValue(value);
//                }
//                attribute.getAttributeTypeInformation().setDataElement(userDefinedDE);
//            }
            return attribute;
        }
    },
    FLOAT("Float") {
        /**
         * @see DataType#createAttribute(Attribute)
         */
        public AttributeInterface createAttribute(Attribute umlAttribute) {
            AttributeInterface attribute = domainObjectFactory.createFloatAttribute();
            attribute.setName(umlAttribute.getName());
//            ValueDomain valueDomain = umlAttribute.getValueDomain();
//
//            Enumeration[] arr = valueDomain.getEnumerationCollection().getEnumeration();
//            if (arr != null) {
//                UserDefinedDEInterface userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
//                for (Enumeration e : arr) {
//                    FloatValueInterface value = domainObjectFactory.createFloatValue();
//                    value.setValue(new Float(e.getPermissibleValue()));
//                    XMIImporterUtil.setSemanticMetadata(value, e.getSemanticMetadata());
//                    userDefinedDE.addPermissibleValue(value);
//                }
//                attribute.getAttributeTypeInformation().setDataElement(userDefinedDE);
//            }
            return attribute;
        }
    },
    BOOLEAN("Boolean") {
        /**
         * @see DataType#createAttribute(Attribute)
         */
        public AttributeInterface createAttribute(Attribute umlAttribute) {
            AttributeInterface attribute = domainObjectFactory.createBooleanAttribute();
            attribute.setName(umlAttribute.getName());
//            ValueDomain valueDomain = umlAttribute.getValueDomain();
//
//            Enumeration[] arr = valueDomain.getEnumerationCollection().getEnumeration();
//            if (arr != null) {
//                UserDefinedDEInterface userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
//                for (Enumeration e : arr) {
//                    BooleanValueInterface value = domainObjectFactory.createBooleanValue();
//                    Logger.out.info("boolean Attribute has permissible value : " + e.getPermissibleValue());
//                    //TODO what is meaning of permissible values for boolean???
//                    //is any string processing needed??
//                    value.setValue(new Boolean(e.getPermissibleValue()));
//                    XMIImporterUtil.setSemanticMetadata(value, e.getSemanticMetadata());
//                    userDefinedDE.addPermissibleValue(value);
//                }
//                attribute.getAttributeTypeInformation().setDataElement(userDefinedDE);
//            }
            return attribute;
        }
    },
    LONG("Long") {
        /**
         * @see DataType#createAttribute(Attribute)
         */
        public AttributeInterface createAttribute(Attribute umlAttribute) {
            AttributeInterface attribute = domainObjectFactory.createLongAttribute();
            attribute.setName(umlAttribute.getName());
//            ValueDomain valueDomain = umlAttribute.getValueDomain();
//
//            Enumeration[] arr = valueDomain.getEnumerationCollection().getEnumeration();
//            if (arr != null) {
//                UserDefinedDEInterface userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
//                for (Enumeration e : arr) {
//                    LongValueInterface value = domainObjectFactory.createLongValue();
//                    value.setValue(new Long(e.getPermissibleValue()));
//                    XMIImporterUtil.setSemanticMetadata(value, e.getSemanticMetadata());
//                    userDefinedDE.addPermissibleValue(value);
//                }
//                attribute.getAttributeTypeInformation().setDataElement(userDefinedDE);
//            }
            return attribute;
        }
    },
    DOUBLE("Double") {
        /**
         * @see DataType#createAttribute(Attribute)
         */
        public AttributeInterface createAttribute(Attribute umlAttribute) {
            AttributeInterface attribute = domainObjectFactory.createDoubleAttribute();
            attribute.setName(umlAttribute.getName());
//            ValueDomain valueDomain = umlAttribute.getValueDomain();
//
//            Enumeration[] arr = valueDomain.getEnumerationCollection().getEnumeration();
//            if (arr != null) {
//                UserDefinedDEInterface userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
//                for (Enumeration e : arr) {
//                    DoubleValueInterface value = domainObjectFactory.createDoubleValue();
//                    value.setValue(new Double(e.getPermissibleValue()));
//                    XMIImporterUtil.setSemanticMetadata(value, e.getSemanticMetadata());
//                    userDefinedDE.addPermissibleValue(value);
//                }
//                attribute.getAttributeTypeInformation().setDataElement(userDefinedDE);
//            }
            return attribute;
        }

    },
    NUMBER("Number") {
        /**
         * @see DataType#createAttribute(Attribute)
         */
        public AttributeInterface createAttribute(Attribute umlAttribute) {
            AttributeInterface attribute = domainObjectFactory.createDoubleAttribute();
            attribute.setName(umlAttribute.getName());
//            ValueDomain valueDomain = umlAttribute.getValueDomain();
//
//            Enumeration[] arr = valueDomain.getEnumerationCollection().getEnumeration();
//            if (arr != null) {
//                UserDefinedDEInterface userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
//                for (Enumeration e : arr) {
//                    DoubleValueInterface value = domainObjectFactory.createDoubleValue();
//                    value.setValue(new Double(e.getPermissibleValue()));
//                    XMIImporterUtil.setSemanticMetadata(value, e.getSemanticMetadata());
//                    userDefinedDE.addPermissibleValue(value);
//                }
//                attribute.getAttributeTypeInformation().setDataElement(userDefinedDE);
//            }
            return attribute;
        }
    },
    OBJECT("Object"), COLLECTION("Collection"), VECTOR("Vector"), ARRAY_LIST(
            "ArrayList"), HASH_SET("HashSet");

    String value;

    private static DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();

    /**
     * Method which creates dynamic extension attribute based on datatype. 
     * Each enumration must provide implement of this method.    
     * @param umlAttribute source UML attribute 
     * @return the newly created dynamic extension attribute.
     */
    public AttributeInterface createAttribute(Attribute umlAttribute) {
        // TODO bypassing attributes, need to decide how to handle it.
//        Logger.out.error("found attribute with type" + value + ". Not storing it");
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
            if (dataType.value.equalsIgnoreCase(value)) {
                return dataType;
            }
        }
        return null;
       // throw new RuntimeException("unknown datatype found : " + value);
    }

}