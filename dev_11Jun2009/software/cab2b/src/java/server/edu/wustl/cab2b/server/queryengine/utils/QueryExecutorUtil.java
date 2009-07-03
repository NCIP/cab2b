package edu.wustl.cab2b.server.queryengine.utils;

import java.util.List;
import java.util.Map;

import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;

/**
 * @author Deepak
 */
public class QueryExecutorUtil {
    /**
     * It takes list of root ICRs for a URL and count the resulting number of Total spreadsheet records.
     * On the basis of max limit for total spreadsheet records, it returns 
     * true = if URL is feasible for conversion
     * or false = if its infeasible/or/ if num of records are 0 for failed URLs
     * A->B->D          A is the root categorial class and B,C,F are the child categorial classes for A
     *  ->C->G->H
     *  ->F
     * @param recordList
     * @param maxLimit
     * @return
     */
    public static boolean isURLFeasibleForConversion(List<ICategorialClassRecord> recordList, int maxLimit) {
        if (recordList != null) {
            if (recordList.size() > maxLimit) //if no of Root records exceed the limit, return false 
                return false;

            int finalSpreadSheetCount = 0;
            for (ICategorialClassRecord rootRecord : recordList) {
                int rootRecordCount = 1; // A1, A2,A3,A4
                Map<CategorialClass, List<ICategorialClassRecord>> categorialclassVsRecordsMap =
                        rootRecord.getChildrenCategorialClassRecords();
                if (categorialclassVsRecordsMap.isEmpty()) {
                    finalSpreadSheetCount = finalSpreadSheetCount + rootRecordCount;
                    continue;
                }
                for (CategorialClass categorialClass : categorialclassVsRecordsMap.keySet()) //B,C,F
                {
                    int childLeavesCount = 0;
                    for (ICategorialClassRecord categorialChildRecord : categorialclassVsRecordsMap
                        .get(categorialClass)) //B1,B2,C1,C2,F1,F2
                    {
                        childLeavesCount = childLeavesCount + countLeavesForEachChildRecord(categorialChildRecord);
                        ;
                    }
                    rootRecordCount = rootRecordCount * childLeavesCount;
                }
                finalSpreadSheetCount = finalSpreadSheetCount + rootRecordCount;
            }

            if (finalSpreadSheetCount < maxLimit)
                return true; // feasible URL good enough to be transformed
            else
                return false; // infeasible URL, can't invoke transformer
        } else
            return false; //case of failed URL => num of records= 0, Transformer should not be invoked
    }

    /**
     * This method will take input as a CategoryRecord and just count the number of leaves of the tree.
     * It will return the total leaf count.
     * @param categorialChildRecord
     * @return
     */
    public static int countLeavesForEachChildRecord(ICategorialClassRecord categorialChildRecord)//B1,B2,C1,C2,F1,F2
    {
        int leavesCount = 0;
        Map<CategorialClass, List<ICategorialClassRecord>> categorialclassVsRecordsMap =
                categorialChildRecord.getChildrenCategorialClassRecords();

        if (categorialclassVsRecordsMap.isEmpty())
            return 1; //if leaf has come, return 1; D,H,F

        for (CategorialClass categorialClass : categorialclassVsRecordsMap.keySet()) //G
        {
            for (ICategorialClassRecord childRecord : categorialclassVsRecordsMap.get(categorialClass)) {
                leavesCount = leavesCount + countLeavesForEachChildRecord(childRecord); //recursive call
            }
        }
        return leavesCount; //if its not a leaf, while returning in the called stack, return 0 for the interleaved nodes.
    }
}
