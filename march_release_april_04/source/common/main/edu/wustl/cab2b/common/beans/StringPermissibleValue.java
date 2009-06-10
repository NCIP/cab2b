/**
 * <p>Title: StringPermissibleValue Class>
 * <p>Description:	This class represents the Permissible value metadata.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Hrishikesh Rajpathak
 */
package edu.wustl.cab2b.common.beans;

import java.rmi.RemoteException;
import java.util.Iterator;
import edu.wustl.cab2b.common.util.Utility;

/**
 * This class represents the Permissible value metadata.
 * 
 * @author hrishikesh_rajpathak
 */
public class StringPermissibleValue extends edu.common.dynamicextensions.domain.StringValue implements
		IStringPermissibleValue {
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Comppares the entered string permissible value or concept code with cached data and returns matchStatus
	 * 
	 * @param obj
	 *            IStringPermissibleValue object
	 * @return The boolean value MatchStatus
	 * @throws RemoteException
	 */
	public boolean newEquals(IStringPermissibleValue obj) {
		boolean matchStatus = false;
		String targetPermissibleValue = null;
		if ((targetPermissibleValue = (String) obj.getValueAsObject()) != null) {
			String sourcePermissibleValue = (String) this.getValueAsObject();
			matchStatus = Utility.compareRegEx(targetPermissibleValue, sourcePermissibleValue);
		}
		if (obj.getSemanticPropertyCollection() != null && getSemanticPropertyCollection() != null) {
			Iterator iterator = obj.getSemanticPropertyCollection().iterator();
			while (iterator.hasNext()) {
				SemanticProperty sourceSemanticProperty = (SemanticProperty) iterator.next();
				Iterator itr = getSemanticPropertyCollection().iterator();
				while (itr.hasNext()) {
					SemanticProperty targetSemanticProperty = (SemanticProperty) itr.next();
					matchStatus = matchStatus || targetSemanticProperty.newEquals(sourceSemanticProperty);
				}
			}
		}
		return matchStatus;
	}
}
