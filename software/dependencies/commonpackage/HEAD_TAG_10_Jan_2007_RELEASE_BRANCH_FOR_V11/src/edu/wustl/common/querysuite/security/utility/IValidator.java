/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.security.utility;

import java.util.List;

import edu.wustl.common.beans.SessionDataBean;

public interface IValidator 
{
	
	public boolean hasPrivilegeToView(SessionDataBean sessionDataBean, String baseObjectId, String privilegeName);

	public boolean hasPrivilegeToViewGlobalParticipant(SessionDataBean sessionDataBean);
	
	public boolean hasPrivilegeToViewTemporalColumn(List tqColumnMetadataList,List<String> row,boolean isAuthorizedUser);

}
