package edu.wustl.cab2b.server.queryengine.resulttransformers;

import edu.wustl.cab2b.common.queryengine.result.FQPUrlStatus;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener;
import gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException;

import java.io.Serializable;
import java.util.Collection;
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
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(FQPQueryListener.class);

    private Map<String, FQPUrlStatus> urlVsStatus = new HashMap<String, FQPUrlStatus>();

    private static final long serialVersionUID = 1L;

    private DCQLQuery query = null;

    /**
     * DCQLQuery query parameter is required for getting object condition information
     * @param query
     */
    public FQPQueryListener(DCQLQuery query) {
        this.query = query;
    }

    /**
     * Returns Map of url to <code>FailedTargetURL</code> object 
     * for the associated query
     * @return the urlVsException
     */
    public Map<String, FQPUrlStatus> getFQPUrlStatus() {
        return urlVsStatus;
    }

    /**
     * Sets status of overall query 
     * @param urlVsStatus
     *            the urlVsException to set
     */
    public void processingStatusChanged(ProcessingStatus status, String message) {
        Collection<FQPUrlStatus> urlStatuss = urlVsStatus.values();
        for (FQPUrlStatus urlStatus : urlStatuss) {
            urlStatus.setStatus(status.getValue());
        }
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener#targetServiceConnectionRefused(java.lang.String)
     */
    public void targetServiceConnectionRefused(String serviceURL) {
        FQPUrlStatus failedUrl =
                new FQPUrlStatus(serviceURL, "Target service connection refused.", "Connection refused", query
                    .getTargetObject());
        urlVsStatus.put(serviceURL, failedUrl);
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener#targetServiceOk(java.lang.String)
     */
    public void targetServiceOk(String serviceURL) {
        FQPUrlStatus fqpUrl =
                new FQPUrlStatus(serviceURL, "targetServiceOk", "Target Service OK", query.getTargetObject());
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener#targetServiceReturnedInvalidResult(java.lang.String, gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException)
     */
    public void targetServiceReturnedInvalidResult(String serviceURL, FederatedQueryProcessingException ex) {
        FQPUrlStatus fqpUrl =
                new FQPUrlStatus(serviceURL, ex.getMessage(), "Invalid result", query.getTargetObject());
        urlVsStatus.put(serviceURL, fqpUrl);
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener#targetServiceReturnedResults(java.lang.String, org.cagrid.fqp.results.metadata.ResultsRange)
     */
    public void targetServiceReturnedResults(String serviceURL, ResultsRange range) {
        logger.info("targetServiceReturnedResults : " + serviceURL + "\tRange :Start Element Index"
                + range.getEndElementIndex() + "End Element Index:" + range.getStartElementIndex());
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener#targetServiceThrowsException(java.lang.String, java.lang.Exception)
     */
    public void targetServiceThrowsException(String serviceURL, Exception ex) {
        FQPUrlStatus failedUrl =
                new FQPUrlStatus(serviceURL, ex.getMessage(), "Service Throws exception", query.getTargetObject());
        urlVsStatus.put(serviceURL, failedUrl);
    }
}
