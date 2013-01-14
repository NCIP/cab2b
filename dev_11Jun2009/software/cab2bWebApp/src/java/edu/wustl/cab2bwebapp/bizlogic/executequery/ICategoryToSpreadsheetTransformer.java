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
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.cab2b.common.queryengine.result.ICategoryResult;

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

    /**
     * Method to write Category class tree structure to CSV file. 
     * @param records
     * @param url
     * @return
     * @throws IOException
     */
    public void writeToCSV(ICategoryResult<ICategorialClassRecord> result, String fileName,List<AttributeInterface> attributes, String filePath) throws IOException;
}
