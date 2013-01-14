/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

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
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.server.datalist.AbstractDataListSaver;
import edu.wustl.cab2b.server.queryengine.LazyInitializer;

/**
 * Saver for IPartiallyInitializedBioAssayDataRecord's.
 * <p>
 * Creates virtual attributes of DE type "object" for the cube and the labels of
 * the three dimensions.
 * 
 * @author srinath_k
 * 
 */
public class BioAssayDataDataListSaver extends AbstractDataListSaver<IPartiallyInitializedBioAssayDataRecord> {
    // names of the virtual attributes.
    static final String CUBE_ATTRIBUTE_NAME = "cube";

    static final String DIM1LABELS_ATTRIBUTE_NAME = "dim1Labels";

    static final String DIM2LABELS_ATTRIBUTE_NAME = "dim2Labels";

    static final String DIM3LABELS_ATTRIBUTE_NAME = "dim3Labels";

    /**
     * Populates the new entity with attributes from original entity and the
     * virtual attributes related to biodatacube.
     * <p>
     * Following are the virtual attributes (all of type object) added to the
     * new entity:<br>
     * <ul>
     * <li>{@link #CUBE_ATTRIBUTE_NAME}</li>
     * <li>{@link #DIM1LABELS_ATTRIBUTE_NAME}</li>
     * <li>{@link #DIM2LABELS_ATTRIBUTE_NAME}</li>
     * <li>{@link #DIM3LABELS_ATTRIBUTE_NAME}</li>
     * </ul>
     * 
     * @throws IllegalArgumentException if the given entity is not a subtype of
     *             {@link BioAssayData}.
     * @see edu.wustl.cab2b.server.datalist.AbstractDataListSaver#populateNewEntity(edu.common.dynamicextensions.domaininterface.EntityInterface)
     */
    @Override
    protected void populateNewEntity(EntityInterface oldEntity) {
        /* OLD CODE
         try {
         if (!BioAssayData.class.isAssignableFrom(Class.forName(oldEntity.getName()))) {
         throw new IllegalArgumentException();
         }
         } catch (ClassNotFoundException e) {
         throw new IllegalArgumentException();
         }
         DataListUtil.copyNonVirtualAttributes(newEntity, oldEntity);
         newEntity.addAttribute(createObjectAttribute(CUBE_ATTRIBUTE_NAME));
         newEntity.addAttribute(createObjectAttribute(DIM1LABELS_ATTRIBUTE_NAME));
         newEntity.addAttribute(createObjectAttribute(DIM2LABELS_ATTRIBUTE_NAME));
         newEntity.addAttribute(createObjectAttribute(DIM3LABELS_ATTRIBUTE_NAME));
         */
    }

    /**
     * Returns the DE representation of the
     * {@link IPartiallyInitializedBioAssayDataRecord}. This method first calls
     * {@link AbstractDataListSaver#transformToMap} to copy the values of the
     * attributes in {@link IRecord}.
     * <p>
     * Then it obtains the {@link IFullyInitializedBioAssayDataRecord} for the
     * given {@link IPartiallyInitializedBioAssayDataRecord} using
     * {@link LazyInitializer#getFullyInitialializedRecord(int)}. Then the
     * values for the virtual attributes related to biodatacube are obtained
     * from this full record and  put in the map.
     * 
     * @see edu.wustl.cab2b.server.datalist.AbstractDataListSaver#transformToMap(edu.wustl.cab2b.common.queryengine.result.IRecord)
     */
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
