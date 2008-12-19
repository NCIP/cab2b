package edu.wustl.cab2b.client.ui.query;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.wustl.cab2b.client.ui.util.ClientConstants;
import edu.wustl.cab2b.common.category.DataCategory;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.category.CategoryBusinessInterface;
import edu.wustl.cab2b.common.ejb.category.CategoryHomeInterface;
import edu.wustl.cab2b.common.ejb.datacategory.DataCategoryBusinessInterface;
import edu.wustl.cab2b.common.ejb.datacategory.DataCategoryHomeInterface;
import edu.wustl.cab2b.common.locator.Locator;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;
import edu.wustl.common.querysuite.metadata.associations.IIntraModelAssociation;
import edu.wustl.common.querysuite.metadata.category.Category;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;

/**
 * This class contains the utility methods required for building the UI.
 * @author Chandrakant Talele
 * @author Gautam Shetty
 */
public class Utility {

    /**
     * Returns the RelationalOperator, given the relational operator string. 
     * @param operator The relational operator string.
     * @return the RelationalOperator, given the relational operator string.
     */
    public static RelationalOperator getRelationalOperator(String operator) {
        return RelationalOperator.getOperatorForStringRepresentation(operator);
    }

    /**
     * @param relationalOperator Input Relational Operator
     * @return Display string for given operator 
     */
    public static String displayStringForRelationalOperator(RelationalOperator relationalOperator) {
        return relationalOperator.getStringRepresentation();
    }

    /**
     * Returns the LogicalOperator, given the logical operator string. 
     * @param operator The logical operator string.
     * @return the LogicalOperator, given the logical operator string.
     */
    public static LogicalOperator getLogicalOperator(String operator) {
        LogicalOperator logicalOperator = LogicalOperator.And;
        if (ClientConstants.OPERATOR_OR.equals(operator)) {
            logicalOperator = LogicalOperator.Or;
        }

        return logicalOperator;
    }

    /**
     * @param path Input path
     * @return string representation of given path
     */
    public static String getPathDisplayString(IPath path) {
        StringBuffer sb = new StringBuffer(40);
        sb.append("<HTML><B>Path</B>:");
        sb.append(path.getSourceEntity().getName());
        for (IAssociation association : path.getIntermediateAssociations()) {
            sb.append("<B>----></B>");
            sb.append(association.getTargetEntity().getName());
        }
        sb.append("<HTML>");
        return sb.toString();
    }

    /**
     * @param association Input association object
     * @return The roleName for given association
     */
    public static String getRoleName(IAssociation association) {
        String roleName = "";
        if (association instanceof IIntraModelAssociation) {
            IIntraModelAssociation intraModel = (IIntraModelAssociation) association;
            roleName = intraModel.getDynamicExtensionsAssociation().getTargetRole().getName();
        } else {
            IInterModelAssociation interModel = (IInterModelAssociation) association;
            roleName = interModel.getSourceAttribute().getName() + " = "
                    + interModel.getTargetAttribute().getName();
        }
        return roleName;
    }

    /**
     * This method returns number of records present in a query result.
     * @param queryResult
     * @return
     */
    public static int getRecordNum(IQueryResult queryResult) {
        Map<String, List<IRecord>> allRecords = queryResult.getRecords();
        int n = 0;
        for (List<IRecord> values : allRecords.values()) {
            n += values.size();
        }
        return n;
    }

    /**
     * This method returns the entity for the given record.
     * @param record
     * @return
     */
    public static EntityInterface getEntity(IRecord record) {
        EntityInterface outputEntity = null;
        Iterator<AttributeInterface> attributeIterator = record.getAttributes().iterator();
        if (attributeIterator.hasNext()) {
            outputEntity = attributeIterator.next().getEntity();
        }
        return outputEntity;
    }

    /**
     * This method returns the entity for the given list of records.
     * @param records
     * @return
     */
    public static EntityInterface getEntity(List<IRecord> records) {
        if (!records.isEmpty()) {
            return getEntity(records.get(0));
        }
        return null;
    }

    public static String getAttributeCDEDetails(AttributeInterface attribute) {
        return getAttributeCDEDetails(attribute, 75);
    }

    public static String getAttributeCDEDetails(AttributeInterface attribute, int offset) {
        StringBuffer tooltip = new StringBuffer(30);

        String attributeDescription = attribute.getDescription();
        String wrappedDescription = "";
        if (attributeDescription != null) {
            wrappedDescription = getWrappedText(attributeDescription, offset);
            tooltip.append("<P>" + wrappedDescription + "</P>");
        }

        if (attribute.getPublicId() != null) {
            tooltip.append("<B>Public Id : </B>" + attribute.getPublicId() + " ");
        }

        StringBuffer allConceptCode = new StringBuffer();
        boolean isFirst = true;
        for (SemanticPropertyInterface semanticProperty : attribute.getSemanticPropertyCollection()) {
            String conceptCode = semanticProperty.getConceptCode();

            if (conceptCode != null) {
                if (isFirst) {
                    allConceptCode.append(conceptCode);
                    isFirst = false;
                } else {
                    allConceptCode.append(", ");
                    allConceptCode.append(conceptCode);
                }
            }
        }

        if (allConceptCode.length() > 0) {
            tooltip.append("<B>Concept Code : </B>");
            tooltip.append(allConceptCode.toString());
        }

        String tooltipString = null;
        if (tooltip.length() != 0) {
            tooltipString = "<HTML>" + tooltip.toString() + "</HTML>";
        }

        return tooltipString;
    }

    /**
     * Method to wrap the text and send it across
     * @return
     */
    public static String getWrappedText(String text, int offset) {
        StringBuffer wrappedText = new StringBuffer();

        String currentString = null;
        int currentStart = 0;
        int strLen = 0;
        int len = 0;

        while (currentStart < text.length() && text.length() > offset) {
            currentString = text.substring(currentStart, (currentStart + offset));
            strLen += currentString.length() + len;
            wrappedText.append(currentString);

            int index = text.indexOf(" ", (currentStart + offset));
            if (index == -1) {
                index = text.indexOf(".", (currentStart + offset));
            }
            if (index == -1) {
                index = text.indexOf(",", (currentStart + offset));
            }
            if (index != -1) {
                len = index - strLen;
                currentString = text.substring((currentStart + offset), (currentStart + offset + len));
                wrappedText.append(currentString);
                wrappedText.append("<BR>");
            } else {
                if (currentStart == 0) {
                    currentStart = offset;
                }
                wrappedText.append(text.substring(currentStart));
                return wrappedText.toString();
            }

            currentStart += offset + len;
            if ((currentStart + offset + len) > text.length()) {
                break;
            }
        }
        wrappedText.append(text.substring(currentStart));
        return wrappedText.toString();
    }

    public static boolean convertAllCategoryToDataCategory() {
        boolean result = true;
        CategoryBusinessInterface categoryBusinessInterface = (CategoryBusinessInterface) Locator.getInstance().locate(
                                                                                                                       EjbNamesConstants.CATEGORY_BEAN,
                                                                                                                       CategoryHomeInterface.class);
        DataCategoryBusinessInterface dataCategoryBusinessInterface = (DataCategoryBusinessInterface) Locator.getInstance().locate(
                                                                                                                                   EjbNamesConstants.DATACATEGORY_BEAN,
                                                                                                                                   DataCategoryHomeInterface.class);
        DataCategory dataCategory = null;
        try {
            List<Category> categoryList = categoryBusinessInterface.getAllCategories();
            for (Category category : categoryList) {
                dataCategory = new DataCategory(category);
                dataCategoryBusinessInterface.saveDataCategory(dataCategory);
            }
        } catch (RemoteException exception) {
            exception.printStackTrace();
            result = false;
        }

        return result;
    }

}