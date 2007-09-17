package cab2b.common.caarray;

import java.util.ArrayList;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.queryengine.result.ILazyParams;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.queryengine.result.RecordId;
import edu.wustl.cab2b.common.queryengine.result.I3DDataRecord.LazyParams.Range;
import edu.wustl.cab2b.server.queryengine.LazyInitializer;

/**
 * Implementats both {@link IFullyInitializedBioAssayDataRecord} and
 * {@link IPartiallyInitializedBioAssayDataRecord}. A boolean is used to
 * identify which interface is being implemented by an object.
 * <p>
 * The constructor is private, and an object is created using either
 * {@link #createFullyInitializedRecord(Set, RecordId)} or
 * {@link #createLazyForm(BioAssayDataRecord)}.
 * 
 * @author srinath_k
 * 
 */
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
        if (cube == null) {
            cube = new Object[0][0][0];
        }
        return cube;
    }

    public void setCube(Object[][][] cube) {
        this.cube = cube;
    }

    public String[] getDim1Labels() {
        if (dim1Labels == null) {
            dim1Labels = new String[0];
        }
        return dim1Labels;
    }

    public void setDim1Labels(String[] dim1Labels) {
        this.dim1Labels = dim1Labels;
    }

    public String[] getDim2Labels() {
        if (dim2Labels == null) {
            dim2Labels = new String[0];
        }
        return dim2Labels;
    }

    public void setDim2Labels(String[] dim2Labels) {
        this.dim2Labels = dim2Labels;
    }

    public String[] getDim3Labels() {
        if (dim3Labels == null) {
            dim3Labels = new String[0];
        }
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

        return lazy;
    }

    /**
     * Copies the attributes' values, and inits the arrays to appropriate size;
     * note that the contents of the arrays are not copied.
     * 
     * @see edu.wustl.cab2b.common.queryengine.result.Record#copyValuesFrom(edu.wustl.cab2b.common.queryengine.result.IRecord)
     */
    @Override
    public void copyValuesFrom(IRecord record) {
        if (!(record instanceof BioAssayDataRecord)) {
            throw new IllegalArgumentException();
        }
        super.copyValuesFrom(record);
        BioAssayDataRecord sourceRecord = (BioAssayDataRecord) record;
        // if (!sourceRecord.fullyInitialized) {
        // throw new IllegalArgumentException();
        // }
        int dim1 = sourceRecord.getDim1Labels().length;
        int dim2 = sourceRecord.getDim2Labels().length;
        int dim3 = sourceRecord.getDim3Labels().length;
        setCube(new Object[dim1][dim2][dim3]);
        setDim1Labels(new String[dim1]);
        setDim2Labels(new String[dim2]);
        setDim3Labels(new String[dim3]);
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
