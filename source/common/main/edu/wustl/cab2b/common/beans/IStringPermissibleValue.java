/**
 * <p>Title: IStringPermissibleValue Interface>
 * <p>Description:	IStringPermissibleValue interface.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Hrishikesh Rajpathak
 * @version 1.1
 */
package edu.wustl.cab2b.common.beans;

/**
 * IStringPermissibleValue Interface
 * 
 * @author hrishikesh rajpathak
 */
public interface IStringPermissibleValue extends
		edu.common.dynamicextensions.domaininterface.PermissibleValueInterface {
	/**
	 * Returns true if the passed IStringPermissibleValue object stringPermissibleValue is equal to this object
	 * else returns false.
	 * 
	 * @param obj
	 *            The IStringPermissibleValue stringPermissibleValue.
	 * @return true if the passed IStringPermissibleValue object stringPermissibleValue is equal to this object
	 *         else returns false.
	 */
	public boolean newEquals(IStringPermissibleValue stringPermissibleValue);
}