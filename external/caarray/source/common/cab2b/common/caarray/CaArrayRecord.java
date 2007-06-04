package cab2b.common.caarray;

import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.queryengine.result.RecordWithAssociatedIds;

public class CaArrayRecord extends RecordWithAssociatedIds implements
        ICaArrayRecord {

    private static final long serialVersionUID = -7864369803132187094L;

    public CaArrayRecord(Set<AttributeInterface> attributes, String id) {
        super(attributes, id);
    }
}
