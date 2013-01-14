/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.datalist;

import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.result.IRecord;

/**
 * This interface defines the entity-specific operations that are required while
 * saving a data list. The first method called on a saver must be
 * <code>initialize()</code>. All other methods are expected to throw an
 * {@link IllegalStateException} if <code>initialize()</code> has not been
 * called previously.
 * <p>
 * A saver provides Dynamic-extensions (DE) specific representation of the
 * records of an entity that are to be saved in a datalist. For this, a new
 * entity has to be created; and this operation may be customized for a
 * particular entity. The new entity is obtained from
 * <code>getNewEntity()</code>.
 * <p>
 * The records (represented by {@link IRecord} then have to be transformed to a
 * DE compatible {@link Map}; this is done by the method
 * <code>getRecordAsMap()</code>.
 * <p>
 * For example, consider a specialization of {@link IRecord} called
 * <code>IFooBarRecord</code> which represents records for an entity
 * FooBarEnt. <code>IFooBarRecord</code> provides additional info, say,
 * through the method <code>getFoo()</code>. In this case, we can have a
 * <code>FooBarSaver</code>. <code>FooBarSaver.getNewEntity()</code> method
 * will return an entity that contains all attributes from FooBarEnt, and an
 * additional attribute called "foo". The method
 * <code>FooBarSaver.getRecordAsMap()</code> will appropriately put an entry
 * into the map for the attribute "foo", by reading the value from
 * <code>IFooBarRecord.getFoo()</code>.
 * <p>
 * A datalistSaver will typically be paired with a {@link DataListRetriever}.
 * Such a pair is to be registered in the file
 * <code>ResultConfiguration.xml</code>.
 * 
 * @author srinath_k
 * 
 * @param <R> the type of record that is to be saved.
 * 
 * @see DataListRetriever
 * @see DataListOperationsController#save(edu.wustl.cab2b.common.datalist.DataList)
 */
public interface DataListSaver<R extends IRecord> {
    /**
     * Initializes the saver based on the given entity. It is expected (but not
     * mandatory) that the new entity be created during this initialization
     * process.
     * @param oldEntity the entity whose records are to be saved.
     * 
     * @throws IllegalArgumentException if the saver is incapable of saving the
     *             specified entity.
     * 
     */
    void initialize(EntityInterface oldEntity);

    /**
     * Transforms a given record to a DE specific representation. Note that each
     * <code>AbstractAttributeInterface</code> key of the returned map will be
     * an abstractAttribute of the entity obtained by calling
     * <code>getNewEntity()</code>.
     * 
     * @param record the record to be transformed.
     * @throws IllegalStateException if {@link #initialize(EntityInterface)} has
     *             not been called.
     * @return DE specific representation of the record.
     */
    Map<AbstractAttributeInterface, Object> getRecordAsMap(R record);

    /**
     * Returns the new entity into which the datalist records will be written.
     * <b>It is mandatory that multiple calls of <code>getNewEntity()</code>
     * (without an intervening call to <code>initialize()</code>) return the
     * <i>same</i> (by reference) entity.
     * 
     * @throws IllegalStateException if {@link #initialize(EntityInterface)} has
     *             not been called.
     * @return the new entity.
     */
    EntityInterface getNewEntity();
}
