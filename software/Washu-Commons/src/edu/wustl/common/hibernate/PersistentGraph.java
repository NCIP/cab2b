package edu.wustl.common.hibernate;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.wustl.common.util.Graph;

@SuppressWarnings("unused")
public class PersistentGraph<V, E> extends Graph<V, E> {
    private static final long serialVersionUID = 5607285510168767311L;

    private static class Edge<V, E> implements Serializable {
        private static final long serialVersionUID = -5521651636166250171L;

        private V source, target;

        private E edge;

        private Long id;

        private Edge() {

        }

        private Edge(V source, V target, E edge) {
            this.source = source;
            this.target = target;
            this.edge = edge;
        }

        private E getEdge() {
            return edge;
        }

        private void setEdge(E edge) {
            this.edge = edge;
        }

        private V getSource() {
            return source;
        }

        private void setSource(V source) {
            this.source = source;
        }

        private V getTarget() {
            return target;
        }

        private void setTarget(V target) {
            this.target = target;
        }

        private Long getId() {
            return id;
        }

        private void setId(Long id) {
            this.id = id;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Edge)) {
                return false;
            }
            Edge<V, E> o = (Edge<V, E>) obj;
            return source.equals(o.source) && target.equals(o.target)
                    && (edge == null ? o.edge == null : edge.equals(o.edge));
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().append(source).append(target).append(edge).toHashCode();
        }
    }

    private Long id;

    private Set<Edge> edges = new HashSet<Edge>();

    private Set<V> vertices = new HashSet<V>();

    private Set<Edge> getEdges() {
        return edges;
    }

    private void setEdges(Set<Edge> edges) {
        if (edges != null) {
            for (Edge<V, E> entry : edges) {
                putEdge(entry.source, entry.target, entry.edge);
            }
        }
    }

    private void setVertices(Set<V> vertices) {
        if (vertices != null) {
            for (V vertex : vertices) {
                addVertex(vertex);
            }
        }
    }

    private Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    @Override
    public E putEdge(V source, V target, E edge) {
        E res = super.putEdge(source, target, edge);
        edges.add(newEdge(source, target, edge));
        return res;
    }

    @Override
    public E removeEdge(V source, V target) {
        E edge = super.removeEdge(source, target);
        edges.remove(newEdge(source, target, edge));
        return edge;
    }

    @Override
    public boolean addVertex(V vertex) {
        return super.addVertex(vertex) ? vertices.add(vertex) : false;
    }

    @Override
    public boolean removeVertex(V vertex) {
        return super.removeVertex(vertex) ? vertices.remove(vertex) : false;
    }

    private Edge<V, E> newEdge(V source, V target, E edge) {
        return new Edge<V, E>(source, target, edge);
    }
}
