package edu.wustl.common.querysuite.queryobject.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import edu.wustl.common.hibernate.PersistentGraph;
import edu.wustl.common.querysuite.exceptions.CyclicException;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IJoinGraph;
import edu.wustl.common.util.Graph;

/**
 * @author Mandar Shidhore
 * @author chetan_patil
 * @version 1.0
 * @created 12-Oct-2006 15.00.04 AM
 * @hibernate.class table="QUERY_JOIN_GRAPH"
 * @hibernate.cache usage="read-write"
 */
public class JoinGraph extends BaseQueryObject implements IJoinGraph {
    private static final long serialVersionUID = 2671567170682456142L;

    private Graph<IExpression, IAssociation> graph = new PersistentGraph<IExpression, IAssociation>();

    // private Collection<GraphEntry> graphEntryCollection = new
    // HashSet<GraphEntry>();

    /**
     * Returns the identifier assigned to BaseQueryObject.
     * 
     * @return a unique id assigned to the Condition.
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     *               unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="JOIN_GRAPH_SEQ"
     */
    public Long getId() {
        return id;
    }

    /**
     * To check wether there is an association between two Expression ids.
     * 
     * @param parentExpression The parent Expression id.
     * @param childExpression The child Expression id.
     * @return true if the graph contains an association between the specified
     *         expressions.
     * @see edu.wustl.common.querysuite.queryobject.IJoinGraph#containsAssociation(edu.wustl.common.querysuite.queryobject.IExpression,
     *      edu.wustl.common.querysuite.queryobject.IExpression)
     */
    public boolean containsAssociation(IExpression parentExpression, IExpression childExpression) {
        return graph.containsEdge(parentExpression, childExpression);
    }

    /**
     * To get the association between two Expression ids.
     * 
     * @param parentExpression The parent Expression id.
     * @param childExpression The child Expression id.
     * @return The association betweent the thwo Expression ids.
     * @see edu.wustl.common.querysuite.queryobject.IJoinGraph#getAssociation(edu.wustl.common.querysuite.queryobject.IExpression,
     *      edu.wustl.common.querysuite.queryobject.IExpression)
     */
    public IAssociation getAssociation(IExpression parentExpression, IExpression childExpression) {
        return graph.getEdge(parentExpression, childExpression);
    }

    /**
     * Checks if the graph is connected by getting the root IExpression The
     * traversing is done on root and if more than one root found, the graph is
     * considered to be disjoint and a MultipleRootsException is thrown
     * 
     * @return true if graph is connected; false if graph is disjoint
     */
    public boolean isConnected() {
        return graph.isConnected();
    }

    /**
     * @param parentExpression The parent Expression id to be added in
     *            joingraph.
     * @param childExpression The child Expression id to be added in
     *            joingraph.
     * @param association The association between two expression ids.
     * @return previous association for the given expression's which was
     *         overwritten by this association; null if no association existed
     *         previously.
     * @throws CyclicException if adding this edge will cause a cycle in the
     *             graph
     * @see edu.wustl.common.querysuite.queryobject.IJoinGraph#putAssociation(edu.wustl.common.querysuite.queryobject.IExpression,
     *      edu.wustl.common.querysuite.queryobject.IExpression,
     *      edu.wustl.common.querysuite.queryobject.IAssociation)
     */
    public IAssociation putAssociation(IExpression parentExpression, IExpression childExpression,
            IAssociation association) throws CyclicException {
/*JJJ        if (graph.willCauseNewCycle(parentExpression, childExpression)) {
            throw new CyclicException("Adding this association causes a cycle in the graph.");
        }*/
	System.out.println("JJJ parentExpression,ChildExpression,Association"+parentExpression+","+childExpression+","+association);
        return graph.putEdge(parentExpression, childExpression, association);
    }

    /**
     * Removes the association from the graph.
     * 
     * @param firstExpression The parent Expression id
     * @param secondExpression The child Expression id
     * @return true if the association between the specified expressions
     *         existed.
     * @see edu.wustl.common.querysuite.queryobject.IJoinGraph#removeAssociation(edu.wustl.common.querysuite.queryobject.IExpression,
     *      edu.wustl.common.querysuite.queryobject.IExpression)
     */
    public boolean removeAssociation(IExpression firstExpression, IExpression secondExpression) {
        return graph.removeEdge(firstExpression, secondExpression) != null;
    }

    /**
     * Removes the specified id from the list of IExpression if one exists
     * 
     * @param id The Expression id to be removed.
     * @return true upon removing specified existing id; false otherwise
     */
    public boolean removeIExpression(IExpression id) {
        return graph.removeVertex(id);
    }

    /**
     * For each element in IExpression list, the root node will be checked for
     * incoming edges for that element.The node having no incomming edges will
     * be treated as Root node.
     * 
     * @return root node of the expression, null if no root exists for the
     *         expression tree
     * @throws MultipleRootsException if more than 1 roots exists.
     */
    public IExpression getRoot() throws MultipleRootsException {
        Set<IExpression> unReachableNode = graph.getUnreachableVertices();

        if (unReachableNode.size() == 0) {
            return null;
            // Prafull: instead of throwing exception it will return null.
            // throw new MultipleRootsException("No Root Exist for the Joing
            // Graph!!!!");
        }

        if (unReachableNode.size() != 1) {
            throw new MultipleRootsException("Multiple Root Exist for the Join Graph");
        }
        return unReachableNode.iterator().next();
    }

    /**
     * To add vertex in joingraph.
     * 
     * @param expression the expression to be added in join graph.
     * @return true upon adding vertex to existing vertex list; false otherwise
     */
    public boolean addIExpression(IExpression expression) {
        return graph.addVertex(expression);
    }

    /**
     * To get the list of Parents of the given Expression.
     * 
     * @param childExpression the Child Expression Id reference.
     * @return The List parent of Expression for th given childExpression.
     * @see edu.wustl.common.querysuite.queryobject.IJoinGraph#getParentList(edu.wustl.common.querysuite.queryobject.IExpression)
     */
    public List<IExpression> getParentList(IExpression childExpression) {
        return asList(graph.getInNeighbours(childExpression), exprIdCmp);
    }

    /**
     * To get the path of the given Expression from the root Expression.
     * 
     * @param expression reference to Expression
     * @return the List of paths of the given Expression from root. returns
     *         empty path list if there is no path.
     * @throws MultipleRootsException if more than 1 roots exists.
     */
    public List<List<IExpression>> getPaths(IExpression expression) throws MultipleRootsException {
        return asList(graph.getVertexPaths(getRoot(), expression));
    }

    /**
     * To the the List of intermediate Expression between the given soure &
     * target Expressions with the given List of association.
     * 
     * @param source The Source Expression Id.
     * @param target The Target Expression.
     * @param associations The List of Associations.
     * @return List of intermediate Expressions for matching List of
     *         Associations. null, if there is no path between source & target
     *         or There is no matching path with the given associations.
     */
    public List<IExpression> getIntermediateExpressions(IExpression source, IExpression target,
            List<IAssociation> associations) {
        // getting path between two vertices.
        Set<List<IExpression>> reachablePaths = graph.getVertexPaths(source, target);

        for (List<IExpression> rechablePath : reachablePaths) {
            // compairing path size & the association list size.
            // This path includes source & target ExprssionIds.
            // For maching path, if there are 2 associations in list, there
            // there will be 3 expressions in obtained path.
            if (rechablePath.size() == associations.size() + 1) {
                // check whether its matching ot not.
                boolean isMatching = true;
                for (int index = 0; index < rechablePath.size() - 1; index++) {
                    IExpression fromExpId = rechablePath.get(index);
                    IExpression toExpId = rechablePath.get(index + 1);
                    // check for association match
                    if (associations.get(index).equals(graph.getEdge(fromExpId, toExpId))) {
                        fromExpId = toExpId;
                    } else {
                        // association is not matching, so this path is not the
                        // required path.
                        isMatching = false;
                        break;
                    }
                }
                if (isMatching) {
                    // matching path found, so logic terminates here.....Their
                    // will be only one matching path.
                    // Remove source & target ExprssionIds from path.
                    rechablePath.remove(0);
                    rechablePath.remove(rechablePath.size() - 1);
                    return rechablePath;
                }
            }
        }

        // No matching path found !!!!
        return null;
    }

    /**
     * To get the first path of the given Expression from the root Expression.
     * 
     * @param expression regerence to Expression
     * @return the List of vertices representing path of the given Expression
     *         from root. returns empty path list if there is no path.
     * @throws MultipleRootsException if more than 1 roots exists.
     */
    public List<IExpression> getPath(IExpression expression) throws MultipleRootsException {
        Set<List<IExpression>> paths = graph.getVertexPaths(getRoot(), expression);

        if (paths.isEmpty()) {
            return new ArrayList<IExpression>();
        }
        return paths.iterator().next();
    }

    /**
     * To get the edge path of the given Expression from the root Expression.
     * 
     * @param expression reference to Expression
     * @return the List of paths of the given Expression from root. returns
     *         empty path list if there is no path.
     * @throws MultipleRootsException if there are multpile roots present in
     *             join graph.
     */
    public List<List<IAssociation>> getEdgesPaths(IExpression expression) throws MultipleRootsException {
        return asList(graph.getEdgePaths(getRoot(), expression));
    }

    /**
     * To get the first edge path of the given Expression from the root
     * Expression.
     * 
     * @param expression regerence to Expression
     * @return the List of Associations representing path of the given
     *         Expression from root. returns empty path list if there is no
     *         path.
     * @throws MultipleRootsException if there are multpile roots present in
     *             join graph.
     */
    public List<IAssociation> getEdgePath(IExpression expression) throws MultipleRootsException {
        Set<List<IAssociation>> paths = graph.getEdgePaths(getRoot(), expression);

        if (paths.isEmpty()) {
            return new ArrayList<IAssociation>();
        }
        return paths.iterator().next();
    }

    /**
     * @param expression the expr id whose children are to be found.
     * @return List of Vertices directly reachable from the given vertex.
     *         Returns null if vertex is not present in graph, Returns empty
     *         list if vertex has no directly reachable node.
     */
    public List<IExpression> getChildrenList(IExpression expression) {
        return asList(graph.getOutNeighbours(expression), exprIdCmp);
    }

    /**
     * To get the expressions having multiple parent nodes.
     * 
     * @return the List of expression ids having multiple parent nodes.
     */
    public List<IExpression> getNodesWithMultipleParents() {
        List<IExpression> nodes = new ArrayList<IExpression>();
        for (IExpression expression : graph.getVertices()) {
            if (graph.getInNeighbours(expression).size() > 1) {
                nodes.add(expression);
            }
        }
        return nodes;
    }

    public List<IExpression> getAllRoots() {
        return asList(graph.getUnreachableVertices(), exprIdCmp);
    }

    private static <T> List<T> asList(Set<T> set) {
        return new ArrayList<T>(set);
    }

    private static <T> List<T> asList(Set<T> set, Comparator<? super T> cmp) {
        List<T> res = new ArrayList<T>(set);
        Collections.sort(res, cmp);
        return res;
    }

    private static final Comparator<IExpression> exprIdCmp = new Comparator<IExpression>() {

        public int compare(IExpression o1, IExpression o2) {
            Integer i1 = o1.getExpressionId();
            Integer i2 = o2.getExpressionId();
            return i1.compareTo(i2);
        }
    };

    // for hibernate
    @SuppressWarnings("unused")
    private Graph<IExpression, IAssociation> getGraph() {
        return graph;
    }

    @SuppressWarnings("unused")
    private void setGraph(Graph<IExpression, IAssociation> graph) {
        this.graph = graph;
    }
}
