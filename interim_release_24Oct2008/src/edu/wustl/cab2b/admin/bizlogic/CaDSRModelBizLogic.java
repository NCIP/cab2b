package edu.wustl.cab2b.admin.bizlogic;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis.types.URI.MalformedURIException;
import org.apache.log4j.Logger;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.admin.beans.CaDSRModelDetailsBean;
import edu.wustl.cab2b.admin.beans.LoadModelResult;
import edu.wustl.cab2b.admin.util.ServerProperties;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.path.DomainModelParser;
import edu.wustl.cab2b.server.path.PathBuilder;
import edu.wustl.cab2b.server.path.PathFinder;
import edu.wustl.cab2b.server.util.ConnectionUtil;
import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cagrid.cadsr.client.CaDSRServiceClient;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

/**
 * @author chetan_patil
 * @author Hrishikesh Rajpathak
 */
public class CaDSRModelBizLogic {
    private static final int PATH_MAX_LENGTH = 6;

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(CaDSRModelBizLogic.class);

    /**
     * Default Constructor
     */
    public CaDSRModelBizLogic() {
        super();
    }

    /**
     * This method returns the list of model details needed for display.
     * @return List of CaDSRModelDetailsBean
     * @throws RemoteException if exception occurs while finding projects
     */
    public List<CaDSRModelDetailsBean> getProjectsDisplayDetails() throws RemoteException {
        CaDSRServiceClient client = getCaDSRServiceClient();
        Project[] projects = client.findAllProjects();
        Collection<EntityGroupInterface> cab2bEntityGroups = getCab2bEntityGroups();

        List<CaDSRModelDetailsBean> projectDetails = new ArrayList<CaDSRModelDetailsBean>();
        for (Project project : projects) {
            String projectLongName = project.getLongName();

            if (projectLongName != null && !isProjectPresent(project, cab2bEntityGroups)) {
                CaDSRModelDetailsBean caDSRModelDataBean = new CaDSRModelDetailsBean();
                caDSRModelDataBean.setId(project.getId());
                caDSRModelDataBean.setLongName(projectLongName);
                caDSRModelDataBean.setDescription(project.getDescription());
                caDSRModelDataBean.setVersion(project.getVersion());
                projectDetails.add(caDSRModelDataBean);
            }
        }
        Collections.sort(projectDetails);
        return projectDetails;
    }

    /**
     * This method checks if project is already loaded in local application
     * @param project
     * @param cab2bEntityGroups
     * @return
     */
    private boolean isProjectPresent(Project project, Collection<EntityGroupInterface> cab2bEntityGroups) {
        boolean isPresent = false;

        String projectLongName = project.getLongName();
        for (EntityGroupInterface entityGroup : cab2bEntityGroups) {
            String entityLongName = entityGroup.getLongName();
            if (projectLongName.compareTo(entityLongName) == 0) {
                isPresent = true;
                break;
            }
        }

        return isPresent;
    }

    /**
     * This method returns the corresponding domain model form caDSR for the
     * given project's long name.
     * @param projectLongName long name of the project
     * @return domain model
     * @throws RemoteException on invalid project name or could not get the instance of CaDSRServiceClient
     */
    public DomainModel getDomainModel(String projectLongName, String version) throws RemoteException {
        CaDSRServiceClient client = getCaDSRServiceClient();
        Project project = new Project();
        project.setLongName(projectLongName);
        project.setVersion(version);
        DomainModel model = client.generateDomainModelForProject(project);

        return model;
    }

    /**
     * This method returns the instance of CaDSRServiceClient
     * @return Object of type CaDSRServiceClient
     * @throws RemoteException on error while getting caDSR service client or caDSR service URL is incorrect
     */
    private CaDSRServiceClient getCaDSRServiceClient() throws RemoteException {
        CaDSRServiceClient client = null;
        String url = ServerProperties.getCadsrUrl();
        try {
            client = new CaDSRServiceClient(url);
        } catch (MalformedURIException e) {
            throw new RemoteException(e.getMessage());
        }

        return client;
    }

    /**
     * This method saves the selected domain models in data bases. Also does
     * path forming and saving operations.
     * @param longNames
     * @throws RemoteException
     */
    public Map<String, LoadModelResult> persistDomainModel(Collection<CaDSRModelDetailsBean> modelsToLoad) {
        Map<String, LoadModelResult> map = new HashMap<String, LoadModelResult>();

        for (CaDSRModelDetailsBean model : modelsToLoad) {
            LoadModelResult result = new LoadModelResult(model, true, "", null);

            DomainModel domainModel = null;
            try {
                domainModel = getDomainModel(model.getLongName(), model.getVersion());
            } catch (RemoteException e1) {
                String error = "Unable to get model from caDSR for project";
                logger.error(error, e1);
                result.setErrorMessage(error);
                result.setStackTrace(Utility.getStackTrace(e1));
                result.setLoaded(false);

            }

            if (domainModel != null) {
                DomainModelParser parser = new DomainModelParser(domainModel);
                Connection connection = ConnectionUtil.getConnection();
                try {
                    PathBuilder.loadSingleModelFromParserObject(connection, parser, model.getLongName(),
                                                                PATH_MAX_LENGTH);
                } catch (Throwable e) {
                    /*TODO to return meaning message to UI in all cases. 
                     Catching  Throwable to avoid showing direct stacktrace to user. */
                    String error = "Unable to load model into MDR";
                    logger.error(error, e);
                    result.setErrorMessage(error);
                    result.setStackTrace(Utility.getStackTrace(e));
                    result.setLoaded(false);
                } finally {
                    ConnectionUtil.close(connection);
                }
            } else { //no error but somehow the model is NULL
                String error = "Error fetching model from index service";
                logger.error(error, null);
                result.setErrorMessage(error);
                result.setLoaded(false);
            }
            map.put(model.getLongName(), result);
        }
      //  edu.wustl.cab2b.admin.util.Utility.refreshCab2bCache(true);
        Connection connection = ConnectionUtil.getConnection();
        PathFinder.refreshCache(connection, true);
        return map;
    }

    Collection<EntityGroupInterface> getCab2bEntityGroups() {
        return EntityCache.getInstance().getEntityGroups();
    }
}
