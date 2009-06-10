package cab2b.common.caarray;

import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.queryengine.result.RecordId;

public class DerivedBioAssayDataRecord extends CaArrayRecord implements IDerivedBioAssayDataRecord {
    private static final long serialVersionUID = -6479610453654712409L;

    private Object[][][] cube;

    private String[] dim1Labels;

    private String[] dim2Labels;

    private String[] dim3Labels;

    public DerivedBioAssayDataRecord(Set<AttributeInterface> attributes, RecordId id) {
        super(attributes, id);
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
}
