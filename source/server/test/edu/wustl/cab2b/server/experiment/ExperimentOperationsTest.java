package edu.wustl.cab2b.server.experiment;

import java.util.Collection;

import junit.framework.TestCase;
import net.sf.hibernate.HibernateException;
import edu.wustl.cab2b.common.domain.DataListMetadata;
import edu.wustl.cab2b.common.domain.Experiment;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

public class ExperimentOperationsTest extends TestCase {

	public void testGetRootId() throws HibernateException, DAOException {
	//	Logger.configure();

		long id = 1;
		long expected=2490;

	//	Experiment expt = new ExperimentOperations().getExperiment(id);

	//	Collection<DataListMetadata> dataListColl = expt.getDataListMetadataCollection();
	//	for (DataListMetadata dl : dataListColl) {
		
		System.out.println(new ExperimentOperations().getAllDataLists());
	//	}

	}

}
