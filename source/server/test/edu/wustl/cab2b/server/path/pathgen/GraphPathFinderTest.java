package edu.wustl.cab2b.server.path.pathgen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import edu.wustl.common.util.logger.Logger;

/**
 * @author Chandrakant Talele
 */
public class GraphPathFinderTest extends TestCase {
    int size = 4;

    Set<Path> allPaths;

    GraphPathFinder gpf = new GraphPathFinder();

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        Logger.configure();
        boolean[][] matrix = getMatrix();
        allPaths = getAllPaths(matrix);
        assertEquals(expectedPaths(size), allPaths.size());
    }

    public void test0() {
        // no paths only nodes
        Set<Path> paths = getAllPaths(new boolean[size][size]);
        assertEquals(0, paths.size());
    }

    public void test1() {
        // simplest 0-->1
        boolean[][] res = new boolean[size][size];
        res[0][1] = true;
        Set<Path> paths = getAllPaths(res);
        assertEquals(1, paths.size());
        Path p = new Path(new Node(0), new Node(1));
        assertTrue(paths.contains(p));
    }

    public void test2() {
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

    public void test3() {
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

    public void test4() {
        // 0<-->1
        boolean[][] res = new boolean[size][size];
        res[0][1] = res[1][0] = true;
        Set<Path> paths = getAllPaths(res);
        assertEquals(2, paths.size());
        assertTrue(allPaths.containsAll(paths));
        assertTrue(paths.contains(new Path(new Node(0), new Node(1))));
        assertTrue(paths.contains(new Path(new Node(1), new Node(0))));
    }

    public void test5() {
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

    public void test6() {
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

    public void test7() {
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

    public void test8() {
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

    public void test9() {
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

    public void test10() {
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

    public void test11() {
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

    private Set<Path> getAllPaths(boolean[][] matrix) {
        return gpf.getAllPaths(matrix, new HashMap<Integer, Set<Integer>>(), null);
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

    //    private boolean[][] getForbiddenMatrix() {
    //        boolean[][] res = new boolean[size][size];
    //        for (int i = 0; i < size; i++) {
    //            for (int j = 0; j < size; j++) {
    //                res[i][j] = false;
    //            }
    //        }
    //
    //        return res;
    //    }

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
