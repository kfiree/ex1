package ex1.src;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms {

    private weighted_graph graph = new WGraph_DS();
    private int visitedTag = 1;

    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g new graph
     */
    @Override
    public void init(weighted_graph g) {
        if(g==null) {
            this.graph = new WGraph_DS();
        }else {
            this.graph = g;
        }
    }

    /**
     * Return the underlying graph of which this class works.
     * @return class's graph
     */
    @Override
    public weighted_graph getGraph() {
        return this.graph;
    }

    /**
     * Compute a deep copy of this weighted graph.
     * @return deep copy of class's graph
     */
    @Override
    public weighted_graph copy() {
        return new WGraph_DS(this.graph);
    }

    /**
     * Returns true if and only if (iff) there is a valid path from EVREY node to each
     * other node.
     *
     * method based on BFS algorithm
     * @return boolean is connected
     */
    @Override
    public boolean  isConnected() {
        if(graph.nodeSize()<2){
            return true;
        }
        //add ex1.src to queue - nodesQ, and set his tag to visitedTag (indicates that the node were visited)
        Queue<node_info> nodesQ = new LinkedList<>();
        node_info srcNode = graph.getNode(this.graph.getV().iterator().next().getKey());
        nodesQ.add(srcNode);
        srcNode.setTag(visitedTag);
        int visitedCounter=1;

        //iterate over graph
        while(!nodesQ.isEmpty()) {
            node_info node = nodesQ.poll();

            for(node_info next : graph.getV(node.getKey())) {
                if(next.getTag() != visitedTag) {
                    nodesQ.add(next);
                    next.setTag(visitedTag); //mark as visited
                    visitedCounter++;
                }
            }
        }
        visitedTag++;

        return visitedCounter==graph.nodeSize();
    }

    /**
     * returns the length of the shortest path between ex1.src to dest
     * Note: if no such path --> returns -1
     * @param src - start node
     * @param dest - end (target) node
     * @return path length
     */
    @Override
    public double shortestPathDist(int src, int dest) {

        //if no such path return -1
        if(this.graph.getNode(src) ==null || this.graph.getNode(dest)==null)
            return -1;

        //set path info
        setPathInfo(src, dest);

        //return path distance
        return graph.getNode(dest).getTag();
    }

    /**
     * returns the the shortest path between ex1.src to dest - as an ordered List of nodes:
     * ex1.src--> n1-->n2-->...dest
     *  if no such path --> returns null;
     * @param src - start node
     * @param dest - end (target) node
     * @return path
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {

        //if node doesn't exists return null
        if(this.graph.getNode(src) ==null || this.graph.getNode(dest)==null)
            return null;

        List<node_info> path = new ArrayList<>();

        //if dest doesn't exist return null
        node_info targetNode = graph.getNode(dest);
        if(targetNode == null)
            return null;

        //set path info
        setPathInfo(src, dest);

        //if there is no path from ex1.src to dest return null
        if(targetNode.getInfo()==null)
            return null;

        path.add(graph.getNode(dest));

        //reconstruct path using nodes's info (represent previous)
        node_info node = graph.getNode(dest);
        while(node.getKey()!= src && node.getInfo()!=null){
            //get prev in path
            int nextKey = Integer.parseInt(node.getInfo());
            node = graph.getNode(nextKey);
            path.add(node);
        }
        Collections.reverse(path);

        return path;
    }

    /**
     * update all node's data (info and tag) using dijkstra's algorithm
     * node's tag = distance between node to source node
     * node's info = key of previous node in the shortest path to destination
     *
     * @param src start node
     * @param dest end (target) node
     */
    private void setPathInfo(int src, int dest) {

        PriorityQueue<node_info> nieQ = new PriorityQueue<>();
        HashMap<Integer, node_info> unvisited = new HashMap<>();

        //reset nodes data and add to unvisited
        for(node_info node : graph.getV()) {
            node.setTag(-1);
            node.setInfo(null);
            unvisited.put(node.getKey(), node);
        }

        //set first node data
        graph.getNode(src).setTag(0);
        nieQ.add(graph.getNode(src));
        graph.getNode(src).setTag(0);

        //iterate the nodes
        while(!nieQ.isEmpty()){
            //get node with shortest path
            node_info node = nieQ.poll();
            double prevDist = node.getTag();

            // update node's neighbours data
            for (node_info next : graph.getV(node.getKey())) {
                //update new path is is shorter
                double curDist = prevDist + graph.getEdge(next.getKey(), node.getKey());
                if(next.getTag()==-1 || next.getTag() > curDist) {
                    next.setTag(curDist);
                    next.setInfo(String.valueOf((node.getKey())));
                }

                //if unvisited add to queue
                if (unvisited.containsKey(next.getKey())) {
                    nieQ.add(next);
                }

            }
            unvisited.remove(node.getKey());
        }
    }


    /**
     * Saves this weighted (undirected) graph to the given
     * file name
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        try {
            ObjectOutputStream os = new ObjectOutputStream( new FileOutputStream(file));
            os.writeObject(graph);
            os.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph will  remain as is.
     * @param file - file name
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        WGraph_DS LoadedGraph;
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            try {
                LoadedGraph = (WGraph_DS) ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            ois.close();
            fis.close();
        } catch(IOException e){
            e.printStackTrace();
            return false;
        }
        this.graph = LoadedGraph;
        return true;
    }
}