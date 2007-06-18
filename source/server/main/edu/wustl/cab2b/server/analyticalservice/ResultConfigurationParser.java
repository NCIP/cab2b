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
 * This is a singleton class which parses the ResultConfiguration.xml file and
 * stores the mapping information into an internal map.
 * 
 * @author Deepak Shingan
 * 
 */
public class ResultConfigurationParser {

    /**
     * Self reference
     */
    private static ResultConfigurationParser resultConfigurationMapper = null;

    /**
     * Map to store the application_entity to attribute object mapping KEY for
     * map = ApplicationName_EntityName
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

    /**
     * Default tag
     */

    private static final String DEFAULT = "default";

    /**
     * Name tag
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
    private static final String DATALIST_TRANSFORMERS = "data-list-transformers";

    private static final String DATALIST_SAVER = "saver";

    private static final String DATALIST_RETRIEVER = "retriever";

    private static final String DEFAULT_ELEMENT_KEY = null;

    private static final String KEY_DELIM = ":";
    /**
     * Constructor
     */

    private ResultConfigurationParser() {
        parseResultConfigurationMapperXMLFile();
    }

    /**
     * This method returns an instance of this class
     * 
     * @return an instance of this class
     */
    public static synchronized ResultConfigurationParser getInstance() {
        if (resultConfigurationMapper == null) {
            resultConfigurationMapper = new ResultConfigurationParser();
        }
        return resultConfigurationMapper;
    }

    /**
     * This method parses the ResultConfiguration.XML file and stores the parsed
     * data into an internally maintained Maps.
     */
    private void parseResultConfigurationMapperXMLFile() {
        // Read the xml file
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(
                                                                                       RESULT_CONFIGURATION_MAPPER_FILENAME);
        if (inputStream == null) {
            throw new RuntimeException("File not found: " + RESULT_CONFIGURATION_MAPPER_FILENAME);
        }

        // Parse xml into the Document
        Document document = null;
        try {
            document = new SAXReader().read(inputStream);
        } catch (DocumentException e) {
            throw new RuntimeException("Unable to parse XML file: " + RESULT_CONFIGURATION_MAPPER_FILENAME, e);
        }

        // Traverse and fetch the data from the Document
        Element applicationsElement = document.getRootElement();
        if (applicationsElement != null) {
            List<Element> applicationElementList = applicationsElement.elements(APPLICATION);
            if (applicationElementList == null || applicationElementList.isEmpty()) {
                throw new RuntimeException("Invalid XML file: Application entries not found.");
            }
            registerDefaultElement(applicationsElement, DEFAULT_ELEMENT_KEY);
            registerApplicationElements(applicationElementList);
        } else {
            throw new RuntimeException("Invalid XML file: Root element not found.");
        }
    }

    /**
     * Method to parse Attribute values under entity tag and store them in map
     */

    private void setElementData(List<Element> elementList, String mapKey) {
        for (Element entityElement : elementList) {

            if (entityElement.attributeValue(NAME) != null) {
                mapKey = mapKey + KEY_DELIM + entityElement.attributeValue(NAME);
            }

            Element resultTransformer = entityElement.element(RESULT_TRANSFORMER);
            Element resultRenderer = entityElement.element(RESULT_RENDERER);
            Element dataListTransformer = entityElement.element(DATALIST_TRANSFORMERS);

            String resultTransformerValue = null;
            String resultRendererValue = null;

            if (resultTransformer != null) {
                resultTransformerValue = resultTransformer.getTextTrim();
            }

            if (resultRenderer != null) {
                resultRendererValue = resultRenderer.getTextTrim();
            }

            DataListTransformer dataListTransformerValue = null;
            if (dataListTransformer != null) {
                Element saverElem = dataListTransformer.element(DATALIST_SAVER);
                Element retrieverElem = dataListTransformer.element(DATALIST_RETRIEVER);

                dataListTransformerValue = new DataListTransformer(saverElem.getTextTrim(),
                        retrieverElem.getTextTrim());
            }

            EntityTransformerInfo entityAttributes = new EntityTransformerInfo(resultTransformerValue,
                    resultRendererValue, dataListTransformerValue);

            applicationEntityNameMap.put(mapKey, entityAttributes);

        }
    }

    /**
     * This method stores the data of all the application tags into
     * applicationEntityNameMap
     * 
     * @param applicationElementList the root element of the XML document
     */
    private void registerApplicationElements(List<Element> applicationElementList) {
        for (Element applicationElement : applicationElementList) {
            List<Element> entityElementList = applicationElement.elements(ENTITY);
            String mapKey = applicationElement.attributeValue(NAME);
            setElementData(entityElementList, mapKey);

            registerDefaultElement(applicationElement, mapKey);
        }
    }

    private void registerDefaultElement(Element parentElem, String mapKey) {
        List<Element> defaultElementList = parentElem.elements(DEFAULT);
        if (defaultElementList.size() > 1) {
            throw new IllegalArgumentException("only one default permitted.");
        }
        setElementData(defaultElementList, mapKey);
    }

    /**
     * Method to get default DataListTransformer value for given application
     * name
     * 
     * @param String applicationName
     */

    private DataListTransformer getDataListTransformer(String applicationName) {
        if (applicationEntityNameMap.get(applicationName) != null) {
            return applicationEntityNameMap.get(applicationName).getDataListTransformer();
        }
        return null;
    }

    /**
     * Method to get default ResultRenderer value for given application name
     * 
     * @param String applicationName
     */
    private String getResultRenderer(String applicationName) {
        if (applicationEntityNameMap.get(applicationName) != null) {
            return applicationEntityNameMap.get(applicationName).getResultRenderer();
        }
        return null;
    }

    /**
     * Method to get default ResultTransformer value for given application name
     * 
     * @param String applicationName
     */
    private String getResultTransformer(String applicationName) {
        if (applicationEntityNameMap.get(applicationName) != null) {
            return applicationEntityNameMap.get(applicationName).getResultTransformer();
        }
        return null;
    }

    /**
     * Method to get ResultRenderer value for given application name and
     * entityName
     * 
     * @param String applicationName
     * @param String entityName
     */

    public String getResultRenderer(String applicationName, String entityName) {

        if (applicationEntityNameMap.get(applicationName + KEY_DELIM + entityName) != null)
            return applicationEntityNameMap.get(applicationName + KEY_DELIM + entityName).getResultRenderer();

        String res = getResultRenderer(applicationName);
        if (res == null) {
            res = getDefault().getResultRenderer();
        }
        return res;
    }

    /**
     * Method to get ResultTransformer value for given application name and
     * entityName
     * 
     * @param applicationName
     * @param entityName
     * 
     */
    public String getResultTransformer(String applicationName, String entityName) {

        if (applicationEntityNameMap.get(applicationName + KEY_DELIM + entityName) != null)
            return applicationEntityNameMap.get(applicationName + KEY_DELIM + entityName).getResultTransformer();
        String res = getResultTransformer(applicationName);
        if (res == null) {
            res = getDefault().getResultTransformer();
        }
        return res;

    }

    /**
     * Method to get DataListTransformer value for given application name and
     * entityName
     * 
     * @param applicationName
     * @param entityName
     * 
     */
    private DataListTransformer getDataListTransformer(String applicationName, String entityName) {
        if (applicationEntityNameMap.get(applicationName + KEY_DELIM + entityName) != null)
            return applicationEntityNameMap.get(applicationName + KEY_DELIM + entityName).getDataListTransformer();
        DataListTransformer res = getDataListTransformer(applicationName);
        if (res == null) {
            res = getDefault().getDataListTransformer();
        }
        return res;
    }

    public String getDataListSaver(String applicationName, String entityName) {
        return getDataListTransformer(applicationName, entityName).getSaver();
    }

    public String getDataListRetriever(String applicationName, String entityName) {
        return getDataListTransformer(applicationName, entityName).getRetriever();
    }

    private EntityTransformerInfo getDefault() {
        return applicationEntityNameMap.get(DEFAULT_ELEMENT_KEY);
    }

    private class DataListTransformer {
        String saver;

        String retriever;

        public DataListTransformer(String saver, String retriever) {
            this.saver = saver;
            this.retriever = retriever;
        }

        public String getRetriever() {
            return retriever;
        }

        public String getSaver() {
            return saver;
        }

    }

    /**
     * Class to store parsed resultTransformer, resultRenderer and
     * dataListTransformer values
     * 
     * @author Deepak_Shingan
     * 
     */
    private class EntityTransformerInfo {

        String resultTransformer;

        String resultRenderer;

        DataListTransformer dataListTransformer;

        EntityTransformerInfo(
                String resultTransformer,
                String resultRenderer,
                DataListTransformer dataListTransformer) {
            this.resultTransformer = resultTransformer;
            this.resultRenderer = resultRenderer;
            this.dataListTransformer = dataListTransformer;
        }

        /**
         * Method to get datalistTransfarmer value
         * 
         * @return String dataListTransformer
         */
        public DataListTransformer getDataListTransformer() {
            return dataListTransformer;
        }

        /**
         * Method to get ResultRenderer value
         * 
         * @return String resultRenderer
         */
        public String getResultRenderer() {
            return resultRenderer;
        }

        /**
         * Method to get ResultTransformer value
         * 
         * @return String ResultTransformer
         */
        public String getResultTransformer() {
            return resultTransformer;
        }
    }

    public static void main(String[] args) {
        Logger.configure();
        ResultConfigurationParser entityServiceMapper = ResultConfigurationParser.getInstance();
        Logger.out.info(entityServiceMapper.getDataListSaver("CategoryEntityGroup", "foobar"));
        Logger.out.info(entityServiceMapper.getResultRenderer("caArray","gov.nih.nci.mageom.domain.BioAssayData.DerivedBioAssayData"));
        Logger.out.info(entityServiceMapper.getResultRenderer("caArray", "foobar"));
        Logger.out.info(entityServiceMapper.getResultRenderer(null, null));
        Logger.out.info(entityServiceMapper.getDataListRetriever("foo", "bar"));
        Logger.out.info(entityServiceMapper.getDataListRetriever(null, null));
    }
}
