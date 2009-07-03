/**
 * 
 */
package edu.wustl.cab2bwebapp.bizlogic.executequery;

import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;

/**
 * Interface for defining APIs for converting category class tree record structure 
 * to spreadsheet(Table) view.
 * 
 * @author deepak_shingan
 */
public interface ICategoryToSpreadsheetTransformer {
    /**
     * Method to convert Category class tree structure to spreadsheet view 
     * @param records
     * @return
     */
    public List<Map<AttributeInterface, Object>> convert(List<ICategorialClassRecord> records, int transformationMaxLimit);

}
