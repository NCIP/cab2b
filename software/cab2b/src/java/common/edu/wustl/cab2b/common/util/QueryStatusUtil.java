/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.common.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.wustl.cab2b.common.queryengine.querystatus.AbstractStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.URLStatus;

/**
 * @author deepak_shingan
 *
 */
public class QueryStatusUtil {

    /**
     * Returns true if status equals to Complete, Complete With Error or Failed else returns false.
     * @param qStatus
     * @return
     */
    public static boolean isStatusProcessingDone(QueryStatus qStatus) {
        String status = qStatus.getStatus();
        if (status != null
                && (status.equals(AbstractStatus.Complete) || status.equals(AbstractStatus.Complete_With_Error) || status
                    .equals(AbstractStatus.FAILED))) {
            return true;
        }
        return false;
    }

    /**
     * @param statusString
     * @return
     */
    public static boolean isEveryChildStatusEqualsTo(String statusString, QueryStatus qStatus) {
        for (QueryStatus subQueryStatus : qStatus.getChildrenQueryStatus()) {
            String childStatus = subQueryStatus.getStatus();
            if (!childStatus.equals(statusString)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param statusString
     * @return
     */
    public static boolean isEveryUrlStatusEqualsTo(String statusString, QueryStatus qStatus) {
        for (URLStatus uStatus : qStatus.getUrlStatus()) {
            String uStatusString = uStatus.getStatus();
            if (!uStatusString.equals(statusString)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param ChildQueryStatus
     * @return
     */
    public static boolean areAllSubQueriesFinished(QueryStatus queryStatus) {
        boolean finished = true;
        Collection<QueryStatus> childQueryStatus = queryStatus.getChildrenQueryStatus();
        for (QueryStatus fqpUrl : childQueryStatus) {
            String urlStatus = fqpUrl.getStatus();
            if (urlStatus.equals(AbstractStatus.Processing)) {
                finished = false;
                break;
            }
        }
        return finished;
    }

    /**
     * @param fqpUrlStatus
     * @return
     */
    public static boolean areAllUrlsFinished(QueryStatus qStatus) {
        boolean finished = true;
        Set<URLStatus> urlS = qStatus.getUrlStatus();
        for (URLStatus uStatus : urlS) {
            String urlStatus = uStatus.getStatus();
            if (urlStatus.equals(AbstractStatus.Processing)) {
                finished = false;
                break;
            }
        }
        return finished;
    }
   

    /**
     * This method checks url status and result count of the given query status object, 
     * if url status is Complete With Error and url result count is null sets url status as failed
     *  else does nothing.     
     */
    public static void checkAndSetIfUrlFailedFor(QueryStatus qStatus) {
        for (URLStatus uStatus : qStatus.getUrlStatus()) {
            if (uStatus.getStatus().equals(AbstractStatus.Complete_With_Error) && uStatus.getResultCount() == null) {
                uStatus.setStatus(AbstractStatus.FAILED);
            }
        }
    }

    /**
     * Method which returns associated URL object for given url string. 
     * @param url
     * @return
     */
    public static URLStatus getStatusUrlObject(String url, QueryStatus qStatus) {
        if (qStatus != null) {
            Collection<URLStatus> urlStatus = qStatus.getUrlStatus();
            for (URLStatus uStatus : urlStatus) {
                String fqpUrl = uStatus.getUrl();
                if (fqpUrl != null && fqpUrl.equals(url)) {
                    return uStatus;
                }
            }
        }
        return null;
    }

    /**
     * Returns set of failed urls.
     * @return {@link Set}
     */
    public static Set<String> getFailedURLs(QueryStatus qStatus) {
        Set<URLStatus> urlStatusSet = qStatus.getUrlStatus();
        Set<String> failedUrls = new HashSet<String>();
        for (URLStatus urlStatus : urlStatusSet) {
            if (urlStatus.getStatus().equals(AbstractStatus.Complete_With_Error)
                    || urlStatus.getStatus().equals(AbstractStatus.FAILED)) {
                failedUrls.add(urlStatus.getUrl());
            }
        }
        return failedUrls;
    }

}
