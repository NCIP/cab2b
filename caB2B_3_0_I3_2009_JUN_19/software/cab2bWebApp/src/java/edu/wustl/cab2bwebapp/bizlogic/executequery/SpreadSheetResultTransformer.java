/**
 *
 */

package edu.wustl.cab2bwebapp.bizlogic.executequery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.cab2b.common.queryengine.result.ICategoryResult;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.metadata.category.CategorialAttribute;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import edu.wustl.common.querysuite.metadata.category.Category;

/**
 * Class that converts query results in <code>IQueryResult</code> format
 * into spreadsheet data-model format.
 * @author deepak_shingan
 *
 */
public class SpreadSheetResultTransformer {

    private ICab2bQuery query;

    private IQueryResult<? extends IRecord> queryResult;

    /**
     * List of attributes(columns) to be shown in result spreadsheet.
     */
    private List<AttributeInterface> attributeOrderList = null;

    /**
     * Parameterized constructor
     */
    public SpreadSheetResultTransformer(ICab2bQuery query, IQueryResult<? extends IRecord> queryResult) {
        this.query = query;
        this.queryResult = queryResult;
    }

    /**
     * Main method that converts query results in <code>IQueryResult</code> format
     * into spreadsheet data-model format.
     * @param queryResult
     * @param query
     * @return List<Map<AttributeInterface, Object>>
     */
    public TransformedResultObjectWithContactInfo transResultToSpreadSheetView() {
        TransformedResultObjectWithContactInfo resultObj = null;
        if (query != null && queryResult != null) {

            if (Utility.isCategory(query.getOutputEntity())) {
                resultObj = processCategoryQueryResult();
            } else {
                resultObj = processQueryResult();
            }
            resultObj.setFailedServiceUrl(queryResult.getFailedURLs());
            resultObj.setAllowedAttributes(attributeOrderList);
        }
        return resultObj;
    }

    /**
     * Method to process query result (DO NOT USE for query containing any
     * categories).
     * @param query
     * @param result
     * @param urls
     * @return List<Map<AttributeInterface, Object>>
     */
    private TransformedResultObjectWithContactInfo processQueryResult() {
        attributeOrderList = new ArrayList<AttributeInterface>();
        for (AttributeInterface attribute : query.getOutputEntity().getAttributeCollection()) {
            if (AttributeFilter.isVisible(attribute)) {
                attributeOrderList.add(attribute);
            }
        }

        Map<String, ?> urlVsRecords = queryResult.getRecords();
        TransformedResultObjectWithContactInfo resultObj = new TransformedResultObjectWithContactInfo();
        resultObj.setAllowedAttributes(attributeOrderList);

        List<String> urls = query.getOutputUrls();
        for (String url : urls) {
            List<IRecord> results = (List<IRecord>) urlVsRecords.get(url);
            if (results != null) {
                List<Map<AttributeInterface, Object>> recordsForUrl =
                        new ArrayList<Map<AttributeInterface, Object>>();
                for (IRecord record : results) {
                    Map<AttributeInterface, Object> newRecord = new LinkedHashMap<AttributeInterface, Object>();
                    for (AttributeInterface a : attributeOrderList) {
                        Object value = record.getValueForAttribute(a);
                        newRecord.put(a, value);
                    }
                    recordsForUrl.add(newRecord);
                }
                removeUnwantedAttributes(recordsForUrl, attributeOrderList);
                resultObj.addUrlAndResult(url, recordsForUrl);
            }
        }
        return resultObj;
    }

    /**
     * Method to process query result containing category.
     * @param res
     * @param urls
     * @return
     */
    private TransformedResultObjectWithContactInfo processCategoryQueryResult() {
        ICategoryResult<ICategorialClassRecord> result = (ICategoryResult<ICategorialClassRecord>) queryResult;
        Category cat = result.getCategory();
        attributeOrderList = getAttributesWithOrder(cat);

        TransformedResultObjectWithContactInfo resultObject = new TransformedResultObjectWithContactInfo();
        resultObject.setAllowedAttributes(attributeOrderList);

        ICategoryToSpreadsheetTransformer transformer = new CategoryToSpreadsheetTransformer();
        Map<String, List<ICategorialClassRecord>> map = result.getRecords();
        List<String> urls = query.getOutputUrls();
        for (String url : urls) {
            List<ICategorialClassRecord> recordList = map.get(url);
            resultObject.addUrlAndResult(url, transformer.convert(recordList));
        }

        removeUnwantedAttributes(resultObject.getResultForAllUrls(), attributeOrderList);
        return resultObject;

    }

    /**
     * Method to remove unwanted attribute-value pairs from result.
     * @param masterList
     * @param orderList
     */
    private List<Map<AttributeInterface, Object>> removeUnwantedAttributes(
                                                                           List<Map<AttributeInterface, Object>> masterList,
                                                                           List<AttributeInterface> orderList) {
        Set<AttributeInterface> nonEmptyColList = new HashSet<AttributeInterface>();
        for (Map<AttributeInterface, Object> map : masterList) {
            Set<Map.Entry<AttributeInterface, Object>> attrValueSet = map.entrySet();
            for (Map.Entry<AttributeInterface, Object> attrValue : attrValueSet) {
                Object value = attrValue.getValue();
                if (value != null && !"".equals(value.toString())) {
                    nonEmptyColList.add(attrValue.getKey());
                }
            }
        }
        // Also removing empty-column
        for (Map<AttributeInterface, Object> map : masterList) {
            Set<AttributeInterface> keySet = map.keySet();
            if (keySet.size() != 0) {
                keySet.retainAll(nonEmptyColList);
            }
        }
        if (nonEmptyColList.size() != 0) {
            orderList.retainAll(nonEmptyColList);
        }
        return new ArrayList<Map<AttributeInterface, Object>>(new HashSet<Map<AttributeInterface, Object>>(
                masterList));
    }

    /**
     * Method to set root category class attribute collection first in attribute
     * order list. Eg. If Participant class is the root of the given
     * category,all participant attributes should be visible first in output
     * spreadsheet view
     *
     * @param category
     * @return
     */
    private List<AttributeInterface> getAttributesWithOrder(Category category) {
        List<AttributeInterface> orderAttr = getAttributes(category);
        List<AttributeInterface> orderAttrTwo = getAttributes(category);

        orderAttr.removeAll(category.getCategoryEntity().getAllAttributes());
        orderAttrTwo.remove(orderAttr);
        orderAttr.addAll(0, orderAttrTwo);
        
        /*List<AttributeInterface> list1 = getAttributes(category);
        Collection<AttributeInterface> list2 = category.getCategoryEntity().getAllAttributes();
        list1.removeAll(list2);        
        list1.addAll(0, list2);*/

        return orderAttr;
    }

    /**
     * Returns AttributeInterface collection for the given category class.
     * @param category
     * @return
     */
    private List<AttributeInterface> getAttributes(Category category) {
        List<AttributeInterface> attributes = new ArrayList<AttributeInterface>();
        CategorialClass root = category.getRootClass();
        List<CategorialAttribute> list = getCategorialAttributes(root);
        for (CategorialAttribute attr : list) {
            if (AttributeFilter.isVisible(attr.getSourceClassAttribute())) {
                attributes.add(attr.getCategoryAttribute());
            }
        }
        return attributes;
    }

    /**
     * Returns CategorialAttribute collection for the given category class.
     * @param catClass
     * @return
     */
    private List<CategorialAttribute> getCategorialAttributes(CategorialClass catClass) {
        List<CategorialAttribute> list = new ArrayList<CategorialAttribute>();
        list.addAll(catClass.getCategorialAttributeCollection());
        for (CategorialClass c : catClass.getChildren()) {
            list.addAll(getCategorialAttributes(c));
        }
        return list;
    }
}