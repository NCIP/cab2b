/**
 * 
 */
package edu.wustl.cab2bwebapp.dvo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.KeywordQuery;
import edu.wustl.cab2b.common.queryengine.querystatus.AbstractStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.URLStatus;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.serviceurl.ServiceURLOperations;

/**
 * Class for DashBoardDVO related operations.
 * @author deepak_shingan
 *
 */
public class DashBoardDvoUtility {

    private DashBoardDvoUtility() {

    }

    /**
     * @param queryStatusDVOSet
     * @return
     */
    public static int getQueryStatusDVOProcessingResultCount(Set<QueryStatusDVO> queryStatusDVOSet) {
        int count = 0;
        if (queryStatusDVOSet != null) {
            for (QueryStatusDVO qStatusDVO : queryStatusDVOSet) {
                if (qStatusDVO.getStatus().equals(AbstractStatus.Processing)) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * @param queryStatusFromMemory
     * @param queryStatusDVOSet
     * @return
     */
    public static void updateStatusDVO(Collection<QueryStatus> queryStatusFromMemory,
                                       Collection<QueryStatusDVO> queryStatusDVOSet) {

        if (queryStatusDVOSet == null && queryStatusFromMemory != null) {
            queryStatusDVOSet = getStatusDVO(queryStatusFromMemory);
        } else {
            for (QueryStatus queryStatus : queryStatusFromMemory) {
                QueryStatusDVO dvo = getDvoForQueryStatus(queryStatus, queryStatusDVOSet);
                updateDVOForQueryStatus(queryStatus, dvo);
            }
        }
    }

    /**
     * @param qsCollection
     * @return
     */
    private static List<QueryStatusDVO> getStatusDVO(Collection<QueryStatus> qsCollection) {
        List<QueryStatusDVO> queryStatusDVOSet = new ArrayList<QueryStatusDVO>();
        for (QueryStatus qs : qsCollection) {
            QueryStatusDVO queryStatusDVO = new QueryStatusDVO();
            updateDVOForQueryStatus(qs, queryStatusDVO);
            queryStatusDVOSet.add(queryStatusDVO);
        }
        return queryStatusDVOSet;
    }

    /**
     * @param queryStatus
     * @param queryStatusDVOSet
     * @return
     */
    private static QueryStatusDVO getDvoForQueryStatus(QueryStatus queryStatus,
                                                       Collection<QueryStatusDVO> queryStatusDVOSet) {
        QueryStatusDVO dvo = null;
        for (QueryStatusDVO queryStatusDVO : queryStatusDVOSet) {
            if (queryStatusDVO.getId() == queryStatus.getId().longValue()) {
                dvo = queryStatusDVO;
                break;
            }
        }
        if (dvo == null) {
            dvo = new QueryStatusDVO();
            queryStatusDVOSet.add(dvo);
        }
        return dvo;
    }

    /**
     * @param qs
     * @param queryStatusDVO
     */
    private static void updateDVOForQueryStatus(QueryStatus qs, QueryStatusDVO queryStatusDVO) {
        ICab2bQuery query = qs.getQuery();
        queryStatusDVO.setId(qs.getId());
        queryStatusDVO.setType("ANDed".equalsIgnoreCase(query.getType()) ? "Saved Search" : "Keyword");
        queryStatusDVO.setStatus(qs.getStatus());
        if (qs.getResultCount() != null) {
            queryStatusDVO.setResultCount(qs.getResultCount());
        }
        queryStatusDVO.setExecutedOn(qs.getQueryStartTime());
        List<QueryConditionDVO> queryConditions = createQueryConditionsDVO(qs);
        queryStatusDVO.setConditions(queryConditions);
        queryStatusDVO.setTitle(createQueryTitle(query, queryConditions.get(0).getValue()));
        queryStatusDVO.setServiceInstances(createServiceInstanceDVO(qs));
        queryStatusDVO.setFileName(qs.getFileName());
    }

    /**
     * @return
     */
    private static List<QueryConditionDVO> createQueryConditionsDVO(QueryStatus qs) {
        List<QueryConditionDVO> queryConditions = new ArrayList<QueryConditionDVO>();
        String pattern = "(.*)\\((.*)\\)(.*)";
        String values[] = qs.getQueryConditions().split(";");
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        String conditionValue = null;
        for (int j = 0; j < values.length; j++) {
            QueryConditionDVO queryCondition = new QueryConditionDVO();
            Matcher m = p.matcher(values[j]);
            m.find();
            queryCondition.setParameter(m.group(1));
            queryCondition.setCondition(edu.wustl.cab2b.common.util.Utility.getFormattedString(m.group(2)));
            conditionValue = m.group(3);
            queryCondition.setValue(conditionValue);
            queryConditions.add(queryCondition);
        }
        return queryConditions;
    }

    /**
     * @param query
     * @param keyword
     * @return
     */
    private static String createQueryTitle(ICab2bQuery query, String keyword) {
        String title = null;
        if (query instanceof KeywordQuery) {
            KeywordQuery keywordQuery = (KeywordQuery) query;
            StringBuffer keyWordTitle =
                    new StringBuffer("Keyword search for ").append('"').append(keyword).append('"').append(" on ")
                        .append('"').append(keywordQuery.getApplicationGroup().getModelGroupName()).append('"');
            title = keyWordTitle.toString();
        } else {
            title = query.getName();
        }
        return title;
    }

    /**
     * @param qs
     * @return
     */
    private static List<ServiceInstanceDVO> createServiceInstanceDVO(QueryStatus qs) {
        List<ServiceInstanceDVO> serviceInstances = new ArrayList<ServiceInstanceDVO>();
        Iterator<URLStatus> itr = qs.getUrlStatus().iterator();
        while (itr.hasNext()) {
            URLStatus urlStatus = (URLStatus) itr.next();
            ServiceInstanceDVO sreviceInstance = new ServiceInstanceDVO();
            ServiceURLOperations serviceURLOpreration = new ServiceURLOperations();
            ServiceURLInterface serviceURL = serviceURLOpreration.getServiceURLbyURLLocation(urlStatus.getUrl());
            sreviceInstance.setName(Utility.getHostingInstitutionName(serviceURL));
            sreviceInstance.setStatus(urlStatus.getStatus());
            sreviceInstance.setResultCount(urlStatus.getResultCount() == null ? 0 : urlStatus.getResultCount());
            serviceInstances.add(sreviceInstance);
        }
        serviceInstances = mergeUrlProperties(serviceInstances);
        return serviceInstances;
    }

    /**
     * @param s1
     * @param s2
     * @return
     */
    private static String decideStatus(String s1, String s2) {
        String status = "";
        if (s1.equals(AbstractStatus.Processing) || s2.equals(AbstractStatus.Processing)) {
            status = AbstractStatus.Processing;
        } else if (s1.equals(AbstractStatus.Complete) && s2.equals(AbstractStatus.Complete)) {
            status = AbstractStatus.Complete;
        } else if (s1.equals(AbstractStatus.SUSPENDED) || s2.equals(AbstractStatus.SUSPENDED)) {
            status = AbstractStatus.SUSPENDED;
        } else {
            status = AbstractStatus.Complete_With_Error;
        }
        return status;
    }

    /**
     * Display unique record count for same service instances in different queries.         
     * @param serviceInstances
     * @return
     */
    private static List<ServiceInstanceDVO> mergeUrlProperties(List<ServiceInstanceDVO> serviceInstances) {
        Map<String, ServiceInstanceDVO> map = new HashMap<String, ServiceInstanceDVO>();
        for (ServiceInstanceDVO dvo : serviceInstances) {
            if (map.containsKey(dvo.getName())) {
                ServiceInstanceDVO oldDVO = map.get(dvo.getName());
                oldDVO.setResultCount(oldDVO.getResultCount() + dvo.getResultCount());
                oldDVO.setStatus(decideStatus(oldDVO.getStatus(), dvo.getStatus()));
            } else {
                map.put(dvo.getName(), dvo);
            }
        }
        return new ArrayList<ServiceInstanceDVO>(map.values());
    }
}
