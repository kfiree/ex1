package ex1;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is a simple test class to test WGraph_DS and inner class nodeInfo
 * the test checks functionality & performance
 * @author Tehila
 */

class WGraph_DS_myTest {


    /**
     * Generate a random graph with v_size nodes and e_size edges
     *
     * @param v_size
     * @param e_size
     * @param seed
     * @return
     */
//this function based on Graph_Ex0_Test2 graph_creator function
    public static weighted_graph makeGraph(int v_size, int e_size, int seed) {

        Random rand = new Random();

        weighted_graph g = new WGraph_DS();
        rand = new Random(seed);
        for (int i = 0; i < v_size; i++) {
            g.addNode(i);
        }

        int[] nodes = nodes(g);
        while (g.edgeSize() < e_size) {
            int a = nextRnd(0, v_size);
            int b = nextRnd(0, v_size);
            int i = nodes[a];
            int j = nodes[b];
            double w = rand.nextDouble();
            g.connect(i, j, w);
        }
        return g;
    }



    //this functions based on Graph_Ex0_Test2 nextRnd function to make rand num in range
    private static int nextRnd(int min, int max) {
        double v = nextRnd(0.0+min, (double)max);
        int ans = (int)v;
        return ans;
    }
    private static double nextRnd(double min, double max) {
        Random rand = new Random();
        double d = rand.nextDouble();
        double dx = max-min;
        double ans = d*dx+min;
        return ans;
    }
    /**
     * Simple method for returning an array with all the node_data of the graph,
     * Note: this should be using an Iterator<node_edge> to be fixed in Ex1
     * @param g
     * @return
     */
    //this function based on Graph_Ex0_Test2 nodes function
    private static int[] nodes(weighted_graph g) {
        int size = g.nodeSize();
        Collection<node_info> V = g.getV();
        node_info[] nodes = new node_info[size];
        V.toArray(nodes); // O(n) operation
        int[] ans = new int[size];
        for(int i=0;i<size;i++) {ans[i] = nodes[i].getKey();}
        Arrays.sort(ans);
        return ans;
    }

    /**
     * simple test  getNode from graph
     * checks:
     * get un exist node
     * get node from empty graph
     */
    @Test
    void getNode() {
        weighted_graph g = makeGraph(2,0,1) ;
        weighted_graph n = new WGraph_DS();

        int [] test = {3,0,-1};
        assertAll(
                ()-> assertNull(g.getNode(test[0]) ,"get node un exist") ,
                ()-> assertNotNull (g.getNode(test[1]), "get real node" ),
                ()-> assertNull(g.getNode(test[2]) ,"get negative node "),
                ()-> assertNull(n.getNode(0), "get node from empty graph")
        );

    }

    /**
     * simple test for hasEdge between nodes in graph
     * checks:
     * edge from node to himself
     * edge from empty graph
     * edge from one of node not exist
     */
    @Test
    void hasEdge() {
        weighted_graph g0 = makeGraph(2,0,1) ;
        weighted_graph g1 = makeGraph(2,1,1) ;
        weighted_graph n = new WGraph_DS();

        assertAll(
                ()-> assertFalse(g0.hasEdge(0,1), "graph with no edges"),
                ()-> assertFalse(g1.hasEdge(0,0),"edge from node to himself") ,
                ()-> assertFalse(g1.hasEdge(0,3), "un exsist node"),
                ()-> assertTrue(g1.hasEdge(0,1)),
                ()-> assertFalse(n.hasEdge(0,1) , "empty graph")
        );

    }
    /**
     * simple test for getEdge between nodes in graph
     * checks:
     * edge from node to himself
     * edge from empty graph
     * edge from one of node not exist
     */
    @Test
    void getEdge() {
        weighted_graph g0 = makeGraph(2,0,1) ;
        weighted_graph g1 = makeGraph(2,1,1) ;
        weighted_graph n = new WGraph_DS();
        assertAll(
                ()-> assertEquals(-1, g0.getEdge(0,1)),
                ()-> assertEquals(-1, g0.getEdge(0,3), "one of nodes not in graph"),
                ()-> assertNotEquals(-1, g1.getEdge(0,1) ),
                ()-> assertEquals(0, g1.getEdge(0,0), "node with himself"),
                ()-> assertEquals(-1, n.getEdge(0,0), "empty graph")

        );

    }

    /**
     * simple test fot addNode
     * checks:
     * insert new node and get him from graph
     * add same key
     */
    @Test
    void addNode() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        int size = g.nodeSize();
        assertAll(
                ()-> assertNotNull(g.getNode(0)) ,
                ()-> g.addNode(0),
                ()-> assertEquals(size, g.nodeSize(), "add already exist")
        );


    }

    /** simple test for connect two nodes with edge, w>=0
     * check:
     * connect with w<0
     * connect with un exist node
     * connect connected nodes with different w
     * connect node to himself
     * connect empty graph
     */
    @Test
    void connect() {
        weighted_graph g = makeGraph(5,0,1) ;
        weighted_graph n = new WGraph_DS();
        int size = g.edgeSize();

        assertAll(
                ()-> g.connect(0,1,-1),
                ()-> assertEquals(size,g.edgeSize(), "connect with negativ w"),
                ()-> g.connect(1,6,1),
                ()-> assertEquals(size,g.edgeSize(), "connect with node not exist"),
                ()-> g.connect(0,1,1),
                ()-> assertNotEquals(size, g.edgeSize()),
                ()-> assertEquals(1,g.getEdge(0,1)),
                ()-> g.connect(0,1,0),
                ()-> assertNotEquals(1,g.getEdge(0,1) , "nodes connected but w change"),
                ()-> g.connect(2,2,7),
                ()-> assertNotEquals(size+2, g.edgeSize(),"connect node with himself"),
                ()-> n.connect(1,2,1),
                ()-> assertEquals(0,n.edgeSize(), "empty graph")

        );
    }

    /**
     * simple test for getV
     * check:
     * the collection size same as nodeSize
     */
    @Test
    void getV() {
        weighted_graph g = makeGraph(5,0,1) ;
        weighted_graph n = new WGraph_DS();
        Collection <node_info> G = g.getV();
        Collection <node_info> N = n.getV();

        assertEquals(G.size(),g.nodeSize());
        assertTrue(N.isEmpty());
    }

    /**
     * simple test for getV(node)
     * checks:
     * the na collection of Complete graph for each node
     * the na collection of empty graph
     */

    @Test
    @DisplayName("getV_ofNode)")
    void testGetV() {
        weighted_graph g = makeGraph(5,10,1) ;
        weighted_graph n = new WGraph_DS();

        for (int i=0; i<5; i++){
            assertEquals(4, g.getV(i).size());
        }
        assertTrue(n.getV(0).isEmpty());
    }

    /**
     * simple test for removeNode
     * check:
     * remove from empty graph
     * remove un exist node
     * the removes node not exist in na collection
     */
    @Test
    void removeNode() {
        weighted_graph g = makeGraph(2,1,1) ;
        weighted_graph n = new WGraph_DS();
        int size = g.nodeSize();
        int edgSize = g.edgeSize();

        assertAll(
                ()-> g.removeNode(3),
                ()-> assertEquals(size, g.nodeSize(), "remove node not exist"),
                ()-> g.removeNode(0),
                ()-> assertNotEquals(size, g.nodeSize()),
                ()-> assertNotEquals(edgSize, g.edgeSize()),
                ()-> assertEquals(0, g.getV(1).size()),
                ()-> assertNull(n.removeNode(0),"empty graph")

        );
    }

    /**
     * simple test for removeEdge
     * check:
     * remove from empty graph
     * remove edge from un exist node
     * after remove, the na collection changed
     */
    @Test
    void removeEdge() {
        weighted_graph g = makeGraph(2,1,1) ;
        weighted_graph n = new WGraph_DS();
        int edgSize = g.edgeSize();

        assertAll(
                ()-> g.removeEdge(0,2),
                ()-> assertEquals(edgSize, g.edgeSize(), "remove edge from node not exist"),
                ()-> g.removeEdge(0,1),
                ()-> assertNotEquals(edgSize, g.edgeSize()),
                ()-> assertEquals(0, g.getV(1).size()),
                ()-> assertEquals(edgSize-1,n.edgeSize()),
                ()-> n.removeEdge(0,1),
                ()-> assertEquals(edgSize-1,n.edgeSize())

        );
    }

    /**
     * simple test of nodeSize
     * check:
     * after add and remove the size updates
     */
    @Test
    void nodeSize() {
        weighted_graph g = makeGraph(10,0,1) ;
        assertEquals(10,g.nodeSize());
        g.removeNode(0);
        assertEquals(9,g.nodeSize());
        g.addNode(9);
        assertEquals(9,g.nodeSize());
        g.addNode(10);
        assertEquals(10,g.nodeSize());
    }
    /**
     * simple test of edgeSize
     * check:
     * after add and remove the size updates
     */
    @Test
    void edgeSize() {
        weighted_graph g = makeGraph(10,45,1) ;
        assertEquals(45,g.edgeSize());
        g.removeNode(0);
        assertEquals(36,g.edgeSize());
    }
    /**
     * simple test of getMC
     * check:
     * MC not changed by add node already exist
     * MC not changed by connect nodes already connected
     * MC changed by connect nodes already connected with different w
     */

    @Test
    void getMC() {
        weighted_graph g = makeGraph(10,0,1) ;
        assertEquals(10,g.getMC());
        g.addNode(0);
        assertEquals(10,g.getMC());
        g.connect(1,2,0);
        assertEquals(11,g.getMC());
        g.connect(1,2,0);
        assertEquals(11,g.getMC());
        g.connect(1,2,2);
        assertEquals(12,g.getMC());

    }
}