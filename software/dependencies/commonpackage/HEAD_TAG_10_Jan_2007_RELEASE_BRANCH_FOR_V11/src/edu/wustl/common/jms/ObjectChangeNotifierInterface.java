/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.jms;

import java.io.Serializable;

/**
 * 
 * @author kalpana_thakur
 * TODO This interface is used to read the Object 
 * 
 */
public interface ObjectChangeNotifierInterface extends Serializable
{	
	/**
	 * This method is used to read the object 
	 * */
	public void read(Object obj);
}
