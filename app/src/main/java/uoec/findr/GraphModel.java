package uoec.findr;

/**
 * Created by nikhilperi on 2017-01-14.
 */

import java.io.*;
import java.util.*;

public class GraphModel {
    public Graph g;
    public HashMap<Integer, Point> points;

    public GraphModel(HashMap<String, Point> points) {
        this.points = points;
        LinkedList<Graph.Edge> edgeList = new LinkedList<Graph.Edge>();
        ListIterator<Point> pointsIterator = points.values().listIterator();

        while(pointsIterator.hasNext()){
            Point p = pointsIterator.next();

            LinkedList<Integer> neighboors = p.neighbours;
            ListIterator<Point> neighboorsIterator = neighboors.listIterator();

            while(neighboorsIterator.hasNext()){
                Point n = points.get(neighboorsIterator.next());
                Graph.Edge e = new Graph.Edge(p, n);
                edgeList.add(e);
            }
        }
        g = new Graph(edgeList);
    }

    public LinkedList<Point> findPath(String startQRHash, String endQRHash){
        g.dijkstra(startQRHash);

        LinkedList<Point> path = new LinkedList<Point>();
        LinkedList<Graph.Vertex> vertexPath= g.printPath(endQRHash);
        ListIterator<Graph.Vertex> vertexListIterator = vertexPath.listIterator();

        while(vertexListIterator.hasNext()){
            path.add(points.get(vertexListIterator.next().name));
        }

        return path;
    }
}

class Graph {
    private final Map<String, Vertex> graph; // mapping of vertex names to Vertex objects, built from a set of Edges

    /** One edge of the graph (only used by Graph constructor) */
    public static class Edge {
        public final String v1, v2;
        public final float dist;

        public Edge(Point p1, Point p2) {
            this.v1 = p1.id;
            this.v2 = p2.id;
            if (p1.name == "stairs" && p2.name == "stair"){
                this.dist = 0;
            }
            else
            {
                this.dist = Math.sqrt(Math.pow((p1.xCoord - p2.xCoord), 2) + Math.pow((v1.yCoord - v2.yCoord), 2));
            }
        }
    }

    /** One vertex of the graph, complete with mappings to neighbouring vertices */
    public static class Vertex implements Comparable<Vertex>{
        public final String name;
        public int dist = Integer.MAX_VALUE; // MAX_VALUE assumed to be infinity
        public Vertex previous = null;
        public final Map<Vertex, Float> neighbours = new HashMap<>();

        public Vertex(String id)
        {
            this.name = id;
        }

        private LinkedList<Vertex> printPath(LinkedList<Vertex> list)
        {
            if (this == this.previous || this.previous == null)
            {
                return list;
            }
            else
            {
                list.add(this);
                this.previous.printPath(list);
            }
        }

        public int compareTo(Vertex other)
        {
            if (dist == other.dist)
                return name.compareTo(other.name);

            return Float.compare(dist, other.dist);
        }

        @Override public String toString()
        {
            return name;
        }
    }

    /** Builds a graph from a set of edges */
    public Graph(LinkedList<Edge> edges) {
        graph = new HashMap<>(edges.size());

        //one pass to find all vertices
        for (Edge e : edges) {
            if (!graph.containsKey(e.v1)) graph.put(e.v1, new Vertex(e.v1));
            if (!graph.containsKey(e.v2)) graph.put(e.v2, new Vertex(e.v2));
        }

        //another pass to set neighbouring vertices
        for (Edge e : edges) {
            graph.get(e.v1).neighbours.put(graph.get(e.v2), e.dist);
            //graph.get(e.v2).neighbours.put(graph.get(e.v1), e.dist); // also do this for an undirected graph
        }
    }

    /** Runs dijkstra using a specified source vertex */
    public void dijkstra(String startName) {
        if (!graph.containsKey(startName)) {
            System.err.printf("Graph doesn't contain start vertex \"%s\"\n", startName);
            return;
        }
        final Vertex source = graph.get(startName);
        NavigableSet<Vertex> q = new TreeSet<>();

        // set-up vertices
        for (Vertex v : graph.values()) {
            v.previous = v == source ? source : null;
            v.dist = v == source ? 0 : Integer.MAX_VALUE;
            q.add(v);
        }

        dijkstra(q);
    }

    /** Implementation of dijkstra's algorithm using a binary heap. */
    private void dijkstra(final NavigableSet<Vertex> q) {
        Vertex u, v;
        while (!q.isEmpty()) {

            u = q.pollFirst(); // vertex with shortest distance (first iteration will return source)
            if (u.dist == Integer.MAX_VALUE) break; // we can ignore u (and any other remaining vertices) since they are unreachable

            //look at distances to each neighbour
            for (Map.Entry<Vertex, Integer> a : u.neighbours.entrySet()) {
                v = a.getKey(); //the neighbour in this iteration

                final int alternateDist = u.dist + a.getValue();
                if (alternateDist < v.dist) { // shorter path to neighbour found
                    q.remove(v);
                    v.dist = alternateDist;
                    v.previous = u;
                    q.add(v);
                }
            }
        }
    }

    /** Prints a path from the source to the specified vertex */
    public LinkedList<Vertex> printPath(String endName) {
        if (!graph.containsKey(endName)) {
            System.err.printf("Graph doesn't contain end vertex \"%s\"\n", endName);
            return null;
        }
        return graph.get(endName).printPath();
    }
}