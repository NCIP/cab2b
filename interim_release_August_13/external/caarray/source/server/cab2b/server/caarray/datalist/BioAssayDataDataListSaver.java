package cab2b.server.caarray.datalist;

import static edu.wustl.cab2b.server.datalist.DataListUtil.getAttributeByName;
import static edu.wustl.cab2b.server.datalist.DataListUtil.markVirtual;

import java.io.IOException;
import java.util.Map;

import cab2b.common.caarray.IFullyInitializedBioAssayDataRecord;
import cab2b.common.caarray.IPartiallyInitializedBioAssayDataRecord;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.ObjectAttributeRecordValueInterface;
import edu.wustl.cab2b.server.datalist.AbstractDataListSaver;
import edu.wustl.cab2b.server.datalist.DataListUtil;
import edu.wustl.cab2b.server.queryengine.LazyInitializer;

public class BioAssayDataDataListSaver extends AbstractDataListSaver<IPartiallyInitializedBioAssayDataRecord> {
    static final String CUBE_ATTRIBUTE_NAME = "cube";

    static final String DIM1LABELS_ATTRIBUTE_NAME = "dim1Labels";

    static final String DIM2LABELS_ATTRIBUTE_NAME = "dim2Labels";

    static final String DIM3LABELS_ATTRIBUTE_NAME = "dim3Labels";

    @Override
    protected void populateNewEntity(EntityInterface oldEntity) {
        DataListUtil.copyNonVirtualAttributes(newEntity, oldEntity);
        newEntity.addAttribute(createObjectAttribute(CUBE_ATTRIBUTE_NAME));
        newEntity.addAttribute(createObjectAttribute(DIM1LABELS_ATTRIBUTE_NAME));
        newEntity.addAttribute(createObjectAttribute(DIM2LABELS_ATTRIBUTE_NAME));
        newEntity.addAttribute(createObjectAttribute(DIM3LABELS_ATTRIBUTE_NAME));
    }

    @Override
    public Map<AbstractAttributeInterface, Object> transformToMap(IPartiallyInitializedBioAssayDataRecord record) {
        Map<AbstractAttributeInterface, Object> recordsMap = super.transformToMap(record);
        IFullyInitializedBioAssayDataRecord fullRec = (IFullyInitializedBioAssayDataRecord) LazyInitializer.getFullyInitialializedRecord(record.handle());
        recordsMap.put(getAttributeByName(newEntity, CUBE_ATTRIBUTE_NAME),
                       createObjectRecordValue(fullRec.getCube()));
        recordsMap.put(getAttributeByName(newEntity, DIM1LABELS_ATTRIBUTE_NAME),
                       createObjectRecordValue(fullRec.getDim1Labels()));
        recordsMap.put(getAttributeByName(newEntity, DIM2LABELS_ATTRIBUTE_NAME),
                       createObjectRecordValue(fullRec.getDim2Labels()));
        recordsMap.put(getAttributeByName(newEntity, DIM3LABELS_ATTRIBUTE_NAME),
                       createObjectRecordValue(fullRec.getDim3Labels()));
        return recordsMap;
    }

    private ObjectAttributeRecordValueInterface createObjectRecordValue(Object value) {
        ObjectAttributeRecordValueInterface objectRecordValue = DomainObjectFactory.getInstance().createObjectAttributeRecordValue();
        try {
            objectRecordValue.setObject(value);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return objectRecordValue;
    }

    private AttributeInterface createObjectAttribute(String name) {
        AttributeInterface attribute = DomainObjectFactory.getInstance().createObjectAttribute();
        attribute.setName(name);
        markVirtual(attribute);
        return attribute;
    }
}
