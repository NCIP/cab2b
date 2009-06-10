package edu.wustl.cab2b.server.queryengine.resulttransformers;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.util.ResultConfigurationParser;
import edu.wustl.cab2b.common.util.Utility;

/**
 * Factory to create transformer.
 * 
 * @author srinath_k
 */
public final class QueryResultTransformerFactory {
    public static IQueryResultTransformer<?, ?> createTransformer(EntityInterface outputEntity) {
        try {
            String transformerName = ResultConfigurationParser.getInstance().getResultTransformer(
                                                                                                  Utility.getApplicationName(outputEntity),
                                                                                                  outputEntity.getName());
            Object o = Class.forName(transformerName).newInstance();
            return (IQueryResultTransformer<?, ?>) o;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static <R extends IRecord, C extends ICategorialClassRecord> IQueryResultTransformer<R, C> createTransformer(
                                                                                                                        EntityInterface outputEntity,
                                                                                                                        Class<R> recordClass,
                                                                                                                        Class<C> categorialRecordClass) {
        return (IQueryResultTransformer<R, C>) QueryResultTransformerFactory.createTransformer(outputEntity);

    }
}
