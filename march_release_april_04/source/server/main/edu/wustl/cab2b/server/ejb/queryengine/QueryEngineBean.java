package edu.wustl.cab2b.server.ejb.queryengine;

import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.queryengine.QueryExecutor;

public class QueryEngineBean extends AbstractStatelessSessionBean implements
        QueryEngineBusinessInterface {

    private static final long serialVersionUID = 8416841912609836063L;

    public IQueryResult executeQuery(ICab2bQuery query) {
        return QueryExecutor.executeQuery(query);
    }
}