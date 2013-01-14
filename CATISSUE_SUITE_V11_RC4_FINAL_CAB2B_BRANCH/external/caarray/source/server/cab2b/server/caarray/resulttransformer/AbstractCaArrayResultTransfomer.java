/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

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


/**
 * Skeletal implementation for a query result transformer of caArray
 * application. Concrete implementations need only implement the
 * {@link #createCaArrayRecord(Set, RecordId)} method to create the appropriate
 * caArray record.<br>
 * caArray service has following features which are implemented in this class:
 * <ul>
 * <li>Custom deserializers are provided which are used to deserialize the CQL
 * results.</li> 
 * <li>In most cases, identifiers of classes associated to the target are also
 * returned.</li>
 * </ul>
 * Reflection is used to obtain the values of the attributes and associated
 * identifiers; thus this implementation is generic and should suffice for most
 * caArray classes.
 * <p>
 * Note : Currently this class can only process DCQLs whose target object is
 * <code>instanceOf</code> {@link Identifiable}. Also, caArray categories are
 * not supported.
 * 
 * @author srinath_k
 * 
 * @param <R> the type of caArray record that this transformer creates.
 */
public abstract class AbstractCaArrayResultTransfomer<R extends ICaArrayRecord>
        extends
        AbstractQueryResultTransformer<R, ICaArrayCategoryRecord> implements
        IQueryResultTransformer<R, ICaArrayCategoryRecord> {

    private static final String GET_ID_METHOD_NAME = "getId";

    protected CaArrayResultTransformerUtil transformerUtil = new CaArrayResultTransformerUtil(super.queryLogger);

    /**
     * Transforms the cql results and returns a list of records. This method
     * first calls
     * {@link CaArrayResultTransformerUtil#getObjectsFromCQLResults(String, CQLQueryResults)}
     * to get a list of objects. Then, for each object in this list, it calls
     * {@link #createRecordForObject(String, Object, EntityInterface)} to get
     * corresponding record. The list of records thus obtained is returned.
     * 
     * @throws IllegalArgumentException if the specified target is not an
     *             <code>instanceOf</code> {@link Identifiable}.
     * @see edu.wustl.cab2b.server.queryengine.resulttransformers.AbstractQueryResultTransformer#createRecords(java.lang.String,
     *      gov.nih.nci.cagrid.cqlresultset.CQLQueryResults,
     *      edu.common.dynamicextensions.domaininterface.EntityInterface)
     */
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

    /**
     * Transforms an object to appropriate record.<br>
     * The abstract method {@link #createCaArrayRecord(Set, RecordId)} is called
     * to create the new record. Then, using reflection, this new record is
     * populated with
     * <ol>
     * <li>values of the attributes of the entity</li>
     * <li>values of identifiers of entities associated to this output entity;
     * note that this implies that only associated entities that are an
     * <code>instanceOf</code> {@link Identifiable} are considered.</li>
     * </ol>
     * 
     * @param url the url from which this object was obtained.
     * @param objRec the object to be transformed to appropriate record.
     * @param outputEntity the entity by querying for which, this object was
     *            obtained.
     * @return
     */
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
            // only identifiable associations are considered.
            if (isIdentifiable(associatedObj.getClass())) {
                // get the id from the object.
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
        // if there is only one associated object, it is associationValue. If
        // there is more than one object, there is an array of these objects;
        // associationValue is then this array.

        // the isAssignableFrom() checks are done for cases such as this: if
        // query was for Experiment, then there is an array of related
        // BioAssayData. This array also contains objects of subtypes of
        // BioAssayData such as MeasuredBioAssayData, DerivedBioAssayData. Thus,
        // the same array has 3 types of objects, but caB2B has 3 different
        // associations with Experiment for these classes. So, when association
        // with BioAssayData is being processed, its result should contain all 3
        // objects; but for association with MeasuredBioAssayData, only
        // MeasuredBioAssayData objects should be returned.
        // Generalizing, the result of this method should contain objects of
        // only desiredType or its subclasses.
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
        return true;
//        return associatedClass.isArray() ? isIdentifiable(associatedClass.getComponentType())
//                : Identifiable.class.isAssignableFrom(associatedClass);
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
        throw new UnsupportedOperationException("Yet to implement.");
    }

}
