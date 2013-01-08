/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.experiment;

import java.util.ArrayList;
import java.util.List;

import edu.wustl.cab2b.common.domain.AdditionalMetadata;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.dao.JDBCDAOImpl;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * 
 * @author chetan_bh
 *
 */
public class MetadataOperations extends DefaultBizLogic {

    /**
     * Returns Metadata associated with an experiment or an experiment group.
     * @param id experiment or experiment group identifier.
     * @return AdditionalMetadata object.
     * @throws DAOException 
     */
    public AdditionalMetadata getMetadata(long id) throws DAOException {
        AdditionalMetadata metaData = null;

        List results = (List) retrieve(AdditionalMetadata.class.getName(), "id", new Long(id));
        metaData = (AdditionalMetadata) results.get(0);
        return metaData;
    }

    /**
     * Returns hierarchy of limited metadata for experimet/experiment group in a flat format. 
     * @param expOrGrpId experiment or experiment group identifier.
     * @return metadata hierarchy.
     * @throws DAOException 
     * @throws ClassNotFoundException 
     */
    //	 TODO method name should be changed to getExperimentHierarchy() - because metadata hierarchy 
    //		can include category hierarchy, and other hierarchies. 
    public List getMetadataHierarchy(Long expOrGrpId) throws DAOException, ClassNotFoundException {
        JDBCDAO jdbcDao = new JDBCDAOImpl();

        List expGrp = new ArrayList();
        jdbcDao.openSession(null);

        String sql_query1 = "Select EXP_ID, EXG_ID from expgrpmapping";
        String sql_query2 = "Select EXG_ID, PARENT_EXG_ID from experimentgroup";

        String sql_query3 = "";

        expGrp = jdbcDao.executeQuery(sql_query2, null, false, null);
        List expExpGrp = jdbcDao.executeQuery(sql_query1, null, false, null);

        expGrp.addAll(expExpGrp);
        for (int i = 0; i < expGrp.size(); i++) {
            List metadata = (List) expGrp.get(i);
            String id = metadata.get(0).toString();

            sql_query3 = "Select AMD_NAME, AMD_DESCRIPTION from additionalmetadata where AMD_ID = " + id;

            List nameDescs = jdbcDao.executeQuery(sql_query3, null, false, null);
            //Logger.out.info("nameDesc  "+nameDescs);
            List nameDesc = (List) nameDescs.get(0);
            metadata.addAll(nameDesc);
        }

        return expGrp;
    }
}
