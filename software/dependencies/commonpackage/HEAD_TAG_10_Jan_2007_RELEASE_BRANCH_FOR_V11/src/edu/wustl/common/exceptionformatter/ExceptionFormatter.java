/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.exceptionformatter;
/**
 * 
 * @author sachin_lale
 * Description: Interface defines method for formatting the database specific Exception message  
 */
public interface ExceptionFormatter {
	public String formatMessage(Exception objExcp,Object args[]);
}
