/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.queryengine.resulttransformers;

import edu.wustl.cab2b.common.queryengine.result.FailedTargetURL;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener;
import gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.cagrid.fqp.results.metadata.ProcessingStatus;
import org.cagrid.fqp.results.metadata.ResultsRange;

/**
 * FQPProcessingStatusListener implementation class which listens 
 * for changes in FQP's (D)CQL broadcasting status.
 * @author deepak_shingan
 *
 */
public class FQPQueryListener implements FQPProcessingStatusListener, Serializable {
    private Map<String, FailedTargetURL> urlVsException = new HashMap<String, FailedTargetURL>();

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(FQPQueryListener.class);

    private DCQLQuery query = null;

    /**
     * DCQLQuery query parameter is required for getting object condition information
     * @param query
     */
    FQPQueryListener(DCQLQuery query) {
        this.query = query;
    }

    /**
     * Returns Map of url to <code>FailedTargetURL</code> object 
     * for the associated query
     * @return the urlVsException
     */
    public Map<String, FailedTargetURL> getFailedURLs() {
        return urlVsException;
    }

    /**
     * @param urlVsException
     *            the urlVsException to set
     */
    public void processingStatusChanged(ProcessingStatus status, String message) {
        logger.info("Processing status : " + status + "\t Message :" + message);
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener#targetServiceConnectionRefused(java.lang.String)
     */
    public void targetServiceConnectionRefused(String serviceURL) {
        FailedTargetURL failedUrl = new FailedTargetURL(serviceURL, "Target service connection refused.",
                "Connection refused", query.getTargetObject());
        urlVsException.put(serviceURL, failedUrl);
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener#targetServiceOk(java.lang.String)
     */
    public void targetServiceOk(String serviceURL) {
        logger.info("targetServiceOk : " + serviceURL);
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener#targetServiceReturnedInvalidResult(java.lang.String, gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException)
     */
    public void targetServiceReturnedInvalidResult(String serviceURL, FederatedQueryProcessingException ex) {
        FailedTargetURL failedUrl = new FailedTargetURL(serviceURL, ex.getMessage(), "Invalid result",
                query.getTargetObject());
        urlVsException.put(serviceURL, failedUrl);
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener#targetServiceReturnedResults(java.lang.String, org.cagrid.fqp.results.metadata.ResultsRange)
     */
    public void targetServiceReturnedResults(String serviceURL, ResultsRange range) {
        logger.info("targetServiceReturnedResults : " + serviceURL + "\tRange P:" + range);
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener#targetServiceThrowsException(java.lang.String, java.lang.Exception)
     */
    public void targetServiceThrowsException(String serviceURL, Exception ex) {
        FailedTargetURL failedUrl = new FailedTargetURL(serviceURL, ex.getMessage(), "Service Throws exception",
                query.getTargetObject());
        urlVsException.put(serviceURL, failedUrl);
    }
}
