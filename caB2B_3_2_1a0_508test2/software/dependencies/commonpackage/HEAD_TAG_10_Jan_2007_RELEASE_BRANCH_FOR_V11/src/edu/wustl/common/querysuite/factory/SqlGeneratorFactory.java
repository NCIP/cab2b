
package edu.wustl.common.querysuite.factory;

import edu.wustl.common.querysuite.queryengine.ISqlGenerator;
import edu.wustl.common.querysuite.queryengine.impl.SqlGenerator;

/**
 * Factory to return the SqlGenerator's instance. 
 * @author deepti_shelar
 *
 */
public abstract class SqlGeneratorFactory
{

	/**
	 * Method to create instance of class SqlGenerator. 
	 * @return The reference of SqlGenerator. 
	 */
	public static ISqlGenerator getInstance()
	{
		return new SqlGenerator();
	}
}
