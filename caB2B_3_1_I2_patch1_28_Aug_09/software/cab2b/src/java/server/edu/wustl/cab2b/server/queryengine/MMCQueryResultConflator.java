/**
 * 
 */
package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.multimodelcategory.MultiModelAttribute;
import edu.wustl.cab2b.common.multimodelcategory.MultiModelCategory;
import edu.wustl.cab2b.common.queryengine.MultiModelCategoryQuery;
import edu.wustl.cab2b.common.queryengine.result.FQPUrlStatus;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.cab2b.common.queryengine.result.ICategoryResult;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.queryengine.result.QueryResultFactory;
import edu.wustl.cab2b.server.multimodelcategory.MultiModelCategoryOperations;
import edu.wustl.common.querysuite.metadata.category.CategorialAttribute;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;

/**
 * @author chetan_patil
 *
 */
public class MMCQueryResultConflator {
    private MultiModelCategoryQuery mmcQuery;

    private Map<AttributeInterface, Collection<AttributeInterface>> attributeMapping =
            new HashMap<AttributeInterface, Collection<AttributeInterface>>();

    public MMCQueryResultConflator(MultiModelCategoryQuery mmcQuery) {
        this.mmcQuery = mmcQuery;
        populateEntityAttributeMap(mmcQuery);
    }

    /**
     * This method mergers the query results of sub-queries of the given multi-model category query 
     * @param queryResults
     * @param mmcQuery
     * @return
     */
    public IQueryResult<? extends IRecord> conflate(Collection<IQueryResult<? extends IRecord>> queryResults) {
        IQueryResult<IRecord> conflatedResult = QueryResultFactory.createResult(mmcQuery.getOutputEntity());
        Collection<FQPUrlStatus> fqpQueryUrlStatus = new HashSet<FQPUrlStatus>();

        for (IQueryResult<? extends IRecord> queryResult : queryResults) {

            ICategoryResult<ICategorialClassRecord> categoryResult =
                    (ICategoryResult<ICategorialClassRecord>) queryResult;
            fqpQueryUrlStatus.addAll(categoryResult.getFQPUrlStatus());
            Map<String, List<ICategorialClassRecord>> catRecords = categoryResult.getRecords();
            if (catRecords != null && !catRecords.isEmpty()) {
                Set<Entry<String, List<ICategorialClassRecord>>> urlRecordEntries = catRecords.entrySet();
                for (Entry<String, List<ICategorialClassRecord>> urlRecordEntry : urlRecordEntries) {
                    String url = urlRecordEntry.getKey();

                    List<IRecord> flattenedRecords = new ArrayList<IRecord>();
                    List<ICategorialClassRecord> catClassRecords = urlRecordEntry.getValue();
                    for (ICategorialClassRecord catClassRecord : catClassRecords) {
                        flattenedRecords.addAll(generateIRecord(catClassRecord));
                    }
                    conflatedResult.addRecords(url, flattenedRecords);
                }
            }
        }
        conflatedResult.setFQPUrlStatus(fqpQueryUrlStatus);
        return conflatedResult;
    }

    private Map<AttributeInterface, Collection<AttributeInterface>> populateEntityAttributeMap(
                                                                                               MultiModelCategoryQuery mmcQuery) {
        EntityInterface outputEntity = mmcQuery.getOutputEntity();
        MultiModelCategory mmc = new MultiModelCategoryOperations().getMultiModelCategoryByEntity(outputEntity);

        Collection<MultiModelAttribute> mmcAttributes = mmc.getMultiModelAttributes();
        for (MultiModelAttribute mmcAttribute : mmcAttributes) {
            Collection<CategorialAttribute> catAttributes = mmcAttribute.getCategorialAttributes();
            for (CategorialAttribute catAttribute : catAttributes) {
                AttributeInterface catDEAttribute = catAttribute.getCategoryAttribute();

                Collection<AttributeInterface> attributes = attributeMapping.get(mmcAttribute.getAttribute());
                if (attributes == null) {
                    attributes = new ArrayList<AttributeInterface>();
                    attributeMapping.put(mmcAttribute.getAttribute(), attributes);
                }
                attributes.add(catDEAttribute);
            }
        }

        return attributeMapping;
    }

    private List<IRecord> generateIRecord(ICategorialClassRecord catClassRecord) {
        List<IRecord> flattenedCategoryClassRecord = new ArrayList<IRecord>();

        List<Map<AttributeInterface, Object>> flattenedCatClassRecords = flattenCategoryRecord(catClassRecord);
        for (Map<AttributeInterface, Object> flattenedCatClassRecord : flattenedCatClassRecords) {
            Set<AttributeInterface> attributeSet = flattenedCatClassRecord.keySet();
            if (!attributeSet.isEmpty()) {
                IRecord record = QueryResultFactory.createRecord(attributeSet, catClassRecord.getRecordId());
                Set<Entry<AttributeInterface, Object>> recordValues = flattenedCatClassRecord.entrySet();
                for (Entry<AttributeInterface, Object> recordValue : recordValues) {
                    record.putValueForAttribute(recordValue.getKey(), recordValue.getValue());
                }

                flattenedCategoryClassRecord.add(record);
            }
        }

        return flattenedCategoryClassRecord;
    }

    /**
     * Converting single <code>ICategorialClassRecord</code> record to required format.
     * @param catClassRecord
     * @return List<Map<AttributeInterface, Object>>
     */
    private List<Map<AttributeInterface, Object>> flattenCategoryRecord(ICategorialClassRecord catClassRecord) {
        List<Map<AttributeInterface, Object>> flattenedCatClassRecords =
                new ArrayList<Map<AttributeInterface, Object>>();

        //Attribute-Value pair of current record
        Map<AttributeInterface, Object> attributeValuePairs = extractAttributeValuePairs(catClassRecord);

        Set<Entry<CategorialClass, List<ICategorialClassRecord>>> childrenCatClassRecordEntries =
                catClassRecord.getChildrenCategorialClassRecords().entrySet();
        if (childrenCatClassRecordEntries.size() == 0) {
            flattenedCatClassRecords.add(attributeValuePairs);
        } else {
            boolean isRecordAdded = false;
            for (Entry<CategorialClass, List<ICategorialClassRecord>> childCatClassRecordEntry : childrenCatClassRecordEntries) {
                List<Map<AttributeInterface, Object>> flattenedChildrenCatClassRecords =
                        new ArrayList<Map<AttributeInterface, Object>>();

                List<ICategorialClassRecord> childCatClassRecords = childCatClassRecordEntry.getValue();
                if (childCatClassRecords != null) {
                    for (ICategorialClassRecord childCatClassRecord : childCatClassRecords) {
                        flattenedChildrenCatClassRecords.addAll(flattenCategoryRecord(childCatClassRecord));
                    }

                    for (Map<AttributeInterface, Object> flattenedChildCatClassRecord : flattenedChildrenCatClassRecords) {
                        flattenedChildCatClassRecord.putAll(attributeValuePairs);
                    }

                    if (flattenedCatClassRecords.isEmpty()) {
                        flattenedCatClassRecords = flattenedChildrenCatClassRecords;
                    } else {
                        flattenedCatClassRecords =
                                createCrossProduct(flattenedCatClassRecords, flattenedChildrenCatClassRecords);
                    }
                    isRecordAdded = true;
                }
            }

            //This check is required if child categorialclass is present but has no records present.
            //In this case, add parent class records into the list and return it.
            if (isRecordAdded == false) {
                flattenedCatClassRecords.add(attributeValuePairs);
            }
        }

        return flattenedCatClassRecords;
    }

    /**
     * This method extracts the attribute-value pairs from the given catagorialclass record
     * @param catClassRecord
     * @return
     */
    private Map<AttributeInterface, Object> extractAttributeValuePairs(ICategorialClassRecord catClassRecord) {
        Map<AttributeInterface, Object> attributeValuePairs = new HashMap<AttributeInterface, Object>();

        Set<Entry<AttributeInterface, Collection<AttributeInterface>>> attributeMapEntries =
                attributeMapping.entrySet();
        for (Entry<AttributeInterface, Collection<AttributeInterface>> attributeMapEntry : attributeMapEntries) {
            AttributeInterface mmcDEAttribute = attributeMapEntry.getKey();
            Collection<AttributeInterface> originalAttributes = attributeMapEntry.getValue();

            for (AttributeInterface attribute : catClassRecord.getAttributes()) {
                if (originalAttributes.contains(attribute)) {
                    attributeValuePairs.put(mmcDEAttribute, catClassRecord.getValueForAttribute(attribute));
                }
            }
        }

        return attributeValuePairs;
    }

    /**
     * This method generates cross product of the child with all previously processed children
     * @param flattenedCatClassRecords
     * @param flattenedChildrenCatClassRecords
     * @return
     */
    private List<Map<AttributeInterface, Object>> createCrossProduct(
                                                                     List<Map<AttributeInterface, Object>> flattenedCatClassRecords,
                                                                     List<Map<AttributeInterface, Object>> flattenedChildrenCatClassRecords) {
        List<Map<AttributeInterface, Object>> crossProdcuct = new ArrayList<Map<AttributeInterface, Object>>();

        for (Map<AttributeInterface, Object> flattenedCatClassRecord : flattenedCatClassRecords) {
            for (Map<AttributeInterface, Object> flattenedChildCatClassRecord : flattenedChildrenCatClassRecords) {
                Map<AttributeInterface, Object> unionMap =
                        new LinkedHashMap<AttributeInterface, Object>(flattenedChildCatClassRecord.size()
                                + flattenedCatClassRecord.size());
                unionMap.putAll(flattenedChildCatClassRecord);
                unionMap.putAll(flattenedCatClassRecord);
                crossProdcuct.add(unionMap);
            }
        }

        return crossProdcuct;
    }

}
