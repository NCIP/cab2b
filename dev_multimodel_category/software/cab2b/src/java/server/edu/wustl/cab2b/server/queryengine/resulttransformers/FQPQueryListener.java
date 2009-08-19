package edu.wustl.cab2b.server.queryengine.resulttransformers;

import edu.wustl.cab2b.common.queryengine.querystatus.AbstractStatus;
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

    protected FQPQueryListener(AbstractQueryResultTransformer queryResultTransformer) {
        this.queryResultTransformer = queryResultTransformer;
    }

    /**
     * Sets status of overall query 
     * @param urlVsStatus
     *            the urlVsException to set
     */
    public void processingStatusChanged(ProcessingStatus statusObj, String message) {
        logger.info("Status changed to :" + statusObj.getValue();
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener#targetServiceConnectionRefused(java.lang.String)
     */
    public void targetServiceConnectionRefused(String serviceURL) {
        if (queryResultTransformer != null) {
            queryResultTransformer.updateStatus(serviceURL, "Target service connection refused.",
                                                "Connection refused.", AbstractStatus.Complete_With_Error);
        }
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener#targetServiceOk(java.lang.String)
     */
    public void targetServiceOk(String serviceURL) {
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener#targetServiceReturnedInvalidResult(java.lang.String, gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException)
     */
    public void targetServiceReturnedInvalidResult(String serviceURL, FederatedQueryProcessingException ex) {
        if (queryResultTransformer != null) {
            queryResultTransformer.updateStatus(serviceURL, ex.getMessage(), "Invalid result", AbstractStatus.Complete_With_Error );
        }
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener#targetServiceReturnedResults(java.lang.String, org.cagrid.fqp.results.metadata.ResultsRange)
     */
    public void targetServiceReturnedResults(String serviceURL, ResultsRange range) {
        if (queryResultTransformer != null) {
            queryResultTransformer.updateStatus(serviceURL, "Succesfully Completed.", "Succesfully completed.",
                                                AbstractStatus.Complete);
        }
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener#targetServiceThrowsException(java.lang.String, java.lang.Exception)
     */
    public void targetServiceThrowsException(String serviceURL, Exception ex) {
        if (queryResultTransformer != null) {
            queryResultTransformer.updateStatus(serviceURL, ex.getMessage(), "Service Throws exception", AbstractStatus.Complete_With_Error);
        }
    }

}
