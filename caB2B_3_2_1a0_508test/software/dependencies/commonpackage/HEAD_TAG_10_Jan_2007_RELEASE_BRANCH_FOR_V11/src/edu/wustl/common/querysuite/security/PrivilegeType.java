/**
 * 
 */

package edu.wustl.common.querysuite.security;

import edu.wustl.common.util.global.Constants;

/**
 * Enumeration that will contains different types of class level privileges. 
 * @author prafull_kadam
 *
 */
public enum PrivilegeType {
	ObjectLevel(Constants.OBJECT_LEVEL_SECURE_RETRIEVE), InsecureLevel(Constants.INSECURE_RETRIEVE), ClassLevel(
			Constants.CLASS_LEVEL_SECURE_RETRIEVE);

	int value;

	/**
	 * @param value The value
	 */
	PrivilegeType(int value)
	{
		this.value = value;
	}

	/**
	 * To get the PrivilegeType for the given value.
	 * @param value The value
	 * @return The PrivilegeType for the given value
	 */
	public static PrivilegeType getPrivilegeType(int value)
	{
		PrivilegeType type;
		switch (value)
		{
			case Constants.OBJECT_LEVEL_SECURE_RETRIEVE :
				type = ObjectLevel;
				break;
			case Constants.CLASS_LEVEL_SECURE_RETRIEVE :
				type = ClassLevel;
				break;
			case Constants.INSECURE_RETRIEVE :
			default :
				type = InsecureLevel;
				break;
		}
		return type;
	}

}
