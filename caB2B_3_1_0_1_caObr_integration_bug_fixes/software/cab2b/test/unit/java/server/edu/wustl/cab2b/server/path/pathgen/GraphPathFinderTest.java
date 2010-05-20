package edu.wustl.cab2b.server.path.pathgen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

/**
 * @author Chandrakant Talele
 */
public class GraphPathFinderTest extends TestCase {
    int size = 4;

    Set<Path> allPaths;

    GraphPathFinder gpf = new GraphPathFinder();

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        boolean[][] matrix = getMatrix();
        allPaths = getAllPaths(matrix);
        assertEquals(expectedPaths(size), allPaths.size());
    }

    public void testNoPaths() {
        // no paths only nodes
        Set<Path> paths = getAllPaths(new boolean[size][size]);
        assertEquals(0, paths.size());
    }

    public void testSinglePath() {
        // simplest 0-->1
        boolean[][] res = new boolean[size][size];
        res[0][1] = true;
        Set<Path> paths = getAllPaths(res);
        assertEquals(1, paths.size());
        Path p = new Path(new Node(0), new Node(1));
        assertTrue(paths.contains(p));
    }

    public void testLinerPathOgThree() {
        // simple 0-->1-->2
        boolean[][] res = new boolean[size][size];
        res[0][1] = res[1][2] = true;
        Set<Path> paths = getAllPaths(res);
        assertEquals(3, paths.size());
        assertTrue(allPaths.containsAll(paths));
        Path p = new Path(new Node(0), new Node(2), get(new Node(1)));
        assertTrue(paths.contains(p));
        assertTrue(paths.contains(new Path(new Node(0), new Node(1))));
        assertTrue(paths.contains(new Path(new Node(1), new Node(2))));
    }

    public void testCircularPath() {
        // 0-->1 -->2-->0 (circular)
        boolean[][] res = new boolean[size][size];
        res[0][1] = res[1][2] = res[2][0] = true;
        Set<Path> paths = getAllPaths(res);
        assertEquals(6, paths.size());
        assertTrue(allPaths.containsAll(paths));
        assertTrue(paths.contains(new Path(new Node(0), new Node(1))));
        assertTrue(paths.contains(new Path(new Node(1), new Node(2))));
        assertTrue(paths.contains(new Path(new Node(2), new Node(0))));

        assertTrue(paths.contains(new Path(new Node(0), new Node(2), get(new Node(1)))));
        assertTrue(paths.contains(new Path(new Node(1), new Node(0), get(new Node(2)))));
        assertTrue(paths.contains(new Path(new Node(2), new Node(1), get(new Node(0)))));
    }

    public void testSizeLimited() {
        // input copied from testCircularPath()
        // 0-->1 -->2-->0 (circular)
        boolean[][] res = new boolean[size][size];
        res[0][1] = res[1][2] = res[2][0] = true;
        Set<Path> paths = getAllPaths(res, 2);
        assertEquals(3, paths.size());
        assertTrue(allPaths.containsAll(paths));
        assertTrue(paths.contains(new Path(new Node(0), new Node(1))));
        assertTrue(paths.contains(new Path(new Node(1), new Node(2))));
        assertTrue(paths.contains(new Path(new Node(2), new Node(0))));

        paths = getAllPaths(res, 3);
        assertEquals(6, paths.size());
        assertTrue(allPaths.containsAll(paths));
        assertTrue(paths.contains(new Path(new Node(0), new Node(1))));
        assertTrue(paths.contains(new Path(new Node(1), new Node(2))));
        assertTrue(paths.contains(new Path(new Node(2), new Node(0))));

        assertTrue(paths.contains(new Path(new Node(0), new Node(2), get(new Node(1)))));
        assertTrue(paths.contains(new Path(new Node(1), new Node(0), get(new Node(2)))));
        assertTrue(paths.contains(new Path(new Node(2), new Node(1), get(new Node(0)))));

        Set<Path> paths2 = getAllPaths(res, 9);
        assertEquals(paths, paths2);

        try {
            getAllPaths(res, 1);
            fail();
        } catch (IllegalArgumentException e) {

        }
    }

    public void testOneBidirectional() {
        // 0<-->1
        boolean[][] res = new boolean[size][size];
        res[0][1] = res[1][0] = true;
        Set<Path> paths = getAllPaths(res);
        assertEquals(2, paths.size());
        assertTrue(allPaths.containsAll(paths));
        assertTrue(paths.contains(new Path(new Node(0), new Node(1))));
        assertTrue(paths.contains(new Path(new Node(1), new Node(0))));
    }

    public void testPathWithSelfLoopInBetween() {
        // 0 --> 1 --> 2 with self loop at 1
        boolean[][] res = new boolean[size][size];
        res[0][1] = res[1][2] = res[1][1] = true;
        Set<Path> paths = getAllPaths(res);
        assertEquals(4, paths.size());
        Set<Path> sub = minus(paths, allPaths);
        assertEquals(1, sub.size());
        assertEquals(new Path(new Node(1), new Node(1)), sub.iterator().next());
        assertTrue(paths.contains(new Path(new Node(0), new Node(1))));
        assertTrue(paths.contains(new Path(new Node(1), new Node(2))));
        assertTrue(paths.contains(new Path(new Node(0), new Node(2), get(new Node(1)))));
    }

    public void testFullyConnectedWithoutOneUnidirectional() {
        // fully connected- only one edge is unidirectional
        boolean[][] matrix = getMatrix();
        matrix[0][2] = false;
        Set<Path> paths = getAllPaths(matrix);
        assertTrue(allPaths.containsAll(paths));
        Set<Path> s = minus(allPaths, paths);
        for (Path p : s) {
            assertTrue(p.toString().contains("0#2"));
        }
    }

    public void testFullyConnectedWithoutOneBidirectional() {
        boolean[][] matrix = getMatrix();
        // fully connected- only one bidirectional edge missing
        matrix[0][2] = matrix[2][0] = false;
        Set<Path> paths = getAllPaths(matrix);
        Set<Path> s = minus(allPaths, paths);

        assertTrue(allPaths.containsAll(paths));
        for (Path p : s) {
            assertTrue(p.toString().contains("0#2") || p.toString().contains("2#0"));
        }
    }

    public void testFullyConnectedOneSubpathMissing() {
        // fully connected - one sub path 0-->2-->1 missing
        boolean[][] matrix = getMatrix();
        matrix[0][2] = matrix[2][1] = false;
        Set<Path> paths = getAllPaths(matrix);
        Set<Path> s = minus(allPaths, paths);

        assertTrue(allPaths.containsAll(paths));
        for (Path p : s) {
            assertTrue(p.toString().contains("0#2") || p.toString().contains("2#1"));
        }
    }

    public void testFullyConnectedLongerSubpathMissing() {
        // fully connected - one sub path 0-->1-->2-->3 missing
        boolean[][] matrix = getMatrix();
        matrix[0][1] = matrix[1][2] = matrix[2][3] = false;

        Set<Path> paths = getAllPaths(matrix);
        Set<Path> s = minus(allPaths, paths);

        assertTrue(allPaths.containsAll(paths));
        for (Path p : s) {
            assertTrue(p.toString().contains("0#1") || p.toString().contains("1#2")
                    || p.toString().contains("2#3"));
        }
    }

    public void testFullyConnectedCircularSubpathMissing() {
        boolean[][] matrix = getMatrix();
        // fully connected - one sub path 0-->1-->2-->3-->0 (circular) missing
        matrix[0][1] = matrix[1][2] = matrix[2][3] = matrix[3][0] = false;
        Set<Path> paths = getAllPaths(matrix);
        Set<Path> s = minus(allPaths, paths);

        assertTrue(allPaths.containsAll(paths));
        for (Path p : s) {
            assertTrue(p.toString().contains("0#1") || p.toString().contains("1#2")
                    || p.toString().contains("2#3") || p.toString().contains("3#0"));
        }
    }

    public void testFullyConnectedOneSelfLoopMissing() {
        boolean[][] matrix = getMatrix();
        matrix[0][1] = matrix[1][2] = matrix[2][1] = matrix[2][3] = false;

        Set<Path> paths = getAllPaths(matrix);
        Set<Path> s = minus(allPaths, paths);

        assertTrue(allPaths.containsAll(paths));
        for (Path p : s) {
            assertTrue(p.toString().contains("0#1") || p.toString().contains("1#2")
                    || p.toString().contains("2#3") || p.toString().contains("2#1"));
        }
    }

    public void testPathReplication() {
        // fully connected - 0th node does not have any incoming or outgoing edge.
        //1th is parent of 0th 
        boolean[][] matrix = getMatrix();
        Node child = new Node(0);
        for (int i = 0; i < matrix.length; i++) {
            matrix[child.getId()][i] = false;
            matrix[i][child.getId()] = false;
        }

        Node parent = new Node(child.getId() + 1);
        Map<Integer, Set<Integer>> replicationNodes = new HashMap<Integer, Set<Integer>>();
        replicationNodes.put(parent.getId(), Collections.singleton(child.getId()));

        Set<Path> paths = gpf.getAllPaths(matrix, replicationNodes, null);

        for (Path path : paths) {
            if (path.containsNode(parent)) {
                Path clone = path.replace(parent, child);
                assertTrue(paths.contains(clone));
            }
        }
    }
    /*
    public void testPathReplicationForBug5483() {
        // A->B and C->D and C is parent of B
        //A=0, B=1, C=2,D=3
        boolean[][] matrix = new boolean[4][4];
        matrix[0][1] = true; 
        matrix[2][3] = true;
        Map<Integer, Set<Integer>> replicationNodes= new HashMap<Integer, Set<Integer>>();
        Set<Integer> set = new HashSet<Integer>();
        set.add(1);
        replicationNodes.put(2, set);
        Set<Path> paths = new GraphPathFinder().getAllPaths(matrix,replicationNodes,null );
        System.out.println(paths);
        Set<Path> expected = new HashSet<Path>();
        expected.add(new Path(new Node(0), new Node(1)));
        expected.add(new Path(new Node(2), new Node(3)));
        expected.add(new Path(new Node(1), new Node(3)));
        expected.add(new Path(new Node(0), new Node(3)));
        assertEquals(expected, paths);
        
    }
    */
    public void testPathReplicationParentSelfEdge() {
        // 0th is parent of 1st
        // 0th is parent of 2nd
        //0th has self edge
        boolean[][] matrix = new boolean[3][3];
        matrix[0][0] = true;
        HashSet<Integer> children = new HashSet<Integer>(2);
        children.add(1);
        children.add(2);

        Map<Integer, Set<Integer>> replicationNodes = new HashMap<Integer, Set<Integer>>();
        replicationNodes.put(0, children);

        Set<Path> paths = gpf.getAllPaths(matrix, replicationNodes, null);
        assertEquals(9, paths.size());

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertTrue(paths.contains(new Path(new Node(i), new Node(j))));
            }
        }
    }

    private Set<Path> getAllPaths(boolean[][] matrix) {
        return gpf.getAllPaths(matrix, new HashMap<Integer, Set<Integer>>(), null);
    }

    private Set<Path> getAllPaths(boolean[][] matrix, int maxLength) {
        return gpf.getAllPaths(matrix, new HashMap<Integer, Set<Integer>>(), null, maxLength);
    }

    private List<Node> get(Node... params) {
        ArrayList<Node> list = new ArrayList<Node>(params.length);
        for (int i = 0; i < params.length; i++) {
            list.add(params[i]);
        }
        return list;
    }

    private boolean[][] getMatrix() {
        boolean[][] res = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                res[i][j] = true;
            }
            res[i][i] = false;
        }

        return res;
    }

    private Set<Path> minus(Set<Path> bigger, Set<Path> smaller) {
        Set<Path> temp = new HashSet<Path>(bigger);
        temp.removeAll(smaller);
        return temp;

    }

    private static int expectedPaths(int n) {
        int r = 0;
        int fn = fact(n);
        for (int i = 2; i <= n; i++) {
            r = r + fn / fact(n - i);
        }
        return r;
    }

    private static int fact(int n) {
        int f = 1;
        for (int i = 2; i <= n; i++) {
            f = f * i;
        }
        return f;
    }
}
