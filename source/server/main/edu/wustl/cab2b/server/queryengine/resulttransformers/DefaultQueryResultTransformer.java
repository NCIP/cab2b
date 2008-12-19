package edu.wustl.cab2b.server.queryengine.resulttransformers;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;



import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.queryengine.result.QueryResultFactory;
import edu.wustl.cab2b.common.queryengine.result.RecordId;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;

/**
 * The default query result transformer. It parses the
 * {@link gov.nih.nci.cagrid.cqlresultset.CQLQueryResults} xml and extracts the
 * values for the attributes of the target entity. The records in the results
 * are of the basic types
 * {@link edu.wustl.cab2b.common.queryengine.result.IRecord} and
 * {@link edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord}.
 * 
 * @author srinath_k
 */
final class DefaultQueryResultTransformer extends AbstractQueryResultTransformer<IRecord, ICategorialClassRecord> {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(DefaultQueryResultTransformer.class);

    /**
     * Parses the {@link gov.nih.nci.cagrid.cqlresultset.CQLQueryResults} xml
     * and extracts the values for the attributes of the target entity.
     * 
     * @see edu.wustl.cab2b.server.queryengine.resulttransformers.AbstractQueryResultTransformer#createRecords(gov.nih.nci.cagrid.cqlresultset.CQLQueryResults,
     *      edu.common.dynamicextensions.domaininterface.EntityInterface)
     */
    @Override
    protected List<IRecord> createRecords(String url, CQLQueryResults cqlQueryResults, EntityInterface targetEntity) {
        List<IRecord> res = new ArrayList<IRecord>();
        CQLQueryResultsIterator itr = new CQLQueryResultsIterator(cqlQueryResults, true);
        Set<AttributeInterface> attributes = new HashSet<AttributeInterface>(targetEntity.getAttributeCollection());

        AttributeInterface idAttribute = Utility.getIdAttribute(targetEntity);
        while (itr.hasNext()) {
            String singleRecordXml = (String) itr.next();
            DOMParser parser = new DOMParser();
            try {
                parser.parse(new InputSource(new StringReader(singleRecordXml)));
            } catch (SAXException e) {
                logger.error("Unable to build DOM, Malformed XML", e);
                throw new RuntimeException("Unable to build DOM, Malformed XML", e, ErrorCodeConstants.QM_0004);
            } catch (IOException e) {
                logger.error("Unexpected IOException while generating DOM from CQLQueryResults", e);
                throw new RuntimeException("Unexpected IOException while generating DOM from CQLQueryResults", e,
                        ErrorCodeConstants.QM_0004);
            }
            Element oneRec = parser.getDocument().getDocumentElement();
            String id = oneRec.getAttribute(idAttribute.getName());
            IRecord record = createRecord(attributes, new RecordId(id, url));
            for (AttributeInterface attr : attributes) {
                String value = oneRec.getAttribute(attr.getName());
                record.putStringValueForAttribute(attr, parseValueString(value));
            }
            res.add(record);
        }
        return res;
    }

    private IRecord createRecord(Set<AttributeInterface> attributes, RecordId id) {
        return QueryResultFactory.createRecord(attributes, id);
    }

    /**
     * Replace special characters found in string with appropriate values. 
     * @param modify
     * @return modified string
     */
    public String parseValueString(String modify) {
        modify = Utility.replaceAllWords(modify, "&amp", "&");
        modify = Utility.replaceAllWords(modify, "&quot", "\"");
        modify = Utility.replaceAllWords(modify, "&lt", "<");
        modify = Utility.replaceAllWords(modify, "&gt", ">");
        return modify;
    }

    /**
     * Returns the default {@link ICategorialClassRecord}.
     * 
     * @see edu.wustl.cab2b.server.queryengine.resulttransformers.AbstractQueryResultTransformer#createCategoryRecord(edu.wustl.common.querysuite.metadata.category.CategorialClass,
     *      java.util.Set, java.lang.String)
     * @see QueryResultFactory#createCategorialClassRecord(CategorialClass, Set,
     *      String)
     */
    @Override
    protected ICategorialClassRecord createCategoryRecord(CategorialClass categorialClass,
                                                          Set<AttributeInterface> categoryAttributes, RecordId id) {

        return QueryResultFactory.createCategorialClassRecord(categorialClass, categoryAttributes, id);
    }
}
