/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * <p>Title: LookupLogic Class>
 * <p>Description:	This Interface is use for Lookup Logic.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author vaishali_khandelwal
 * 
 */
package edu.wustl.common.lookup;

import java.util.List;


public interface LookupLogic
{
	List lookup(LookupParameters params) throws Exception;
}
