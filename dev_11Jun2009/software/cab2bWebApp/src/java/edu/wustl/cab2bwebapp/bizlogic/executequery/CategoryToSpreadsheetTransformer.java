package edu.wustl.cab2bwebapp.bizlogic.executequery;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;

/**
 * @author deepak_shingan
 * 
 * The spreadsheet representation of category class structure for following structure 
 * can be shown in spreadsheet.    
 *   --------------       -----------------------------------
 *  |TissueSpecimen| ---> |TissueSpecimenReviewEventParameter|
 *   ------|-------       ------------------------------------
 *         |
 *         V
 *   -------------------------         -------------------- 
 *  | SpecimenCollectionGroup |-----> | Participant        |
 *   -------------------------         -------------------- 
 *     
 *  
 *  TissueSpecimen    TissueSpecimenReviewEventParameter  SpecimenCollectionGroup   Participant
    T1                TSREP1                                 SCG1                       PAR1
           
 *
 */
public class CategoryToSpreadsheetTransformer implements ICategoryToSpreadsheetTransformer {

    /**
     * Converting <code>ICategorialClassRecord</code> records  
     * @param records
     * @return List<Map<AttributeInterface, Object>>
     */
    public List<Map<AttributeInterface, Object>> convert(List<ICategorialClassRecord> records, int transformationMaxLimit) {
        List<Map<AttributeInterface, Object>> list = new ArrayList<Map<AttributeInterface, Object>>();
        if (records != null) {
            for (ICategorialClassRecord record : records) {
                List<LinkedHashMap<AttributeInterface, Object>> res = convert(record);
                list.addAll(res);
                if(list.size() >  transformationMaxLimit) {  
                    break;
                } 
            }
        }
        return list;
    }

    /**
     * Converting single <code>ICategorialClassRecord</code> record  
     * to required format.
     * @param record
     * @return List<Map<AttributeInterface, Object>>
     */
    private List<LinkedHashMap<AttributeInterface, Object>> convert(ICategorialClassRecord record) {
        Set<CategorialClass> children = record.getCategorialClass().getChildren();

        //Attribute-Value pair of current record
        LinkedHashMap<AttributeInterface, Object> avPairs = new LinkedHashMap<AttributeInterface, Object>();
        for (AttributeInterface a : record.getAttributes()) {
            avPairs.put(a, record.getValueForAttribute(a));
        }
        List<LinkedHashMap<AttributeInterface, Object>> processedRecords =
                new ArrayList<LinkedHashMap<AttributeInterface, Object>>();
        if (children.size() == 0) {
            processedRecords.add(avPairs);
            return processedRecords;
        }
        Map<CategorialClass, List<ICategorialClassRecord>> childVsRecords =
                record.getChildrenCategorialClassRecords();
        boolean isRecordAdded = false;
        for (CategorialClass child : children) {
            List<LinkedHashMap<AttributeInterface, Object>> processedChildRecords =
                    new ArrayList<LinkedHashMap<AttributeInterface, Object>>();
            List<ICategorialClassRecord> childRecords = childVsRecords.get(child);
            if (childRecords == null) {
                continue;
            }
            for (ICategorialClassRecord childRecord : childRecords) {
                processedChildRecords.addAll(convert(childRecord));
            }
            for (Map<AttributeInterface, Object> childRecord : processedChildRecords) {
                childRecord.putAll(avPairs);
            }
            if (processedRecords.isEmpty()) {
                processedRecords = processedChildRecords;
            } else {
                List<LinkedHashMap<AttributeInterface, Object>> tempProcessedRecords =
                        new ArrayList<LinkedHashMap<AttributeInterface, Object>>();
                //cross product current child with all previously processed children
                for (LinkedHashMap<AttributeInterface, Object> processedRecord : processedRecords) {
                    for (LinkedHashMap<AttributeInterface, Object> childrenRecord : processedChildRecords) {
                        LinkedHashMap<AttributeInterface, Object> newMap =
                                new LinkedHashMap<AttributeInterface, Object>();
                        newMap.putAll(childrenRecord);
                        newMap.putAll(processedRecord);
                        tempProcessedRecords.add(newMap);
                    }
                }
                processedRecords = tempProcessedRecords;
            }
            isRecordAdded = true;
        }
        //This check is required if child some categorial class is present
        //but no child records are available for any of the child categorial  
        //class. Then add parent class records into the list and return it.
        if (isRecordAdded == false) {
            processedRecords.add(avPairs);
        }
        return processedRecords;
    }
}
