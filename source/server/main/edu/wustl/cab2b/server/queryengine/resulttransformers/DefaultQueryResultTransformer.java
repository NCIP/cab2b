package edu.wustl.cab2b.server.queryengine.resulttransformers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.queryengine.result.QueryResultFactory;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;

/**
 * The default query result transformer. It parses the
 * {@link gov.nih.nci.cagrid.cqlresultset.CQLQueryResults} xml and extracts the
 * values for the attributes of the target entity. The records in the results
 * are the of the basic types
 * {@link edu.wustl.cab2b.common.queryengine.result.IRecord} and
 * {@link edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord}.
 * @author srinath_k
 */
final class DefaultQueryResultTransformer
        extends
        AbstractQueryResultTransformer<IRecord, ICategorialClassRecord> {

    /**
     * Parses the {@link gov.nih.nci.cagrid.cqlresultset.CQLQueryResults} xml
     * and extracts the values for the attributes of the target entity.
     * @see edu.wustl.cab2b.server.queryengine.resulttransformers.AbstractQueryResultTransformer#createRecords(gov.nih.nci.cagrid.cqlresultset.CQLQueryResults,
     *      edu.common.dynamicextensions.domaininterface.EntityInterface)
     */
    @Override
    protected List<IRecord> createRecords(CQLQueryResults cqlQueryResults,
                                          EntityInterface targetEntity) {
        List<IRecord> res = new ArrayList<IRecord>();
        CQLQueryResultsIterator itr = new CQLQueryResultsIterator(
                cqlQueryResults, true);
        Set<AttributeInterface> attributes = new HashSet<AttributeInterface>(
                targetEntity.getAttributeCollection());

        AttributeInterface idAttribute = Utility.getIdAttribute(targetEntity);
        while (itr.hasNext()) {
            String singleRecordXml = (String) itr.next();
            String id = getValueForAttribute(singleRecordXml, idAttribute);
            IRecord record = createRecord(attributes, id);
            populateRecord(singleRecordXml, record);
            res.add(record);
        }

        return res;
    }

    private IRecord createRecord(Set<AttributeInterface> attributes, String id) {
        return QueryResultFactory.createRecord(attributes, id);
    }

    private void populateRecord(String singleRecordXml, IRecord record) {
        for (AttributeInterface attribute : record.getAttributes()) {
            String value = getValueForAttribute(singleRecordXml, attribute);
            record.putValueForAttribute(attribute, value);
            // TODO anything for dates??
        }
    }

    private String getValueForAttribute(String singleRecordXml,
                                        AttributeInterface attribute) {
        String searchStr = attribute.getName() + "=\"";
        int attrStartIndex = singleRecordXml.indexOf(searchStr);
        if (attrStartIndex == -1) {
            return "";
        }
        // find the ending quote of the value...
        int valStartIndex = attrStartIndex + searchStr.length();
        int endQuoteIndex = singleRecordXml.indexOf("\"", valStartIndex);
        String value = singleRecordXml.substring(valStartIndex, endQuoteIndex);
        return value;
    }

    /**
     * Returns the default {@link ICategorialClassRecord}.
     * @see edu.wustl.cab2b.server.queryengine.resulttransformers.AbstractQueryResultTransformer#createCategoryRecord(edu.wustl.common.querysuite.metadata.category.CategorialClass,
     *      java.util.Set, java.lang.String)
     * @see QueryResultFactory#createCategorialClassRecord(CategorialClass, Set,
     *      String)
     */
    @Override
    protected ICategorialClassRecord createCategoryRecord(
                                                          CategorialClass categorialClass,
                                                          Set<AttributeInterface> categoryAttributes,
                                                          String id) {

        return QueryResultFactory.createCategorialClassRecord(
                                                              categorialClass,
                                                              categoryAttributes,
                                                              id);
    }
}
