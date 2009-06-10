
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
import edu.wustl.cab2b.common.user.ServiceURL;
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
			for (Map<AttributeInterface, Object> recordMap : result) {
				AttributeInterface attribute = DomainObjectFactory.getInstance().createStringAttribute();

				attribute.setName("Hosting Cancer Research Center");
				recordMap.put(attribute, serviceUrlMetadata.getHostingCenter());

				attribute = DomainObjectFactory.getInstance().createStringAttribute();
				attribute.setName("Point of Contact");
				recordMap.put(attribute, serviceUrlMetadata.getContactMailId());

				attribute = DomainObjectFactory.getInstance().createStringAttribute();
				attribute.setName("Hosting Institution");
				recordMap.put(attribute, serviceUrlMetadata.getHostingCenterShortName());
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
		List<Map<AttributeInterface, Object>> collectedResult = new ArrayList<Map<AttributeInterface, Object>>();
		Collection<List<Map<AttributeInterface, Object>>> collectionOfResults = urlToResultMap.values();
		for (List<Map<AttributeInterface, Object>> result : collectionOfResults) {
			collectedResult.addAll(result);
		}
		return collectedResult;
	}
}
