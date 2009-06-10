package edu.wustl.cab2b.admin.searchdata.action;

import static edu.wustl.cab2b.admin.util.AdminConstants.CATEGORY_INSTANCE;
import static edu.wustl.cab2b.admin.util.AdminConstants.FAILURE;
import static edu.wustl.cab2b.admin.util.AdminConstants.PASS;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.admin.action.BaseAction;
import edu.wustl.cab2b.admin.bizlogic.CreateCategoryBizLogic;
import edu.wustl.cab2b.admin.flex.CategoryDagPanel;
import edu.wustl.cab2b.admin.flex.DAGLink;
import edu.wustl.cab2b.admin.flex.DAGNode;
import edu.wustl.cab2b.admin.util.AdminConstants;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.common.util.Utility;

/**
 * @author atul_jawale
 * @author lalit_chand
 */
public class CreateCategoryAttributeOrder extends BaseAction {
	private static final long serialVersionUID = 5127133829185857760L;

	private static final Logger logger = edu.wustl.common.util.logger.Logger
			.getLogger(CreateCategoryAttributeOrder.class);

	@SuppressWarnings("unchecked")
	private String formAction = null;

	private String title;

	private String description;

	/**
	 * @return the action
	 */
	public String getFormAction() {
		return formAction;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setFormAction(String action) {
		this.formAction = action;
	}

	@SuppressWarnings("unchecked")
	/*
	 * @return
	 */
	public String execute() throws RemoteException, IOException {
		// this is for showing pop up window for showing error detail
		if (this.request.getParameter("newWindow") != null) {
			return AdminConstants.ERROR_DETAIL;
		}

		Map<Long, List<String>> allAttributes = null;
		if (getFormAction() != null) {
			Map<String, String> unFormattedAttributeMap = (Map<String, String>) session
					.get("unFormattedAttributeMap");
			allAttributes = (Map<Long, List<String>>) getSession().get(
					"attributeList");
			Map<String, String> allAttributeDisplayNameMap = new HashMap<String, String>();

			for (Long nodeId : allAttributes.keySet()) {
				List<String> nodeIDAttributeList = allAttributes.get(nodeId);
				for (String expIdAttributeName : nodeIDAttributeList) {
					String attributeDisplayName = getServletRequest()
							.getParameter(expIdAttributeName);
					attributeDisplayName = attributeDisplayName == null ? expIdAttributeName
							: attributeDisplayName;
					allAttributeDisplayNameMap.put(nodeId + "."
							+ unFormattedAttributeMap.get(expIdAttributeName),
							attributeDisplayName);
				}
			}

			Set<DAGNode> dagNodeSet = null;
			Set<DAGLink> dagLinkSet = null;
			if ((session.get(CATEGORY_INSTANCE) != null)) {
				CategoryDagPanel categoryDagPanel = (CategoryDagPanel) session
						.get(CATEGORY_INSTANCE);
				dagNodeSet = categoryDagPanel.getDagNodeSet();
				dagLinkSet = categoryDagPanel.getDagPathSet();
			}

			CreateCategoryBizLogic bizlogic = new CreateCategoryBizLogic(
					dagNodeSet, dagLinkSet, allAttributeDisplayNameMap);
			try {
				bizlogic.saveCategory(getTitle(), getDescription());
			} catch (Exception e) {
				logger.debug(e.getMessage());
				getSession().put("exceptionMessage",
						edu.wustl.cab2b.common.util.Utility.getStackTrace(e));
				getSession().put("title", getTitle());
				return FAILURE;
			} finally {
				getSession().remove("attributeList");
				getSession().remove("pathMap");
			}

			return PASS;
		} else {
			allAttributes = new HashMap<Long, List<String>>();
			Map<String, String> unFormattedAttributeMap = new HashMap<String, String>();
			Set<DAGNode> setOfNodes = null;

			if ((session.get(CATEGORY_INSTANCE) != null)) {
				CategoryDagPanel categoryDagPanel = (CategoryDagPanel) session
						.get(CATEGORY_INSTANCE);
				setOfNodes = categoryDagPanel.getDagNodeSet();
			}

			boolean flagForFullName = false;
			for (DAGNode dagNode : setOfNodes) {
				String nodeName = Utility.parseClassName(EntityCache.getCache()
						.getEntityById(dagNode.getEntityId()).getName());
				for (DAGNode innerDagNode : setOfNodes) {
					String innerNodeName = Utility.parseClassName(EntityCache
							.getCache().getEntityById(
									innerDagNode.getEntityId()).getName());
					if ((dagNode.getNodeId() != innerDagNode.getNodeId())
							&& nodeName.equals(innerNodeName)) {
						flagForFullName = true;
					}
				}
			}

			for (DAGNode dagNode : setOfNodes) {
				long nodeId = dagNode.getNodeId();
				String nodeName = null;
				if (!flagForFullName) {
					EntityInterface entity = EntityCache.getCache()
							.getEntityById(dagNode.getEntityId());
					nodeName = Utility.parseClassName(entity.getName());
				} else {
					nodeName = dagNode.getNodeName();
				}

				List<String> attributes = new ArrayList<String>();
				List<String> attibutesList = dagNode.getAttributeList();
				for (String attribute : attibutesList) {
					String[] attributeName = attribute.split(":");
					// attributes.add(nodeId + "." +
					// CommonUtils.getFormattedString(attributeName[0]));
					attributes.add(nodeName
							+ " "
							+ edu.wustl.cab2b.common.util.Utility
									.getFormattedString(attributeName[0]));
					unFormattedAttributeMap.put(nodeName
							+ " "
							+ edu.wustl.cab2b.common.util.Utility
									.getFormattedString(attributeName[0]),
							attributeName[0]);
				}
				allAttributes.put(nodeId, attributes);
			}
			getSession()
					.put("unFormattedAttributeMap", unFormattedAttributeMap);
			getSession().put("attributeList", allAttributes);

			return SUCCESS;
		}
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
