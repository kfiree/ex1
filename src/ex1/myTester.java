package ex1;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class myTester {
    private static int wUpdateCounter;
    private weighted_graph g0, g1, g2, g3, small_g, big_g;

    private static Random numGenerator;


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

    @Test
    void getAndAddTest() {
        makeBasicGraph();
        //check addition
        node_info node = g1.getNode(1);
        assertNotEquals(null, node);

        //check key
        assertEquals(1,node.getKey());

        //check add existed key
        int sizeBefore = g1.nodeSize();
        g1.addNode(1);
        assertEquals(sizeBefore, g1.nodeSize());


        //check negative
        g1.addNode(-1);
        node = g1.getNode(-1);
        assertNotEquals(null, node);

        //check get not existed
        node = g1.getNode(3);
        assertNull(node);

    }

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

    @Test
    void getedgeTest(){
        makeBasicGraph();
        //check unexisted node
        assertEquals(-1, g1.getEdge(1,2));
        assertEquals(-1, g1.getEdge(2,1));

        //check unexisted edge
        assertEquals(-1, g2.getEdge(1,2));
        assertEquals(-1, g2.getEdge(2,1));

        //check legal
        assertTrue(g3.getEdge(1,2)==g3.getEdge(2,1));
    }

    @Test
    void connectTest(){
        makeBasicGraph();

        //check unexisted node
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

    @Test
    void getVTest(){
        makeBasicGraph();
        big_g = createGraph(20, 90, 1);

        //---getV(key)---

        //check unexisted node
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

    @Test
    void removeEdge(){
        makeBasicGraph();
        //check unexisted node
        assertEquals(null, g1.removeNode(4));

        //check legal input
        assertSame(g1.getNode(1),g1.removeNode(1));
        //TODO check edgeSize before and after
        //TODO check return node

    }

    @Test
    void counterTest(){
        big_g = createGraph(50, 200, 3);

        assertEquals(big_g.getMC(), 250+wUpdateCounter);
        int neiNum = big_g.getV(10).size();
        node_info removedNode= big_g.removeNode(10);
        assertEquals(200-neiNum, big_g.edgeSize());
        assertEquals(49, big_g.nodeSize());
    }

    @Test
    void createHugeTest(){
        big_g = createGraph(50000, 300000, 1);
        assertEquals(1, 1);
    }

    @Test
    void initAndCopyTest(){
//        big_g = createGraph(100, 200, 3);
//
//        weighted_graph_algorithms ga = new WGraph_Algo();
//        assertNull(ga.getGraph());
//
//        ga.init(big_g);
//        assertSame(ga.getGraph(), big_g);
//        weighted_graph g1 =  ga.copy();
//        ga.init(null);
//        assertNull(ga.getGraph());
//
//        g1.equals()
//
    }

    private static weighted_graph createGraph(int nSize, int eSize, int seed){
        wUpdateCounter=0;
        numGenerator = new Random(seed);
        int max = nSize*2;
        int min = 0;
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
