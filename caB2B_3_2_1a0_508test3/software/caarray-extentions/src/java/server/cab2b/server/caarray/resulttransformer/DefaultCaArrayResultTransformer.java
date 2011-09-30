package cab2b.server.caarray.resulttransformer;

import java.util.Set;

import cab2b.common.caarray.CaArrayRecord;
import cab2b.common.caarray.ICaArrayRecord;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.queryengine.result.RecordId;

/**
 * The default implementation of caArray result transformer that creates the
 * basic {@link ICaArrayRecord}.
 * 
 * @author srinath_k
 * 
 */
public class DefaultCaArrayResultTransformer extends AbstractCaArrayResultTransfomer<ICaArrayRecord> {

    /**
     * Creates the basic {@link CaArrayRecord}.
     * 
     * @see cab2b.server.caarray.resulttransformer.AbstractCaArrayResultTransfomer#createCaArrayRecord(java.util.Set,
     *      edu.wustl.cab2b.common.queryengine.result.RecordId)
     */
    @Override
    protected ICaArrayRecord createCaArrayRecord(Set<AttributeInterface> attributes, RecordId id) {
        return new CaArrayRecord((attributes), id);
    }

}
