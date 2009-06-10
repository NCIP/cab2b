package cab2b.server.caarray;

import java.util.Set;

import cab2b.common.caarray.CaArrayRecord;
import cab2b.common.caarray.ICaArrayRecord;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.queryengine.result.RecordId;

public class DefaultCaArrayResultTransformer extends AbstractCaArrayResultTransfomer<ICaArrayRecord> {

    @Override
    protected ICaArrayRecord createCaArrayRecord(Set<AttributeInterface> attributes, RecordId id) {
        return new CaArrayRecord((attributes), id);
    }

}
