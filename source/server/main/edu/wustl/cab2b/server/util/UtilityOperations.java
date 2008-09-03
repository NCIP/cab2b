package edu.wustl.cab2b.server.util;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.datalist.DataListOperationsController;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.global.Constants;

/**
 * Operations class for utility bean
 * @author chandrakant_talele
 */
public class UtilityOperations extends DefaultBizLogic {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(UtilityOperations.class);

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
            logger.debug(cab2bObject.getClass().getName() + " inserted Successfully ");
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
            logger.debug(cab2bObject.getClass().getName() + " inserted Successfully ");
        } catch (BizLogicException e) {
            logger.debug(Utility.getStackTrace(e));
            throw (new RuntimeException(e.getMessage(), e));
        } catch (UserNotAuthorizedException e) {
            logger.debug(Utility.getStackTrace(e));
            throw (new RuntimeException(e.getMessage(), e));
        }
    }

    /**
     * This method returns the list of tree set containing the unique record values for a given entity identifier.
     * Tree set stores the values in sorted order. 
     * @param entityId entityId
     * @return List of TreeSet
     */
    public List<TreeSet<Comparable<?>>> getUniqueRecordValues(Long entityId) {
        //getting record list
        List<IRecord> entityRecordList = new DataListOperationsController().getEntityRecords(entityId);
        if (entityRecordList == null || entityRecordList.size() == 0) {
            return null;
        }

        //initializing column value set
        ArrayList<TreeSet<Comparable<?>>> entityRecordValues = new ArrayList<TreeSet<Comparable<?>>>();
        for (int i = 0; i < entityRecordList.get(0).getAttributes().size(); i++) {
            entityRecordValues.add(i, new TreeSet<Comparable<?>>());
        }
        
        for (IRecord entityRecord : entityRecordList) {
            process(entityRecord, entityRecordValues);
        }
        return entityRecordValues;
    }

    private void process(IRecord record, List<TreeSet<Comparable<?>>> entityRecordValues) {
        int index = 0;
        List<AttributeInterface> attributeList = Utility.getAttributeList(record.getAttributes());
        for (AttributeInterface attribute : attributeList) {
            TreeSet<Comparable<?>> columnValues = entityRecordValues.get(index);
            Comparable<?> attributeValue = (Comparable<?>) record.getValueForAttribute(attribute);
            if (attributeValue != null) {
                columnValues.add(attributeValue);
            }
            index++;
        }
    }
}
