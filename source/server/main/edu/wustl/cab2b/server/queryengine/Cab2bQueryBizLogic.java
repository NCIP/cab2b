/**
 * 
 */
package edu.wustl.cab2b.server.queryengine;

import edu.wustl.cab2b.common.queryengine.Cab2bQuery;
import edu.wustl.cab2b.common.queryengine.ICab2bParameterizedQuery;
import edu.wustl.common.querysuite.bizlogic.QueryBizLogic;

/**
 * This class processes the Cab2bQuery object before persisting and after retreival.
 * @author chetan_patil
 */
public class Cab2bQueryBizLogic extends QueryBizLogic<ICab2bParameterizedQuery> {

    /** 
     * @see edu.wustl.common.querysuite.queryobject.util.QueryProcessor#getQueryClassName()
     */
    protected String getQueryClassName() {
        return Cab2bQuery.class.getName();
    }

}
