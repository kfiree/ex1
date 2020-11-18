package ex1;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms {

    private weighted_graph graph = new WGraph_DS();
    private int visitedTag = 1;

    @Override
    public void init(weighted_graph g) {
        this.graph = g;
    }

    @Override
    public weighted_graph getGraph() {
        return this.graph;
    }

    @Override
    public weighted_graph copy() {
        weighted_graph newGraph = new WGraph_DS(this.graph);
        return newGraph;
    }

    @Override
    public boolean  isConnected() {
        //TODO add test if graph null/small
        if(graph.nodeSize()<2){
            return true;
        }
        //add src to queue - nodesQ, and set his tag to visitedTag (indicates that the node were visited)
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

    @Override
    public double shortestPathDist(int src, int dest) {

        if(this.graph.getNode(src) ==null || this.graph.getNode(dest)==null)
            return -1;

        //set path info
        setPathInfo(src, dest);
        //return
        return graph.getNode(dest).getTag();
    }

    @Override
    public List<node_info> shortestPath(int src, int dest) {

        if(this.graph.getNode(src) ==null || this.graph.getNode(dest)==null)
            return null;

        List<node_info> path = new ArrayList<>();

        //set path info
        setPathInfo(src, dest);

        path.add(graph.getNode(dest));

        //TODO check if node's info legit
        //reconstruct path by nodes's info (represent previous)
        node_info node = graph.getNode(dest);
        while(node.getKey()!= src){
            //get prev
            int nextKey = Integer.parseInt(node.getInfo());
            node = graph.getNode(nextKey);
            path.add(node);
        }
        Collections.reverse(path);

        return path;
    }

    private void setPathInfo(int src, int dest) {
        //TODO add test if graph null/small
        //TODO check if i initialize unvisited necessary

        PriorityQueue<node_info> nieQ = new PriorityQueue<node_info>();
        HashMap<Integer, node_info> unvisited = new HashMap<>();


        //TODO stop when dest and his neighbours visited
        //TODO use iterator?
        //reset nodes data and add to unvisited
        for(node_info node : graph.getV()) {
            node.setTag(-1);
            node.setInfo(null);
            unvisited.put(node.getKey(), node);
        }
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

    @Override
    public boolean save(String file) {
        try {
            ObjectOutputStream os = new ObjectOutputStream( new FileOutputStream(file));
            os.writeObject(graph);
            os.close();
        } catch (IOException e) {
            System.out.println("failed to save");
            return false;
        }
        return true;
    }

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
                System.out.println("failed to load");
                return false;
            }
            ois.close();
            fis.close();
        } catch(IOException e){
            e.printStackTrace();
            System.out.println("failed to load");
            return false;
        }
        this.graph = LoadedGraph;
        return true;
    }
}