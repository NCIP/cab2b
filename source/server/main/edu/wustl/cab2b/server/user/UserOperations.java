package edu.wustl.cab2b.server.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.global.Constants;

/**
 * User operations like saving user, retrieving user, validating user are
 * handled by this class
 * It also sync the globus certificate of server .
 * 
 * @author hrishikesh_rajpathak
 * @author lalit_chand
 * 
 */
public class UserOperations extends DefaultBizLogic {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(UserOperations.class);

    /**
     * This method returns user from database with given user name
     * @param name user name
     * @return User
     */
    public UserInterface getUserByName(String value) {
        List<UserInterface> userList = null;

        final String queryStr = "Select {User.*} from cab2b_user User where name COLLATE latin1_bin='";
        String query = new StringBuilder().append(queryStr).append(value).append("'").toString();
        try {
            SQLQuery sqlQuery = DBUtil.currentSession().createSQLQuery(query);
            userList = sqlQuery.addEntity("User", edu.wustl.cab2b.common.user.User.class).list();
            DBUtil.closeSession();
        } catch (HibernateException hbe) {
            logger.error(hbe.getMessage(), hbe);
            throw new RuntimeException("Error occurred while fetching User", ErrorCodeConstants.UR_0003);
        }
        UserInterface user = null;
        if (userList != null && !userList.isEmpty()) {
            user = userList.get(0);
        }
        return user;
    }

    /**
     * This method returns user from database with given user name
     * @param name user name
     * @return User
     */
    public UserInterface getUserById(String value) {
        return getUser("userId", value);
    }

    /**
     * This method returns the user with administrative rights
     * @return Administrator 
     */
    public UserInterface getAdmin() {
        return getUserByName("Admin");
    }

    /**
     * Method to fetch user. Called by getUserByName and getAdmin. If user not
     * found, it returns null
     * 
     * @param column column name in database
     * @param value value for the column
     * @return User 
     */
    @SuppressWarnings("unchecked")
    private UserInterface getUser(String column, String value) {
        List<UserInterface> userList = null;
        try {
            userList = (List<UserInterface>) retrieve(UserInterface.class.getName(), column, value);
        } catch (DAOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching User", ErrorCodeConstants.UR_0003);
        }

        UserInterface user = null;
        if (userList != null && !userList.isEmpty()) {
            user = userList.get(0);
        }
        return user;
    }

    /**
     * Saves the given user as a new user in database. Before saving it checks
     * for user with duplicate name
     * 
     * @param user user to insert
     */
    public UserInterface insertUser(UserInterface user) {
        if (getUserByName(user.getUserName()) != null) {
            throw new RuntimeException("User already exists", ErrorCodeConstants.UR_0002);
        } else {
            try {
                insert(user, Constants.HIBERNATE_DAO);
            } catch (UserNotAuthorizedException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException("Error while inserting user in database", ErrorCodeConstants.UR_0004);
            } catch (BizLogicException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException("Error while inserting user in database", ErrorCodeConstants.UR_0004);
            }
        }
        return user;
    }

    /**
     * Updates information about user in database. This does not create new  user.
     * 
     * @param user User to update
     */
    public void updateUser(UserInterface user) {
        try {
            update(user, Constants.HIBERNATE_DAO);
        } catch (UserNotAuthorizedException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Error while updating user information in database ",
                    ErrorCodeConstants.UR_0005);
        } catch (BizLogicException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Error while updating user information in database ",
                    ErrorCodeConstants.UR_0005);
        }
    }

    /**
     * Returns a map of EntityGroup name versus set of links for a specific
     * user. If user does not have any specific link configured, then it takes
     * it from administrator
     * 
     * @param user User
     * @return Returns a map of EntityGroup name versus set of links
     */
    public Map<String, List<String>> getServiceURLsForUser(UserInterface user) {
        Map<String, List<String>> entityGroupByUrls = new HashMap<String, List<String>>();
        Collection<ServiceURLInterface> userServiceCollection = user.getServiceURLCollection();
        if (userServiceCollection == null) {
            userServiceCollection = new ArrayList<ServiceURLInterface>();
        }
        for (ServiceURLInterface url : userServiceCollection) {
            String entityName = url.getEntityGroupName();
            if (entityGroupByUrls.containsKey(entityName)) {
                (entityGroupByUrls.get(entityName)).add(url.getUrlLocation());
            } else {
                ArrayList<String> list = new ArrayList<String>();
                list.add(url.getUrlLocation());
                entityGroupByUrls.put(entityName, list);
            }
        }

        Collection<EntityGroupInterface> allEntityGroups = EntityCache.getInstance().getEntityGroups();
        Collection<String> absentEntityGroups = new ArrayList<String>();
        for (EntityGroupInterface entityGroupInterface : allEntityGroups) {
            String name = Utility.createModelName(entityGroupInterface.getLongName(),
                                                  entityGroupInterface.getVersion());
            if (!entityGroupByUrls.containsKey(name)) {
                absentEntityGroups.add(name);
            }
        }

        if (!absentEntityGroups.isEmpty()) {
            entityGroupByUrls = getAdminURLs(entityGroupByUrls, absentEntityGroups);
        }
        return entityGroupByUrls;
    }

    /**
     * Called when current user does not have links for any entity-group.
     * @param egGroupToUrlListMap
     *            is a map of entity group to its links
     * @param absentEntityGroups
     * @return
     */
    private Map<String, List<String>> getAdminURLs(Map<String, List<String>> egGroupToUrlListMap,
                                                   Collection<String> absentEntityGroups) {
        UserInterface admin = getAdmin();
        Collection<ServiceURLInterface> adminServices = admin.getServiceURLCollection();

        for (ServiceURLInterface url : adminServices) {
            String egName = url.getEntityGroupName();
            if (absentEntityGroups.contains(egName)) {
                if (egGroupToUrlListMap.containsKey(egName)) {
                    (egGroupToUrlListMap.get(egName)).add(url.getUrlLocation());
                } else {
                    ArrayList<String> list = new ArrayList<String>();
                    list.add(url.getUrlLocation());
                    egGroupToUrlListMap.put(egName, list);
                }
            } else {
                continue;
            }
        }
        return egGroupToUrlListMap;
    }

}
