package cab2b.server.caarray.resulttransformer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cab2b.common.caarray.ICaArrayCategoryRecord;
import cab2b.common.caarray.ICaArrayRecord;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.result.RecordId;
import edu.wustl.cab2b.server.queryengine.resulttransformers.AbstractQueryResultTransformer;
import edu.wustl.cab2b.server.queryengine.resulttransformers.IQueryResultTransformer;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.mageom.domain.Identifiable;

public abstract class AbstractCaArrayResultTransfomer<R extends ICaArrayRecord>
        extends
        AbstractQueryResultTransformer<R, ICaArrayCategoryRecord> implements
        IQueryResultTransformer<R, ICaArrayCategoryRecord> {

    private static final String GET_ID_METHOD_NAME = "getIdentifier";

    protected CaArrayResultTransformerUtil transformerUtil = new CaArrayResultTransformerUtil(super.queryLogger);

    @Override
    protected List<R> createRecords(String url, CQLQueryResults cqlQueryResults, EntityInterface targetEntity) {

        if (!isIdentifiable(targetEntity)) {
            throw new IllegalArgumentException();
        }

        List<R> res = new ArrayList<R>();
        for (Object objRec : transformerUtil.getObjectsFromCQLResults(targetEntity.getName(), cqlQueryResults)) {
            res.add(createRecordForObject(url, objRec, targetEntity));
        }
        return res;
    }

    protected R createRecordForObject(String url, Object objRec, EntityInterface outputEntity) {
        String id = executeMethods(objRec, GET_ID_METHOD_NAME)[0].toString();
        List<AttributeInterface> attributes = new ArrayList<AttributeInterface>(
                outputEntity.getAttributeCollection());
        List<AssociationInterface> associations = new ArrayList<AssociationInterface>(
                outputEntity.getAssociationCollection());
        R rec = createCaArrayRecord(new HashSet<AttributeInterface>(attributes), new RecordId(id, url));
        String[] attributeGetters = getAttributeGettersNames(attributes);
        String[] associationGetters = getAssociationGettersNames(associations);
        Object[] attributesValues = executeMethods(objRec, attributeGetters);
        Object[] associationsValues = executeMethods(objRec, associationGetters);
        for (int i = 0; i < attributes.size(); i++) {
            rec.putValueForAttribute(attributes.get(i), attributesValues[i] == null ? ""
                    : attributesValues[i].toString());
        }
        for (int i = 0; i < associations.size(); i++) {
            processAssociation(rec, associations.get(i), associationsValues[i]);
        }
        return rec;
    }

    private void processAssociation(R rec, AssociationInterface association, Object associationValue) {
        if (associationValue == null) {
            return;
        }
        List<String> associatedIds = new ArrayList<String>();
        Class<?> desiredType = getClassForEntity(association.getTargetEntity());
        for (Object associatedObj : getAssociatedObjects(desiredType, associationValue)) {
            if (isIdentifiable(associatedObj.getClass())) {
                associatedIds.add(executeMethods(associatedObj, GET_ID_METHOD_NAME)[0].toString());
            }
        }
        if (!associatedIds.isEmpty()) {
            rec.getAssociatedClassesIdentifiers().put(association, associatedIds);
        }
    }

    private List<Object> getAssociatedObjects(Class<?> desiredType, Object associationValue) {
        Class<?> associatedClass = associationValue.getClass();

        List<Object> associatedObjects = new ArrayList<Object>();
        if (associatedClass.isArray()) {
            Object[] associatedObjectsArr = (Object[]) associationValue;
            for (Object associatedObj : associatedObjectsArr) {
                if (desiredType.isAssignableFrom(associatedObj.getClass())) {
                    associatedObjects.add(associatedObj);
                }
            }

        } else {
            if (desiredType.isAssignableFrom(associatedClass)) {
                associatedObjects.add(associationValue);
            }
        }

        return associatedObjects;
    }

    private Class<?> getClassForEntity(EntityInterface entity) {
        try {
            return Class.forName(entity.getName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isIdentifiable(EntityInterface entity) {
        return isIdentifiable(getClassForEntity(entity));
    }

    private boolean isIdentifiable(Class<?> associatedClass) {
        return associatedClass.isArray() ? isIdentifiable(associatedClass.getComponentType())
                : Identifiable.class.isAssignableFrom(associatedClass);
    }

    private Object[] executeMethods(Object obj, String... methodNames) {
        Object[] result = new Object[methodNames.length];
        Class<?> clazz = obj.getClass();
        for (int i = 0; i < methodNames.length; i++) {
            try {
                result[i] = clazz.getMethod(methodNames[i]).invoke(obj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /**
     * Genrated the names of all the GET methods based on the attribute name.
     * 
     * @param list List of Attributes
     * @return Returns the String[] Array of method names
     */
    private String[] getAttributeGettersNames(List<AttributeInterface> attributes) {

        String[] methodNames = new String[attributes.size()];
        int i = 0;
        for (AttributeInterface attribute : attributes) {
            methodNames[i++] = getGetterMethodName(attribute.getName());
        }
        return methodNames;
    }

    private String[] getAssociationGettersNames(List<AssociationInterface> associations) {
        String[] methodNames = new String[associations.size()];
        int i = 0;
        for (AssociationInterface association : associations) {
            methodNames[i++] = getGetterMethodName(association.getTargetRole().getName());
        }
        return methodNames;
    }

    private String getGetterMethodName(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    protected abstract R createCaArrayRecord(Set<AttributeInterface> attributes, RecordId id);

    // /////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected ICaArrayCategoryRecord createCategoryRecord(CategorialClass categorialClass,
                                                          Set<AttributeInterface> categoryAttributes, RecordId id) {
        // TODO Auto-generated method stub
        return null;
    }

}
