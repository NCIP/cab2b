package edu.wustl.cab2b.server.queryengine.resulttransformers;

import gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener;
import gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException;

import java.io.Serializable;

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

    private static final long serialVersionUID = 1L;

    private AbstractQueryResultTransformer queryResultTransformer = null;

    private String status = null;

    protected FQPQueryListener(AbstractQueryResultTransformer queryResultTransformer) {
        this.queryResultTransformer = queryResultTransformer;
    }

    /**
     * Sets status of overall query 
     * @param urlVsStatus
     *            the urlVsException to set
     */
    public void processingStatusChanged(ProcessingStatus statusObj, String message) {
        status = statusObj.getValue();
        logger.info("Status changed to :" + status);
        if (queryResultTransformer != null) {
            queryResultTransformer.updateStatus("All", message, message, status);
        }
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener#targetServiceConnectionRefused(java.lang.String)
     */
    public void targetServiceConnectionRefused(String serviceURL) {
        if (queryResultTransformer != null) {
            queryResultTransformer.updateStatus(serviceURL, "Target service connection refused.",
                                                "Connection refused.", status);
        }
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener#targetServiceOk(java.lang.String)
     */
    public void targetServiceOk(String serviceURL) {
        if (queryResultTransformer != null) {
            queryResultTransformer.updateStatus(serviceURL, "targetServiceOk", "Target Service OK", status);
        }
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener#targetServiceReturnedInvalidResult(java.lang.String, gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException)
     */
    public void targetServiceReturnedInvalidResult(String serviceURL, FederatedQueryProcessingException ex) {
        if (queryResultTransformer != null) {
            queryResultTransformer.updateStatus(serviceURL, ex.getMessage(), "Invalid result", status);
        }
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener#targetServiceReturnedResults(java.lang.String, org.cagrid.fqp.results.metadata.ResultsRange)
     */
    public void targetServiceReturnedResults(String serviceURL, ResultsRange range) {
        if (queryResultTransformer != null) {
            queryResultTransformer.updateStatus(serviceURL, "Succesfully Completed.", "Succesfully completed.",
                                                status);
        }
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener#targetServiceThrowsException(java.lang.String, java.lang.Exception)
     */
    public void targetServiceThrowsException(String serviceURL, Exception ex) {
        if (queryResultTransformer != null) {
            queryResultTransformer.updateStatus(serviceURL, ex.getMessage(), "Service Throws exception", status);
        }
    }

}
