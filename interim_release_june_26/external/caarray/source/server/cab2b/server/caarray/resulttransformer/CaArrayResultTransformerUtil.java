package cab2b.server.caarray.resulttransformer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis.utils.ClassUtils;

import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.server.queryengine.resulttransformers.QueryLogger;
import edu.wustl.cab2b.server.queryengine.resulttransformers.QueryResultTransformerUtil;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.mageom.domain.Identifiable;

public class CaArrayResultTransformerUtil extends QueryResultTransformerUtil {
    public CaArrayResultTransformerUtil(QueryLogger logger) {
        super(logger);
    }

    public static final String IDENTIFIER_ATTRIBUTE_NAME = "identifier";

    public <T extends Identifiable> T getObjectById(Class<T> clazz, String id,
                                                    String url) {
        String targetName = clazz.getName();
        CQLQueryResults cqlQueryResults = getCQLResultsById(
                                                            targetName,
                                                            IDENTIFIER_ATTRIBUTE_NAME,
                                                            id, url);
        List<Object> objects = getObjectsFromCQLResults(targetName,
                                                        cqlQueryResults);
        return objects.isEmpty() ? null : (T) objects.get(0);
    }

    public List<Object> getObjectsFromCQLResults(String targetName,
                                                 CQLQueryResults cqlQueryResults) {
        cqlQueryResults.setTargetClassname(getTargetClassName(targetName));
        InputStream inputStream = getWsddFile();
        CQLQueryResultsIterator itr = new CQLQueryResultsIterator(
                cqlQueryResults, inputStream);

        List<Object> res = new ArrayList<Object>();
        while (itr.hasNext()) {
            res.add(itr.next());
        }
        return res;
    }

    private String getTargetClassName(String outputClassName) {
        try {
            Class outputClass = Class.forName(outputClassName);

            if (outputClass.isInterface()) {
                int index = outputClassName.lastIndexOf(".");
                String classNameOnly = outputClassName.substring(index);
                String packageName = outputClass.getPackage().getName();
                outputClassName = packageName + ".impl" + classNameOnly
                        + "Impl";
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return outputClassName;
    }

    public InputStream getWsddFile() {
        InputStream inputStream = ClassUtils.getResourceAsStream(
                                                                 CaArrayResultTransformerUtil.class,
                                                                 getClientConfigWsddFileName());
        return inputStream;
    }

    private String getClientConfigWsddFileName() {
        return "caarray-client-config.wsdd";
    }
}
