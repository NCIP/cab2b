/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package cab2b.server.caarray.resulttransformer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis.utils.ClassUtils;
import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.server.queryengine.resulttransformers.QueryLogger;
import edu.wustl.cab2b.server.queryengine.resulttransformers.QueryResultTransformerUtil;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;

/**
 * Util for caArray transformers that provides methods related to
 * deserialization of cql results.
 * 
 * @author srinath_k
 * 
 */
public class CaArrayResultTransformerUtil extends QueryResultTransformerUtil {
    public CaArrayResultTransformerUtil(QueryLogger logger) {
        super(logger);
    }

    /**
     * This is the name of the id attribute for all classes in caArray.
     */
    public static final String IDENTIFIER_ATTRIBUTE_NAME = "id";

    /**
     * Returns the object with given id.
     * 
     * @param <T> the type of the object to be fetched.
     * @param clazz the class of the object
     * @param id the id of the object to be fetched
     * @param url the service url
     * @param cred security credentials
     * @return the object
     * @see QueryResultTransformerUtil#getCQLResultsById(String, String, String,
     *      String)
     */
    public <T> T getObjectById(Class<T> clazz, String id, String url, GlobusCredential cred) {
        String targetName = clazz.getName();
        CQLQueryResults cqlQueryResults = getCQLResultsById(targetName, IDENTIFIER_ATTRIBUTE_NAME, id, url, cred);
        List<Object> objects = getObjectsFromCQLResults(targetName, cqlQueryResults);
        return objects.isEmpty() ? null : (T) objects.get(0);
    }

    /**
     * Deserializes the given CQL results using the custom deserializers
     * provided by caArray service.
     * 
     * @param targetName the name of the target class
     * @param cqlQueryResults the cql results
     * @param cred security credentials
     * @return the list of objects obtained by deserializing the cql resultss.
     */
    public List<Object> getObjectsFromCQLResults(String targetName, CQLQueryResults cqlQueryResults) {
        cqlQueryResults.setTargetClassname(getTargetClassName(targetName));
        InputStream inputStream = getWsddFile();
        CQLQueryResultsIterator itr = new CQLQueryResultsIterator(cqlQueryResults, inputStream);

        List<Object> res = new ArrayList<Object>();
        while (itr.hasNext()) {
            Object obj = itr.next();
            if (obj != null)
                res.add(obj);
        }
        return res;
    }

    /**
     * Currrently returns the input as it is.
     * 
     * @param outputClassName the output class name given in DCQL.
     * @return the name of the actual class in the results.
     */
    private String getTargetClassName(String outputClassName) {
        // Previously there was a mapping; the impl class name was obtained
        // using following logic. But latest caArray jars do not need this
        // conversion.

        // try {
        // Class outputClass = Class.forName(outputClassName);
        //
        // if (outputClass.isInterface()) {
        // int index = outputClassName.lastIndexOf(".");
        // String classNameOnly = outputClassName.substring(index);
        // String packageName = outputClass.getPackage().getName();
        // outputClassName = packageName + ".impl" + classNameOnly
        // + "Impl";
        // }
        // } catch (ClassNotFoundException e) {
        // throw new RuntimeException(e);
        // }
        return outputClassName;
    }

    /**
     * @return the client-config wsdd for the caArray service.
     */
    public InputStream getWsddFile() {
        InputStream inputStream = ClassUtils.getResourceAsStream(CaArrayResultTransformerUtil.class,
                                                                 getClientConfigWsddFileName());
        return inputStream;
    }

    private String getClientConfigWsddFileName() {
        return "caarray-client-config.wsdd";
    }
}
