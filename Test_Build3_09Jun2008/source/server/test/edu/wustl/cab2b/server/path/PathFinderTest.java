package edu.wustl.cab2b.server.path;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.server.util.ConnectionUtil;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.associations.impl.InterModelAssociation;
import edu.wustl.common.querysuite.metadata.path.CuratedPath;
import edu.wustl.common.querysuite.metadata.path.ICuratedPath;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.metadata.path.Path;
import edu.wustl.common.util.logger.Logger;

/**
 * @author Chandrakant Talele
 */
public class PathFinderTest extends TestCase {
    private static long counter = 0;

    public void testGetStringRepresentationEmptySet() {
        Set<EntityInterface> set = new HashSet<EntityInterface>();
        String str = new PathFinder().getStringRepresentation(set);
        assertEquals("", str);
    }

    public void testGetStringRepresentation() {
        Set<EntityInterface> set = new HashSet<EntityInterface>();
        DomainObjectFactory fact = DomainObjectFactory.getInstance();
        for (long i = 0; i < 3; i++) {
            EntityInterface e1 = fact.createEntity();
            e1.setId(i);
            set.add(e1);
        }
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(0);
        strBuilder.append(PathConstants.ID_CONNECTOR);
        strBuilder.append(1);
        strBuilder.append(PathConstants.ID_CONNECTOR);
        strBuilder.append(2);
        String str = new PathFinder().getStringRepresentation(set);
        assertEquals(strBuilder.toString(), str);
    }

    public void testGetStringRepresentationUnsorted() {
        Set<EntityInterface> set = new HashSet<EntityInterface>();
        DomainObjectFactory fact = DomainObjectFactory.getInstance();

        EntityInterface e0 = fact.createEntity();
        e0.setId(0L);
        set.add(e0);
        EntityInterface e2 = fact.createEntity();
        e2.setId(2L);
        set.add(e2);
        EntityInterface e1 = fact.createEntity();
        e1.setId(1L);
        set.add(e1);

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(0);
        strBuilder.append(PathConstants.ID_CONNECTOR);
        strBuilder.append(1);
        strBuilder.append(PathConstants.ID_CONNECTOR);
        strBuilder.append(2);
        String str = new PathFinder().getStringRepresentation(set);
        assertEquals(strBuilder.toString(), str);
    }

    public void testConnectPathsEmptyPreAndPostPaths() {
        List<Path> prePaths = new ArrayList<Path>();
        List<Path> postPaths = new ArrayList<Path>();
        InterModelAssociation association = getAssociation();
        PathFinder p = new PathFinder();
        List<IPath> list = p.connectPaths(prePaths, association, postPaths);
        assertEquals(1, list.size());
        assertEquals(1, list.get(0).getIntermediateAssociations().size());

        //to test cloing
        assertNotSame(association, list.get(0).getIntermediateAssociations().get(0));
        assertTrue(list.get(0).getIntermediateAssociations().get(0) instanceof InterModelAssociation);
        verify(association, list.get(0).getIntermediateAssociations().get(0));
    }

    public void testConnectPathsEmptyPrePaths() {
        List<Path> prePaths = new ArrayList<Path>();
        List<Path> postPaths = new ArrayList<Path>();

        InterModelAssociation p1Association = getAssociation();
        InterModelAssociation p2Association = getAssociation();

        postPaths.add(getPath(p1Association));
        postPaths.add(getPath(p2Association));

        InterModelAssociation association = getAssociation();
        PathFinder p = new PathFinder();
        List<IPath> list = p.connectPaths(prePaths, association, postPaths);
        assertEquals(2, list.size());

        assertEquals(2, list.get(0).getIntermediateAssociations().size());
        verify(association, list.get(0).getIntermediateAssociations().get(0));
        verify(p1Association, list.get(0).getIntermediateAssociations().get(1));

        assertEquals(2, list.get(1).getIntermediateAssociations().size());
        verify(association, list.get(1).getIntermediateAssociations().get(0));
        verify(p2Association, list.get(1).getIntermediateAssociations().get(1));
    }

    public void testConnectPathsEmptyPostPaths() {
        List<Path> prePaths = new ArrayList<Path>();
        List<Path> postPaths = new ArrayList<Path>();

        InterModelAssociation p1Association = getAssociation();
        InterModelAssociation p2Association = getAssociation();

        prePaths.add(getPath(p1Association));
        prePaths.add(getPath(p2Association));

        InterModelAssociation association = getAssociation();
        PathFinder p = new PathFinder();
        List<IPath> list = p.connectPaths(prePaths, association, postPaths);
        assertEquals(2, list.size());

        assertEquals(2, list.get(0).getIntermediateAssociations().size());
        verify(p1Association, list.get(0).getIntermediateAssociations().get(0));
        verify(association, list.get(0).getIntermediateAssociations().get(1));

        assertEquals(2, list.get(1).getIntermediateAssociations().size());
        verify(p2Association, list.get(1).getIntermediateAssociations().get(0));
        verify(association, list.get(1).getIntermediateAssociations().get(1));
    }

    public void testConnectPaths() {
        List<Path> prePaths = new ArrayList<Path>();
        List<Path> postPaths = new ArrayList<Path>();

        InterModelAssociation post1Association = getAssociation();
        InterModelAssociation post2Association = getAssociation();
        postPaths.add(getPath(post1Association));
        postPaths.add(getPath(post2Association));

        InterModelAssociation pre1Association = getAssociation();
        InterModelAssociation pre2Association = getAssociation();

        prePaths.add(getPath(pre1Association));
        prePaths.add(getPath(pre2Association));

        InterModelAssociation association = getAssociation();
        PathFinder p = new PathFinder();
        List<IPath> list = p.connectPaths(prePaths, association, postPaths);
        assertEquals(4, list.size());

        for (IPath path : list) {
            assertEquals(3, path.getIntermediateAssociations().size());
            verify(association, path.getIntermediateAssociations().get(1));
        }
        verify(pre1Association, list.get(0).getIntermediateAssociations().get(0));
        verify(post1Association, list.get(0).getIntermediateAssociations().get(2));

        verify(pre1Association, list.get(1).getIntermediateAssociations().get(0));
        verify(post2Association, list.get(1).getIntermediateAssociations().get(2));

        verify(pre2Association, list.get(2).getIntermediateAssociations().get(0));
        verify(post1Association, list.get(2).getIntermediateAssociations().get(2));

        verify(pre2Association, list.get(3).getIntermediateAssociations().get(0));
        verify(post2Association, list.get(3).getIntermediateAssociations().get(2));

    }

    private Path getPath(IAssociation association) {
        List<IAssociation> allAssociation = new ArrayList<IAssociation>(1);
        allAssociation.add(association);
        return new Path(association.getSourceEntity(), association.getTargetEntity(), allAssociation);
    }

    private InterModelAssociation getAssociation() {
        DomainObjectFactory fact = DomainObjectFactory.getInstance();
        EntityInterface srcEn = fact.createEntity();
        srcEn.setId(counter++);
        EntityInterface tgtEn = fact.createEntity();
        tgtEn.setId(counter++);
        AttributeInterface srcAttr = fact.createStringAttribute();
        srcAttr.setEntity(srcEn);
        srcAttr.setId(counter++);
        AttributeInterface tgtAttr = fact.createStringAttribute();
        tgtAttr.setEntity(tgtEn);
        tgtAttr.setId(counter++);

        return new InterModelAssociation(srcAttr, tgtAttr);
    }

    private void verify(InterModelAssociation expected, IAssociation returnedAssociation) {

        assertTrue(returnedAssociation instanceof InterModelAssociation);

        InterModelAssociation res = (InterModelAssociation) returnedAssociation;
        assertEquals(expected.getSourceEntity(), res.getSourceEntity());
        assertEquals(expected.getTargetEntity(), res.getTargetEntity());
        assertEquals(expected.getSourceAttribute(), res.getSourceAttribute());
        assertEquals(expected.getTargetAttribute(), res.getTargetAttribute());
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        Logger.configure();
    }

//    public void testAddCuratedPath() {
//        PathFinder pathFinder = PathFinder.getInstance(ConnectionUtil.getConnection());
//
//        CuratedPath curatedPath = new CuratedPath();
//        curatedPath.setSelected(false);
//
//        IPath path = pathFinder.getPathById(1L);
//        curatedPath.addPath(path);
//
//        Set<EntityInterface> entitySet = new HashSet<EntityInterface>();
//        entitySet.add(path.getSourceEntity());
//        entitySet.add(path.getTargetEntity());
//        curatedPath.setEntitySet(entitySet);
//
//        pathFinder.addCuratedPath(curatedPath);
//
//        Set<ICuratedPath> curatedPaths = pathFinder.getCuratedPaths(path.getSourceEntity(), path.getTargetEntity());
//        
//        ICuratedPath actual = null;
//        for(ICuratedPath cp: curatedPaths) {
//            if(curatedPath.equals(cp)) {
//                actual = cp;
//            }
//        }
//        
//        assertEquals(curatedPath, (CuratedPath)actual);
//    }

}
