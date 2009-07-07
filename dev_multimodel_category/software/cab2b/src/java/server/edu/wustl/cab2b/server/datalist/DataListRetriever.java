package edu.wustl.cab2b.server.datalist;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.result.IRecord;

/**
 * Specifies the operations required for retrieving the records that have been
 * saved as part of a datalist. The first method called on a retriever must be
 * <code>initialize()</code>. All other methods are expected to throw an
 * {@link IllegalStateException} if <code>initialize()</code> has not been
 * called already.
 * <p>
 * A retriever obtains the records of an entity using the dynamic-extensions
 * (DE) API, and transforms the records obtained into an appropriate
 * {@link IRecord}.
 * <p>
 * For example, consider a specialization of {@link IRecord} called
 * <code>IFooBarRecord</code> which represents records for an entity
 * FooBarEnt. <code>IFooBarRecord</code> provides additional info, say,
 * through the method <code>getFoo()</code>. Suppose
 * <code>IFooBarRecord</code> is saved as explained in the example given for
 * {@link DataListSaver}. Then, while retrieving the records, the value of the
 * attribute "foo" of the entity "FooBarEnt" will be set for the property
 * <code>IFooBarRecord.foo</code>.
 * <p>
 * A dataListRetriever will typically be paired with a {@link DataListSaver}.
 * Such a pair is to be registered in the file
 * <code>ResultConfiguration.xml</code>.
 * 
 * @author srinath_k
 * 
 * @param <R> the type of records that will be retrieved.
 */
public interface DataListRetriever<R extends IRecord> {
    /**
     * Performs any initialization that may be required.
     * 
     * @param entity the entity whose records are to be retrieved.
     */
    void initialize(EntityInterface entity);

    /**
     * Fetches the specified records using DE and transforms these DE records
     * into appropriate specialization of {@link IRecord}.
     * 
     * @param recordIds the ids of the records that are to be fetched.
     * @return the list of appropriate specialization of {@link IRecord}.
     * @throws IllegalStateException if {@link #initialize(EntityInterface)} has
     *             not been called.
     */
    List<R> getEntityRecords(List<Long> recordIds);
}
