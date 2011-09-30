package edu.wustl.common.querysuite.queryobject;

import java.io.Serializable;

/**
 * This interface must be implemented by each Query interface Objects.
 * 
 * @author prafull_kadam
 * @version 1.0
 */
public interface IBaseQueryObject extends Serializable {

    Long getId();

    void setId(Long id);
    
    /**
	 * Returns is object systemGenerated
	 * @return
	 */
    Boolean getIsSystemGenerated();
    
    /**
	 * sets the object systemGenerated
	 * @param isSystemGenerated
	 */
    void setIsSystemGenerated(Boolean isSystemGenerated);
    
}
