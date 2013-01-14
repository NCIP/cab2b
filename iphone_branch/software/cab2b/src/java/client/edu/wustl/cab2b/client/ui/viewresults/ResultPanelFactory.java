/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.viewresults;

import java.lang.reflect.Constructor;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdesktop.swingx.JXPanel;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.query.Utility;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.datalist.DataRow;
import edu.wustl.cab2b.common.datalist.IDataRow;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.path.PathFinderBusinessInterface;
import edu.wustl.cab2b.common.ejb.path.PathFinderHomeInterface;
import edu.wustl.cab2b.common.ejb.utility.UtilityBusinessInterface;
import edu.wustl.cab2b.common.ejb.utility.UtilityHomeInterface;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.util.DataListUtil;
import edu.wustl.cab2b.common.util.ResultConfigurationParser;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;

/**
 * @author rahul_ner
 * 
 */
public class ResultPanelFactory {

    private ResultPanelFactory() {

    }

    /**
     * @param queryResult
     */
    public static JXPanel getResultPanel(SimpleSearchResultBreadCrumbPanel searchPanel,
                                         IQueryResult<IRecord> queryResult, IDataRow parentDataRow,
                                         IAssociation association) {
        ResultPanel resultPanel = null;
        int recordNo = Utility.getRecordNum(queryResult);

        if (recordNo == 0) {
            return getNoResultFoundPopup(queryResult);
        }

        Map<String, List<IRecord>> allRecords = queryResult.getRecords();
        IRecord record = null;
        String urlKey = null;

        for (String key : allRecords.keySet()) {
            List<IRecord> recordList = allRecords.get(key);
            if (!recordList.isEmpty()) {
                urlKey = key;
                record = recordList.get(0);
                break;
            }
        }

        Collection<AssociationInterface> incomingAssociationCollection = null;
        List<IInterModelAssociation> intraModelAssociationCollection = null;
        EntityInterface entity = null;
        if (record instanceof ICategorialClassRecord) {
            entity = ((ICategorialClassRecord) record).getCategorialClass().getCategory().getCategoryEntity();
        } else {
            entity = queryResult.getOutputEntity();
        }

        try {
            PathFinderBusinessInterface pathFinder = (PathFinderBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                                    EjbNamesConstants.PATH_FINDER_BEAN,
                                                                                                                    PathFinderHomeInterface.class);
            UtilityBusinessInterface utilityBean = (UtilityBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                               EjbNamesConstants.UTILITY_BEAN,
                                                                                                               UtilityHomeInterface.class);
            incomingAssociationCollection = utilityBean.getIncomingIntramodelAssociations(entity.getId());
            intraModelAssociationCollection = pathFinder.getInterModelAssociations(entity.getId());
        } catch (RemoteException re) {
            CommonUtils.handleException(re, searchPanel, true, true, false, false);
        }

        if (recordNo == 1) {
            resultPanel = getDetailedResultPanel(searchPanel, record, urlKey, parentDataRow, association,
                                                 incomingAssociationCollection, intraModelAssociationCollection);
        } else {
            resultPanel = new ViewSearchResultsSimplePanel(searchPanel, queryResult, association, parentDataRow,
                    incomingAssociationCollection, intraModelAssociationCollection);

            resultPanel.doInitialization();
        }

        return resultPanel;
    }

    /**
     * This method returns the detailed panel by first creating a data row for
     * it
     * 
     * @param searchPanel
     * @param queryResult
     */
    private static ResultPanel getDetailedResultPanel(
                                                      SimpleSearchResultBreadCrumbPanel searchPanel,
                                                      IRecord record,
                                                      String url,
                                                      IDataRow parentDataRow,
                                                      IAssociation association,
                                                      Collection<AssociationInterface> incomingAssociationCollection,
                                                      List<IInterModelAssociation> intraModelAssociationCollection) {

        EntityInterface entity = null;
        Iterator<AttributeInterface> attributeIterator = record.getAttributes().iterator();

        if (attributeIterator.hasNext()) {
            entity = attributeIterator.next().getEntity();
        }

        DataRow dataRow = new DataRow(record, entity);
        dataRow.setParent(parentDataRow);
        dataRow.setAssociation(association);

        return getSearchResultPanel(searchPanel, record, dataRow, incomingAssociationCollection,
                                    intraModelAssociationCollection);
    }

    /**
     * This method returns the detailed panel without creating a data row for
     * it. Data row ia already created which is passed as parameter
     * 
     * @param searchPanel
     * @param record
     * @param dataRow
     * @param incomingAssociationCollection
     * @param intraModelAssociationCollection
     * @return
     */
    public static ResultPanel getSearchResultPanel(SimpleSearchResultBreadCrumbPanel searchPanel, IRecord record,
                                                   IDataRow dataRow,
                                                   Collection<AssociationInterface> incomingAssociationCollection,
                                                   List<IInterModelAssociation> intraModelAssociationCollection) {
        ResultPanel resultPanel = null;

        DefaultDetailedPanel defaultDetailedPanel = getResultDetailedPanel(record);

        resultPanel = new ResultObjectDetailsPanel(searchPanel, dataRow, record, incomingAssociationCollection,
                intraModelAssociationCollection, defaultDetailedPanel);

        resultPanel.doInitialization();

        return resultPanel;

    }

    /**
     * @param record
     * @return
     */
    public static DefaultDetailedPanel getResultDetailedPanel(IRecord record) {
        EntityInterface entity = Utility.getEntity(record);
        EntityInterface originEntity = DataListUtil.getOriginEntity(entity);

        try {
            String rendererName = ResultConfigurationParser.getInstance().getResultRenderer(
                                                                                            edu.wustl.cab2b.common.util.Utility.getApplicationName(originEntity),
                                                                                            originEntity.getName());
            Constructor<?> constructor = Class.forName(rendererName).getDeclaredConstructor(IRecord.class);
            DefaultDetailedPanel defaultDetailedPanel = (DefaultDetailedPanel) constructor.newInstance(record);
            defaultDetailedPanel.doInitialization();
            return defaultDetailedPanel;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param queryResult
     * @return
     */
    public static JXPanel getNoResultFoundPopup(IQueryResult queryResult) {
        String className = edu.wustl.cab2b.common.util.Utility.getDisplayName(queryResult.getOutputEntity());
        return getNoResultFoundPopup(className);

    }

    /**
     * @param className
     * @return
     */
    public static JXPanel getNoResultFoundPopup(String className) {
        JXPanel noResultFoundPopup = new Cab2bPanel();
        Cab2bLabel noResultFoundErroLabel = new Cab2bLabel("No results found for Class " + className);
        noResultFoundPopup.add(noResultFoundErroLabel);
        return noResultFoundPopup;
    }

}
