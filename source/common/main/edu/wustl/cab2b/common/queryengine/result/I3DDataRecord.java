package edu.wustl.cab2b.common.queryengine.result;

import edu.wustl.cab2b.common.queryengine.result.IRecord;

public interface I3DDataRecord extends IRecord {
    Object[][][] getCube();

    String[] getDim1Labels();

    String[] getDim2Labels();

    String[] getDim3Labels();
}
