/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.admin.searchdata.action;

import static edu.wustl.cab2b.admin.util.AdminConstants.ATTRIBUTE_LIST1;
import static edu.wustl.cab2b.admin.util.AdminConstants.ATTRIBUTE_LIST2;
import static edu.wustl.cab2b.admin.util.AdminConstants.ATTRIBUTE_PAIR_SET;
import static edu.wustl.cab2b.admin.util.AdminConstants.ENTITY1;
import static edu.wustl.cab2b.admin.util.AdminConstants.ENTITY2;
import static edu.wustl.cab2b.admin.util.AdminConstants.SOURCE_CLASS_ID;
import static edu.wustl.cab2b.admin.util.AdminConstants.TARGET_CLASS_ID;

import java.io.IOException;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.admin.action.BaseAction;
import edu.wustl.cab2b.admin.util.AttributePair;
import edu.wustl.cab2b.admin.util.InterModelConnectionsUtil;
import edu.wustl.cab2b.common.cache.AbstractEntityCache;
import edu.wustl.cab2b.common.util.Utility;

/**
 * This class retrieves the matching attribute pairs of the selected entities.
 * 
 * @author chetan_patil
 * 
 */
public class InterModelMatching extends BaseAction {
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	/**
	 * Main method gets called from Ajax call
	 * 
	 * @return
	 * @throws IOException
	 */
	public String execute() throws IOException {
		if (request.getParameter("newWindow") != null) {
			return SUCCESS;
		}

		AbstractEntityCache entityCache = AbstractEntityCache.getCache();

		String sourceEntityId = ((Long) session.get(SOURCE_CLASS_ID))
				.toString();
		EntityInterface sourceEntity = entityCache.getEntityById(Long
				.valueOf(sourceEntityId));

		String targetEntityId = ((Long) session.get(TARGET_CLASS_ID))
				.toString();
		EntityInterface targetEntity = entityCache.getEntityById(Long
				.valueOf(targetEntityId));

		String returnAction = SUCCESS;
		try {
			Set<AttributePair> attributePairSet = InterModelConnectionsUtil
					.determineConnections(sourceEntity, targetEntity);
			session.put(ATTRIBUTE_PAIR_SET, attributePairSet);
			session.put(ATTRIBUTE_LIST1, sourceEntity.getAttributeCollection());
			session.put(ATTRIBUTE_LIST2, targetEntity.getAttributeCollection());

			String sourceEntityName = Utility.getDisplayName(sourceEntity);
			String targetEntityName = Utility.getDisplayName(targetEntity);

			session.put(ENTITY1, sourceEntityName);
			session.put(ENTITY2, targetEntityName);

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			returnAction = e.getMessage();
		}

		response.setContentType("text/html");
		response.getWriter().write(returnAction);

		return null;
	}
}
