/**
 * 
 */
package edu.wustl.cab2b.server.analyticalservice;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.common.util.logger.Logger;

/**
 * This is a singleton class which parses the ResultConfiguration.xml file 
 * and stores the mapping information into an internal map.
 * @author Deepak Shingan
 *
 */
public class ResultConfigurationParser {

    /**
     * Self reference
     */
    private static ResultConfigurationParser resultConfigurationMapper = null;

    /**
     * Map to store the application_entity to attribute object mapping
     * KEY for map = ApplicationName_EntityName 
     */

    private Map<String, EntityTransformerInfo> applicationEntityNameMap = new HashMap<String, EntityTransformerInfo>();

    /**
     * Name of the Entity Service Mapper file
     */
    private static final String RESULT_CONFIGURATION_MAPPER_FILENAME = "ResultConfiguration.xml";

    /**
     * Application tag
     */
    private static final String APPLICATION = "application";

    /**
     * Entity tag
     */
    private static final String ENTITY = "entity";

    private static final String DEFAULT = "default";

    /**
     * Entity tag
     */
    private static final String NAME = "name";

    /**
     * Result Transfarmer tag
     */
    private static final String RESULT_TRANSFORMER = "result-transformer";

    /**
     * Result Renderer tag
     */
    private static final String RESULT_RENDERER = "result-renderer";

    /**
     * Datalist Transformer tag
     */
    private static final String DATALIST_TRANSFORMER = "data-list-transformer";

    private ResultConfigurationParser() {
        parseResultConfigurationMapperXMLFile();
    }

    /**
     * This method returns an instance of this class
     * @return an instance of this class
     */
    public static synchronized ResultConfigurationParser getInstance() {
        if (resultConfigurationMapper == null) {
            resultConfigurationMapper = new ResultConfigurationParser();
        }
        return resultConfigurationMapper;
    }

    /**
     * This method parses the ResultConfiguration.XML file and stores the parsed data into an internally maintained Maps.
     */
    private void parseResultConfigurationMapperXMLFile() {
        //Read the xml file
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(
                                                                                       RESULT_CONFIGURATION_MAPPER_FILENAME);
        if (inputStream == null) {
            throw new RuntimeException("File not found: " + RESULT_CONFIGURATION_MAPPER_FILENAME);
        }

        //Parse xml into the Document
        Document document = null;
        try {
            document = new SAXReader().read(inputStream);
        } catch (DocumentException e) {
            throw new RuntimeException("Unable to parse XML file: " + RESULT_CONFIGURATION_MAPPER_FILENAME, e);
        }

        //Traverse and fetch the data from the Document
        Element applicationsElement = document.getRootElement();
        if (applicationsElement != null) {
            List<Element> applicationElementList = applicationsElement.elements(APPLICATION);
            if (applicationElementList == null || applicationElementList.isEmpty()) {
                throw new RuntimeException("Invalid XML file: Application entries not found.");
            }
            registerApplicationElements(applicationElementList);
        } else {
            throw new RuntimeException("Invalid XML file: Root element not found.");
        }
    }

    private void setElementData(List<Element> elementList, String mapKey) {
        for (Element entityElement : elementList) {

            if (entityElement.attributeValue(NAME) != null) {
                mapKey = mapKey + "_" + entityElement.attributeValue(NAME);
            }

            Element resultTransformer = entityElement.element(RESULT_TRANSFORMER);
            Element resultRenderer = entityElement.element(RESULT_RENDERER);
            Element dataListTransformer = entityElement.element(DATALIST_TRANSFORMER);

            String resultTransformerValue = null;
            String resultRendererValue = null;
            String dataListTransformerValue = null;

            if (resultTransformer != null) {
                resultTransformerValue = resultTransformer.getTextTrim();
            }

            if (resultRenderer != null) {
                resultRendererValue = resultRenderer.getTextTrim();
            }

            if (dataListTransformer != null) {
                dataListTransformerValue = dataListTransformer.getTextTrim();
            }

            EntityTransformerInfo entityAttributes = new EntityTransformerInfo(resultTransformerValue,
                    resultRendererValue, dataListTransformerValue);

            applicationEntityNameMap.put(mapKey, entityAttributes);

        }
    }

    /**
     * This method stores the data of all the application tags into applicationEntityNameMap
     * @param applicationElementList the root element of the XML document
     */
    private void registerApplicationElements(List<Element> applicationElementList) {
        for (Element applicationElement : applicationElementList) {

            List<Element> entityElementList = applicationElement.elements(ENTITY);
            String mapKey = applicationElement.attributeValue(NAME);
            setElementData(entityElementList, mapKey);

            List<Element> defaultElementList = applicationElement.elements(DEFAULT);
            setElementData(defaultElementList, mapKey);
        }
    }



    public String getDataListTransformer(String applicationName) {
        if (applicationEntityNameMap.get(applicationName) != null) {
            return applicationEntityNameMap.get(applicationName).getDataListTransformer();
        }
        return null;
    }

    public String getResultRenderer(String applicationName) {
        if (applicationEntityNameMap.get(applicationName) != null) {
            return applicationEntityNameMap.get(applicationName).getResultRenderer();
        }
        return null;
    }

    public String getResultTransformer(String applicationName) {
        if (applicationEntityNameMap.get(applicationName) != null) {
            return applicationEntityNameMap.get(applicationName).getResultTransformer();
        }
        return null;
    }

    public String getResultRenderer(String applicationName, String entiyName) {

        if (applicationEntityNameMap.get(applicationName + "_" + entiyName) != null)
            return applicationEntityNameMap.get(applicationName + "_" + entiyName).getResultRenderer();
        else
            return getResultRenderer(applicationName);

    }

    public String getResultTransformer(String applicationName, String entiyName) {

        if (applicationEntityNameMap.get(applicationName + "_" + entiyName) != null)
            return applicationEntityNameMap.get(applicationName + "_" + entiyName).getResultTransformer();
        else
            return getResultTransformer(applicationName);

    }
    
    public String getDataListTransformer(String applicationName, String entiyName) {
        if (applicationEntityNameMap.get(applicationName + "_" + entiyName) != null)
            return applicationEntityNameMap.get(applicationName + "_" + entiyName).getDataListTransformer();
        else
            return getDataListTransformer(applicationName);
    }

    private class EntityTransformerInfo {

        String resultTransformer;

        String resultRenderer;

        String dataListTransformer;

        EntityTransformerInfo(String resultTransformer, String resultRenderer, String dataListTransformer) {
            this.resultTransformer = resultTransformer;
            this.resultRenderer = resultRenderer;
            this.dataListTransformer = dataListTransformer;
        }

        public String getDataListTransformer() {
            return dataListTransformer;
        }

        public String getResultRenderer() {
            return resultRenderer;
        }

        public String getResultTransformer() {
            return resultTransformer;
        }
    }

    public static void main(String[] args) {

        Logger.configure();
        ResultConfigurationParser entityServiceMapper = ResultConfigurationParser.getInstance();
        Logger.out.info(entityServiceMapper.getDataListTransformer("category group"));
        Logger.out.info(entityServiceMapper.getResultRenderer("caArray_bioDataCube"));
        Logger.out.info(entityServiceMapper.getResultRenderer(null,null));
        Logger.out.info(entityServiceMapper.getResultRenderer("caArray"));
    }
}
