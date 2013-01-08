/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.searchDataWizard.addLimit;

import edu.wustl.common.querysuite.queryobject.IExpression;

public interface IUpdateAddLimitUIInterface {
    public void editAddLimitUI(IExpression expression);

    public void clearAddLimitUI();
}
