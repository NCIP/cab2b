/**
 * 
 */
package edu.wustl.cab2b.server.modelgroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.modelgroup.ModelGroup;
import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.cab2b.server.cache.EntityCache;

/**
 * @author gaurav_mehta
 *
 */
public class ModelGroupOperationsTest extends TestCase {

    EntityCache entityCache = EntityCache.getInstance();

    ModelGroupInterface modelGroupInstance = new ModelGroup();

    public void testGetAllModelGroups() {
        Collection<ModelGroupInterface> allModelGroup = new ModelGroupOperations().getAllModelGroups();
        if(allModelGroup.size()!=0) {
            for(ModelGroupInterface modelGroup: allModelGroup) {
                System.out.println(modelGroup.getModelGroupName());
            }
        }
    }

    public void testDeleteModelGroup() {
        Collection<ModelGroupInterface> allModelGroup = new ModelGroupOperations().getAllModelGroups();
        if(allModelGroup.size()!=0) {
            ModelGroupOperations modelGroupOperations = new ModelGroupOperations();
            for(ModelGroupInterface modelGroup: allModelGroup) {
                modelGroupOperations.deleteModelGroup(modelGroup);
            }
        }
        System.out.println("All Model Groups Deleted");
    }

    public void testSaveModelGroup() {

        List<EntityGroupInterface> entitygroupList = new ArrayList<EntityGroupInterface>();
        entitygroupList.addAll(entityCache.getEntityGroups());

        modelGroupInstance.setModelGroupName("TestModelGroup");
        modelGroupInstance.setSecured(false);
        modelGroupInstance.setEntityGroupList(entitygroupList);

        boolean value = new ModelGroupOperations().saveModelGroup(modelGroupInstance);
        assertEquals(true, value);
        System.out.println("Model Group Created");
        testGetAllModelGroups();
    }

    public void testGetAllSecuredModelGroups() {

        Collection<ModelGroupInterface> securedModelGroups = new ModelGroupOperations().getAllSecuredModelGroups();
        for (ModelGroupInterface modelGroup : securedModelGroups) {
            if (!modelGroup.isSecured()) {
                assertEquals(false, true);
            }
        }
    }

    public void testGetAllNonSecuredModelGroups() {

        Collection<ModelGroupInterface> nonSecuredModelGroups = new ModelGroupOperations().getAllNonSecuredModelGroups();
        for (ModelGroupInterface modelGroup : nonSecuredModelGroups) {
            if (modelGroup.isSecured()) {
                assertEquals(false, true);
            }
        }
    }
}