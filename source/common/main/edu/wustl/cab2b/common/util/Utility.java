/**
 * <p>Title: Utility Class>
 * <p>Description:  Utility Class contain general methods used through out the application. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 */
package edu.wustl.cab2b.common.util;

import static edu.wustl.cab2b.common.util.Constants.PROJECT_VERSION;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.common.dynamicextensions.domain.BooleanAttributeTypeInformation;
import edu.common.dynamicextensions.domain.DateAttributeTypeInformation;
import edu.common.dynamicextensions.domain.DoubleAttributeTypeInformation;
import edu.common.dynamicextensions.domain.FloatAttributeTypeInformation;
import edu.common.dynamicextensions.domain.IntegerAttributeTypeInformation;
import edu.common.dynamicextensions.domain.LongAttributeTypeInformation;
import edu.common.dynamicextensions.domain.StringAttributeTypeInformation;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.common.querysuite.queryobject.DataType;
import edu.wustl.common.util.logger.Logger;

/**
 * Utility Class contain general methods used through out the application.
 * @author Chandrakant Talele
 * @author Gautam Shetty
 */
public class Utility {
    static Properties props;

    /*
     * This static block loads properties from file at class loading time.
     * */
    static String propertyfile = "demo.properties";
    static {

        InputStream is = Utility.class.getClassLoader().getResourceAsStream(propertyfile);
        if (is == null) {
            Logger.out.error("Unable fo find property file : " + propertyfile
                    + "\n please put this file in classpath");
        }
        props = new Properties();
        try {
            props.load(is);
        } catch (IOException e) {
            Logger.out.error("Unable to load properties from : " + propertyfile);
            e.printStackTrace();
        }
    }

    /**
     * @return Returns all the properties present in {@link Utility#propertyfile}
     */
    public static Properties getProperties() {
        return props;
    }

    /**
     * Compares whether given searchPattern is present in passed searchString
     * @param searchPattern search Pattern to look for 
     * @param searchString  String which is to be searched
     * @return Returns TRUE if given searchPattern is present in searchString , else return returns false.
     */
    public static boolean compareRegEx(String searchPattern, String searchString) {
        searchPattern = searchPattern.replace("*", ".*");
        Pattern pat = Pattern.compile(searchPattern, Pattern.CASE_INSENSITIVE);
        Matcher mat = pat.matcher(searchString);
        return mat.matches();
    }

    /**
     * Returns all the URLs of the deployed services which are exposing given entity
     * @param entity Entity to check
     * @return Returns the List of URLs
     */
    public static String[] getServiceURLS(EntityInterface entity) {
        EntityGroupInterface eg = getEntityGroup(entity);
        String shortName = eg.getShortName();

        String[] urls = props.getProperty(shortName + ".ServiceURL").split(",");
        if (urls == null || urls.length == 0) {
            Logger.out.error("No URLs are configured for application : " + shortName + " in  : " + propertyfile);
        }

        return urls;
    }

    /**
     * Returns the entity group of given entity
     * @param entity Entity to check
     * @return Returns parent Entity Group
     */
    public static EntityGroupInterface getEntityGroup(EntityInterface entity) {
        for (EntityGroupInterface entityGroup : entity.getEntityGroupCollection()) {
            Collection<TaggedValueInterface> taggedValues = entityGroup.getTaggedValueCollection();
            if (getTaggedValue(taggedValues, Constants.CAB2B_ENTITY_GROUP) != null) {
                return entityGroup;
            }
        }
        throw new RuntimeException("This entity does not have DE entity group", new java.lang.RuntimeException(),
                ErrorCodeConstants.DE_0003);
    }

    /**
     * return tagged value for given key in given tagged value collection.
     * @param taggedValues
     * @param key
     * @return
     */
    public static TaggedValueInterface getTaggedValue(Collection<TaggedValueInterface> taggedValues, String key) {
        for (TaggedValueInterface taggedValue : taggedValues) {
            if (taggedValue.getKey().equals(key)) {
                return taggedValue;
            }
        }
        return null;
    }

    /**
     * Checks whether passed Entity is a category or not.
     * @param entity Entity to check
     * @return Returns TRUE if given entity is Category, else returns false.
     */
    public static boolean isCategory(EntityInterface entity) {
        boolean isCategory = false;
        if (getTaggedValue(entity.getTaggedValueCollection(), Constants.TYPE_CATEGORY) != null) {
            isCategory = true;
        }
        return isCategory;
    }

    //    /**
    //     * Returns all the categories which don't have any super category.
    //     * @return List of root categories.
    //     */
    //    public static List<EntityInterface> getAllRootCategories() {
    //        // TODO add implementation
    //        throw new RuntimeException("this method is not implemented yet !!!");
    //    }
    //
    //    /**
    //     * Returns all the subcategories of a given super category
    //     * @param superCategory The super category
    //     * @return List of sub categories
    //     */
    //    public static List<EntityInterface> getAllSubCategories(
    //                                                            EntityInterface superCategory) {
    //        if (isCategory(superCategory)) {
    //            // input category is not a category
    //        }
    //        // TODO add implementation
    //        throw new RuntimeException("this method is not implemented yet !!!");
    //    }

    /**
     * Converts DE datatype to queryObject dataType.
     * @param type the DE attribute type.
     * @return the DataType.
     */
    public static DataType getDataType(AttributeTypeInformationInterface type) {
        if (type instanceof StringAttributeTypeInformation) {
            return DataType.String;
        } else if (type instanceof DoubleAttributeTypeInformation) {
            return DataType.Double;
        } else if (type instanceof IntegerAttributeTypeInformation) {
            return DataType.Integer;
        } else if (type instanceof DateAttributeTypeInformation) {
            return DataType.Date;
        } else if (type instanceof FloatAttributeTypeInformation) {
            return DataType.Float;
        } else if (type instanceof BooleanAttributeTypeInformation) {
            return DataType.Boolean;
        } else if (type instanceof LongAttributeTypeInformation) {
            return DataType.Long;
        } else {
            throw new RuntimeException("Unknown Attribute type");
        }

    }

    /**
     * @param attribute Check will be done for this Attribute.
     * @return TRUE if there are any permissible values associated with this attribute, otherwise returns false. 
     */
    public static boolean isEnumerated(AttributeInterface attribute) {
        if (attribute.getAttributeTypeInformation().getDataElement() instanceof UserDefinedDEInterface) {
            UserDefinedDEInterface de = (UserDefinedDEInterface) attribute.getAttributeTypeInformation().getDataElement();
            return de.getPermissibleValueCollection().size() != 0;
        }
        return false;
    }

    /**
     * @param attribute Attribute to process.
     * @return Returns all the permissible values associated with this attribute.
     */
    public static Collection<PermissibleValueInterface> getPermissibleValues(AttributeInterface attribute) {
        if (isEnumerated(attribute)) {
            UserDefinedDEInterface de = (UserDefinedDEInterface) attribute.getAttributeTypeInformation().getDataElement();
            return de.getPermissibleValueCollection();
        }
        return new ArrayList<PermissibleValueInterface>(0);
    }

    /**
     * Returns the display name if present as tagged value. Else returns the actual name of the entity
     * @param entity The entity to process
     * @return The display name.
     */
    public static String getDisplayName(EntityInterface entity) {
        String name = entity.getName();
        if (isCategory(entity)) {
            return name;
        }
        EntityGroupInterface eg = getEntityGroup(entity);
        String version = "";
        for (TaggedValueInterface tag : eg.getTaggedValueCollection()) {
            if (tag.getKey().equals(PROJECT_VERSION)) {
                version = tag.getValue();
                break;
            }
        }
        StringBuffer buff = new StringBuffer();
        buff.append(eg.getLongName());
        buff.append(" :: ");
        buff.append(version);
        buff.append(" :: ");
        buff.append(name.substring(name.lastIndexOf(".") + 1, name.length()));
        return buff.toString();
    }

    //    /**
    //     * Checks whether passed attribute/association is inheriated.
    //     * @param abstractAttribute Attribute/Association to check.
    //     * @return TRUE if it is inherited else returns FALSE
    //     */
    //    public static boolean isInherited(AbstractAttributeInterface abstractAttribute) {
    //        for (TaggedValueInterface tag : abstractAttribute.getTaggedValueCollection()) {
    //            if (tag.getKey().equals(Constants.TYPE_DERIVED)) {
    //                return true;
    //            }
    //        }
    //        return false;
    //    }
    //
    //    /**
    //     * Returns actual attribute if passed attribute is a derieved one. Else returns the passed attribute
    //     * @param attribute Attribute for which actual attribute is expected.
    //     * @return The actual attribute
    //     */
    //    public static AttributeInterface getActualAttribute(AttributeInterface attribute) {
    //        if (!isInherited(attribute)) {
    //            return attribute;
    //        }
    //        EntityInterface parent = attribute.getEntity().getParentEntity();
    //        String attributeName = attribute.getName();
    //        while (true) {
    //            for (AttributeInterface attributeFromParent : parent.getAttributeCollection()) {
    //                if (attributeName.equals(attributeFromParent.getName())) {
    //                    if (isInherited(attributeFromParent)) {
    //                        parent = parent.getParentEntity();
    //                        break;
    //                    } else {
    //                        return attributeFromParent;
    //                    }
    //                }
    //            }
    //        }
    //    }
    //
    //    /**
    //     * Returns actual association if passed association is a derieved one. Else returns the passed association
    //     * @param association Attribute for which actual association is expected.
    //     * @return The actual association
    //     */
    //    public AssociationInterface getActualAassociation(AssociationInterface association) {
    //        if (!isInherited(association)) {
    //            return association;
    //        }
    //        String key=""; 
    //        for (TaggedValueInterface tag : association.getTaggedValueCollection()) {
    //            if (tag.getKey().equals(Constants.ORIGINAL_ASSOCIATION_POINTER)) {
    //                key = tag.getValue();
    //                break;
    //            }
    //        }
    //        EntityCache cache = EntityCache.getInstance();
    //        AssociationInterface actualAssociation = cache.getAssociationBySrcEntityTargetRole(key);
    //        return actualAssociation;
    //    }
    //
    //    public static String generateUniqueId(AssociationInterface association) {
    //        return association.getEntity().getName() + CONNECTOR + association.getTargetRole().getName();
    //    }
}