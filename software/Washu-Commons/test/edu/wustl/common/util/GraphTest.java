package edu.wustl.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

/**
 * @author chandrakant_talele
 */
public class GraphTest extends TestCase {
    // vertex = city and weight = length

    public void testEdgeMethods() {
        Graph<String, Integer> graph = newGraph();
        assertEquals(0, graph.numEdges());

        assertNull(graph.putEdge("Delhi", "Mumbai", 200));
        assertEquals(1, graph.numEdges());
        assertEquals(asSet("Delhi", "Mumbai"), graph.getVertices());
        assertNull(graph.putEdge("Delhi", "Madras", 500));
        assertEquals(asSet("Delhi", "Mumbai", "Madras"), graph.getVertices());
        assertEquals(2, graph.numEdges());
        assertNull(graph.putEdge("Mumbai", "Madras", 300));
        assertEquals(3, graph.numEdges());
        assertNull(graph.putEdge("Andaman", "Andaman", null));
        assertEquals(asSet("Delhi", "Mumbai", "Madras", "Andaman"), graph.getVertices());
        assertEquals(4, graph.numEdges());

        assertEquals(integer(300), graph.putEdge("Mumbai", "Madras", 400));
        assertEquals(4, graph.numEdges());
        assertNull(graph.putEdge("Andaman", "Andaman", null));
        assertEquals(4, graph.numEdges());

        assertTrue(graph.containsEdge("Delhi", "Mumbai"));
        assertTrue(graph.containsEdge("Delhi", "Madras"));
        assertTrue(graph.containsEdge("Mumbai", "Madras"));
        assertTrue(graph.containsEdge("Andaman", "Andaman"));
        assertFalse(graph.containsEdge("Madras", "Delhi"));
        assertFalse(graph.containsEdge("Madras", "Mumbai"));
        assertFalse(graph.containsEdge("Mumbai", "Delhi"));
        assertFalse(graph.containsEdge("Mumbai", "Andaman"));
        assertFalse(graph.containsEdge("Andaman", "Mumbai"));
        assertFalse(graph.containsEdge("Delhi", "Andaman"));
        assertFalse(graph.containsEdge("Andaman", "Delhi"));
        assertFalse(graph.containsEdge("Madras", "Andaman"));
        assertFalse(graph.containsEdge("Andaman", "Madras"));

        Map<String, Integer> tempM = graph.getOutgoingEdges("Delhi");
        assertEquals(asMap("Mumbai", 200, "Madras", 500), tempM);
        tempM.remove("Mumbai"); // doesn't affect graph.
        assertEquals(asMap("Mumbai", 200, "Madras", 500), graph.getOutgoingEdges("Delhi"));
        assertEquals(asMap("Madras", 400), graph.getOutgoingEdges("Mumbai"));
        assertTrue(graph.getOutgoingEdges("Madras").isEmpty());
        assertEquals(asMap("Andaman", null), graph.getOutgoingEdges("Andaman"));

        assertEquals(asMap("Mumbai", 400, "Delhi", 500), graph.getIncomingEdges("Madras"));
        assertEquals(asMap("Delhi", 200), graph.getIncomingEdges("Mumbai"));
        assertTrue(graph.getIncomingEdges("Delhi").isEmpty());
        assertEquals(asMap("Andaman", null), graph.getIncomingEdges("Andaman"));

        assertEquals(integer(200), graph.getEdge("Delhi", "Mumbai"));
        assertEquals(integer(500), graph.getEdge("Delhi", "Madras"));
        assertEquals(integer(400), graph.getEdge("Mumbai", "Madras"));
        assertNull(graph.getEdge("Andaman", "Andaman"));// null weight

        // no edge
        assertNull(graph.getEdge("Madras", "Delhi"));
        assertNull(graph.getEdge("Madras", "Mumbai"));
        assertNull(graph.getEdge("Mumbai", "Delhi"));

        assertNull(graph.removeEdge("Madras", "Delhi"));
        assertEquals(4, graph.numEdges());
        assertNull(graph.removeEdge("Madras", "Mumbai"));
        assertEquals(4, graph.numEdges());
        assertNull(graph.removeEdge("Mumbai", "Delhi"));
        assertEquals(4, graph.numEdges());

        assertNull(graph.removeEdge("Andaman", "Andaman"));
        assertEquals(3, graph.numEdges());
        assertFalse(graph.containsEdge("Andaman", "Andaman"));
        assertNull(graph.getEdge("Andaman", "Andaman"));

        assertEquals(integer(200), graph.removeEdge("Delhi", "Mumbai"));
        assertEquals(2, graph.numEdges());
        assertEquals(asMap("Madras", 500), graph.getOutgoingEdges("Delhi"));
        assertTrue(graph.getIncomingEdges("Mumbai").isEmpty());
        assertNull(graph.getEdge("Delhi", "Mumbai"));
    }

    public void testEdgeMethodsExceptions() throws Exception {
        Graph<String, Integer> graph = newGraph();
        try {
            graph.containsEdge("Delhi", "Mumbai");
            fail();
        } catch (IllegalArgumentException e) {

        }
        try {
            graph.removeEdge("Delhi", "Mumbai");
            fail();
        } catch (IllegalArgumentException e) {

        }
        try {
            graph.getEdge("Delhi", "Mumbai");
            fail();
        } catch (IllegalArgumentException e) {

        }
        graph.putEdge("Delhi", "Mumbai", 200);
        graph.putEdge("Delhi", "Madras", 500);
        graph.putEdge("Mumbai", "Madras", 300);
        assertEquals(3, graph.numEdges());
        edgeMethodFailure(getEdgeMethod("containsEdge"), graph, "Mumbai", "Pune");
        assertEquals(3, graph.numEdges());
        edgeMethodFailure(getEdgeMethod("getEdge"), graph, "Mumbai", "Pune");
        assertEquals(3, graph.numEdges());
        edgeMethodFailure(getEdgeMethod("removeEdge"), graph, "Mumbai", "Pune");
        assertEquals(3, graph.numEdges());

        try {
            graph.getOutgoingEdges(null);
            fail();
        } catch (NullPointerException e) {

        }

        try {
            graph.getIncomingEdges(null);
            fail();
        } catch (NullPointerException e) {

        }
        try {
            graph.getOutgoingEdges("Pune");
            fail();
        } catch (IllegalArgumentException e) {

        }
        try {
            graph.getIncomingEdges("Pune");
            fail();
        } catch (IllegalArgumentException e) {

        }
    }

    private Method getEdgeMethod(String name) throws Exception {
        return Graph.class.getMethod(name, Object.class, Object.class);
    }

    private void edgeMethodFailure(Method method, Graph<?, ?> graph, String validVertex, String invalidVertex)
            throws IllegalAccessException {
        try {
            method.invoke(graph, validVertex, null);
            fail();
        } catch (InvocationTargetException e) {
            checkCause(e, NullPointerException.class);
        }
        try {
            method.invoke(graph, null, validVertex);
            fail();
        } catch (InvocationTargetException e) {
            checkCause(e, NullPointerException.class);
        }
        try {
            method.invoke(graph, null, null);
            fail();
        } catch (InvocationTargetException e) {
            checkCause(e, NullPointerException.class);
        }
        try {
            method.invoke(graph, null, invalidVertex);
            fail();
        } catch (InvocationTargetException e) {
            checkCause(e, NullPointerException.class);
        }
        try {
            method.invoke(graph, invalidVertex, null);
            fail();
        } catch (InvocationTargetException e) {
            checkCause(e, IllegalArgumentException.class);
        }
        try {
            method.invoke(graph, invalidVertex, validVertex);
            fail();
        } catch (InvocationTargetException e) {
            checkCause(e, IllegalArgumentException.class);
        }
    }

    private void checkCause(InvocationTargetException i, Class<?> causeClass) {
        assertEquals(causeClass, i.getCause().getClass());
    }

    public void testVertexMethods() {
        Graph<String, Integer> graph = newGraph();
        assertEquals(0, graph.numVertices());
        assertTrue(graph.getVertices().isEmpty());
        assertFalse(graph.containsVertex("Delhi"));
        assertTrue(graph.addVertex("Delhi"));
        assertEquals(1, graph.numVertices());
        assertTrue(graph.containsVertex("Delhi"));
        assertFalse(graph.addVertex("Delhi"));
        assertEquals(1, graph.numVertices());

        assertTrue(graph.addVertex("Mumbai"));
        assertEquals(2, graph.numVertices());
        assertFalse(graph.addVertices(asSet("Delhi", "Mumbai")));
        assertEquals(2, graph.numVertices());
        assertTrue(graph.addVertices(asSet("Delhi", "Mumbai", "Madras")));
        assertEquals(3, graph.numVertices());

        Set<String> tempVs = graph.getVertices();
        assertEquals(asSet("Delhi", "Mumbai", "Madras"), tempVs);
        tempVs.remove("Madras"); // doesn't affect graph.
        assertEquals(asSet("Delhi", "Mumbai", "Madras"), graph.getVertices());

        assertTrue(graph.getOutNeighbours("Delhi").isEmpty());
        assertTrue(graph.getOutNeighbours("Mumbai").isEmpty());
        assertTrue(graph.getOutNeighbours("Madras").isEmpty());
        assertTrue(graph.getInNeighbours("Delhi").isEmpty());
        assertTrue(graph.getInNeighbours("Mumbai").isEmpty());
        assertTrue(graph.getInNeighbours("Madras").isEmpty());

        graph.putEdge("Delhi", "Mumbai", 200);
        assertEquals(asSet("Mumbai"), graph.getOutNeighbours("Delhi"));
        assertEquals(asSet("Delhi"), graph.getInNeighbours("Mumbai"));
        assertTrue(graph.getOutNeighbours("Madras").isEmpty());
        assertTrue(graph.getInNeighbours("Madras").isEmpty());

        graph.putEdge("Andaman", "Andaman", null);
        assertEquals(asSet("Andaman"), graph.getInNeighbours("Andaman"));
        assertEquals(asSet("Andaman"), graph.getOutNeighbours("Andaman"));
    }

    public void testRemoveVertex() {
        Graph<String, Integer> graph = newGraph();
        assertFalse(graph.removeVertex("Delhi"));
        graph.addVertex("Delhi");
        assertTrue(graph.removeVertex("Delhi"));

        graph.putEdge("Delhi", "Mumbai", 200);
        graph.putEdge("Delhi", "Madras", 500);
        graph.putEdge("Mumbai", "Madras", 300);
        graph.putEdge("Andaman", "Andaman", null);

        Graph<String, Integer> expectedGraph = newGraph();
        expectedGraph.putEdge("Delhi", "Mumbai", 200);
        expectedGraph.putEdge("Delhi", "Madras", 500);
        expectedGraph.putEdge("Mumbai", "Madras", 300);

        // remove andaman
        graph.removeVertex("Andaman");
        assertEquals(expectedGraph, graph);

        // remove madras, check andaman not affected
        graph.putEdge("Andaman", "Andaman", null);
        graph.removeVertex("Madras");
        expectedGraph.clear();
        expectedGraph.putEdge("Delhi", "Mumbai", 200);
        expectedGraph.putEdge("Andaman", "Andaman", null);
        assertEquals(expectedGraph, graph);
        graph.removeVertex("Andaman");

        // remove mumbai
        graph.putEdge("Delhi", "Madras", 500);
        graph.putEdge("Mumbai", "Madras", 300);
        graph.removeVertex("Mumbai");
        expectedGraph.clear();
        expectedGraph.putEdge("Delhi", "Madras", 500);
        assertEquals(expectedGraph, graph);

        // remove delhi
        graph.putEdge("Delhi", "Mumbai", 200);
        graph.putEdge("Mumbai", "Madras", 300);
        graph.removeVertex("Delhi");
        expectedGraph.clear();
        expectedGraph.putEdge("Mumbai", "Madras", 300);
        assertEquals(expectedGraph, graph);
    }

    public void testVertexMethodsExceptions() {
        Graph<String, Integer> graph = newGraph();
        try {
            graph.getOutNeighbours(null);
            fail();
        } catch (NullPointerException e) {

        }
        try {
            graph.getOutNeighbours("Delhi");
            fail();
        } catch (IllegalArgumentException e) {

        }
        try {
            graph.getInNeighbours(null);
            fail();
        } catch (NullPointerException e) {

        }
        try {
            graph.getInNeighbours("Delhi");
            fail();
        } catch (IllegalArgumentException e) {

        }
    }

    public void testClear() {
        Graph<String, Integer> graph = newGraph();
        Graph<String, Integer> emptyGraph = newGraph();
        graph.putEdge("Delhi", "Mumbai", 200);
        graph.clear();
        assertEquals(emptyGraph, graph);
    }

    public void testGetUnreachableVertices() {
        Graph<String, Integer> graph = newGraph();
        assertTrue(graph.getUnreachableVertices().isEmpty());

        graph.addVertex("Delhi");
        assertEquals(asSet("Delhi"), graph.getUnreachableVertices());

        graph.addVertex("Mumbai");
        assertEquals(asSet("Mumbai", "Delhi"), graph.getUnreachableVertices());

        graph.putEdge("Delhi", "Mumbai", 200);
        assertEquals(asSet("Delhi"), graph.getUnreachableVertices());

        graph.addVertex("Andaman");
        assertEquals(asSet("Delhi", "Andaman"), graph.getUnreachableVertices());

        graph.putEdge("Andaman", "Andaman", null);
        assertEquals(asSet("Delhi", "Andaman"), graph.getUnreachableVertices());
    }

    public void testIsConnected() {
        Graph<String, Integer> graph = newGraph();
        assertTrue(graph.isConnected());

        graph.addVertex("Delhi");
        assertTrue(graph.isConnected());
        graph.putEdge("Delhi", "Delhi", 0);
        assertTrue(graph.isConnected());
        graph.removeEdge("Delhi", "Delhi");

        graph.putEdge("Delhi", "Mumbai", 200);
        assertTrue(graph.isConnected());
        graph.removeEdge("Delhi", "Mumbai");
        assertFalse(graph.isConnected());

        graph.putEdge("Mumbai", "Madras", 300);
        assertFalse(graph.isConnected());

        graph.putEdge("Delhi", "Delhi", null);
        assertFalse(graph.isConnected());

        graph.putEdge("Delhi", "Pune", 150);
        assertFalse(graph.isConnected());

        graph.putEdge("Pune", "Mumbai", 80);
        assertTrue(graph.isConnected());
        graph.removeEdge("Pune", "Mumbai");
        assertFalse(graph.isConnected());
        // check direction is immaterial.
        graph.putEdge("Mumbai", "Pune", 80);
        assertTrue(graph.isConnected());
    }

    public void testIsTree() {
        Graph<String, Integer> graph = newGraph();
        assertTrue(graph.isTree());

        graph.addVertex("Delhi");
        assertTrue(graph.isTree());

        graph.putEdge("Delhi", "Delhi", 0);
        assertFalse(graph.isTree());

        graph.putEdge("Delhi", "Mumbai", 200);
        assertFalse(graph.isTree());

        graph.removeEdge("Delhi", "Delhi");
        assertTrue(graph.isTree());

        graph.removeEdge("Delhi", "Mumbai");
        assertFalse(graph.isTree());

        graph.putEdge("Delhi", "Mumbai", 200);
        graph.putEdge("Delhi", "Madras", 500);
        assertTrue(graph.isTree());
        graph.putEdge("Mumbai", "Madras", 300);
        assertFalse(graph.isTree());
    }

    public void testIsReachable() {
        Graph<String, Integer> graph = newGraph();
        graph.addVertex("Delhi");

        assertTrue(graph.isReachable("Delhi", "Delhi"));
        graph.putEdge("Delhi", "Delhi", null);
        assertTrue(graph.isReachable("Delhi", "Delhi"));

        graph.addVertex("Mumbai");
        assertFalse(graph.isReachable("Delhi", "Mumbai"));
        assertFalse(graph.isReachable("Mumbai", "Delhi"));
        graph.putEdge("Delhi", "Mumbai", 200);
        assertTrue(graph.isReachable("Delhi", "Mumbai"));
        assertFalse(graph.isReachable("Mumbai", "Delhi"));

        graph.putEdge("Mumbai", "Madras", 300);
        assertTrue(graph.isReachable("Mumbai", "Madras"));
        assertTrue(graph.isReachable("Delhi", "Madras"));
        assertFalse(graph.isReachable("Madras", "Delhi"));
    }

    public void testWillCauseNewCycle() {
        Graph<String, Integer> graph = newGraph();
        assertTrue(graph.willCauseNewCycle("Delhi", "Delhi"));
        assertFalse(graph.willCauseNewCycle("Delhi", "Mumbai"));

        graph.addVertex("Delhi");
        assertTrue(graph.willCauseNewCycle("Delhi", "Delhi"));
        assertFalse(graph.willCauseNewCycle("Delhi", "Mumbai"));

        graph.putEdge("Delhi", "Delhi", null);
        assertFalse(graph.willCauseNewCycle("Delhi", "Delhi"));

        graph.putEdge("Delhi", "Mumbai", 200);
        assertFalse(graph.willCauseNewCycle("Delhi", "Mumbai"));
        assertTrue(graph.willCauseNewCycle("Mumbai", "Delhi"));

        graph.putEdge("Mumbai", "Madras", 300);
        assertFalse(graph.willCauseNewCycle("Delhi", "Madras"));
        assertTrue(graph.willCauseNewCycle("Madras", "Delhi"));
    }

    public void testAssign() {
        Graph<String, Integer> graph = newGraph();
        graph.putEdge("Delhi", "Mumbai", 200);
        graph.putEdge("Delhi", "Madras", 500);
        graph.putEdge("Mumbai", "Madras", 400);
        graph.putEdge("Andaman", "Andaman", null);

        Graph<String, Integer> clone = newGraph();
        clone.assign(graph);
        assertEquals(graph, clone);

        assertEquals(asMap("Mumbai", 200, "Madras", 500), clone.getOutgoingEdges("Delhi"));
        assertEquals(asMap("Madras", 400), clone.getOutgoingEdges("Mumbai"));
        assertTrue(clone.getOutgoingEdges("Madras").isEmpty());
        assertEquals(asMap("Andaman", null), clone.getOutgoingEdges("Andaman"));

        assertEquals(asMap("Mumbai", 400, "Delhi", 500), clone.getIncomingEdges("Madras"));
        assertEquals(asMap("Delhi", 200), clone.getIncomingEdges("Mumbai"));
        assertTrue(clone.getIncomingEdges("Delhi").isEmpty());
        assertEquals(asMap("Andaman", null), clone.getIncomingEdges("Andaman"));
    }

    public void testVertexPaths() {
        Graph<String, Integer> graph = newGraph();
        graph.putEdge("Delhi", "Mumbai", 200);
        graph.putEdge("Delhi", "Madras", 500);
        graph.putEdge("Mumbai", "Madras", 400);
        graph.putEdge("Andaman", "Andaman", null);

        checkPaths(graph);

        graph.putEdge("Madras", "Madras", null);
        checkPaths(graph);
    }

    @SuppressWarnings("unchecked")
    private void checkPaths(Graph<String, Integer> graph) {
        assertEquals(asSet(asList("Delhi", "Madras"), asList("Delhi", "Mumbai", "Madras")), graph.getVertexPaths(
                "Delhi", "Madras"));
        assertEquals(asSet(asList("Delhi", "Mumbai")), graph.getVertexPaths("Delhi", "Mumbai"));
        assertEquals(asSet(asList("Mumbai", "Madras")), graph.getVertexPaths("Mumbai", "Madras"));
        assertEquals(asSet(), graph.getVertexPaths("Delhi", "Delhi"));
        assertEquals(asSet(), graph.getVertexPaths("Mumbai", "Delhi"));
        assertEquals(asSet(), graph.getVertexPaths("Madras", "Delhi"));
        assertEquals(asSet(), graph.getVertexPaths("Madras", "Mumbai"));
        assertEquals(asSet(), graph.getVertexPaths("Andaman", "Andaman"));
    }

    @SuppressWarnings("unchecked")
    public void testVertexPathsFullyConnectedBase() {
        final int size = 5;
        Graph<Integer, Integer> graph = fullyConnectedGraph(size);

        Set[][] fullyConnectedPaths = new Set[size][size];
        for (int i = 0; i < size; i++) {
            fullyConnectedPaths[i] = new Set[size];
            for (int j = 0; j < size; j++) {
                Set<List<Integer>> paths = graph.getVertexPaths(i, j);
                fullyConnectedPaths[i][j] = paths;
                int expected;
                if (i == j) {
                    expected = 0;
                } else {
                    expected = nprSum(size - 2);
                }
                assertEquals(expected, paths.size());
            }
        }

        graph.removeEdge(0, 1);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Set<List<Integer>> paths = graph.getVertexPaths(i, j);
                Set<List<Integer>> temp = new HashSet<List<Integer>>(fullyConnectedPaths[i][j]);
                assertTrue(temp.containsAll(paths));
                temp.removeAll(paths);
                for (List<Integer> path : temp) {
                    assertTrue(path.toString().contains("0, 1"));
                }
            }
        }

        graph.removeEdge(3, 4);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Set<List<Integer>> paths = graph.getVertexPaths(i, j);
                Set<List<Integer>> temp = new HashSet<List<Integer>>(fullyConnectedPaths[i][j]);
                assertTrue(temp.containsAll(paths));
                temp.removeAll(paths);
                for (List<Integer> path : temp) {
                    assertTrue(path.toString().contains("0, 1") || path.toString().contains("3, 4"));
                }
            }
        }
    }

    private Graph<Integer, Integer> fullyConnectedGraph(int size) {
        Graph<Integer, Integer> graph = new Graph<Integer, Integer>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                graph.putEdge(i, j, null);
            }
        }
        return graph;
    }

    @SuppressWarnings("unchecked")
    public void testEdgePaths() {
        Graph<String, Integer> graph = newGraph();
        graph.putEdge("Delhi", "Mumbai", 200);
        graph.putEdge("Delhi", "Madras", 500);
        graph.putEdge("Mumbai", "Madras", 400);
        graph.putEdge("Andaman", "Andaman", null);

        assertEquals(asSet(asList(500), asList(200, 400)), graph.getEdgePaths("Delhi", "Madras"));
        assertEquals(asSet(asList(200)), graph.getEdgePaths("Delhi", "Mumbai"));
        assertEquals(asSet(asList(400)), graph.getEdgePaths("Mumbai", "Madras"));
        assertEquals(asSet(), graph.getEdgePaths("Delhi", "Delhi"));
        assertEquals(asSet(), graph.getEdgePaths("Mumbai", "Delhi"));
        assertEquals(asSet(), graph.getEdgePaths("Madras", "Delhi"));
        assertEquals(asSet(), graph.getEdgePaths("Madras", "Mumbai"));
        assertEquals(asSet(), graph.getEdgePaths("Andaman", "Andaman"));
    }

    public void testSerial() {
        Graph<String, Integer> graph = newGraph();
        graph.putEdge("Delhi", "Mumbai", 200);
        graph.putEdge("Delhi", "Madras", 500);
        graph.putEdge("Mumbai", "Madras", 400);
        graph.putEdge("Andaman", "Andaman", null);

        Graph<String, Integer> clone = new ObjectCloner().clone(graph);
        assertEquals(graph, clone);

        assertEquals(asMap("Mumbai", 200, "Madras", 500), clone.getOutgoingEdges("Delhi"));
        assertEquals(asMap("Madras", 400), clone.getOutgoingEdges("Mumbai"));
        assertTrue(clone.getOutgoingEdges("Madras").isEmpty());
        assertEquals(asMap("Andaman", null), clone.getOutgoingEdges("Andaman"));

        assertEquals(asMap("Mumbai", 400, "Delhi", 500), clone.getIncomingEdges("Madras"));
        assertEquals(asMap("Delhi", 200), clone.getIncomingEdges("Mumbai"));
        assertTrue(clone.getIncomingEdges("Delhi").isEmpty());
        assertEquals(asMap("Andaman", null), clone.getIncomingEdges("Andaman"));
    }

    private Integer integer(int i) {
        return new Integer(i);
    }

    private Graph<String, Integer> newGraph() {
        return new Graph<String, Integer>();
    }

    private Map<Object, Object> asMap(Object... entries) {
        if (entries.length % 2 != 0) {
            throw new IllegalArgumentException();
        }
        Map<Object, Object> res = new HashMap<Object, Object>();
        for (int i = 0; i < entries.length - 1; i += 2) {
            res.put(entries[i], entries[i + 1]);
        }
        return res;
    }

    private static <T> Set<T> asSet(T... elems) {
        Set<T> res = new HashSet<T>();
        for (T elem : elems) {
            res.add(elem);
        }
        return res;
    }

    private static <T> List<T> asList(T... elems) {
        List<T> res = new ArrayList<T>();
        for (T elem : elems) {
            res.add(elem);
        }
        return res;
    }

    private static int nprSum(int n) {
        int r = 0;
        int fn = fact(n);
        for (int i = 0; i <= n; i++) {
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