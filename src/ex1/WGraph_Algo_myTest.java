package ex1;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This is a simple test class to test WGraph_Algo
 * the test checks functionality & performance
 *@author Tehila
 */

class WGraph_Algo_myTest {

    /**
     * test for init
     * checks:
     * get by init the graph
     * use shallow copy
     */
    @Test
    void init() {
        weighted_graph g = WGraph_DS_myTest.makeGraph(5, 10, 1);
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(g);
        assertTrue(ga.isConnected());
        int i = 1;
        while (i < 5) {
            g.removeEdge(0, i);
            i++;
        }
        assertFalse(ga.isConnected());
    }

    /**
     * test for getGraph
     * check:
     * the graph init and the graph getGraph are equals
     */
    @Test
    void getGraph() {
        weighted_graph g = WGraph_DS_myTest.makeGraph(5, 10, 1);
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(g);
        weighted_graph g1 = ga.getGraph();
        assertEquals(g, g1);
    }

    /**
     * test for copy
     * checks:
     * copy two graphs by deep copy
     * copy empty graphs
     */
    @Test
    void copy() {

        weighted_graph g = WGraph_DS_myTest.makeGraph(5, 10, 1);
        weighted_graph_algorithms ga = new WGraph_Algo();

        ga.init(g);
        weighted_graph g1 = new WGraph_DS();
        g1 = ga.copy();
        assertEquals(g, g1);
        g.removeNode(0);
        assertNotEquals(g, g1,"remove node should not affect deep copy");

        weighted_graph g2 = new WGraph_DS();
        ga.init(g2);
        g1 = ga.copy();
        assertEquals(g1, g2, "copy empty graph- empty is equal");
        g2.addNode(0);
        assertNotEquals(g1, g2, "add node should not affect deep copy");

    }

    /**
     * test for isConnected
     * checks:
     * empty graph - is connected
     * graph with only one node - is connected
     * Complete graph - is connected
     * insert new node to  Complete graph - not connected
     */
    @Test
    void isConnected() {
        weighted_graph g = WGraph_DS_myTest.makeGraph(5, 10, 1);
        weighted_graph g1 = new WGraph_DS();
        weighted_graph_algorithms ga = new WGraph_Algo();


        ga.init(g);
        assertTrue(ga.isConnected(), "Complete graph");


        g.addNode(5);
        assertFalse(ga.isConnected(), "insert node- not Complete graph");


        ga.init(g1);
        assertTrue(ga.isConnected(),"empty graph");
        g1.addNode(0);
        assertTrue(ga.isConnected(),"graph with one node");
        g1.addNode(1);
        assertFalse(ga.isConnected(),"two nodes not connect");
        g = ga.copy();
        g.connect(0,1,1);
        assertFalse(ga.isConnected(),"deep copy- two nodes not connect");
        ga.init(g);
        assertTrue(ga.isConnected(), "init- after connect nodes");

    }
    /**
     * test for shortestPathDist
     * checks:
     * empty graph
     * path with node not exist
     * shortestPathDist change if w changes
     */
    @Test
    void shortestPathDist() {
        weighted_graph_algorithms ga = new WGraph_Algo();
        weighted_graph g = new WGraph_DS();
        ga.init(g);
        assertEquals(-1,ga.shortestPathDist(1,5),"empty graph");

        for (int i=0; i<10; i++){
            g.addNode(i);
        }
        for (int i=0; i<6; i++){
            g.connect(i,i+1,1);
        }
        assertEquals(-1,ga.shortestPathDist(0,11),"node not exist");
        assertEquals(-1,ga.shortestPathDist(0,7),"no path");
        assertEquals(5,ga.shortestPathDist(0,5));
        g.connect(5,9,95);
        assertEquals(100,ga.shortestPathDist(0,9));
        g.connect(5,9,5);
        assertEquals(10,ga.shortestPathDist(0,9));
    }
    /**
     * test for shortestPath
     * checks:
     * empty graph
     * path with node not exist
     * the list nodes have same refer with graph nodes
     * the last node.tag in list = path
     */
    @Test
    void shortestPath() {
        List<node_info> l = new LinkedList<>();
        weighted_graph_algorithms ga = new WGraph_Algo();
        weighted_graph g = new WGraph_DS();
        ga.init(g);
        assertNull(ga.shortestPath(0,10),"empty graph");

        for (int i=0; i<10; i++){
            g.addNode(i);
        }
        for (int i=0; i<6; i++){
            g.connect(i,i+1,1);
        }
        assertNull(ga.shortestPath(0,10),"node not exist");
        l = ga.shortestPath(0,5);
        int[] checkKey = {0, 1, 2, 3, 4,5};
        int i = 0;
        for (node_info n : l){
            assertEquals(checkKey[i],n.getKey());
            i++;
        }
        g.connect(5,9,95);
        l = ga.shortestPath(0,9);
        assertSame (g.getNode(9),l.get(6));
        assertEquals(100,g.getNode(9).getTag(),"tag is the distance");


    }
    /**
     * test for save and load
     * checks:
     * save and load empty graph
     * the load not change when init the graph
     */
    @Test
    void saveAndload() {
        weighted_graph g = WGraph_DS_myTest.makeGraph(5, 10, 1);
        weighted_graph g1 = new WGraph_DS();
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(g);
        String s = "t";
        ga.save(s);
        g.removeNode(0);
        ga.load(s);
        g1 = ga.copy();
        assertNotEquals(g,g1);
        g = ga.getGraph();
        assertEquals(g,g1);

        weighted_graph n = new WGraph_DS();
        ga.init(n);
        ga.save(s);
        n.addNode(0);
        ga.load(s);
        g = ga.getGraph();
        assertTrue(g.nodeSize()== 0);
    }


}