/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2bwebapp.bizlogic.executequery;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.queryengine.result.FailedTargetURL;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.server.serviceurl.ServiceURLOperations;

/**
 * Transformed query result object with service instance contact info.
 * @author deepak_shingan 
 */
public class TransformedResultObjectWithContactInfo {

	private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(ServiceURLOperations.class);

	/**
	 * Map for url to transformed query result.
	 */
	private Map<String, List<Map<AttributeInterface, Object>>> urlToResultMap = null;

	/**
	 * URLs failed during query execution.
	 */
	private Collection<FailedTargetURL> failedServiceUrl = null;

	/**
	 * List of attributes(columns) to be shown in result.
	 */
	private List<AttributeInterface> allowedAttributes = null;

	/**
	 * Default constructor. 
	 */
	public TransformedResultObjectWithContactInfo() {
		this.urlToResultMap = new HashMap<String, List<Map<AttributeInterface, Object>>>();
	}

	/**
	 * Adds Url and corresponding result.
	 * @param url
	 * @param result
	 */
	public void addUrlAndResult(String url, List<Map<AttributeInterface, Object>> result) {

		//Retrive ServiceURLInterface object from URL
		//Adding service meta data inside result map
		ServiceURLInterface serviceUrlMetadata;
		try {
			serviceUrlMetadata = new ServiceURLOperations().getServiceURLbyURLLocation(url);
		} catch (RemoteException e1) {
			logger.info(e1.getMessage(), e1);
			throw new RuntimeException(e1.getMessage(), e1);
		}
		try {
			AttributeInterface attributeHC = null;
			AttributeInterface attributePC = null;
			AttributeInterface attributeCE = null;
			AttributeInterface attributeHI = null;

			attributeHC = DomainObjectFactory.getInstance().createStringAttribute();
			attributeHC.setName("Hosting Cancer Research Center");
			attributePC = DomainObjectFactory.getInstance().createStringAttribute();
			attributePC.setName("Point of Contact");
			attributeCE = DomainObjectFactory.getInstance().createStringAttribute();
			attributeCE.setName("Contact eMail");
			attributeHI = DomainObjectFactory.getInstance().createStringAttribute();
			attributeHI.setName("Hosting Institution");
			allowedAttributes.add(attributeHC);
			allowedAttributes.add(attributePC);
			allowedAttributes.add(attributeCE);
			allowedAttributes.add(attributeHI);
			for (Map<AttributeInterface, Object> recordMap : result) {
				recordMap.put(attributeHC, serviceUrlMetadata.getHostingCenter());
				recordMap.put(attributePC, serviceUrlMetadata.getContactName());
				recordMap.put(attributeCE, serviceUrlMetadata.getContactMailId());
				recordMap.put(attributeHI, serviceUrlMetadata.getHostingCenterShortName());
			}
			urlToResultMap.put(url, result);
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
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
	public Collection<FailedTargetURL> getFailedServiceUrl() {
		return failedServiceUrl;
	}

	/**
	 * @param failedServiceUrl the failedServiceUrl to set
	 */
	public void setFailedServiceUrl(Collection<FailedTargetURL> failedServiceUrl) {
		this.failedServiceUrl = failedServiceUrl;
	}

	/**
	 * @return the allowedAttributes
	 */
	public List<AttributeInterface> getAllowedAttributes() {
		return allowedAttributes;
	}

	/**
	 * @param allowedAttributes the allowedAttributes to set
	 */
	public void setAllowedAttributes(List<AttributeInterface> allowedAttributes) {
		this.allowedAttributes = allowedAttributes;
	}
}
