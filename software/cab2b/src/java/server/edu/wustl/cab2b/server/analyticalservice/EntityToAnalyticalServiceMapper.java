/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.analyticalservice;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.analyticalservice.ServiceDetailsInterface;
import edu.wustl.cab2b.common.exception.RuntimeException;

/**
 * This is a singleton class which parses the EntityToAnalyticalServiceMapping.xml file and stores the mapping information into an internal map.
 * This class provieds the methods to get the service interface and the service invoker interface.
 * @author chetan_patil
 */
public class EntityToAnalyticalServiceMapper {

    /**
     * Self reference
     */
    private static EntityToAnalyticalServiceMapper entityServiceMapper = null;

    /**
     * Map to store the entity to service mapping
     */
    private Map<String, List<String>> entityServiceNameMap = new HashMap<String, List<String>>();

    /**
     * Map to store the service to method mapping
     */
    private Map<String, List<String>> serviceNameDetailClassNameMap = new HashMap<String, List<String>>();

    /**
     * Map to store the service detail class and the corresponding service invoker class.
     */
    private Map<String, String> serviceDetailInvokerMap = new HashMap<String, String>();

    /**
     * Name of the Entity Service Mapper file
     */
    private static final String ENTITY_SERVICE_MAPPER_FILENAME = "EntityToAnalyticalServiceMapping.xml";

    /**
     * Entity tag
     */
    private static final String ENTITY = "entity";

    /**
     * Service tag
     */
    private static final String SERVICE = "service";

    /**
     * Method tag
     */
    private static final String METHOD = "method";

    /**
     * Name attribute
     */
    private static final String ATTRIBUTE_NAME = "name";

    /**
     * Service Detail Class attribute
     */
    private static final String ATTRIBUTE_SERVICE_DETAIL_CLASS = "serviceDetailClass";

    /**
     * Service Invoker Class attribute
     */
    private static final String ATTRIBUTE_SERVICE_INVOKER_CLASS = "serviceInvokerClass";

    /**
     * Service name attribute
     */
    private static final String ATTRIBUTE_SERVICE_NAME = "serviceName";

    /**
     * Private constructor
     */
    private EntityToAnalyticalServiceMapper() {
        parseEntityServiceMapperXMLFile();
    }

    /**
     * This method returns an instance of this class
     * @return an instance of this class
     */
    public static synchronized EntityToAnalyticalServiceMapper getInstance() {
        if (entityServiceMapper == null) {
            entityServiceMapper = new EntityToAnalyticalServiceMapper();
        }
        return entityServiceMapper;
    }

    /**
     * This method parses the EntityServiceMapper.XML file and stores the parsed data into an internally maintained Maps.
     */
    private void parseEntityServiceMapperXMLFile() {
        //Read the xml file
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(
                                                                                       ENTITY_SERVICE_MAPPER_FILENAME);
        if (inputStream == null) {
            throw new RuntimeException("File not found: " + ENTITY_SERVICE_MAPPER_FILENAME);
        }

        //Parse xml into the Document
        Document document = null;
        try {
            document = new SAXReader().read(inputStream);
        } catch (DocumentException e) {
            throw new RuntimeException("Unable to parse XML file: " + ENTITY_SERVICE_MAPPER_FILENAME, e);
        }

        //Traverse and fetch the data from the Document
        Element entityServiceMapperElement = document.getRootElement();
        if (entityServiceMapperElement != null) {
            List<Element> serviceElementList = entityServiceMapperElement.elements(SERVICE);
            if (serviceElementList == null || serviceElementList.isEmpty()) {
                throw new RuntimeException("Invalid XML file: Service entries not found.");
            }
            registerServiceElements(serviceElementList);

            List<Element> entityElementList = entityServiceMapperElement.elements(ENTITY);
            if (entityElementList == null || entityElementList.isEmpty()) {
                throw new RuntimeException("Invalid XML file: Entity entries not found.");
            }
            registerEntityElements(entityElementList);
        } else {
            throw new RuntimeException("Invalid XML file: Root element not found.");
        }
    }

    /**
     * This method stores the data of all the service tags into serviceMethodMap
     * @param serviceElementList the root element of the XML document
     */
    private void registerServiceElements(List<Element> serviceElementList) {
        for (Element serviceElement : serviceElementList) {
            List<String> serviceDetailClassList = new ArrayList<String>();
            List<Element> methodElementList = serviceElement.elements(METHOD);
            for (Element methodElement : methodElementList) {
                String serviceDetailClassName = methodElement.attributeValue(ATTRIBUTE_SERVICE_DETAIL_CLASS);
                String serviceInvokerClassName = methodElement.attributeValue(ATTRIBUTE_SERVICE_INVOKER_CLASS);

                if (!serviceDetailClassList.contains(serviceDetailClassName)) {
                    serviceDetailClassList.add(serviceDetailClassName);
                }

                if (serviceDetailInvokerMap.get(serviceDetailClassName) == null) {
                    serviceDetailInvokerMap.put(serviceDetailClassName, serviceInvokerClassName);
                }
            }

            String serviceName = serviceElement.attributeValue(ATTRIBUTE_NAME);
            if (serviceNameDetailClassNameMap.get(serviceName) == null) {
                serviceNameDetailClassNameMap.put(serviceName, serviceDetailClassList);
            }
        }
    }

    /**
     * This method stores the data of all the entity tags into entityServiceMap
     * @param entityElementList the root element of the XML document
     */
    private void registerEntityElements(List<Element> entityElementList) {
        for (Element entityElement : entityElementList) {
            String entityName = entityElement.attributeValue(ATTRIBUTE_NAME);
            String serviceName = entityElement.attributeValue(ATTRIBUTE_SERVICE_NAME);

            List<String> serviceList = entityServiceNameMap.get(entityName);
            if (serviceList == null) {
                serviceList = new ArrayList<String>();
                entityServiceNameMap.put(entityName, serviceList);
            }
            serviceList.add(serviceName);
        }
    }

    /**
     * This method returns the instance given the respective class name.
     * @param className name of the class
     * @return an instance of the given class name
     */
    private <E> E getInstance(String className, Class<E> clazz) {
        Object instance = null;
        try {
            Class classDefinition = Class.forName(className);
            instance = classDefinition.newInstance();
        } catch (InstantiationException e) {
             
        } catch (IllegalAccessException e) {
             
        } catch (ClassNotFoundException e) {
             
        }
        Class<?> instanceClass = instance.getClass();
        if (!clazz.isAssignableFrom(instanceClass)) {
            throw new RuntimeException(instanceClass.getName() + " does not implement the interface "
                    + clazz.getName());
        }
        return (E) instance;
    }

    /**
     * This method returns the List of the service names of the respective givne entity name.
     * @param entityName the name of the entity
     * @return the List of the service names
     */
    private List<String> getServiceDetailClassNames(String entityName) {
        List<String> serviceDetailClassList = new ArrayList<String>();

        List<String> serviceNameList = entityServiceNameMap.get(entityName);
        if (serviceNameList != null) {
            for (String serviceName : serviceNameList) {
                List<String> serviceDetailClassNameList = serviceNameDetailClassNameMap.get(serviceName);
                if (serviceDetailClassNameList != null) {
                    serviceDetailClassList.addAll(serviceDetailClassNameList);
                }
            }
        }

        return serviceDetailClassList;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    /**
     * Clones this object
     * @return Cloned object
     * @throws CloneNotSupportedException
     */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * This method returns the List of all the corresponding ServiceDetailsClass given the Entity.
     * @param entity an instance of Entity
     * @return List of all the ServiceDetailClass corresponding to the given Entity
     */
    public List<ServiceDetailsInterface> getServices(EntityInterface entity) {
        List<ServiceDetailsInterface> serviceDetailsInstanceList = new ArrayList<ServiceDetailsInterface>();

        List<String> serviceDetailClassList = getServiceDetailClassNames(entity.getName());
        //TODO This is a hack. To be deleted after testing.
        //List<String> serviceDetailClassList = getServiceDetailClassNames("gov.nih.nci.mageom.domain.BioAssay.BioAssay");
        for (String serviceDetailClassName : serviceDetailClassList) {
            ServiceDetailsInterface serviceDetails = getInstance(serviceDetailClassName,
                                                                 ServiceDetailsInterface.class);
            serviceDetailsInstanceList.add(serviceDetails);
        }
        return serviceDetailsInstanceList;
    }

    /**
     * This method returns an instance of the Service Invoker Class given the respective Service Details Class.
     * @param serviceDetails the instance of the Service Details class
     * @return an instance of the Service Invoker Class
     */
    public ServiceInvokerInterface getServiceInvoker(ServiceDetailsInterface serviceDetails) {
        String serviceDetailClassName = serviceDetails.getClass().getName();
        String serviceInvokerClassName = serviceDetailInvokerMap.get(serviceDetailClassName);
        ServiceInvokerInterface serviceInvoker = null;
        if (serviceInvokerClassName != null) {
            serviceInvoker = getInstance(serviceInvokerClassName, ServiceInvokerInterface.class);
        }
        return serviceInvoker;
    }

    //	public static void main(String[] args) {
    //		EntityInterface entity = new Entity();
    //		entity.setName("Entity1");
    //		
    //        EntityToAnalyticalServiceMapper entityServiceMapper = EntityToAnalyticalServiceMapper.getInstance();
    //		List<ServiceDetailsInterface> serviceList = entityServiceMapper.getServices(entity);
    //		ServiceInvokerInterface serviceInvoker1 = entityServiceMapper.getServiceInvoker(serviceList.get(0));
    //		ServiceInvokerInterface serviceInvoker2 = entityServiceMapper.getServiceInvoker(serviceList.get(1));
    //	}
}