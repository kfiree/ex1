package ex1;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class WGraph_DS implements weighted_graph, Serializable {
    int edgeCounter = 0;
    int modeCounter = 0;

    //TODO check inner hashmap for nulls everywhere
    //TODO MAYBE add object edge
    HashMap<Integer, HashMap<Integer, Double>> Ni = new HashMap<>();
    HashMap<Integer, node_info> nodes = new HashMap<>();

    //copy constructor
    public WGraph_DS(weighted_graph oGraph) {
        //TODO fix counters


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
            //TODO check if iterator better
        }
    }

    //empty constructor
    public WGraph_DS() {

    }

    @Override
    public node_info getNode(int key) {
        return nodes.get(key);
    }

    @Override
    public boolean hasEdge(int key1, int key2) {
        //is key1 existed
        if (Ni.get(key1) == null)
            return false;

        //is ke2 in key1's neighbors
        return Ni.get(key1).containsKey(key2);
    }

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

        //update counters
        edgeCounter++;


        //create edge
        Ni.get(key1).put(key2, w);
        Ni.get(key2).put(key1, w);

    }

    @Override
    public Collection<node_info> getV() {
        return nodes.values();
    }

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

    @Override
    public node_info removeNode(int key) {
        //TODO test null(check if key not in graph insted of ni.get != null?)
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

    @Override
    public int nodeSize() {
        return nodes.size();
    }

    @Override
    public int edgeSize() {
        return edgeCounter;
    }


    @Override
    public int getMC() {
        return modeCounter;
    }

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

        public NodeInfo(int newKey) {
            this.key = newKey;
        }

        @Override
        public int getKey() {
            return this.key;
        }

        @Override
        public String getInfo() {
            return this.info;
        }

        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        @Override
        public double getTag() {
            return this.tag;
        }

        @Override
        public void setTag(double t) {
            this.tag = t;
        }

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
