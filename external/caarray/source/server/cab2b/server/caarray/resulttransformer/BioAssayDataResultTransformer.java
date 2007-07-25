package cab2b.server.caarray.resulttransformer;

import static cab2b.server.caarray.resulttransformer.CaArrayResultTransformerUtil.IDENTIFIER_ATTRIBUTE_NAME;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cab2b.common.caarray.BioAssayDataRecord;
import cab2b.common.caarray.IPartiallyInitializedBioAssayDataRecord;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.result.RecordId;
import gov.nih.nci.mageom.domain.Identifiable;
import gov.nih.nci.mageom.domain.BioAssay.BioAssay;
import gov.nih.nci.mageom.domain.BioAssayData.BioAssayData;
import gov.nih.nci.mageom.domain.BioAssayData.BioAssayDimension;
import gov.nih.nci.mageom.domain.BioAssayData.BioDataCube;
import gov.nih.nci.mageom.domain.BioAssayData.CompositeSequenceDimension;
import gov.nih.nci.mageom.domain.BioAssayData.QuantitationTypeDimension;

public class BioAssayDataResultTransformer
        extends
        AbstractCaArrayResultTransfomer<IPartiallyInitializedBioAssayDataRecord> {
    private static final String HEADER_ATTRIBUTE_NAME = "name";

    @Override
    protected IPartiallyInitializedBioAssayDataRecord createCaArrayRecord(Set<AttributeInterface> attributes,
                                                                          RecordId id) {
        return BioAssayDataRecord.createFullyInitializedRecord(attributes, id);
    }

    @Override
    protected IPartiallyInitializedBioAssayDataRecord createRecordForObject(String url, Object objRec,
                                                                            EntityInterface outputEntity) {
        if (!(objRec instanceof BioAssayData)) {
            throw new IllegalArgumentException();
        }
        BioAssayDataRecord rec = (BioAssayDataRecord) super.createRecordForObject(url, objRec, outputEntity);
        BioAssayData derivedBioAssayData = (BioAssayData) objRec;
        String[] bioAssayNames = getBioAssaysNames(derivedBioAssayData.getBioAssayDimension().getIdentifier(), url);
        String[] quantitationTypeNames = getQuantitationTypesNames(
                                                                   derivedBioAssayData.getQuantitationTypeDimension().getIdentifier(),
                                                                   url);
        String[] designElementNames = getDesignElementsNames(
                                                             derivedBioAssayData.getDesignElementDimension().getIdentifier(),
                                                             url);

        rec.setDim1Labels(bioAssayNames);
        rec.setDim2Labels(quantitationTypeNames);
        rec.setDim3Labels(designElementNames);

        BioDataCube bioDataCube = (BioDataCube) derivedBioAssayData.getBioDataValues();
        rec.setCube(transformCubeToBQD(bioDataCube));

        return BioAssayDataRecord.createLazyForm(rec);
//        return rec;
    }

    private Object[][][] transformCubeToBQD(BioDataCube bioDataCube) {
        String order = bioDataCube.getOrder();
        int[] dimensionMap = new int[3];
        char[] bqdOrder = { 'B', 'Q', 'D' };
        for (int i = 0; i < 3; i++) {
            dimensionMap[i] = order.indexOf(bqdOrder[i]);
        }
        return (Object[][][]) MatrixDimensionSwapper.swapDimensions(bioDataCube.getCube(), dimensionMap);
    }

    // begin bioassay
    private String[] getBioAssaysNames(String bioAssayDimensionId, String url) {
        BioAssayDimension bioAssayDimension = getBioAssayDimensionById(bioAssayDimensionId, url);
        return queryForNames(url, getIds(bioAssayDimension.getBioAssays()), BioAssay.class.getName());
    }

    private BioAssayDimension getBioAssayDimensionById(String identifier, String url) {
        return transformerUtil.getObjectById(BioAssayDimension.class, identifier, url);
    }

    // end bioassay

    // begin quantitationType
    private String[] getQuantitationTypesNames(String quantitationTypeDimensionId, String url) {
        QuantitationTypeDimension quantitationTypeDimension = getQuantitationTypeDimensionById(
                                                                                               quantitationTypeDimensionId,
                                                                                               url);
        return getNames(quantitationTypeDimension.getQuantitationTypes());
    }

    private QuantitationTypeDimension getQuantitationTypeDimensionById(String identifier, String url) {
        return transformerUtil.getObjectById(QuantitationTypeDimension.class, identifier, url);
    }

    // end quantitationType

    // begin designElements
    private String[] getDesignElementsNames(String designElementDimensionId, String url) {
        CompositeSequenceDimension compositeSequenceDimension = getCompositeSequenceDimensionById(
                                                                                                  designElementDimensionId,
                                                                                                  url);

        return getNames(compositeSequenceDimension.getCompositeSequences());
    }

    private CompositeSequenceDimension getCompositeSequenceDimensionById(String identifier, String url) {
        return transformerUtil.getObjectById(CompositeSequenceDimension.class, identifier, url);
    }

    // end designElements

    private String[] queryForNames(String url, String[] ids, String className) {
        Map<String, String> idToHeader = new HashMap<String, String>(ids.length);

        List<Map<String, String>> results = transformerUtil.getAttributeResult(
                                                                               className,
                                                                               new String[] { IDENTIFIER_ATTRIBUTE_NAME, HEADER_ATTRIBUTE_NAME },
                                                                               IDENTIFIER_ATTRIBUTE_NAME, ids, url);
        for (Map<String, String> row : results) {
            idToHeader.put(row.get(IDENTIFIER_ATTRIBUTE_NAME), row.get(HEADER_ATTRIBUTE_NAME));
        }
        String[] headers = new String[ids.length];
        for (int i = 0; i < ids.length; i++) {
            headers[i] = idToHeader.get(ids[i]);
        }
        return headers;
    }

    private String[] getIds(Identifiable[] identifiables) {
        String[] ids = new String[identifiables.length];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = identifiables[i].getIdentifier();
        }
        return ids;
    }

    private String[] getNames(Identifiable[] identifiables) {
        String[] names = new String[identifiables.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = identifiables[i].getName();
        }
        return names;
    }

//    @Override
//    public IQueryResult<IPartiallyInitializedBioAssayDataRecord> getResults(DCQLQuery query,
//                                                                            EntityInterface targetEntity) {
//
//        return getQueryResult(targetEntity);
//    }
//
//    private IQueryResult<IPartiallyInitializedBioAssayDataRecord> getQueryResult(EntityInterface targetEntity) {
//        IQueryResult<IPartiallyInitializedBioAssayDataRecord> queryResults = QueryResultFactory.createResult(targetEntity);
//
//        BioAssayDataRecord record = BioAssayDataRecord.createFullyInitializedRecord(new HashSet(
//
//        targetEntity.getAttributeCollection()), new RecordId(
//
//        "gov.nih.nci.ncicb.caarray:DerivedBioAssayData:1015897589771984:1", "asdf"));
//
//        String dim1Labels[] = { "1" };
//
//        String dim2Labels[] = { "Pairs", "Pairs Used", "Signal", "Detection", "Detection P-value" };
//
//        String dim3Labels[] = { "92555_at", "92558_at", "92559_at", "92568_at", "92574_at", "92555_at", "92558_at", "92559_at", "92568_at", "92574_at", "92555_at", "92558_at", "92559_at", "92568_at", "92574_at", "92555_at", "92558_at", "92559_at", "92568_at", "92574_at", "92555_at", "92558_at", "92559_at", "92568_at", "92574_at", "92555_at", "92558_at", "92559_at", "92568_at", "92574_at" };
//
//        Object bioDataCube[][][] = new Object[][][] { { { "20.0", "16.0", "20.0", "16.0", "20.0", "20.0", "16.0", "20.0", "16.0", "20.0", "20.0", "16.0", "20.0", "16.0", "20.0", "20.0", "16.0", "20.0", "16.0", "20.0", "20.0", "16.0", "20.0", "16.0", "20.0", "20.0", "16.0", "20.0", "16.0", "20.0" }, { "Absent", "Present", "Present", "Marginal", "Absent", "Absent", "Present", "Present", "Marginal", "Absent", "Absent", "Present", "Present", "Marginal", "Absent", "Absent", "Present", "Present", "Marginal", "Absent", "Absent", "Present", "Present", "Marginal", "Absent", "Absent", "Present", "Present", "Marginal", "Absent" }, { "0.13876513", "0.3276513", "0.5645876513", "0.464376513", "0.235876513", "0.13876513", "0.3276513", "0.5645876513", "0.464376513", "0.235876513", "0.13876513", "0.3276513", "0.5645876513", "0.464376513", "0.235876513", "0.13876513", "0.3276513", "0.5645876513", "0.464376513", "0.235876513", "0.13876513", "0.3276513", "0.5645876513", "0.464376513", "0.235876513", "0.13876513", "0.3276513", "0.5645876513", "0.464376513", "0.235876513" }, { "2.188886E-4", "3.188886E-4", "4.188886E-4", "5.188886E-4", "6.188886E-4", "2.188886E-4", "3.188886E-4", "4.188886E-4", "5.188886E-4", "6.188886E-4", "2.188886E-4", "3.188886E-4", "4.188886E-4", "5.188886E-4", "6.188886E-4", "2.188886E-4", "3.188886E-4", "4.188886E-4", "5.188886E-4", "6.188886E-4", "2.188886E-4", "3.188886E-4", "4.188886E-4", "5.188886E-4", "6.188886E-4", "2.188886E-4", "3.188886E-4", "4.188886E-4", "5.188886E-4", "6.188886E-4" }, { "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848" } } };
//
//        for (AttributeInterface attribute : targetEntity.getAttributeCollection()) {
//
//            record.putValueForAttribute(attribute, "1");
//
//        }
//
//        record.setDim1Labels(dim1Labels);
//
//        record.setDim2Labels(dim2Labels);
//
//        record.setDim3Labels(dim3Labels);
//
//        record.setCube(bioDataCube);
//
//        queryResults.addRecord("someUrl", BioAssayDataRecord.createLazyForm(record));
//
//        return queryResults;
//
//    }

}
