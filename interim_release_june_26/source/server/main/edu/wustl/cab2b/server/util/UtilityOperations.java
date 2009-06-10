package edu.wustl.cab2b.server.util;

import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

public class UtilityOperations extends DefaultBizLogic {
    /**
     * Hibernate DAO Type to use.
     */
    private static int daoType = Constants.HIBERNATE_DAO;

    /**
     * @param cab2bObject
     */
    public void insert(Object cab2bObject) {
        try {
            insert(cab2bObject, daoType);
            Logger.out.debug(cab2bObject.getClass().getName() + " inserted Successfully ");
        } catch (BizLogicException e) {
            throw (new RuntimeException(e.getMessage(), e));
        } catch (UserNotAuthorizedException e) {
            throw (new RuntimeException(e.getMessage(), e));
        }
    }

    /**
     * @param cab2bObject
     */
    public void update(Object cab2bObject) {
        try {
            update(cab2bObject, daoType);
            Logger.out.debug(cab2bObject.getClass().getName() + " inserted Successfully ");
        } catch (BizLogicException e) {
            Logger.out.debug(Utility.getStackTrace(e));
            throw (new RuntimeException(e.getMessage(), e));
        } catch (UserNotAuthorizedException e) {
            Logger.out.debug(Utility.getStackTrace(e));
            throw (new RuntimeException(e.getMessage(), e));
        }
    }

}
