package edu.wustl.cab2b.client.ui.viewresults;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jdesktop.swingx.JXPanel;

import cab2b.common.caarray.I3DDataRecord;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.query.Utility;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.datalist.DataRow;
import edu.wustl.cab2b.common.datalist.IDataRow;
import edu.wustl.cab2b.common.ejb.path.PathFinderBusinessInterface;
import edu.wustl.cab2b.common.ejb.path.PathFinderHomeInterface;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.cab2b.common.queryengine.result.ICategoryResult;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
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
    public static JXPanel getResultPanel(SimpleSearchResultBreadCrumbPanel searchPanel, IQueryResult queryResult,
                                         IDataRow parentDataRow, IAssociation association) {
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

        PathFinderBusinessInterface busInt = (PathFinderBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                            "edu.wustl.cab2b.server.ejb.path.PathFinderBean",
                                                                                                            PathFinderHomeInterface.class,
                                                                                                            null);

        EntityInterface entity = null;
        if (record instanceof ICategorialClassRecord) {
            entity = ((ICategorialClassRecord) record).getCategorialClass().getCategorialClassEntity();
        } else {
            entity = queryResult.getOutputEntity();
        }

        try {
            incomingAssociationCollection = busInt.getIncomingIntramodelAssociations(entity.getId());

            intraModelAssociationCollection = busInt.getInterModelAssociations(entity.getId());
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
     * This method returns the detailed panel by first creating a data row for it
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
        List<AttributeInterface> attributeList = edu.wustl.cab2b.common.util.Utility.getAttributeList(record.getAttributes());

        Object[] valueArray = new Object[attributeList.size()];
        for (int i = 0; i < attributeList.size(); i++) {
            valueArray[i] = record.getValueForAttribute(attributeList.get(i));
        }

        /*
         * Get the EntityInterface from the map only if the last parameter is null. 
         * This should ideally happen only the first time
         */
        EntityInterface presentEntityInterface = attributeList.get(0).getEntity();
        String strclassName = edu.wustl.cab2b.common.util.Utility.getDisplayName(presentEntityInterface);

        DataRow dataRow = new DataRow();
        dataRow.setRow(valueArray);
        dataRow.setAttributes(attributeList);
        dataRow.setClassName(strclassName);
        dataRow.setParent(parentDataRow);
        dataRow.setId(record.getId());
        dataRow.setURL(url);
        dataRow.setAssociation(association);
        dataRow.setEntityInterface(presentEntityInterface);

        return getDetailedResultPanel(searchPanel, record, dataRow, incomingAssociationCollection,
                                      intraModelAssociationCollection);
    }

    /**
     * This method returns the detailed panel without  creating a data row for it.
     * Data row ia already created which is passed as parameter
     * 
     * @param searchPanel
     * @param record
     * @param dataRow
     * @param incomingAssociationCollection
     * @param intraModelAssociationCollection
     * @return
     */
    public static ResultPanel getDetailedResultPanel(
                                                     SimpleSearchResultBreadCrumbPanel searchPanel,
                                                     IRecord record,
                                                     IDataRow dataRow,
                                                     Collection<AssociationInterface> incomingAssociationCollection,
                                                     List<IInterModelAssociation> intraModelAssociationCollection) {
        ResultPanel resultPanel = null;

        if (record instanceof I3DDataRecord) {
            resultPanel = new ThreeDResultObjectDetailsPanel(searchPanel, dataRow, record,
                    incomingAssociationCollection, intraModelAssociationCollection);
        } else if (record instanceof ICategoryResult) {
            resultPanel = new CategoryObjectDetailsPanel(searchPanel, dataRow, record,
                    incomingAssociationCollection, intraModelAssociationCollection);
        } else {
            resultPanel = new ResultObjectDetailsPanel(searchPanel, dataRow, record,
                    incomingAssociationCollection, intraModelAssociationCollection);
        }
        resultPanel.doInitialization();
        return resultPanel;

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
