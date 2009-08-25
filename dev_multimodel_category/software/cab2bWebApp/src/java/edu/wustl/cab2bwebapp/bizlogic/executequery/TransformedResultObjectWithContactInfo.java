package edu.wustl.cab2bwebapp.bizlogic.executequery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.queryengine.result.FQPUrlStatus;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.serviceurl.ServiceURLOperations;
import edu.wustl.cab2bwebapp.constants.Constants;

/**
 * Transformed query result object with service instance contact info.
 * @author deepak_shingan 
 */
public class TransformedResultObjectWithContactInfo {

    private static final Logger logger =
            edu.wustl.common.util.logger.Logger.getLogger(TransformedResultObjectWithContactInfo.class);

    /**
     * Map for url to transformed query resu.lt.
     */
    private Map<String, List<Map<AttributeInterface, Object>>> urlToResultMap =
            new HashMap<String, List<Map<AttributeInterface, Object>>>();

    /**
     * URLs failed during query execution.
     */
    private Collection<FQPUrlStatus> failedServiceUrl = null;

    /**
     * URLs infeasible for transformation during query execution.
     */
    private Set<String> inFeasibleUrl = new HashSet<String>();

    /**
     * List of attributes(columns) to be shown in result.
     */
    private Collection<AttributeInterface> allowedAttributes = null;

    /**
     * Adds Url and corresponding result.
     * @param url
     * @param result
     */
    public void addUrlAndResult(String url, List<Map<AttributeInterface, Object>> result) {
        //logger.info("Inside addUrlAndResult");
        //Retrive ServiceURLInterface object from URL
        ServiceURLInterface serviceUrlMetadata = new ServiceURLOperations().getServiceURLbyURLLocation(url);

        boolean addAttributesFlag = true;
        AttributeInterface attributeMN = null;
        AttributeInterface attributeHC = null;
        AttributeInterface attributePC = null;
        AttributeInterface attributeCE = null;
        AttributeInterface attributeHI = null;

        for (AttributeInterface attr : allowedAttributes) {
            String attributeName = attr.getName();
            if (attributeName.equals(Constants.MODEL_NAME)) {
                attributeMN = attr;
            } else if (attributeName.equals(Constants.HOSTING_CANCER_RESEARCH_CENTER)) {
                attributeHC = attr;
            } else if (attributeName.equals(Constants.POINT_OF_CONTACT)) {
                attributePC = attr;
            } else if (attributeName.equals(Constants.CONTACT_EMAIL)) {
                attributeCE = attr;
            } else if (attributeName.equals(Constants.HOSTING_INSTITUTION)) {
                attributeHI = attr;
            } else if (attributeHC != null && attributePC != null && attributeCE != null && attributeHI != null) {
                addAttributesFlag = false;
                break;
            }
        }

        if (addAttributesFlag) {
            if (attributeMN == null) {
                attributeMN = DomainObjectFactory.getInstance().createObjectAttribute();
                attributeMN.setName(Constants.MODEL_NAME);
                allowedAttributes.add(attributeMN);
            }
            if (attributeHC == null) {
                attributeHC = DomainObjectFactory.getInstance().createObjectAttribute();
                attributeHC.setName(Constants.HOSTING_CANCER_RESEARCH_CENTER);
                allowedAttributes.add(attributeHC);
            }
            if (attributePC == null) {
                attributePC = DomainObjectFactory.getInstance().createStringAttribute();
                attributePC.setName(Constants.POINT_OF_CONTACT);
                allowedAttributes.add(attributePC);
            }
            if (attributeCE == null) {
                attributeCE = DomainObjectFactory.getInstance().createStringAttribute();
                attributeCE.setName(Constants.CONTACT_EMAIL);
                allowedAttributes.add(attributeCE);
            }
            if (attributeHI == null) {
                attributeHI = DomainObjectFactory.getInstance().createStringAttribute();
                attributeHI.setName(Constants.HOSTING_INSTITUTION);
                allowedAttributes.add(attributeHI);
            }
        }
        //for failed URLs and even infeasible URL's, new ArrayList will be created
        if (result == null) {
            result = new ArrayList<Map<AttributeInterface, Object>>(1);
        }
        //Adding service meta data inside result map
        for (Map<AttributeInterface, Object> recordMap : result) {
            recordMap.put(attributeMN, Utility.createModelName(serviceUrlMetadata.getDomainModel(),
                                                               serviceUrlMetadata.getVersion()));
            recordMap.put(attributeHC, serviceUrlMetadata.getHostingCenter());
            recordMap.put(attributePC, serviceUrlMetadata.getContactName());
            recordMap.put(attributeCE, serviceUrlMetadata.getContactMailId());
            recordMap.put(attributeHI, serviceUrlMetadata.getHostingCenterShortName());
        }
        urlToResultMap.put(url, result);
    }

    /**
     * Returns result for the given url. 
     * @param url
     * @return List<Map<AttributeInterface, Object>>
     */
    public List<Map<AttributeInterface, Object>> getResultForUrl(String url) {
        return urlToResultMap.get(url);
    }

    /**
     * Returns collection of urls available.  
     * @return Collection<String>
     */
    public Collection<String> getAllUrls() {
        return urlToResultMap.keySet();
    }

    /**
     * Returns results for all urls.
     * @return List<Map<AttributeInterface, Object>>
     */
    public List<Map<AttributeInterface, Object>> getResultForAllUrls() {
        List<Map<AttributeInterface, Object>> collectedResult = null;
        Collection<List<Map<AttributeInterface, Object>>> collectionOfResults = urlToResultMap.values();
        if (collectionOfResults != null) {
            collectedResult = new ArrayList<Map<AttributeInterface, Object>>();
            for (List<Map<AttributeInterface, Object>> result : collectionOfResults) {
                collectedResult.addAll(result);
            }
        }
        return collectedResult;
    }

    /**
     * @return the failedServiceUrl
     */
    public Collection<FQPUrlStatus> getFailedServiceUrl() {
        return failedServiceUrl;
    }

    /**
     * @param collection the failedServiceUrl to set
     */
    public void setFailedServiceUrl(Collection<FQPUrlStatus> collection) {
        this.failedServiceUrl = collection;
    }

    /**
     * @return the allowedAttributes
     */
    public Collection<AttributeInterface> getAllowedAttributes() {
        return allowedAttributes;
    }

    /**
     * @param allowedAttributes the allowedAttributes to set
     */
    public void setAllowedAttributes(Collection<AttributeInterface> allowedAttributes) {
        this.allowedAttributes = allowedAttributes;
    }

    public void addInFeasibleUrls(String inFeasibleUrl) {
        this.getInFeasibleUrl().add(inFeasibleUrl);
    }

    public Set<String> getInFeasibleUrl() {
        return inFeasibleUrl;
    }

    public void setInFeasibleUrl(Set<String> inFeasibleUrl) {
        this.inFeasibleUrl = inFeasibleUrl;
    }
}
