package edu.wustl.cab2bwebapp.bizlogic.executequery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.cab2b.common.queryengine.result.ICategoryResult;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.server.serviceurl.ServiceURLOperations;
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
    private class RecordComparator implements Comparator<ICategorialClassRecord> {
        Map<ICategorialClassRecord, Integer> recordVsCount;

        public RecordComparator(Map<ICategorialClassRecord, Integer> recordVsCount) {
            this.recordVsCount = recordVsCount;
        }

        public int compare(ICategorialClassRecord o1, ICategorialClassRecord o2) {
            Integer d1 = recordVsCount.get(o1);
            Integer d2 = recordVsCount.get(o2);
            if (d1 == null || d2 == null) {
                return 0;
            }
            if (d1 < d2) {
                return 1;
            }
            if (d1 > d2) {
                return -1;
            }
            return 0;
        }

    }

    private int getDepth(ICategorialClassRecord o1) {
        if (o1 == null) {
            return 0;
        }
        Set<CategorialClass> mapKeys = o1.getCategorialClass().getChildren();
        if (mapKeys == null || mapKeys.isEmpty()) {
            return 1;
        }
        Map<CategorialClass, List<ICategorialClassRecord>> children = o1.getChildrenCategorialClassRecords();
        int max = 0;
        for (CategorialClass ICCR : mapKeys) {
            if (children != null && ICCR != null && children.get(ICCR) != null) {
                for (ICategorialClassRecord ccr : children.get(ICCR)) {
                    int d = getDepth(ccr);
                    if (max < d) {
                        max = d;
                    }
                }
            }
        }
        return max + 1;
    }

    /**
     * Converting <code>ICategorialClassRecord</code> records  
     * @param records
     * @return List<Map<AttributeInterface, Object>>
     */
    public List<Map<AttributeInterface, Object>> convert(List<ICategorialClassRecord> records,
                                                         int transformationMaxLimit) {
        Map<ICategorialClassRecord, Integer> recordVsCount = new HashMap<ICategorialClassRecord, Integer>();
        List<Map<AttributeInterface, Object>> list = new ArrayList<Map<AttributeInterface, Object>>();
        if (records != null) {
            for (ICategorialClassRecord r : records) {
                recordVsCount.put(r, getDepth(r));
            }
            Collections.sort(records, new RecordComparator(recordVsCount));
            for (ICategorialClassRecord record : records) {
                List<Map<AttributeInterface, Object>> res = convert(record);
                list.addAll(res);
                if (list.size() > transformationMaxLimit) {
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
    private List<Map<AttributeInterface, Object>> convert(ICategorialClassRecord record) {
        Set<CategorialClass> children = record.getCategorialClass().getChildren();

        //Attribute-Value pair of current record
        Map<AttributeInterface, Object> avPairs =
                new HashMap<AttributeInterface, Object>(record.getAttributes().size());
        for (AttributeInterface a : record.getAttributes()) {
            avPairs.put(a, record.getValueForAttribute(a));
        }

        if (children.size() == 0) {
            List<Map<AttributeInterface, Object>> processedRecords =
                    new ArrayList<Map<AttributeInterface, Object>>(1);
            processedRecords.add(avPairs);
            return processedRecords;
        }
        List<Map<AttributeInterface, Object>> processedRecords = new ArrayList<Map<AttributeInterface, Object>>();
        Map<CategorialClass, List<ICategorialClassRecord>> childVsRecords =
                record.getChildrenCategorialClassRecords();
        boolean isRecordAdded = false;
        for (CategorialClass child : children) {
            List<Map<AttributeInterface, Object>> processedChildRecords =
                    new ArrayList<Map<AttributeInterface, Object>>();
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
                List<Map<AttributeInterface, Object>> tempProcessedRecords =
                        new ArrayList<Map<AttributeInterface, Object>>();
                //cross product current child with all previously processed children
                for (Map<AttributeInterface, Object> processedRecord : processedRecords) {
                    for (Map<AttributeInterface, Object> childrenRecord : processedChildRecords) {
                        Map<AttributeInterface, Object> newMap =
                                new LinkedHashMap<AttributeInterface, Object>(childrenRecord.size()
                                        + processedRecord.size());
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

    /**
     * @see edu.wustl.cab2bwebapp.bizlogic.executequery.ICategoryToSpreadsheetTransformer#writeToCSV(edu.wustl.cab2b.common.queryengine.result.ICategoryResult)
     */
    public void writeToCSV(ICategoryResult<ICategorialClassRecord> result, String fileName,
                           List<AttributeInterface> headers) throws IOException {        
        CsvWriter writeToCSV = new CsvWriter(headers, fileName);
        Map<String, List<ICategorialClassRecord>> urlToResultMap = result.getRecords();
        for (String url : urlToResultMap.keySet()) {
            List<ICategorialClassRecord> records = urlToResultMap.get(url);
            ServiceURLInterface serviceUrlMetadata = new ServiceURLOperations().getServiceURLbyURLLocation(url);
            for (ICategorialClassRecord record : records) {
                List<Map<AttributeInterface, Object>> recordList = convert(record);
                writeToCSV.writeData(recordList, serviceUrlMetadata);
            }
        }
        writeToCSV.closeFile();
    }
}