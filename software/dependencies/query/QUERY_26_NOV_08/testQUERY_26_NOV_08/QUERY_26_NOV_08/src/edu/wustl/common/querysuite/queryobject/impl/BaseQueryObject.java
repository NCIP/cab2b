/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.common.querysuite.queryobject.impl;

import edu.wustl.common.querysuite.queryobject.IBaseQueryObject;

/**
 * This is the base class for all the Query objects. Hence all Query objects must extend this class.
 *
 * @author chetan_patil
 * @created Aug 7, 2007
 */
public class BaseQueryObject implements IBaseQueryObject {
    private static final long serialVersionUID = 1L;

    /**
     * Internally generated identifier.
     */
    protected Long id;
    
    protected Boolean isSystemGenerated = false;

    
	/**
	 * Returns is object systemGenerated
	 * @return
	 */
	public Boolean getIsSystemGenerated()
	{
		return isSystemGenerated;
	}

	
	/**
	 * sets the object systemGenerated
	 * @param isSystemGenerated
	 */
	public void setIsSystemGenerated(Boolean isSystemGenerated)
	{
		this.isSystemGenerated = isSystemGenerated;
	}

	public Long getId() {
        return id;
    }

    /**
     * Sets the given id to the domain object
     */
    final public void setId(Long id) {
        this.id = id;
    }
    
}
