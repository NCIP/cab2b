package edu.wustl.cab2b.server.queryengine.resulttransformers;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.util.Utility;

/**
 * Factory to create transformer.
 * 
 * @author srinath_k
 */
public final class QueryResultTransformerFactory {
    public static IQueryResultTransformer<?, ?> createTransformer(EntityInterface outputEntity) {
        try {
            String transformerName = "edu.wustl.cab2b.server.queryengine.resulttransformers.DefaultQueryResultTransformer";
            if (Utility.getApplicationName(outputEntity).equals("caArray")) {
                transformerName = "cab2b.server.caarray.resulttransformer.DefaultCaArrayResultTransformer";
                if (outputEntity.getName().equals("gov.nih.nci.mageom.domain.BioAssayData.DerivedBioAssayData")) {
                    transformerName = "cab2b.server.caarray.resulttransformer.DerivedBioAssayDataResultTransformer";
                }
            }

            Object o = Class.forName(transformerName).newInstance();
            return (IQueryResultTransformer<?, ?>) o;
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static <R extends IRecord, C extends ICategorialClassRecord> IQueryResultTransformer<R, C> createTransformer(
                                                                                                                        EntityInterface outputEntity,
                                                                                                                        Class<R> recordClass,
                                                                                                                        Class<C> categorialRecordClass) {
        return (IQueryResultTransformer<R, C>) QueryResultTransformerFactory.createTransformer(outputEntity);

    }
}
