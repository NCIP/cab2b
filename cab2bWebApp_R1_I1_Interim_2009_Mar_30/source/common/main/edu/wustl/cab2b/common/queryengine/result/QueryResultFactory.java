package edu.wustl.cab2b.common.queryengine.result;

import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import edu.wustl.common.querysuite.metadata.category.Category;

/**
 * Factory to create the basic result objects.
 * 
 * @author srinath_k
 */
public class QueryResultFactory {
    public static <R extends IRecord> IQueryResult<R> createResult(EntityInterface entity) {
        return new QueryResult<R>(entity);
    }

    public static <C extends ICategorialClassRecord> ICategoryResult<C> createCategoryResult(Category category) {
        return new CategoryResult<C>(category);
    }

    public static IRecord createRecord(Set<AttributeInterface> attributes, RecordId id) {
        return new Record(attributes, id);
    }

    public static IRecordWithAssociatedIds createRecordWithAssociatedIds(Set<AttributeInterface> attributes,
                                                                         RecordId id) {
        return new RecordWithAssociatedIds(attributes, id);
    }

    public static ICategorialClassRecord createCategorialClassRecord(CategorialClass categorialClass,
                                                                     Set<AttributeInterface> attributes,
                                                                     RecordId id) {
        return new CategorialClassRecord(categorialClass, attributes, id);
    }
}
