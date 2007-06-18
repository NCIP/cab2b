package cab2b.server.caarray.datalist;

import static edu.wustl.cab2b.server.datalist.DataListUtil.getAttributeByName;
import static cab2b.server.caarray.datalist.DerivedBioAssayDataDataListSaver.CUBE_ATTRIBUTE_NAME;
import static cab2b.server.caarray.datalist.DerivedBioAssayDataDataListSaver.DIM1LABELS_ATTRIBUTE_NAME;
import static cab2b.server.caarray.datalist.DerivedBioAssayDataDataListSaver.DIM2LABELS_ATTRIBUTE_NAME;
import static cab2b.server.caarray.datalist.DerivedBioAssayDataDataListSaver.DIM3LABELS_ATTRIBUTE_NAME;

import java.util.List;
import java.util.Set;

import cab2b.common.caarray.DerivedBioAssayDataRecord;
import cab2b.common.caarray.IDerivedBioAssayDataRecord;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityRecordInterface;
import edu.wustl.cab2b.common.queryengine.result.RecordId;
import edu.wustl.cab2b.server.datalist.AbstractDataListRetriever;
import edu.wustl.cab2b.server.datalist.DataListUtil;

public class DerivedBioAssayDataDataListRetriever extends AbstractDataListRetriever<IDerivedBioAssayDataRecord> {

    public DerivedBioAssayDataDataListRetriever(EntityInterface newEntity) {
        super(newEntity);
    }

    @Override
    protected void copyOtherFields(IDerivedBioAssayDataRecord record, EntityRecordInterface recordInterface,
                                   List<AbstractAttributeInterface> attributesList, EntityInterface entity) {
        if (!entity.equals(newEntity)) {
            throw new IllegalArgumentException();
        }
        DerivedBioAssayDataRecord derivedBioAssayDataRecord = (DerivedBioAssayDataRecord) record;

        int cubeAttributeIndex = attributesList.indexOf(DataListUtil.getAttributeByName(entity,
                                                                                        CUBE_ATTRIBUTE_NAME));
        Object[][][] cube = (Object[][][]) recordInterface.getRecordValueList().get(cubeAttributeIndex);
        derivedBioAssayDataRecord.setCube(cube);

        int dim1LabelsIndex = attributesList.indexOf(getAttributeByName(entity, DIM1LABELS_ATTRIBUTE_NAME));
        String[] dim1Labels = (String[]) recordInterface.getRecordValueList().get(dim1LabelsIndex);
        derivedBioAssayDataRecord.setDim1Labels(dim1Labels);

        int dim2LabelsIndex = attributesList.indexOf(getAttributeByName(entity, DIM2LABELS_ATTRIBUTE_NAME));
        String[] dim2Labels = (String[]) recordInterface.getRecordValueList().get(dim2LabelsIndex);
        derivedBioAssayDataRecord.setDim2Labels(dim2Labels);

        int dim3LabelsIndex = attributesList.indexOf(getAttributeByName(entity, DIM3LABELS_ATTRIBUTE_NAME));
        String[] dim3Labels = (String[]) recordInterface.getRecordValueList().get(dim3LabelsIndex);
        derivedBioAssayDataRecord.setDim3Labels(dim3Labels);
    }

    @Override
    protected IDerivedBioAssayDataRecord createRecord(EntityInterface entity, Set<AttributeInterface> attributes,
                                                      RecordId id) {
        return new DerivedBioAssayDataRecord(attributes, id);
    }
}
