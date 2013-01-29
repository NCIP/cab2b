/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.path;

import java.sql.Connection;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.server.util.ConnectionUtil;
import edu.wustl.cab2b.server.util.SQLQueryUtil;

/**
 * Collection of methods relation to database operations on the table INTER_MODEL_ASSOCIATION.
 * @author srinath_k
 */
public class InterModelConnectionBizLogic {
	
	/**
	 * Saves intermodel connection
	 * @param a1
	 * @param a2
	 */
    public void saveInterModelConnection(AttributeInterface a1, AttributeInterface a2) {
        saveInterModelConnection(new InterModelConnection(a1, a2));
    }

    /**
     * Utility method. Saves intermodel connection
     * @param imc
     */
    public void saveInterModelConnection(InterModelConnection imc) {
        if (PathFinder.getInstance().isInterModelConnectionExist(imc)) {
            return;
        }
        Connection conn=null;
        try {
            conn = ConnectionUtil.getConnection();
            long nextId = PathBuilder.getNextAssociationId(2, conn);
            saveInterModelConnection(imc, nextId, conn);
            saveInterModelConnection(imc.mirror(), nextId + 1, conn);
        } finally{
            ConnectionUtil.close(conn);
        }
        
    }

    private void saveInterModelConnection(InterModelConnection imc, long id, Connection conn) {
        String sql = "insert into inter_model_association(association_id, left_entity_id, left_attribute_id, right_entity_id, right_attribute_id) values (";
        sql = sql + id + "," + imc.getLeftEntityId() + "," + imc.getLeftAttributeId() + ","
                + imc.getRightEntityId() + "," + imc.getRightAttributeId() + ");";
        SQLQueryUtil.executeUpdate(sql, conn);

        sql = "insert into association(association_id, association_type) values (";
        sql = sql + id + "," + AssociationType.INTER_MODEL_ASSOCIATION.getValue() + ");";
        SQLQueryUtil.executeUpdate(sql, conn);
        // TODO transaction??

        PathFinder.getInstance().addInterModelConnection(imc);
    }
}
