/**
 *
 */

package edu.wustl.cab2bwebapp.bizlogic.executequery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

    /**
     * List of attributes(columns) to be shown in result spreadsheet.
     */
    private List<AttributeInterface> allowedAttributes = null;

    /**
     * Main method that converts query results in <code>IQueryResult</code> format
     * into spreadsheet data-model format.
     * @param queryResult
     * @param query
     * @return List<Map<AttributeInterface, Object>>
     */
    public TransformedResultObjectWithContactInfo transResultToSpreadSheetView(
                                                                               ICab2bQuery query,
                                                                               IQueryResult<? extends IRecord> queryResult) {
        TransformedResultObjectWithContactInfo resultObj = null;
        if (query != null && queryResult != null) {
            List<String> urls = query.getOutputUrls();
            if (Utility.isCategory(query.getOutputEntity())) {
                resultObj = processCategoryQueryResult(queryResult, urls);
            } else {
                resultObj = processQueryResult(query, queryResult, urls);
            }
        }
        resultObj.setFailedServiceUrl(queryResult.getFailedURLs());
        resultObj.setAllowedAttributes(allowedAttributes);
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
    private TransformedResultObjectWithContactInfo processQueryResult(ICab2bQuery query,
                                                                      IQueryResult<? extends IRecord> result,
                                                                      List<String> urls) {

        allowedAttributes = new ArrayList<AttributeInterface>();
        for (AttributeInterface attribute : query.getOutputEntity().getAttributeCollection()) {
            if (AttributeFilter.isVisible(attribute)) {
                allowedAttributes.add(attribute);
            }
        }

        Map<String, ?> urlVsRecords = result.getRecords();
        TransformedResultObjectWithContactInfo resultObj = new TransformedResultObjectWithContactInfo();
        for (String url : urls) {
            List<IRecord> results = (List<IRecord>) urlVsRecords.get(url);
            if (results != null) {
                List<Map<AttributeInterface, Object>> recordsForUrl = new ArrayList<Map<AttributeInterface, Object>>();
                for (IRecord record : results) {
                    Map<AttributeInterface, Object> newRecord = new HashMap<AttributeInterface, Object>();
                    for (AttributeInterface a : allowedAttributes) {
                        Object value = record.getValueForAttribute(a);
                        newRecord.put(a, value);
                    }
                    recordsForUrl.add(newRecord);
                }
                removeUnWantedAttributes(recordsForUrl, allowedAttributes);
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
    private TransformedResultObjectWithContactInfo processCategoryQueryResult(IQueryResult<? extends IRecord> res,
                                                                              List<String> urls) {

        ICategoryResult<ICategorialClassRecord> result = (ICategoryResult<ICategorialClassRecord>) res;
        TransformedResultObjectWithContactInfo resultObject = new TransformedResultObjectWithContactInfo();

        Map<String, List<ICategorialClassRecord>> map = result.getRecords();
        ICategoryToSpreadsheetTransformer transformer = new CategoryToSpreadsheetTransformer();
        Category cat = result.getCategory();
        allowedAttributes = getAttributesWithOrder(cat);
        for (String url : urls) {
            List<ICategorialClassRecord> recordList = map.get(url);
            resultObject.addUrlAndResult(url, removeUnWantedAttributes(transformer.convert(recordList),
                                                                       allowedAttributes));
        }
        return resultObject;
    }

    /**
     * Method to remove unwanted attribute-value pairs from result.
     * @param masterList
     * @param orderList
     */
    private List<Map<AttributeInterface, Object>> removeUnWantedAttributes(
                                                                           List<Map<AttributeInterface, Object>> masterList,
                                                                           List<AttributeInterface> orderList) {
        Set<AttributeInterface> nonEmptyColList = new HashSet<AttributeInterface>();
        for (Map<AttributeInterface, Object> map : masterList) {
            Set<AttributeInterface> keySet = map.keySet();
            if (keySet.size() != 0 && orderList.size()!=0) {
                keySet.retainAll(orderList);
                for (AttributeInterface attr : keySet) {
                    Object value = map.get(attr);
                    if (value != null && !value.toString().equals("")) {
                        nonEmptyColList.add(attr);
                    }
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
        if(nonEmptyColList.size()!=0)
        orderList.retainAll(nonEmptyColList);
        return masterList;
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
