package edu.wustl.cab2b.client.ui.controls.temp;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

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
public class BDQDataSource implements CachedTableDataSource<IPartiallyInitialized3DRecord> {

    private static final int NO_OF_ROWS = 200;

    //this is not final for BDQ 
    private static int NO_OF_COLUMNS = 25;

    private static final int CACHED_SIZE = 3;

    private int[] cachedRecordsIndexes = new int[CACHED_SIZE];

    private IPartiallyInitialized3DRecord[] cachedRecords = new IPartiallyInitialized3DRecord[CACHED_SIZE];

    private IPartiallyInitialized3DRecord record;

    public BDQDataSource(IPartiallyInitialized3DRecord<?, ?> record) {
        this.record = record;
        cachedRecordsIndexes[0] = -1;
        getData(0, 0);
    }

    public IPartiallyInitialized3DRecord getData(int startRowIndex, int startColumnIndex) {
        
        //getPage(startRowIndex, startColumnIndex)

        Logger.out.debug("Requested row is : " + startRowIndex);

        int pageNo = startRowIndex / NO_OF_ROWS;
        int indexInCache = pageNo % CACHED_SIZE;
        startRowIndex = NO_OF_ROWS * (pageNo);

        if (cachedRecordsIndexes[indexInCache] != startRowIndex) {

            //if (startRowIndex < (lastRowCached - NO_OF_ROWS) || startRowIndex >= lastRowCached) {

            Logger.out.debug("Cacheing from " + startRowIndex + "to " + (startRowIndex + NO_OF_ROWS));

            NO_OF_COLUMNS = record.getDim2Labels().length * record.getDim1Labels().length;

            List<LazyParams.Range> rangeList = null;
            rangeList = getRanges(record.getCube(), 0, startRowIndex);
            LazyParams lazyParams = new I3DDataRecord.LazyParams(rangeList);
            UtilityBusinessInterface utilityBeanInterface = (UtilityBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                                        EjbNamesConstants.UTILITY_BEAN,
                                                                                                                        UtilityHomeInterface.class,
                                                                                                                        MainFrame.newWelcomePanel);
            try {
                cachedRecords[indexInCache] = null;
                cachedRecords[indexInCache] = (IPartiallyInitialized3DRecord<?, ?>) utilityBeanInterface.getView(
                                                                                                                 record.handle(),
                                                                                                                 lazyParams);
                
                
                cachedRecordsIndexes[indexInCache] = startRowIndex;

            } catch (RemoteException e) {
                CommonUtils.handleException(e, MainFrame.newWelcomePanel, true, true, true, false);
            }
            Logger.out.debug("record cached@ " + indexInCache);
        }

        Logger.out.debug("returning record from cached@ " + indexInCache);
        record = cachedRecords[indexInCache];
        return cachedRecords[indexInCache];

    }

    private List<LazyParams.Range> getRanges(Object[][][] cube, int startColumn, int startRow) {

        int dimi = cube.length;
        int dimj = cube[0].length;
        int dimk = cube[0][0].length;

        List<LazyParams.Range> ranges = new ArrayList<LazyParams.Range>();

        int dx = (startRow + NO_OF_ROWS);
        int dy = (startColumn + NO_OF_COLUMNS);

        if (dy > dimi * dimj) {
            dy = dimi * dimj;
        }

        int starti = startColumn / dimj;
        int endi = dy / dimj;

        int startj = startColumn - (starti * dimj);
        int endj = dy - ((endi-1) * dimj);

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

    public IPartiallyInitialized3DRecord getCurrentData() {
        return record;
    }

    private Page getPage(int rowIndex, int columnIndex) {
        return new Page(rowIndex / NO_OF_ROWS, columnIndex / NO_OF_COLUMNS);
    }

}

class Page {

    private int xLocation;

    private int yLocation;

    private IPartiallyInitialized3DRecord<?, ?> cachedRecord;

    public Page(int xLocation, int yLocation) {
        this.xLocation = xLocation;
        this.yLocation = yLocation;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Page)) {
            return false;
        }
        Page otherPage = (Page) other;

        if (this.xLocation == otherPage.xLocation && this.yLocation == otherPage.yLocation) {
            return true;
        }

        return false;
    }

    /**
     * @return Returns the cachedRecord.
     */
    public IPartiallyInitialized3DRecord<?, ?> getCachedRecord() {
        return cachedRecord;
    }

    /**
     * @param cachedRecord The cachedRecord to set.
     */
    public void setCachedRecord(IPartiallyInitialized3DRecord<?, ?> cachedRecord) {
        this.cachedRecord = cachedRecord;
    }

}
