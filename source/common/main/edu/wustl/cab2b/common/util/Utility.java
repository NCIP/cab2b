package edu.wustl.cab2b.common.util;

import static edu.wustl.cab2b.common.util.Constants.CONNECTOR;
import static edu.wustl.cab2b.common.util.Constants.PROJECT_VERSION;
import static edu.wustl.cab2b.common.util.Constants.TYPE_CATEGORY;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.common.dynamicextensions.domain.BooleanAttributeTypeInformation;
import edu.common.dynamicextensions.domain.DateAttributeTypeInformation;
import edu.common.dynamicextensions.domain.DoubleAttributeTypeInformation;
import edu.common.dynamicextensions.domain.FloatAttributeTypeInformation;
import edu.common.dynamicextensions.domain.IntegerAttributeTypeInformation;
import edu.common.dynamicextensions.domain.LongAttributeTypeInformation;
import edu.common.dynamicextensions.domain.StringAttributeTypeInformation;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.queryobject.DataType;
import edu.wustl.common.util.logger.Logger;

/**
 * Utility Class contain general methods used through out the application.
 * 
 * @author Chandrakant Talele
 * @author Gautam Shetty
 */
public class Utility {
    static Properties props;

    static String propertyfile = "demo.properties";
    static {
        // This static block loads properties from file at class loading time.
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
     * @return Returns all the properties present in
     *         {@link Utility#propertyfile}
     */
    public static Properties getProperties() {
        return props;
    }

    /**
     * Checks whether passed attribute/association is inheriated.
     * 
     * @param abstractAttribute Attribute/Association to check.
     * @return TRUE if it is inherited else returns FALSE
     */
    public static boolean isInherited(AbstractAttributeInterface abstractAttribute) {
        for (TaggedValueInterface tag : abstractAttribute.getTaggedValueCollection()) {
            if (tag.getKey().equals(Constants.TYPE_DERIVED)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param association Association
     * @return Unique string to represent given association
     */
    public static String generateUniqueId(AssociationInterface association) {
        return concatStrings(association.getEntity().getName(), association.getSourceRole().getName(),
                             association.getTargetRole().getName(), association.getTargetEntity().getName());
    }

    /**
     * @param s1 String
     * @param s2 String
     * @param s3 String
     * @param s4 String
     * @return Concatenated string made after connecting s1, s2, s3, s4 by
     *         {@link Constants#CONNECTOR}
     */
    public static String concatStrings(String s1, String s2, String s3, String s4) {
        StringBuffer buff = new StringBuffer();
        buff.append(s1);
        buff.append(CONNECTOR);
        buff.append(s2);
        buff.append(CONNECTOR);
        buff.append(s3);
        buff.append(CONNECTOR);
        buff.append(s4);
        return buff.toString();

    }

    /**
     * Compares whether given searchPattern is present in passed searchString
     * 
     * @param searchPattern search Pattern to look for
     * @param searchString String which is to be searched
     * @return Returns TRUE if given searchPattern is present in searchString ,
     *         else return returns false.
     */
    public static boolean compareRegEx(String searchPattern, String searchString) {
        searchPattern = searchPattern.replace("*", ".*");
        Pattern pat = Pattern.compile(searchPattern, Pattern.CASE_INSENSITIVE);
        Matcher mat = pat.matcher(searchString);
        return mat.matches();
    }

    /**
     * Returns all the URLs of the deployed services which are exposing given
     * entity
     * 
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
     * 
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
     * @param taggedValues collection of TaggedValueInterface
     * @param key string
     * @return The tagged value for given key in given tagged value collection.
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
     * @param taggable taggable object
     * @param key string
     * @return The tagged value for given key.
     */
    public static TaggedValueInterface getTaggedValue(AbstractMetadataInterface taggable, String key) {
        return getTaggedValue(taggable.getTaggedValueCollection(), key);
    }

    /**
     * Checks whether passed Entity is a category or not.
     * 
     * @param entity Entity to check
     * @return Returns TRUE if given entity is Category, else returns false.
     */
    public static boolean isCategory(EntityInterface entity) {
        TaggedValueInterface tag = getTaggedValue(entity.getTaggedValueCollection(), TYPE_CATEGORY);
        return tag != null;
    }

    /**
     * Converts DE datatype to queryObject dataType.
     * 
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
     * @return TRUE if there are any permissible values associated with this
     *         attribute, otherwise returns false.
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
     * @return Returns all the permissible values associated with this
     *         attribute.
     */
    public static Collection<PermissibleValueInterface> getPermissibleValues(AttributeInterface attribute) {
        if (isEnumerated(attribute)) {
            UserDefinedDEInterface de = (UserDefinedDEInterface) attribute.getAttributeTypeInformation().getDataElement();
            return de.getPermissibleValueCollection();
        }
        return new ArrayList<PermissibleValueInterface>(0);
    }

    /**
     * Returns the display name if present as tagged value. Else returns the
     * actual name of the entity
     * 
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
        // As per Bug# 4577 <class name> (app_name v<version name) e.g.
        // Participant (caTissue Core v1.1)
        String projectName = eg.getLongName();
        if (projectName.equals("caFE Server 1.1")) {
            projectName = "caFE Server";
        }
        StringBuffer buff = new StringBuffer();
        buff.append(name.substring(name.lastIndexOf(".") + 1, name.length()));
        buff.append(" (");
        buff.append(projectName);
        buff.append(" v");
        buff.append(version);
        buff.append(")");
        ;
        return buff.toString();
    }

    /**
     * @param path A IPath object
     * @return Display string for given path
     */
    public static String getPathDisplayString(IPath path) {
        String text = "<HTML><B>Path</B>:";
        // text=text.concat("<HTML><B>Path</B>:");
        List<IAssociation> pathList = path.getIntermediateAssociations();
        text = text.concat(Utility.getDisplayName(path.getSourceEntity()));
        for (int i = 0; i < pathList.size(); i++) {
            text = text.concat("<B>----></B>");
            text = text.concat(Utility.getDisplayName(pathList.get(i).getTargetEntity()));
        }
        text = text.concat("</HTML>");
        Logger.out.debug(text);
        StringBuffer sb = new StringBuffer();
        int textLength = text.length();
        Logger.out.debug(textLength);
        int currentStart = 0;
        String currentString = null;
        int offset = 100;
        int strLen = 0;
        int len = 0;
        while (currentStart < textLength && textLength > offset) {
            currentString = text.substring(currentStart, (currentStart + offset));
            strLen = strLen + currentString.length() + len;
            sb.append(currentString);
            int index = text.indexOf("<B>----></B>", (currentStart + offset));
            if (index != -1) {
                len = index - strLen;
                currentString = text.substring((currentStart + offset), (currentStart + offset + len));
                sb.append(currentString);
                sb.append("<P>");
            } else {
                sb.append(text.substring(currentStart));
                return sb.toString();
            }

            currentStart = currentStart + offset + len;
            if ((currentStart + offset + len) > textLength)
                break;
        }
        sb.append(text.substring(currentStart));
        return sb.toString();
    }

    /**
     * @param entity Entity to check
     * @return Attribute whose name is "identifier" OR "id"
     */
    public static AttributeInterface getIdAttribute(EntityInterface entity) {
        for (AttributeInterface attribute : entity.getAttributeCollection()) {
            if (isIdentifierAttribute(attribute)) {
                return attribute;
            }
        }
        return null;
    }

    /**
     * Returns true if an application returns associatied objects information in
     * result of CQLs.
     * 
     * @param entity Entity to check
     * @return true/false
     */
    public static boolean isOutGoingAssociationSupported(EntityInterface entity) {
        EntityGroupInterface eg = getEntityGroup(entity);
        String shortName = eg.getShortName();
        boolean isOutGoingAssociationSupported = false;

        String supportOutGoingAssociation = props.getProperty(shortName + ".supportOutGoingAssociation");
        if (supportOutGoingAssociation != null && supportOutGoingAssociation.equalsIgnoreCase("true")) {
            isOutGoingAssociationSupported = true;
        }

        return isOutGoingAssociationSupported;
    }

    /**
     * @param entity Entity to check
     * @return Name of the application to which given entity belongs
     */
    public static String getApplicationName(EntityInterface entity) {
        return getEntityGroup(entity).getName();
    }

    /**
     * @param attribute Attribute to check
     * @return TRUE if attribute name is "identifier" OR "id"
     */
    public static boolean isIdentifierAttribute(AttributeInterface attribute) {
        String attribName = attribute.getName();
        return attribName.equalsIgnoreCase("id") || attribName.equalsIgnoreCase("identifier");
    }

    /**
     * Converts attribute set into a alphabatically sorted list.
     * 
     * @param inputAttributeSet Attribute set to sort
     * @return Sorted list of attributes
     */
    public static List<AttributeInterface> getAttributeList(Set<AttributeInterface> inputAttributeSet) {
        List<AttributeInterface> attributes = new ArrayList<AttributeInterface>(inputAttributeSet);
        Collections.sort(attributes, new AttributeInterfaceComparator());
        return attributes;
    }

    /**
     * @param queryResult Query result to process.
     * @return Total no of records present in query set (i.e for all services)
     */
    public static int getNoOfRecords(IQueryResult queryResult) {
        int size = 0;
        Map<String, List<IRecord>> allRecords = queryResult.getRecords();

        for (List<IRecord> valueList : allRecords.values()) {
            size += valueList.size();
        }
        return size;
    }

    /**
     * @param queryResult Query result to process.
     * @return List of attributes from query result
     */
    public static List<AttributeInterface> getAttributeList(IQueryResult queryResult) {
        Map<String, List<IRecord>> allRecords = queryResult.getRecords();
        List<AttributeInterface> attributeList = new ArrayList<AttributeInterface>();
        if (!allRecords.isEmpty()) {
            List<IRecord> recordList = allRecords.values().iterator().next();
            if (!recordList.isEmpty()) {
                IRecord record = recordList.get(0);
                attributeList = getAttributeList(record.getAttributes());
            }

        }
        return attributeList;
    }
}