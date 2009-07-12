package edu.wustl.cab2b.common.user;

import java.util.Collection;

/**
 * Interface that represents all basic user information. 
 * @author Hrishikesh Rajpathak
 *
 */
public interface UserInterface {

    /**
     * Returns all services configured for the 
     * user irrespective of any model-group or entity-group.     
     * @return Collection<ServiceURLInterface>
     */
    Collection<ServiceURLInterface> getServiceURLCollection();

    /**
     * @return Returns userId
     */
    Long getUserId();

    /**
     * @return Returns the userName.
     */
    String getUserName();

    /**
     * 
     * @return is the user a Admin or a normal
     */
    public boolean isAdmin();
}
