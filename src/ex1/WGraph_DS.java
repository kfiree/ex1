package ex1;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class WGraph_DS implements weighted_graph{
    int keyGenerator;
    int nodeSize = 0;
    int edgeCounter = 0;
    int Mcoun = 0;

    //MAYBE add object edge
    HashMap<Integer, HashMap<Integer, Double>> Ni = new HashMap<>();
    HashMap<Integer, node_info> nodes = new HashMap<>();

    @Override
    public node_info getNode(int key) {
        return nodes.get(key);
    }

    @Override
    public boolean hasEdge(int key1, int key2) {
        return Ni.get(key1).containsKey(key2);
    }

    @Override
    public double getEdge(int key1, int key2) {
        return Ni.get(key1).get(key2);
    }

    @Override
    public void addNode(int key) {
        this.keyGenerator = key;
        node_info newNode = new NodeInfo();
        nodes.put(key, newNode);
        HashMap<Integer,Double> newNi= new HashMap<>();
        Ni.put(key, newNi);//matti
    }

    @Override
    public void connect(int key1, int key2, double w) {
        //MAYBE check if nodes in node
        if(nodes.containsKey(key1) && nodes.containsKey(key2)){
            Ni.get(key1).put(key2, w);
            Ni.get(key2).put(key1, w);

            edgeCounter++;
        }
    }

    @Override
    public Collection<node_info> getV() {
        return nodes.values();
    }

    @Override
    public Collection getV(int vKey) {
        Collection<Integer> NiKeys = Ni.get(vKey).keySet();
        node_info[] nodeNi = new node_info[NiKeys.size()];
        Iterator<Integer> it = NiKeys.iterator();

        for (int i = 0; i < NiKeys.size(); i++) {
            nodeNi[i] = this.getNode(it.next());
        }
        return (Collection) nodeNi;
    }

    @Override
    public node_info removeNode(int key) {
        return null;
    }

    @Override
    public void removeEdge(int node1, int node2) {

    }

    @Override
    public int nodeSize() {
        return nodeSize;
    }

    @Override
    public int edgeSize() {
        return edgeSize;
    }

    @Override
    public int getMC() {
        return Mcoun;
    }

    class NodeInfo implements node_info{

        int key;
        double tag;
        String info="";
        HashMap<Integer, Double> Ni = new HashMap<>();

        public NodeInfo() {
            this.key = keyGenerator;
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
    }
}
