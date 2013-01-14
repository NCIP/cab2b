/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package cab2b.server.caarray.datalist;

import static cab2b.server.caarray.datalist.BioAssayDataDataListSaver.CUBE_ATTRIBUTE_NAME;
import static cab2b.server.caarray.datalist.BioAssayDataDataListSaver.DIM1LABELS_ATTRIBUTE_NAME;
import static cab2b.server.caarray.datalist.BioAssayDataDataListSaver.DIM2LABELS_ATTRIBUTE_NAME;
import static cab2b.server.caarray.datalist.BioAssayDataDataListSaver.DIM3LABELS_ATTRIBUTE_NAME;
import static edu.wustl.cab2b.server.datalist.DataListUtil.getAttributeByName;

import java.util.List;
import java.util.Set;

import cab2b.common.caarray.BioAssayDataRecord;
import cab2b.common.caarray.IFullyInitializedBioAssayDataRecord;
import cab2b.common.caarray.IPartiallyInitializedBioAssayDataRecord;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.ObjectAttributeRecordValueInterface;
import edu.common.dynamicextensions.entitymanager.EntityRecordInterface;
import edu.wustl.cab2b.common.queryengine.result.RecordId;
import edu.wustl.cab2b.server.datalist.AbstractDataListRetriever;
import edu.wustl.cab2b.server.datalist.DataListUtil;
import edu.wustl.cab2b.server.queryengine.LazyInitializer;

/**
 * Retriever for IPartiallyInitializedBioAssayDataRecord's. Lazy initialization
 * is achieved by:
 * <ol>
 * <li>In {@link #createRecord(EntityInterface, Set, RecordId)} create a blank
 * {@link IFullyInitializedBioAssayDataRecord}. Register it, and return
 * corresponding {@link IPartiallyInitializedBioAssayDataRecord}.</li>
 * <li>In
 * {@link #copyOtherFields(IPartiallyInitializedBioAssayDataRecord, EntityRecordInterface, List, EntityInterface)},
 * <ul>
 * <li> Obtain the full record using
 * {@link LazyInitializer#getFullyInitialializedRecord(int)}. Copy the cube and
 * dimension labels into this full record.</li>
 * <li> Set the cube and dimension labels as empty arrays of appropriate sizes
 * in the partial record.</li>
 * </ul>
 * </li>
 * </ol>
 * 
 * @author srinath_k
 * 
 */
public class BioAssayDataDataListRetriever
        extends
        AbstractDataListRetriever<IPartiallyInitializedBioAssayDataRecord> {

    @Override
    protected void copyOtherFields(IPartiallyInitializedBioAssayDataRecord record,
                                   EntityRecordInterface recordInterface,
                                   List<? extends AbstractAttributeInterface> attributesList,
                                   EntityInterface entity) {
        if (!entity.equals(getNewEntity())) {
            throw new IllegalArgumentException();
        }
        BioAssayDataRecord derivedBioAssayDataRecord = (BioAssayDataRecord) LazyInitializer.getFullyInitialializedRecord(record.handle());

        derivedBioAssayDataRecord.copyValuesFrom(record);

        int cubeAttributeIndex = attributesList.indexOf(DataListUtil.getAttributeByName(entity,
                                                                                        CUBE_ATTRIBUTE_NAME));
        Object[][][] cube = (Object[][][]) getObjectValue(recordInterface.getRecordValueList().get(
                                                                                                   cubeAttributeIndex));
        derivedBioAssayDataRecord.setCube(cube);

        int dim1LabelsIndex = attributesList.indexOf(getAttributeByName(entity, DIM1LABELS_ATTRIBUTE_NAME));
        String[] dim1Labels = (String[]) getObjectValue(recordInterface.getRecordValueList().get(dim1LabelsIndex));
        derivedBioAssayDataRecord.setDim1Labels(dim1Labels);

        int dim2LabelsIndex = attributesList.indexOf(getAttributeByName(entity, DIM2LABELS_ATTRIBUTE_NAME));
        String[] dim2Labels = (String[]) getObjectValue(recordInterface.getRecordValueList().get(dim2LabelsIndex));
        derivedBioAssayDataRecord.setDim2Labels(dim2Labels);

        int dim3LabelsIndex = attributesList.indexOf(getAttributeByName(entity, DIM3LABELS_ATTRIBUTE_NAME));
        String[] dim3Labels = (String[]) getObjectValue(recordInterface.getRecordValueList().get(dim3LabelsIndex));
        derivedBioAssayDataRecord.setDim3Labels(dim3Labels);

        BioAssayDataRecord lazy = (BioAssayDataRecord) record;
        lazy.setCube(new Object[dim1Labels.length][dim2Labels.length][dim3Labels.length]);
        lazy.setDim1Labels(new String[dim1Labels.length]);
        lazy.setDim2Labels(new String[dim2Labels.length]);
        lazy.setDim3Labels(new String[dim3Labels.length]);
    }

    @Override
    protected IPartiallyInitializedBioAssayDataRecord createRecord(EntityInterface entity,
                                                                   Set<AttributeInterface> attributes, RecordId id) {

        BioAssayDataRecord full = BioAssayDataRecord.createFullyInitializedRecord(attributes, id);

        return BioAssayDataRecord.createLazyForm(full);
    }

    private Object getObjectValue(Object value) {
        ObjectAttributeRecordValueInterface recordValue = (ObjectAttributeRecordValueInterface) value;
        return recordValue.getObject();
    }
}
