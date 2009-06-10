package cab2b.server.caarray.resulttransformer;

import static cab2b.server.caarray.resulttransformer.CaArrayResultTransformerUtil.IDENTIFIER_ATTRIBUTE_NAME;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cab2b.common.caarray.DerivedBioAssayDataRecord;
import cab2b.common.caarray.IDerivedBioAssayDataRecord;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.result.RecordId;
import gov.nih.nci.mageom.domain.Identifiable;
import gov.nih.nci.mageom.domain.BioAssay.BioAssay;
import gov.nih.nci.mageom.domain.BioAssayData.BioAssayDimension;
import gov.nih.nci.mageom.domain.BioAssayData.BioDataCube;
import gov.nih.nci.mageom.domain.BioAssayData.CompositeSequenceDimension;
import gov.nih.nci.mageom.domain.BioAssayData.DerivedBioAssayData;
import gov.nih.nci.mageom.domain.BioAssayData.QuantitationTypeDimension;

public class DerivedBioAssayDataResultTransformer extends AbstractCaArrayResultTransfomer<IDerivedBioAssayDataRecord> {
    private static final String HEADER_ATTRIBUTE_NAME = "name";

    @Override
    protected IDerivedBioAssayDataRecord createCaArrayRecord(Set<AttributeInterface> attributes, RecordId id) {
        return new DerivedBioAssayDataRecord(attributes, id);
    }

    @Override
    protected IDerivedBioAssayDataRecord createRecordForObject(String url, Object objRec, EntityInterface outputEntity) {
        if (!(objRec instanceof DerivedBioAssayData)) {
            throw new IllegalArgumentException();
        }
        DerivedBioAssayDataRecord rec = (DerivedBioAssayDataRecord) super.createRecordForObject(url, objRec, outputEntity);
        DerivedBioAssayData derivedBioAssayData = (DerivedBioAssayData) objRec;
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
        return rec;
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
}
