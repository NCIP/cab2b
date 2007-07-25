package cab2b.common.caarray;

import java.util.ArrayList;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.queryengine.result.ILazyParams;
import edu.wustl.cab2b.common.queryengine.result.RecordId;
import edu.wustl.cab2b.common.queryengine.result.I3DDataRecord.LazyParams.Range;
import edu.wustl.cab2b.server.queryengine.LazyInitializer;

public class BioAssayDataRecord extends CaArrayRecord implements IFullyInitializedBioAssayDataRecord,
        IPartiallyInitializedBioAssayDataRecord {
    private static final long serialVersionUID = -6479610453654712409L;

    private Object[][][] cube;

    private String[] dim1Labels;

    private String[] dim2Labels;

    private String[] dim3Labels;

    private boolean fullyInitialized;

    private int handle;

    private LazyParams lazyParams;

    private BioAssayDataRecord(Set<AttributeInterface> attributes, RecordId id) {
        super(attributes, id);
        this.fullyInitialized = true;
    }

    private BioAssayDataRecord(Set<AttributeInterface> attributes, RecordId id, LazyParams lazyParams) {
        super(attributes, id);
        this.fullyInitialized = false;
        this.lazyParams = lazyParams;
    }

    public Object[][][] getCube() {
        return cube;
    }

    public void setCube(Object[][][] cube) {
        this.cube = cube;
    }

    public String[] getDim1Labels() {
        return dim1Labels;
    }

    public void setDim1Labels(String[] dim1Labels) {
        this.dim1Labels = dim1Labels;
    }

    public String[] getDim2Labels() {
        return dim2Labels;
    }

    public void setDim2Labels(String[] dim2Labels) {
        this.dim2Labels = dim2Labels;
    }

    public String[] getDim3Labels() {
        return dim3Labels;
    }

    public void setDim3Labels(String[] dim3Labels) {
        this.dim3Labels = dim3Labels;
    }

    public BioAssayDataRecord view(ILazyParams params, int handle) {
        if (!fullyInitialized) {
            throw new UnsupportedOperationException();
        }

        if (!(params instanceof LazyParams)) {
            throw new IllegalArgumentException();
        }
        LazyParams lazyParams = (LazyParams) params;
        BioAssayDataRecord view = createLazyForm(this, false, lazyParams);

        for (Range range : lazyParams.getRanges()) {
            for (int i = range.getSi(); i < range.getDi(); i++) {
                for (int j = range.getSj(); j < range.getDj(); j++) {
                    for (int k = range.getSk(); k < range.getDk(); k++) {
                        view.getCube()[i][j][k] = getCube()[i][j][k];
                    }
                }
                view.getDim1Labels()[i] = getDim1Labels()[i];
            }
            for (int i = range.getSj(); i < range.getDj(); i++) {
                view.getDim2Labels()[i] = getDim2Labels()[i];
            }
            for (int i = range.getSk(); i < range.getDk(); i++) {
                view.getDim3Labels()[i] = getDim3Labels()[i];
            }
        }

        return view;
    }

    public int handle() {
        if (fullyInitialized) {
            throw new UnsupportedOperationException();
        }
        return handle;
    }

    public void setHandle(int handle) {
        this.handle = handle;
    }

    public static BioAssayDataRecord createLazyForm(BioAssayDataRecord fullRecord) {
        return createLazyForm(fullRecord, true, new LazyParams(new ArrayList<Range>()));
    }

    private static BioAssayDataRecord createLazyForm(BioAssayDataRecord fullRecord, boolean register,
                                                     LazyParams lazyParams) {
        BioAssayDataRecord lazy = new BioAssayDataRecord(fullRecord.getAttributes(), fullRecord.getRecordId(),
                lazyParams);
        if (register) {
            lazy.setHandle(LazyInitializer.register(fullRecord));
        }
        lazy.copyValuesFrom(fullRecord);

        int dim1 = fullRecord.getDim1Labels().length;
        int dim2 = fullRecord.getDim2Labels().length;
        int dim3 = fullRecord.getDim3Labels().length;
        lazy.setCube(new Object[dim1][dim2][dim3]);
        lazy.setDim1Labels(new String[dim1]);
        lazy.setDim2Labels(new String[dim2]);
        lazy.setDim3Labels(new String[dim3]);

        return lazy;
    }

    public static BioAssayDataRecord createFullyInitializedRecord(Set<AttributeInterface> attributes, RecordId id) {
        return new BioAssayDataRecord(attributes, id);
    }

    public ILazyParams initializationParams() {
        if (fullyInitialized) {
            throw new UnsupportedOperationException();
        }
        return lazyParams;
    }
}
