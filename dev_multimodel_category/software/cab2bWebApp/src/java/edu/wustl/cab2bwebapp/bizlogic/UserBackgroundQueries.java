/**
 * 
 */
package edu.wustl.cab2bwebapp.bizlogic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.wustl.cab2b.common.queryengine.querystatus.AbstarctStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.queryengine.querystatus.QueryURLStatusOperations;
import edu.wustl.cab2bwebapp.bizlogic.executequery.QueryBizLogic;

/**
 * @author deepak_shingan
 *
 */
public class UserBackgroundQueries {
    private Map<UserInterface, Set<QueryBizLogic>> userToBackgroundQueries = null;

    private static UserBackgroundQueries ref = null;

    /**
     * private constructor for singleton class. 
     */
    private UserBackgroundQueries() {
        userToBackgroundQueries = new HashMap<UserInterface, Set<QueryBizLogic>>();
    }

    /**
     * @return UserBackgroundQueries
     */
    public static UserBackgroundQueries getInstance() {
        if (ref == null) {
            ref = new UserBackgroundQueries();
        }
        return ref;
    }

    /*(non-Javadoc)
    * @see java.lang.Object#clone()*/
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * Adds currently running query for background execution for the current user and updating properties in database.
     */
    public void addBackgroundQuery(UserInterface user, QueryBizLogic queryBizLogic) {
        //Update and saving to database.
        QueryStatus qStatus = queryBizLogic.getStatus();
        qStatus.setVisible(Boolean.TRUE);
        new QueryURLStatusOperations().updateQueryStatus(qStatus);

        //Adding to user Vs background query map.
        Set<QueryBizLogic> bizLogicSet = userToBackgroundQueries.get(user);
        if (bizLogicSet == null) {
            bizLogicSet = new HashSet<QueryBizLogic>();
            userToBackgroundQueries.put(user, bizLogicSet);
        }
        bizLogicSet.add(queryBizLogic);
    }

    /**
     * Call update in database for all queries executing in background. 
     */
    public synchronized void updateAllBackgroundQueryStatus() {
        Set<UserInterface> users = userToBackgroundQueries.keySet();
        for (UserInterface user : users) {
            Set<QueryBizLogic> querieLogics = userToBackgroundQueries.get(user);
            for (QueryBizLogic queryBizLogic : querieLogics) {
                QueryStatus status = queryBizLogic.getStatus();

                //updating query status in database.                
                new QueryURLStatusOperations().updateQueryStatus(status);

                //If query is complete remove it's reference from map 
                if (status.equals(AbstarctStatus.Complete) || status.equals(AbstarctStatus.Complete_With_Error)) {
                    querieLogics.remove(queryBizLogic);
                }
            }
        }
    }

}
