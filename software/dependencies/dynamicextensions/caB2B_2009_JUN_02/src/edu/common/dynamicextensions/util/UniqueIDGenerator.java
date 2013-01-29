/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.common.dynamicextensions.util;


/**
 * @author chetan_patil
 *
 */
public class UniqueIDGenerator
{
	static long id = 0;
	public static Long getId()
	{
		return ++id;
	}
}
