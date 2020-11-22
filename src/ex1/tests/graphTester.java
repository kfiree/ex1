package ex1.tests;

import ex1.src.WGraph_DS;
import ex1.src.node_info;
import ex1.src.weighted_graph;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  tester for WGraph_DS
 *
 *  @author kfir ettinger
 */
class graphTester {

    private static int wUpdateCounter;
    private weighted_graph g0, g1, g2, g3, big_g;

    private static Random numGenerator;

    /**
     * private method that create simple graphs for the ex1.tests
     */
    void makeBasicGraph(){
        weighted_graph emptyGraph = new WGraph_DS();

        g0 = new WGraph_DS();

        g1 = new WGraph_DS();
        g1.addNode(1);

        g2 = new WGraph_DS();
        g2.addNode(1);
        g2.addNode(2);


        g3 = new WGraph_DS(g2);
        g3.connect(1, 2, 1.55);
    }

    /**
     *
     * test getNode and addNode
     * ex1.tests:
     *  - add new node to graph
     *  - check if node have the given key
     *  - check adding a node with pre-existed key
     *  - check if negative key is valid
     *  - try get method with non-existed key
     *
     * @result code will be persisted without any errors and will return null if key wasnt found
     */
    @Test
    void getAndAddTest() {
        makeBasicGraph();
        //check adding new node to graph
        node_info node = g1.getNode(1);
        assertNotEquals(null, node);

        //check key
        assertEquals(1,node.getKey());

        //check add existed key (size of graph should stay the same
        int sizeBefore = g1.nodeSize();
        g1.addNode(1);
        assertEquals(sizeBefore, g1.nodeSize());


        //check negative key
        g1.addNode(-1);
        node = g1.getNode(-1);
        assertNotEquals(null, node);

        //check get not existed
        node = g1.getNode(3);
        assertNull(node);

    }

    /**
     * Create a valid account.
     * @result Account will be persisted without any errors,
     *         and Account.getId() will no longer be <code>null</code>
     */
    /**
     * test hasEdge method
     * ex1.tests:
     * - try to get an edge between unexisted node to another
     * - try to get an edge that dosen't existed
     * - try get an edge that do exist
     *
     * @result will be persisted without any errors
     */
    @Test
    void hasEdgeTest() {
        makeBasicGraph();

        //check unexisted node
        assertFalse(g1.hasEdge(1, 2));
        assertFalse(g1.hasEdge(2, 1));

        //check unexisted edge
        assertFalse(g2.hasEdge(1, 2));
        assertFalse(g2.hasEdge(2, 1));

        //check legal input
        assertTrue(g3.hasEdge(1, 2));
        assertTrue(g3.hasEdge(2, 1));
    }

    /**
     * test getEdge method
     * ex1.tests:
     * - to try get an edge between non-existed nodes
     * - try to get a non-existed edge
     * - try functionality with valid input
     *
     * @result code will be persisted without any errors and will return -1 if an edge dosen't exist
     */
    @Test
    void getEdgeTest(){
        makeBasicGraph();
        //check non-existed node
        assertEquals(-1, g1.getEdge(1,2));
        assertEquals(-1, g1.getEdge(2,1));

        //check unexisted edge
        assertEquals(-1, g2.getEdge(1,2));
        assertEquals(-1, g2.getEdge(2,1));

        //check legal
        assertTrue(g3.getEdge(1,2)==g3.getEdge(2,1));
    }

    /**
     * test connect method
     * ex1.tests:
     * - try to connect non-existed nodes
     * - try to connect an edge between nodes with pre-existed edge between them
     * - try to add an edge with negative weight
     *
     * @result code will be persisted without any errors and will change weight if change needed
     */
    @Test
    void connectTest(){
        makeBasicGraph();

        //check non-existed node
        g1.connect(1,3, 1.1);
        assertFalse(g1.hasEdge(1,3));
        assertFalse(g1.hasEdge(3,1));
        assertFalse(g1.hasEdge(5,3));

        //check add existed with different weight
        g3.connect(1,2,100.0);
        assertEquals(100.0, g3.getEdge(1, 2));

        //check add existed edge with same weight
        g3.connect(1,2,100.0);
        assertEquals(100.0, g3.getEdge(1, 2));

        //check negative weight
        g2.connect(1,2,-0.5);
        assertEquals(-1, g2.getEdge(1,2));

    }

    /**
     * test both getV method
     * ex1.tests:
     * - try to get neighbors of non-existed node
     * - try to get neighbors of node with no neighbors
     * - try to get nodes of empty graph
     * - check get v method with valid input
     *
     * @result code will be persisted without any errors
     */
    @Test
    void getVTest(){
        makeBasicGraph();
        big_g = createGraph(20, 90, 1, -1);

        //---getV(key)---

        //check non-existed node
        assertEquals(0, g1.getV(3).size());
        //check existed node with no neighbors
        assertEquals(0, g1.getV(1).size());

        //---getV()---

        //check empty graph
        assertEquals(0, g0.getV().size());
        //check legal input
        int bigSize = big_g.nodeSize();
        assertEquals(big_g.getV().size(), bigSize);
    }

    /**
     * test remove method
     * ex1.tests:
     * - try to remove non-existed node
     * - check remove method with valid input
     *
     * @result code will be persisted without any errors
     */
    @Test
    void removeEdge(){
        makeBasicGraph();
        //check non-existed node
        assertEquals(null, g1.removeNode(4));

        //check legal input
        int sizeBefore = g1.nodeSize();
        assertSame(g1.getNode(1),g1.removeNode(1));
        assertEquals(sizeBefore-1, g1.nodeSize());
    }

    /**
     * test counters
     * ex1.tests:
     * - check modification counter, edge counter and node counter after basic operations on graph
     *
     * @result counter should be up-to-date after changes
     */
    @Test
    void counterTest(){
        big_g = createGraph(50, 200, 3, -1);

        assertEquals(big_g.getMC(), 250+wUpdateCounter);
        int neiNum = big_g.getV(10).size();
        node_info removedNode= big_g.removeNode(10);
        assertEquals(200-neiNum, big_g.edgeSize());
        assertEquals(49, big_g.nodeSize());
    }

    /**
     * test creation of big graph
     *
     * @result method will be persisted without any errors and will run in good time complexity
     */
    @Test
    void createHugeTest(){
        big_g = createGraph(50000, 300000, 1, -1);
        assertEquals(1, 1);
    }

    /**
     * public method thats creates a graph for ex1.tests
     * with a given number of node and number of edges and pre-difined range of keys and edge's weights
     *
     * @param nSize number of nodes of new graph
     * @param eSize number of edges in new graph
     * @param seed seed for pseudo-random numbers
     * @param wRange range of weights for edges
     * @return a graph
     */
    public static weighted_graph createGraph(int nSize, int eSize, int seed, int wRange){
        wUpdateCounter=0;
        numGenerator = new Random(seed);
        int max = nSize*2;
        int min = 0;
        if(wRange!=-1) {
            min=wRange;
        }
        weighted_graph graph = new WGraph_DS();
        List<Integer> keyList = new ArrayList<>();
        //generate vertexes
        while(graph.nodeSize()<nSize) {
            int nodeKey = numGenerator.nextInt(max - min) + min;
            graph.addNode(nodeKey);
            keyList.add(nodeKey);
        }

        //generate edges
        int key1 = 0, key2;
        Double w, Dmax = max+0.0, Dmin=min+0.0;

        while(graph.edgeSize() < eSize) {
            key1 = keyList.get(numGenerator.nextInt(keyList.size()));
            key2 = keyList.get(numGenerator.nextInt(keyList.size()));

            w = (numGenerator.nextInt(max - min) + min) + (numGenerator.nextDouble());

            if(graph.hasEdge(key1, key2)&&graph.getEdge(key1, key2)!=w)
                wUpdateCounter++;

            graph.connect(key1, key2, w);
        }
        return graph;
    }
}