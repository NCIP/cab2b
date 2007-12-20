package edu.wustl.cab2b.client.ui.viewresults;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.controls.LazyTable.AbstractLazyDataSource;
import edu.wustl.cab2b.client.ui.controls.LazyTable.CacheInterface;
import edu.wustl.cab2b.client.ui.controls.LazyTable.Page;
import edu.wustl.cab2b.client.ui.controls.LazyTable.PageDimension;
import edu.wustl.cab2b.client.ui.controls.LazyTable.PageInfo;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.utility.UtilityBusinessInterface;
import edu.wustl.cab2b.common.ejb.utility.UtilityHomeInterface;
import edu.wustl.cab2b.common.queryengine.result.I3DDataRecord;
import edu.wustl.cab2b.common.queryengine.result.IPartiallyInitialized3DRecord;
import edu.wustl.cab2b.common.queryengine.result.I3DDataRecord.LazyParams;
import edu.wustl.common.util.logger.Logger;

/**
 * @author rahul_ner
 *
 */
public class BDQDataSource extends AbstractLazyDataSource<IPartiallyInitialized3DRecord<?, ?>> {

    /**
     * this is required to find dimension of the cube
     */
    private IPartiallyInitialized3DRecord uninitailisedRecord;

    public BDQDataSource(
            IPartiallyInitialized3DRecord uninitailisedRecord,
            PageDimension pageDimension,
            CacheInterface cache) {
        super(pageDimension, cache);
        this.uninitailisedRecord = uninitailisedRecord;
    }

    public int getRowCount() {
        return uninitailisedRecord.getDim3Labels().length;
    }

    public int getColumnCount() {
        return uninitailisedRecord.getDim1Labels().length * uninitailisedRecord.getDim2Labels().length;
    }

    public String getColumnName(int columnNo) {
        int dim2Size = uninitailisedRecord.getDim2Labels().length;

        int dim1Index = columnNo / dim2Size;
        int dim2Index = columnNo - dim1Index;

        return currentPage.getData().getDim2Labels()[dim2Index] + "_"
                + currentPage.getData().getDim1Labels()[dim1Index];

    }

    /**
     * @see edu.wustl.cab2b.client.ui.controls.LazyTable.AbstractLazyDataSource#extractDataFromPage(int, int)
     */
    public Object extractDataFromPage(int rowNo, int columnNo) {
        int dim2Size = currentPage.getData().getDim2Labels().length;

        int dim1Index = columnNo / dim2Size;
        int dim2Index = columnNo - dim1Index;
        int dim3Index = rowNo;
        return currentPage.getData().getCube()[dim1Index][dim2Index][dim3Index];
    }

    /**
     * @see edu.wustl.cab2b.client.ui.controls.LazyTable.AbstractLazyDataSource#fetchPageData(edu.wustl.cab2b.client.ui.controls.LazyTable.PageInfo)
     */
    public Page fetchPageData(PageInfo pageInfo) {/*
        List<LazyParams.Range> rangeList = getRanges(uninitailisedRecord.getCube(), pageInfo.getStartX(),
                                                     pageInfo.getStartY());
        LazyParams lazyParams = new I3DDataRecord.LazyParams(rangeList);
        UtilityBusinessInterface utilityBeanInterface = (UtilityBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                                    EjbNamesConstants.UTILITY_BEAN,
                                                                                                                    UtilityHomeInterface.class,
                                                                                                                    MainFrame.newWelcomePanel);
        try {
            IPartiallyInitialized3DRecord newRecord = (IPartiallyInitialized3DRecord<?, ?>) utilityBeanInterface.getView(
                                                                                                                         uninitailisedRecord.handle(),
                                                                                                                         lazyParams);

            return new Page<IPartiallyInitialized3DRecord<?, ?>>(pageInfo, newRecord);
        } catch (RemoteException e) {
            CommonUtils.handleException(e, MainFrame.newWelcomePanel, true, true, true, false);
        }
        return null;
    */
        return null;}

    /**
     * @param cube
     * @param startRow
     * @param startColumn
     * @return
     */
    private List<LazyParams.Range> getRanges(Object[][][] cube, int startRow, int startColumn) {

        int dimi = cube.length;
        int dimj = cube[0].length;
        int dimk = cube[0][0].length;

        List<LazyParams.Range> ranges = new ArrayList<LazyParams.Range>();

        int dx = (startRow + pageDimension.getNoOfRows());
        int dy = (startColumn + pageDimension.getNoOfColumns());

        if (dy > dimi * dimj) {
            dy = dimi * dimj;
        }

        int starti = startColumn / dimj;
        int endi = dy / dimj;

        int startj = startColumn - (starti * dimj);
        int endj = dy - ((endi - 1) * dimj);

        int startk = startRow;
        int endk = dx > dimk ? dimk : dx;

        boolean moreThanOnePage = (endi - starti) > 1;
        boolean moreThanTwoPages = (endi - starti) > 2;
        boolean endFullPage = (endj == dimj);

        ranges.add(new LazyParams.Range(starti, starti + 1, startj, moreThanOnePage ? dimj : endj, startk, endk));

        if (moreThanOnePage) {
            ranges.add(new LazyParams.Range(starti + 1, endFullPage ? endi : (endi - 1), 0,
                    moreThanTwoPages ? dimj : endj, startk, endk));

            if (moreThanTwoPages && !endFullPage) {
                ranges.add(new LazyParams.Range(endi - 1, endi, 0, endj, startk, endk));
            }
        }

        return ranges;
    }

    /**
     * Method to get bio-data cube range for the given columnNo
     * @param columnNo
     * @return
     */
    private LazyParams.Range getCloumnRage(int columnNo) {
        Object[][][] cube = uninitailisedRecord.getCube();
        int dimj = cube[0].length;
        int dimk = cube[0][0].length;
        int starti = columnNo / dimj;
        int endi = starti + 1;
        int startj = columnNo - (starti * dimj);
        int endj = startj + 1;
        int startk = 0;
        int endk = dimk;
        return new LazyParams.Range(starti, endi, startj, endj, startk, endk);
    }

    /**
     * @param selectedColumns
     * @return
     */
    public IPartiallyInitialized3DRecord getColumnsData(int[] selectedColumns) {

        List<LazyParams.Range> rangeList = new ArrayList<LazyParams.Range>();
        for (int i = 0; i < selectedColumns.length; i++) {
            rangeList.add(getCloumnRage(selectedColumns[i]));
        }
        LazyParams lazyParams = new I3DDataRecord.LazyParams(rangeList);
        UtilityBusinessInterface utilityBeanInterface = (UtilityBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                                    EjbNamesConstants.UTILITY_BEAN,
                                                                                                                    UtilityHomeInterface.class,
                                                                                                                    MainFrame.newWelcomePanel);
        try {
            Logger.out.debug("Record Handle " + uninitailisedRecord.handle());
            return (IPartiallyInitialized3DRecord) utilityBeanInterface.getView(uninitailisedRecord.handle(),
                                                                                lazyParams);
        } catch (RemoteException e) {
            CommonUtils.handleException(e, MainFrame.newWelcomePanel, true, true, true, false);
        }

        return null;
    }

    public ArrayList<TreeSet<Comparable>> getUniqueRecordValues() {

        UtilityBusinessInterface utilityBeanInterface = (UtilityBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                                    EjbNamesConstants.UTILITY_BEAN,
                                                                                                                    UtilityHomeInterface.class,
                                                                                                                    MainFrame.newWelcomePanel);
        Set<AttributeInterface> allAttributes = uninitailisedRecord.getAttributes();
        if (!allAttributes.isEmpty()) {
            Long entityId = allAttributes.iterator().next().getEntity().getId();
            try {
                return utilityBeanInterface.getUniqueRecordValues(entityId);
            } catch (RemoteException e) {
                CommonUtils.handleException(e, MainFrame.newWelcomePanel, true, true, true, false);
            }
        }
        return null;
    }

}
