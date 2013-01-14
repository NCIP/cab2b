/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.admin.searchdata.action;

import static edu.wustl.cab2b.admin.util.AdminConstants.CATEGORY_INSTANCE;

import java.io.IOException;

import edu.wustl.cab2b.admin.action.BaseAction;
import edu.wustl.cab2b.admin.flex.CategoryDagPanel;
import edu.wustl.cab2b.admin.util.AttributePair;
import edu.wustl.cab2b.admin.util.InterModelConnectionsUtil;

/**
 * This class persists the InterModelConnection beetween the two classes
 * 
 * @author lalit_chand
 * 
 */
public class PersistInterModel extends BaseAction {
	private static final long serialVersionUID = 1L;

	/**
	 * Main method gets called from Ajax call
	 * 
	 * @return
	 * @throws IOException
	 */
	public String execute() throws IOException {
		CategoryDagPanel categoryDagPanel = (CategoryDagPanel) session
				.get(CATEGORY_INSTANCE);
		AttributePair attributePair = categoryDagPanel.getAttributePair();
		response.setContentType("text/html");
		String returnAction = null;
		try {
			InterModelConnectionsUtil.saveInterModelConnection(attributePair);
			returnAction = SUCCESS;
		} catch (IllegalArgumentException iAE) {
			iAE.printStackTrace();
			returnAction = iAE.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
			returnAction = e.getMessage();
		}
		response.getWriter().write(returnAction);

		return null;
	}

}
