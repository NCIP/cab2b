package edu.wustl.cab2b.server.util;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.datalist.DataListOperationsController;
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

    /**
     * This method returns the list of tree set containing the unique record values for a given entity identifier.
     * Tree set stores the values in sorted order. 
     * @param entityId
     * @return
     * @throws RemoteException
     */
    public List<TreeSet<Comparable<?>>> getUniqueRecordValues(Long entityId) throws RemoteException {
        //getting record list
        List<IRecord> entityRecordList = new DataListOperationsController().getEntityRecords(entityId);
        if (entityRecordList == null || entityRecordList.size() == 0) {
            return null;
        }

        //initilising column value set
        ArrayList<TreeSet<Comparable<?>>> entityRecordValues = new ArrayList<TreeSet<Comparable<?>>>();
        for (int i = 0; i < entityRecordList.get(0).getAttributes().size(); i++) {
            entityRecordValues.add(i, new TreeSet<Comparable<?>>());
        }

        int index = 0;
        for (IRecord entityRecord : entityRecordList) {
            List<AttributeInterface> attributeList = Utility.getAttributeList(entityRecord.getAttributes());
            for (AttributeInterface attribute : attributeList) {
                TreeSet<Comparable<?>> columnValues = entityRecordValues.get(index);
                Comparable<?> attributeValue = (Comparable<?>) entityRecord.getValueForAttribute(attribute);
                if (attributeValue != null) {
                    columnValues.add(attributeValue);
                }
                index++;
            }
            index = 0;
        }
        return entityRecordValues;
    }
}
