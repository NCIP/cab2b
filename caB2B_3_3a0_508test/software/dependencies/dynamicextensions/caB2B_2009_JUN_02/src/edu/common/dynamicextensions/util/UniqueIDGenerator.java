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
