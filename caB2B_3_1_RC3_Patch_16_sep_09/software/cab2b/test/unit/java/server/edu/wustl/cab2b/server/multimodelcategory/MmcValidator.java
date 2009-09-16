package edu.wustl.cab2b.server.multimodelcategory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.path.PathFinder;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.dbManager.DBUtil;
import gov.nih.nci.cagrid.common.SchemaValidator;
/**
 * Utility to validate and write XML files for Multi model categories.
 * This is used to find required paths and validate the XML. It is not invoked during normal application execution. 
 * To use this update hibernate.cfg.xml with database information
 * Ensure hibernate.cfg.xml is present in classpath
 *      
 * @author chandrakant_talele
 */
public class MmcValidator {
    private PathFinder pathFinder = null;

    private EntityCache cache = null;

    /**
     * Main method to run this utility
     * @param args program arguments
     * @throws Exception Any exception thrown 
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Started....");
        
        SchemaValidator schemaValidator = new SchemaValidator(args[0]);
        schemaValidator.validate(new File(args[1]));
        System.out.println("XML Validation Finished against Schema "+ args[0]+".... Structure is correct");
        //redirectConsoleToFile("c:/dbdump/mmc_paths1.txt");
        new MmcValidator().validateXML(args[1]);
        new MmcValidator().getPathsForPair();
        System.out.println("Finished....");
    }

    /**
     * Gets paths
     */
    public void getPathsForPair() {
        List<ClassPair> pairs = new ArrayList<ClassPair>();
        pairs.add(new ClassPair("edu.wustl.catissuecore.domain.Participant", "edu.wustl.catissuecore.domain.Race"));
        pairs.add(new ClassPair("edu.wustl.catissuecore.domain.Participant", "edu.wustl.catissuecore.domain.SpecimenCollectionGroup"));
        /*
        pairs.add(new ClassPair("edu.wustl.catissuecore.domain.TissueSpecimen","edu.wustl.catissuecore.domain.EmbeddedEventParameters"));
        pairs.add(new ClassPair("edu.wustl.catissuecore.domain.TissueSpecimen","edu.wustl.catissuecore.domain.FixedEventParameters"));
        pairs.add(new ClassPair("edu.wustl.catissuecore.domain.TissueSpecimen","edu.wustl.catissuecore.domain.FrozenEventParameters"));
        pairs.add(new ClassPair("edu.wustl.catissuecore.domain.MolecularSpecimen","edu.wustl.catissuecore.domain.FrozenEventParameters"));
        pairs.add(new ClassPair("edu.wustl.catissuecore.domain.CellSpecimen","edu.wustl.catissuecore.domain.FrozenEventParameters"));
        pairs.add(new ClassPair("edu.wustl.catissuecore.domain.CellSpecimen","edu.wustl.catissuecore.domain.FixedEventParameters"));
        pairs.add(new ClassPair("edu.wustl.catissuecore.domain.FluidSpecimen","edu.wustl.catissuecore.domain.FrozenEventParameters"));
        pairs.add(new ClassPair("edu.wustl.catissuecore.domain.SpecimenCollectionGroup","edu.wustl.catissuecore.domain.TissueSpecimen"));
        pairs.add(new ClassPair("edu.wustl.catissuecore.domain.SpecimenCollectionGroup","edu.wustl.catissuecore.domain.MolecularSpecimen"));
        pairs.add(new ClassPair("edu.wustl.catissuecore.domain.SpecimenCollectionGroup","edu.wustl.catissuecore.domain.CellSpecimen"));
        pairs.add(new ClassPair("edu.wustl.catissuecore.domain.SpecimenCollectionGroup", "edu.wustl.catissuecore.domain.FluidSpecimen"));
        pairs.add(new ClassPair("edu.wustl.catissuecore.domain.CollectionProtocolRegistration", "edu.wustl.catissuecore.domain.User"));
        pairs.add(new ClassPair("edu.wustl.catissuecore.domain.CollectionProtocolRegistration", "edu.wustl.catissuecore.domain.CollectionProtocol"));
        pairs.add(new ClassPair("edu.wustl.catissuecore.domain.CollectionProtocol", "edu.wustl.catissuecore.domain.User"));
        pairs.add(new ClassPair("edu.wustl.catissuecore.domain.TissueSpecimen", "edu.wustl.catissuecore.domain.QuantityInMicrogram"));
        pairs.add(new ClassPair("edu.wustl.catissuecore.domain.Participant", "edu.wustl.catissuecore.domain.CollectionProtocolRegistration"));
        pairs.add(new ClassPair("edu.wustl.catissuecore.domain.TissueSpecimen", "edu.wustl.catissuecore.domain.SpecimenCharacteristics"));
        pairs.add(new ClassPair("edu.wustl.catissuecore.domain.MolecularSpecimen", "edu.wustl.catissuecore.domain.SpecimenCharacteristics"));
        pairs.add(new ClassPair("edu.wustl.catissuecore.domain.CellSpecimen", "edu.wustl.catissuecore.domain.SpecimenCharacteristics"));
        pairs.add(new ClassPair("edu.wustl.catissuecore.domain.FluidSpecimen", "edu.wustl.catissuecore.domain.SpecimenCharacteristics"));
        pairs.add(new ClassPair("gov.nih.nci.caarray.domain.sample.Sample", "gov.nih.nci.caarray.domain.hybridization.Hybridization"));
        pairs.add(new ClassPair("gov.nih.nci.caarray.domain.hybridization.Hybridization", "gov.nih.nci.caarray.domain.data.RawArrayData"));
        pairs.add(new ClassPair("gov.nih.nci.caarray.domain.data.RawArrayData", "gov.nih.nci.caarray.domain.project.Experiment"));
        pairs.add(new ClassPair("gov.nih.nci.caarray.domain.project.Experimen", "gov.nih.nci.caarray.domain.sample.Source"));
        pairs.add(new ClassPair("gov.nih.nci.caarray.domain.sample.Source", "gov.nih.nci.caarray.domain.vocabulary.Term"));
        pairs.add(new ClassPair("gov.nih.nci.caarray.domain.project.Experiment", "edu.georgetown.pir.Organism"));
        pairs.add(new ClassPair("gov.nih.nci.caarray.domain.project.Experimen", "gov.nih.nci.caarray.domain.array.ArrayDesign"));
        pairs.add(new ClassPair("gov.nih.nci.caarray.domain.array.ArrayDesign", "gov.nih.nci.caarray.domain.contact.Organization"));
        pairs.add(new ClassPair("gov.nih.nci.caarray.domain.project.Experiment", "gov.nih.nci.caarray.domain.sample.Source"));
        pairs.add(new ClassPair("gov.nih.nci.caarray.domain.sample.Source", "gov.nih.nci.caarray.domain.vocabulary.Term"));
        pairs.add(new ClassPair("gov.nih.nci.caarray.domain.project.Experiment", "gov.nih.nci.caarray.domain.array.ArrayDesign"));
*/
        Collection<EntityGroupInterface> groups = cache.getEntityGroups();
        for (EntityGroupInterface eg : groups) {
            for (ClassPair pair : pairs) {
                getPaths(eg, pair);
            }
        }
    }

    /**
     * Validates attribute entities present in given MMC XML file. 
     * Also prints paths which are used to build graph to ensure classes are connected properly  
     * @param fileName MMC XML 
     * @throws Exception Any exception thrown 
     */
    private void validateXML(String fileName) throws Exception {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(new File(fileName));

        NodeList nodes = doc.getElementsByTagName("Attribute");

        Set<String> classNames = new HashSet<String>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            NamedNodeMap attrs = node.getAttributes();
            String attributeName = attrs.getNamedItem("name").getNodeValue();
            String modelName = attrs.getNamedItem("model").getNodeValue();
            String className = attrs.getNamedItem("class").getNodeValue();
            classNames.add(modelName + ":" + getName(className));
            validate(modelName, className, attributeName);
        }
        nodes = doc.getElementsByTagName("Path");
        List<Long> pathIds = new ArrayList<Long>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            NamedNodeMap paths = node.getAttributes();
            String pathId = paths.getNamedItem("id").getNodeValue();
            pathIds.add(Long.parseLong(pathId));
        }
        System.out.println("Classes involved : " + classNames);
        for (Long id : pathIds) {
            System.out.println("Finding path id=" + id);
            IPath path = pathFinder.getPathById(id);
            print(path);
        }

    }

    private void print(IPath path) {
        String src = path.getSourceEntity().getName();
        src = getName(src) +"(" +path.getSourceEntity().getEntityGroupCollection().iterator().next().getLongName() + ")";

        String target = path.getTargetEntity().getName();
        target = getName(target)+"(" +path.getTargetEntity().getEntityGroupCollection().iterator().next().getLongName() + ")";
        System.out.println(src + "->" + target);
    }

    private String getName(String fullName) {
        return fullName.substring(fullName.lastIndexOf('.') + 1, fullName.length());
    }

    private boolean validate(String model, String clazz, String attribute) {
        EntityGroupInterface eg = cache.getEntityGroupByName(model);
        if (eg == null) {
            System.out.println("Incorrect EntityGroup name : " + model);
            return false;
        }

        EntityInterface en = eg.getEntityByName(clazz);
        if (en == null) {
            System.out.println("Incorrect Entity name : " + clazz);
            return false;
        }
        for (AttributeInterface a : en.getAllAttributes()) {
            if (a.getName().equals(attribute)) {
                return true;
            }
        }
        System.out.println("Incorrect Attribute name : " + attribute);
        return false;
    }

    private void getPaths(EntityGroupInterface eg, ClassPair pair) {
        EntityInterface sourceClass = null;
        EntityInterface targetClass = null;

        for (EntityInterface en : eg.getEntityCollection()) {
            String name = en.getName();
            if (pair.getSource().equals(name)) {
                sourceClass = en;
            } else if (pair.getTarget().equals(name)) {
                targetClass = en;
            }
        }
        if (sourceClass == null || targetClass == null) {
            //System.out.println("\n====================================\n Null Source OR Target. ");
            return;
        }
        List<IPath> paths = new ArrayList<IPath>(0);
        System.out.println("--------------------------------------------------");
        System.out.println("Searching model : " + eg.getName());
        paths = pathFinder.getAllPossiblePaths(sourceClass, targetClass);
        for (IPath path : paths) {
            System.out.println("Path id : " + path.getPathId()+ '\t' + getFullPathNames(path));
        }
        System.out.println("--------------------------------------------------");
    }

    private String getFullPathNames(IPath path) {
        StringBuffer buff = new StringBuffer();
        String roleName;
        List<IAssociation> associationList = path.getIntermediateAssociations();
        boolean isFirstAssociation = true;
        //buff.append("Path id : ").append(path.getPathId()).append('\t');
        for (IAssociation association : associationList) {
            roleName = edu.wustl.cab2b.client.ui.query.Utility.getRoleName(association);
            if (isFirstAssociation) {
                EntityInterface srcEntity = association.getSourceEntity();
                EntityInterface tarEntity = association.getTargetEntity();
                String srcEntityName = Utility.parseClassName(srcEntity.getName());
                String tarEntityName = Utility.parseClassName(tarEntity.getName());
                isFirstAssociation = false;
                buff.append(srcEntityName + " -> (" + roleName + ") -> " + tarEntityName);
            } else {
                EntityInterface tarEntity = association.getTargetEntity();
                String tarEntityName = Utility.parseClassName(tarEntity.getName());
                buff.append(" -> (" + roleName + ") -> " + tarEntityName);
            }
        }
        return buff.toString();
    }

    /**
     * private constructor
     */
    private MmcValidator() {
        pathFinder = PathFinder.getInstance(DBUtil.getConnection());
        cache = EntityCache.getInstance();
    }
    private static void redirectConsoleToFile(String consoleFile) throws FileNotFoundException {
        System.out.println("Redirecting console to " + consoleFile);
        PrintStream p = new PrintStream(new File(consoleFile));
        System.setOut(p);
        System.setErr(p);

    }
}

/**
 * Class to represent source and target
 * @author chandrakant_talele
 */
class ClassPair {
    private String sourceClassName;

    private String targetClassName;

    public ClassPair(String source, String target) {
        sourceClassName = source;
        targetClassName = target;
    }

    public String getSource() {
        return sourceClassName;
    }

    public String getTarget() {
        return targetClassName;
    }
}