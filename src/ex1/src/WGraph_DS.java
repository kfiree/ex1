package ex1.src;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class WGraph_DS implements weighted_graph, Serializable {
    int edgeCounter = 0;
    int modeCounter = 0;

    HashMap<Integer, HashMap<Integer, Double>> Ni = new HashMap<>();
    HashMap<Integer, node_info> nodes = new HashMap<>();

    //copy constructor
    public WGraph_DS(weighted_graph oGraph) {

        //set graph's nodes
        Collection<node_info> oNodes = oGraph.getV();
        if (oNodes != null) {
            for (node_info node : oNodes) {
                addNode(node.getKey());
            }
        }
        for (int key1 : Ni.keySet()) {

            Collection<node_info> nodesNei = oGraph.getV(key1);
            Iterator<node_info> iterator = nodesNei.iterator();
            while (iterator.hasNext()) {
                int key2 = iterator.next().getKey();
                connect(key1, key2, oGraph.getEdge(key1, key2));
            }

            this.modeCounter = oGraph.getMC();
            this.edgeCounter = oGraph.edgeSize();
        }
    }

    //empty constructor
    public WGraph_DS() {

    }

    /**
     * return the node_data by the key,
     * @param key - the node unique key
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_info getNode(int key) {
        return nodes.get(key);
    }

    /**
     * return true iff (if and only if) there is an edge between node1 and node2
     * this method runs in O(1) time.
     * @param key1  node1's key
     * @param key2  node2's key
     * @return does node 1 and node 2 has an edge between them
     */
    @Override
    public boolean hasEdge(int key1, int key2) {
        //is key1 existed
        if (Ni.get(key1) == null)
            return false;

        //is ke2 in key1's neighbors
        return Ni.get(key1).containsKey(key2);
    }
    /**
     * return the weight if the edge (node1, node1). In case
     * there is no such edge - should return -1
     * Note: this method should run in O(1) time.
     * @param key1 node1's key
     * @param key1 node2's key
     * @return
     */
    @Override
    public double getEdge(int key1, int key2) {
        if(this.nodeSize()==0)
            return -1;

        if(key1==key2)
            return 0;

        if (Ni.get(key1) == null || Ni.get(key1).get(key2) == null)
            return -1;

        return Ni.get(key1).get(key2);
    }

    /**
     * add a new node to the graph with the given key.
     * Note: this method runs in O(1) time.
     if there is already a node with such a key -> no action should be performed.
     * @param key
     */
    @Override
    public void addNode(int key) {
        if(this.nodes.containsKey(key))
            return;

        node_info newNode = new NodeInfo(key);
        nodes.put(key, newNode);
        HashMap<Integer, Double> newNi = new HashMap<>();
        Ni.put(key, newNi);

        modeCounter++;

    }

    /**
     * Connect an edge between node1 and node2, with an edge with weight >=0.
     * Note: this method should run in O(1) time.
     * Note2: if the edge node1-node2 already exists - the method simply updates the weight of the edge.
     */
    @Override
    public void connect(int key1, int key2, double w) {

        //MAYBE check if nodes in node ---- is just check if hasEdge before counter is enough
        if (key1 == key2 || !nodes.containsKey(key1) || !nodes.containsKey(key2) || w<0 || this.getEdge(key1,key2)==w) {
            return;
        }

        // if edge exist remove and reconnect in order to update weight
        // else update edgeCounter
        if (this.hasEdge(key1, key2)) {
            this.removeEdge(key1, key2);
        }else{
            modeCounter++;
        }

        edgeCounter++;

        //create edge
        Ni.get(key1).put(key2, w);
        Ni.get(key2).put(key1, w);

    }

    /**
     * This method return a pointer (shallow copy) for a
     * Collection representing all the nodes in the graph.
     * Note: this method runs in O(1) time
     * @return Collection<node_data> collection representations of graph's nodes
     */
    @Override
    public Collection<node_info> getV() {
        return nodes.values();
    }

    /**
     *
     * This method returns a Collection containing all the
     * neighbors of the node with the key
     * Note: this method runs in O(k) time, k - being the degree of node_id.
     * @return Collection<node_data> collection representations of node's neighbors
     */
    @Override
    public Collection getV(int vKey) {
        Collection<node_info> nodeNi = new LinkedList<>();

        if(Ni.get(vKey)!=null) {
            Collection<Integer> NiKeys = Ni.get(vKey).keySet();
            Iterator<Integer> it = NiKeys.iterator();

            for (int i = 0; i < NiKeys.size(); i++) {
                nodeNi.add(this.getNode(it.next()));
            }
        }
        return nodeNi;
    }

    /**
     * Delete the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(n), |V|=n, as all the edges should be removed.
     * @return the removed node
     * @param key the removed node's key
     */
    @Override
    public node_info removeNode(int key) {

        if (Ni.get(key) != null) {

            //remove node's edges
            for (int NiKey : Ni.get(key).keySet()) {
                Ni.get(NiKey).remove(key);
                edgeCounter--;
            }

            // remove
            modeCounter++;
            Ni.remove(key);
            return nodes.remove(key);
        }

        return null;
    }

    /**
     * Delete the edge between node1 and node2 from the graph,
     * Note: this method runs in O(1) time.
     * @param key1
     * @param key2
     */
    @Override
    public void removeEdge(int key1, int key2) {
        if (Ni.containsKey(key1) && Ni.containsKey(key2) && hasEdge(key1, key2)) {
            //remove edge
            Ni.get(key1).remove(key2);
            Ni.get(key2).remove(key1);

            //update counters
            edgeCounter--;
            modeCounter++;
        }
    }

    /** return the number of vertices (nodes) in the graph.
     * Note: this method runs in O(1) time.
     * @return graph size
     */
    @Override
    public int nodeSize() {
        return nodes.size();
    }

    /**
     * return the number of edges (undirectional graph).
     * Note: this method runs in O(1) time.
     * @return edges number
     */
    @Override
    public int edgeSize() {
        return edgeCounter;
    }

    /**
     * return the Mode Count - for testing changes in the graph.
     * Any change in the inner state of the graph should cause an increment in the ModeCount
     * @return number of changes on the graph
     */
    @Override
    public int getMC() {
        return modeCounter;
    }

    /**
     * override the java equals method.
     * compare 2 graph's data
     * @param otherObj
     * @return is this (graph) and other obj (graph) have the same data
     */
    public boolean equals(Object otherObj) {

        //check if same graph
        if(otherObj==this)
            return true;

        //check if same type
        if (!(otherObj instanceof weighted_graph)) {
            return false;
        }

        //casting
        weighted_graph other = (WGraph_DS) otherObj;

        //check if counters equals
        if (this.getMC() != other.getMC() || this.nodeSize() != other.nodeSize() || this.edgeSize() != other.edgeSize())
            return false;

        //compare all graph nodes and edges
        for (int nodeKey : this.Ni.keySet()) {
            //check nodes data
            if(this.getNode(nodeKey).getInfo()!=other.getNode(nodeKey).getInfo() && this.getNode(nodeKey).getTag()!=other.getNode(nodeKey).getTag())
                return false;
            //check edges
            for (int neiKey : this.Ni.get(nodeKey).keySet())
                if (!other.hasEdge(neiKey, nodeKey) && this.getEdge(neiKey, nodeKey) != other.getEdge(neiKey, nodeKey))
                    return false;
        }

        return true;
    }

    class NodeInfo implements node_info, Comparable<node_info>, Serializable {

        int key;
        double tag;
        String info = "";

        /**
         * constructor that create node with the given key
         * @param newKey new node's key
         */
        public NodeInfo(int newKey) {
            this.key = newKey;
        }
        /**
         * Return the key unique(id) associated with this node.
         * @return node's key
         */
        @Override
        public int getKey() {
            return this.key;
        }

        /**
         * return the remark (meta data) associated with this node.
         * @return node's info
         */
        @Override
        public String getInfo() {
            return this.info;
        }

        /**
         * Allows changing the remark (meta data) associated with this node.
         * used in shortestPath method to save previous node's key in path
         * @param s new node's info
         */
        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        /**
         * get tag that holds Temporal data
         * used in shortestPath method to hold the distance between node to the source
         * @return
         */
        @Override
        public double getTag() {
            return this.tag;
        }

        /**
         * set node's tag
         * used in shortestPath method to hold the distance between node to the source
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(double t) {
            this.tag = t;
        }

        /**
         * override the compareTo method
         *
         * @param node
         * @return 1 if this.tag>node.tag, 0 if equals and -1 if this.tag<node.tag
         */
        @Override
        public int compareTo(node_info node) {

            double key1 = this.getTag();
            double key2 = node.getTag();
            if (key1 == key2)
                return 0;
            else if (key1 > key2)
                return 1;
            else
                return -1;
        }
    }
}