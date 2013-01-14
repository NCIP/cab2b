/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 *
 */

package edu.wustl.cab2bwebapp.bizlogic.executequery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.serviceurl.ServiceURLOperations;
import edu.wustl.common.querysuite.metadata.category.CategorialAttribute;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import edu.wustl.common.querysuite.metadata.category.Category;

/**
 * Class that converts query results in <code>IQueryResult</code> format
 * into spreadsheet data-model format.
 * @author deepak_shingan
 *
 */
public class SpreadSheetResultTransformer extends SpreadsheetTransformer {

    private ICab2bQuery query;

    private IQueryResult<? extends IRecord> queryResult;

    /**
     * List of attributes(columns) to be shown in result spreadsheet.
     */
    private List<AttributeInterface> attributeOrderList = null;

    /**
     * Parameterized constructor
     * @param query
     * @param queryResult
     */
    public SpreadSheetResultTransformer(ICab2bQuery query, IQueryResult<? extends IRecord> queryResult) {
        this.query = query;
        this.queryResult = queryResult;
    }

    /**
     * Main method that converts query results in <code>IQueryResult</code> format
     * into spreadsheet data-model format.
     * @param transformationMaxLimit
     * @return {@link TransformedResultObjectWithContactInfo}
     */
    public TransformedResultObjectWithContactInfo transResultToSpreadSheetView(int transformationMaxLimit) {
        TransformedResultObjectWithContactInfo resultObj = null;
        if (query != null && queryResult != null) {

            if (Utility.isCategory(query.getOutputEntity())) {
                resultObj = processCategoryQueryResult(transformationMaxLimit);
            } else {
                resultObj = processQueryResult(transformationMaxLimit);
            }
            resultObj.setFailedServiceUrl(queryResult.getFQPUrlStatus());
        }
        return resultObj;
    }

    /**
     * Method to write IQueryResult to CSV file. 
     * @return FileName in which results are written  
     * @throws IOException 
     */
    public String writeToCSV() throws IOException {
        String fileName = query.getId() + "_" + System.currentTimeMillis() + ".csv";
        if (query != null && queryResult != null) {
            if (Utility.isCategory(query.getOutputEntity())) {
                writeCategoryResultToCSV(fileName);
            } else {
                writeQueryResultToCSV(fileName);
            }
        }
        return fileName;
    }

    /**
     * @return
     * @throws IOException
     */
    private String writeQueryResultToCSV(String fileName) throws IOException {
        List<AttributeInterface> attributeOrder = new ArrayList<AttributeInterface>();
        for (AttributeInterface attribute : query.getOutputEntity().getAttributeCollection()) {
            if (AttributeFilter.isVisible(attribute)) {
                attributeOrder.add(attribute);
            }
        }
        CsvWriter writeToCSV = new CsvWriter(attributeOrder, fileName);
        Map<String, ?> urlVsRecords = queryResult.getRecords();
        Set<String> urls = urlVsRecords.keySet();
        for (String url : urls) {
            List<IRecord> results = (List<IRecord>) urlVsRecords.get(url);
            if (results != null) {
                ServiceURLInterface serviceUrlMetadata =
                        new ServiceURLOperations().getServiceURLbyURLLocation(url);
                List<Map<AttributeInterface, Object>> recordList =
                        new ArrayList<Map<AttributeInterface, Object>>();
                for (IRecord record : results) {
                    Map<AttributeInterface, Object> recordMap = new HashMap<AttributeInterface, Object>();
                    for (AttributeInterface attribute : record.getAttributes()) {
                        recordMap.put(attribute, record.getValueForAttribute(attribute));
                    }
                    recordList.add(recordMap);
                }
                writeToCSV.writeData(recordList, serviceUrlMetadata);
            }
        }
        writeToCSV.closeFile();
        return fileName;
    }

    /**
     * @param fileName
     * @throws IOException
     */
    private void writeCategoryResultToCSV(String fileName) throws IOException {
        ICategoryResult<ICategorialClassRecord> result = (ICategoryResult<ICategorialClassRecord>) queryResult;
        List<AttributeInterface> attributes = getAttributesWithOrder(result.getCategory());
        CategoryToSpreadsheetTransformer transformer = new CategoryToSpreadsheetTransformer();
        transformer.writeToCSV(result, fileName, attributes);
    }

    /**
     * Method to process query result (DO NOT USE for query containing any
     * categories).
     * @param transformationMaxLimit
     * @return {@link TransformedResultObjectWithContactInfo}
     */
    private TransformedResultObjectWithContactInfo processQueryResult(int transformationMaxLimit) {
        attributeOrderList = new ArrayList<AttributeInterface>();
        for (AttributeInterface attribute : query.getOutputEntity().getAttributeCollection()) {
            if (AttributeFilter.isVisible(attribute)) {
                attributeOrderList.add(attribute);
            }
        }

        Map<String, ?> urlVsRecords = queryResult.getRecords();
        TransformedResultObjectWithContactInfo resultObj =
                new TransformedResultObjectWithContactInfo(attributeOrderList);

        List<String> urls = query.getOutputUrls();
        for (String url : urls) {
            List<IRecord> records = (List<IRecord>) urlVsRecords.get(url);
            if (records != null) {
                for (IRecord record : records) {
                    recordVsCount.put(record, getDepth(record));
                }
                Collections.sort(records, new RecordComparator(recordVsCount));
            }
        }

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
                    if (recordsForUrl.size() > transformationMaxLimit) {
                        removeUnwantedAttributes(recordsForUrl, attributeOrderList);
                        break;
                    }
                }
                resultObj.addUrlAndResult(url, recordsForUrl);
            }
        }
        removeUnwantedAttributes(resultObj.getResultForAllUrls(), attributeOrderList);
        return resultObj;
    }

    private int getDepth(IRecord record) {
        int depth = 0;
        for (AttributeInterface attribute : record.getAttributes()) {
            Object value = record.getValueForAttribute(attribute);
            if (value != null && !value.toString().isEmpty()) {
                depth++;
            }
        }
        return depth;
    }

    /**
     * Method to process query result containing category.
     * @param res
     * @param urls
     * @return
     */
    private TransformedResultObjectWithContactInfo processCategoryQueryResult(int transformationMaxLimit) {
        ICategoryResult<ICategorialClassRecord> result = (ICategoryResult<ICategorialClassRecord>) queryResult;
        Category cat = result.getCategory();
        attributeOrderList = getAttributesWithOrder(cat);

        TransformedResultObjectWithContactInfo resultObject =
                new TransformedResultObjectWithContactInfo(attributeOrderList);

        Map<String, List<ICategorialClassRecord>> map = result.getRecords();
        //List<String> urls = query.getOutputUrls();
        Set<String> urls = map.keySet();
        for (String url : urls) {
            List<ICategorialClassRecord> recordList = map.get(url);
            List<Map<AttributeInterface, Object>> transformedResult = null;
            CategoryToSpreadsheetTransformer transformer = new CategoryToSpreadsheetTransformer();
            transformedResult = transformer.convert(recordList, transformationMaxLimit);
            /** After implementing Priority in query executor the results for each URL is transformed till limit
             * Once this is done then the result for each url are combined and trimmed up to limit  
             */
            resultObject.addUrlAndResult(url, transformedResult);
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
