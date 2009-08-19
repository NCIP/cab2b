package edu.wustl.cab2bwebapp.bizlogic.executequery;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
import edu.wustl.cab2bwebapp.bizlogic.UserBackgroundQueries;
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
    public List<Map<AttributeInterface, Object>> convert(List<ICategorialClassRecord> records,
                                                         int transformationMaxLimit) {
        List<Map<AttributeInterface, Object>> list = new ArrayList<Map<AttributeInterface, Object>>();
        if (records != null) {
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
        FileWriter fstream = new FileWriter(UserBackgroundQueries.EXPORT_CSV_DIR + "//" + fileName);
        BufferedWriter out = new BufferedWriter(fstream);

        for (AttributeInterface attribute : headers) {
            out.append(attribute.getName());
            out.append(',');
        }
        Map<String, List<ICategorialClassRecord>> urlToResultMap = result.getRecords();
        out.append("Hosting Cancer Research Center");
        out.append(',');
        out.append("Point of Contact");
        out.append(',');
        out.append("Contact eMail");
        out.append(',');
        out.append("Hosting Institution");
        out.append(',');
        out.append('\n');
        out.flush();

        for (String url : urlToResultMap.keySet()) {
            writeToCSV(urlToResultMap.get(url), url, headers, out);
        }
        out.close();
        fstream.close();
    }

    private void writeToCSV(List<ICategorialClassRecord> records, String url, List<AttributeInterface> headers,
                            BufferedWriter out) throws IOException {
        if (records != null && records.size() != 0) {
            ServiceURLInterface serviceUrlMetadata = new ServiceURLOperations().getServiceURLbyURLLocation(url);

            for (ICategorialClassRecord record : records) {
                List<Map<AttributeInterface, Object>> res = convert(record);
                for (Map<AttributeInterface, Object> recordMap : res) {
                    for (AttributeInterface attribute : headers) {
                        String val = recordMap.get(attribute) == null ? " " : recordMap.get(attribute).toString();
                        val = val.replace("\"", "\"\"");
                        if (val.contains(",") || val.contains("\n")) {
                            out.append('"');
                            out.append(val);
                            out.append('"');
                        } else {
                            out.append(val);
                        }
                        out.append(',');
                    }
                    out.append(serviceUrlMetadata.getHostingCenter());
                    out.append(',');
                    out.append(serviceUrlMetadata.getContactName());
                    out.append(',');
                    out.append(serviceUrlMetadata.getContactMailId());
                    out.append(',');
                    out.append(serviceUrlMetadata.getHostingCenterShortName());
                    out.append(',');
                    out.append('\n');
                    out.flush();
                }
            }
        }
    }
}