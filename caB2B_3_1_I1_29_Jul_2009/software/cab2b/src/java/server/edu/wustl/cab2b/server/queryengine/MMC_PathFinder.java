package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.path.PathFinder;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.dbManager.DBUtil;

public class MMC_PathFinder {

    private static List<EntityPair> pairs = new ArrayList<EntityPair>();

    private static PathFinder pathFinder = null;

    public static void main(String[] args) {
        //pairs.add(new EntityPair("edu.wustl.catissuecore.domain.Participant","edu.wustl.catissuecore.domain.SpecimenCollectionGroup"));
        //pairs.add(new EntityPair("edu.wustl.catissuecore.domain.SpecimenCollectionGroup","edu.wustl.catissuecore.domain.MolecularSpecimen"));
        //pairs.add(new EntityPair("edu.wustl.catissuecore.domain.MolecularSpecimen","edu.wustl.catissuecore.domain.SpecimenCharacteristics"));
        //pairs.add(new EntityPair("edu.wustl.catissuecore.domain.SpecimenCollectionGroup","edu.wustl.catissuecore.domain.TissueSpecimen"));
        //pairs.add(new EntityPair("edu.wustl.catissuecore.domain.TissueSpecimen","edu.wustl.catissuecore.domain.SpecimenCharacteristics"));
        //pairs.add(new EntityPair("edu.wustl.catissuecore.domain.SpecimenCollectionGroup","edu.wustl.catissuecore.domain.CellSpecimen"));
        //pairs.add(new EntityPair("edu.wustl.catissuecore.domain.CellSpecimen","edu.wustl.catissuecore.domain.SpecimenCharacteristics"));
        //pairs.add(new EntityPair("edu.wustl.catissuecore.domain.SpecimenCollectionGroup","edu.wustl.catissuecore.domain.FluidSpecimen"));
        //pairs.add(new EntityPair("edu.wustl.catissuecore.domain.FluidSpecimen","edu.wustl.catissuecore.domain.SpecimenCharacteristics"));
        //pairs.add(new EntityPair("gov.nih.nci.caarray.domain.project.Experiment","gov.nih.nci.caarray.domain.vocabulary.Term"));
        //pairs.add(new EntityPair("edu.wustl.catissuecore.domain.Participant", "edu.wustl.catissuecore.domain.Race"));
        pairs.add(new EntityPair("gov.nih.nci.caarray.domain.hybridization.Hybridization", "gov.nih.nci.caarray.domain.data.RawArrayData"));

        pathFinder = PathFinder.getInstance(DBUtil.getConnection());
        EntityCache cache = EntityCache.getInstance();
        Collection<EntityGroupInterface> groups = cache.getEntityGroups();
        for (EntityGroupInterface eg : groups) {
            for (EntityPair pair : pairs) {
                getPaths(eg, pair);
            }
        }
    }

    private static void getPaths(EntityGroupInterface eg, EntityPair pair) {
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
            System.out.println(getFullPathNames(path));
        }
        System.out.println("--------------------------------------------------");
    }

    private static String getFullPathNames(IPath path) {
        StringBuffer buff = new StringBuffer();
        String roleName;
        List<IAssociation> associationList = path.getIntermediateAssociations();
        boolean isFirstAssociation = true;
        buff.append("Path id : ").append(path.getPathId()).append('\t');
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
}

class EntityPair {
    private String sourceClassName;

    private String targetClassName;

    public EntityPair(String source, String target) {
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
