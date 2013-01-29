/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.viewresults;

import edu.wustl.cab2b.client.ui.controls.LazyTable.DefaultLazyTableModel;
import edu.wustl.cab2b.common.queryengine.result.IPartiallyInitialized3DRecord;

/**
 * Table model for BDQ
 * @author rahul_ner
 *
 */
public class BDQTableModel extends DefaultLazyTableModel<BDQDataSource> {

    public BDQTableModel(BDQDataSource dataSource) {
        super(dataSource);
    }

    public Object[][] getColumnValues(int[] selectedColumns) {

        IPartiallyInitialized3DRecord columnRecord = dataSource.getColumnsData(selectedColumns);

        int dim2Size = columnRecord.getDim2Labels().length;
        int dim3Size = columnRecord.getDim3Labels().length;

        Object[][] output = new Object[dim3Size][selectedColumns.length];

        for (int i = 0; i < selectedColumns.length; i++) {
            int dim1Index = selectedColumns[i] / dim2Size;
            int dim2Index = selectedColumns[i] - dim1Index;

            for (int j = 0; j < dim3Size; j++) {
                output[j][i] = columnRecord.getCube()[dim1Index][dim2Index][j];
            }
        }

        return output;
    }
}
