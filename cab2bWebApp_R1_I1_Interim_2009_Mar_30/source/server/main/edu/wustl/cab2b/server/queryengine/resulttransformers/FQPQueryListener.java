package edu.wustl.cab2b.server.queryengine.resulttransformers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.cagrid.fqp.results.metadata.ProcessingStatus;
import org.cagrid.fqp.results.metadata.ResultsRange;

import edu.wustl.cab2b.common.queryengine.result.FailedTargetURL;

import gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener;
import gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException;

/**
 * FQPProcessingStatusListener implementation class which listens 
 * for changes in FQP's (D)CQL broadcasting status.
 * @author deepak_shingan
 *
 */
public class FQPQueryListener implements FQPProcessingStatusListener, Serializable {
    private Map<String, FailedTargetURL> urlVsException = new HashMap<String, FailedTargetURL>();

    /**
     * @return the urlVsException
     */
    public Map<String, FailedTargetURL> getFailedURLs() {
        return urlVsException;
    }

    /**
     * @param urlVsException
     *            the urlVsException to set
     */
    // public void setUrlVsException(Map<String, Exception> urlVsException) {
    // this.urlVsException = urlVsException;
    // }
    @Override
    public void processingStatusChanged(ProcessingStatus status, String message) {
        //System.out.println("Processing status : " + status + "\t Message :" + message);
    }

    @Override
    public void targetServiceConnectionRefused(String serviceURL) {
        FailedTargetURL failedUrl = new FailedTargetURL(serviceURL, "Target service connection refused.", "");
        urlVsException.put(serviceURL, failedUrl);

    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener#targetServiceOk(java.lang.String)
     */
    @Override
    public void targetServiceOk(String serviceURL) {
        //   System.out.println("targetServiceOk : " + serviceURL);

    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener#targetServiceReturnedInvalidResult(java.lang.String, gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException)
     */
    @Override
    public void targetServiceReturnedInvalidResult(String serviceURL, FederatedQueryProcessingException ex) {
        FailedTargetURL failedUrl = new FailedTargetURL(serviceURL, ex.getMessage(), "");
        urlVsException.put(serviceURL, failedUrl);
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener#targetServiceReturnedResults(java.lang.String, org.cagrid.fqp.results.metadata.ResultsRange)
     */
    @Override
    public void targetServiceReturnedResults(String serviceURL, ResultsRange range) {
        //System.out.println("targetServiceReturnedResults : " + serviceURL + "\tRange P:" + range);
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cagrid.fqp.processor.FQPProcessingStatusListener#targetServiceThrowsException(java.lang.String, java.lang.Exception)
     */
    @Override
    public void targetServiceThrowsException(String serviceURL, Exception ex) {
        FailedTargetURL failedUrl = new FailedTargetURL(serviceURL, ex.getMessage(), "");
        urlVsException.put(serviceURL, failedUrl);
    }
}
