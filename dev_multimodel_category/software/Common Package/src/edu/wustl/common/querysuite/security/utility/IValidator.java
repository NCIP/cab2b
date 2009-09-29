package edu.wustl.common.querysuite.security.utility;

import java.util.List;

import edu.wustl.common.beans.SessionDataBean;

public interface IValidator 
{
	
	public boolean hasPrivilegeToView(SessionDataBean sessionDataBean, String baseObjectId, String privilegeName);

	public boolean hasPrivilegeToViewGlobalParticipant(SessionDataBean sessionDataBean);
	
	public boolean hasPrivilegeToViewTemporalColumn(List tqColumnMetadataList,List<String> row,boolean isAuthorizedUser);

}
