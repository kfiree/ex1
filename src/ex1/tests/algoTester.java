package ex1.tests;

import ex1.src.*;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.text.DecimalFormat;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 *  tester for WGraph_Algo
 *
 *  @author kfir ettinger
 */
class algoTester {

    private weighted_graph big_g;

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
    /**
     * test init and getGraph methods
     * ex1.tests:
     * - try to add a graph to an weighted_graph_algorithms object
     * - check get graph after init method
     * - check init and getGraph with null as input
     *
     * @result code will be persisted without any errors and if null sent to
     *          init then weighted_graph_algorithms should create an empty graph
     */
    @Test
    void initAndGetTest(){
        //initialize graph algo
        big_g = graphTester.createGraph(20, 50, 2, -1);
        weighted_graph g = new WGraph_DS();
        weighted_graph_algorithms ga = new WGraph_Algo();
        assertEquals(ga.getGraph(), g);

        //check init&getGraph
        ga.init(big_g);
        assertSame(ga.getGraph(), big_g);

        //check init&getGraph null
        ga.init(null);
        assertEquals(ga.getGraph(), g);

    }

    /**
     * test copy method
     * ex1.tests:
     * - try the deep copy method
     * - try copy an empty graph
     *
     * @result code will be persisted without any errors
     */
    @Test
    void CopyTest(){
        //initialize graph algo
        big_g = graphTester.createGraph(30, 100, 2, -1);
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(big_g);

        //check copy
        weighted_graph g1 =  ga.copy();
        assertNotSame(g1, ga.getGraph());
        assertEquals(g1, ga.getGraph());

        //check copy when graph is null
        weighted_graph g = new WGraph_DS();
        ga.init(null);
        g1 =  ga.copy();
        assertEquals(g1, ga.getGraph());
    }

    /**
     * test isConnected method
     * ex1.tests:
     * - check isConnected on connected and disconnected graphs
     * - check isConnected when graph is null
     *
     * @result code will be persisted without any errors
     */
    @Test
    void isConnectedTest(){
        //initialize graph algo
        weighted_graph g = graphTester.createGraph(10, 45, 1, -1);
        big_g = graphTester.createGraph(30, 300, 1, -1);
        weighted_graph_algorithms ga = new WGraph_Algo();

        //check 2 connected graphs
        ga.init(g);
        assertTrue(ga.isConnected());
        ga.init(big_g);
        assertTrue(ga.isConnected());

        //check disconnected graph
        ga.init(graphTester.createGraph(10, 0, 2, -1));
        assertFalse(ga.isConnected());

        //check when graph is null
        ga.init(null);
        assertTrue(ga.isConnected());
    }

    /**
     * test shortestPath and shortestPathDist methods
     * ex1.tests:
     * - try to find path with valid input
     * - try to find path between nodes that aren't connected
     * - try to find path between nodes that doesn't exist
     *
     * @result code will be persisted without any errors and if path does'nt exist
     *      shortestPathDist should returns -1 and shortestPath should returns null
     */
    @Test
    void shortestPathTest(){
        //create graph with all edges weight bigger then 10
        big_g = graphTester.createGraph(15, 100, 1, 10);

        //set ex1.src and dest and create shortest path between them
        Collection<node_info> nodes = big_g.getV();
        Iterator<node_info> iterator = nodes.iterator();
        node_info curr;
        List<node_info> path = new ArrayList<>();;
        int src=-1, dest=-1,prevKey=-1, i=0;
        while (iterator.hasNext()&&i<10) {
            curr = iterator.next();

            if(i==0) {
                src=curr.getKey();
            }else{
                big_g.connect(prevKey, curr.getKey(), 1);

            }
            if(i==9) {
                dest= curr.getKey();
            }
            path.add(curr);
            prevKey = curr.getKey();
            i++;
        }

        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(big_g);

        //check with legal input
        assertEquals(9, ga.shortestPathDist(src, dest));
        assertEquals(path, ga.shortestPath(src, dest));

        //check when ex1.src and dest aren't connected
        weighted_graph g = new WGraph_DS();
        g.addNode(1);
        g.addNode(2);
        ga.init(g);
        assertEquals(-1, ga.shortestPathDist(1, 2));
        assertNull(ga.shortestPath(1, 2));

        //check when dest or ex1.src doesn't exists
        assertEquals(-1, ga.shortestPathDist(1, 3));
        assertNull(ga.shortestPath(1, 3));
        assertEquals(-1, ga.shortestPathDist(3, 2));
        assertNull(ga.shortestPath(3, 2));

    }

    /**
     * test save and load methods
     * ex1.tests:
     * -try to save and load a graph and compare the graps before and after operation
     *
     * @result code will be persisted without any errors and the graph should be saved and loaded successfully
     */
    @Test
    void saveAndLoadTest(){
        big_g = graphTester.createGraph(30, 300, 1, -1);
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(big_g);

        //check save and load of graph
        String fileName = "bigGraph";
        ga.save(fileName);  //save graph
        ga.init(null);
        assertNotEquals(ga.getGraph(), big_g);

        ga.load(fileName);  //load graph
        assertEquals(ga.getGraph(), big_g);
        deleteF(fileName);  //delete after test is finished

        //check save empty graph
        weighted_graph g = new WGraph_DS();
        ga.init(g);
        fileName = "g";
        ga.save(fileName);  //save
        ga.load(fileName);  //load
        assertEquals(ga.getGraph(), g);
        deleteF(fileName);  //delete after test is finished
    }

    /**
     * private toString method to represent graph with his weights as a string
     */
    private static void toStringWithWeight(weighted_graph g){
        String niString;
        DecimalFormat df = new DecimalFormat("0.00");
        int nodeSize= g.nodeSize();
        for (node_info node : g.getV()) {
            niString = "[";
            for(node_info ni : g.getV(node.getKey())) {
                niString += ni.getKey() + " |" + df.format(g.getEdge(ni.getKey(), node.getKey()))+"|, ";
            }

            System.out.println("node := "+ node.getKey() + ", Ni := " +  niString + "]");

        }
        System.out.println("###############################################################");
    }

    /**
     * private toString method to represent graph without his weights as a string
     *
     */
    private static void toString(weighted_graph g){
        String niString;
        int nodeSize= g.nodeSize();
        for (node_info node : g.getV()) {
            niString = "[";
            for(node_info ni : g.getV(node.getKey())) {
                niString +=  ni.getKey() +"," ;
            }

            System.out.println("node := "+ node.getKey() + ", Nei := " +  niString + "]");

        }
        System.out.println("###############################################################");
    }

    /**
     * delete file after using it
     * @param fileName
     */
    public static void deleteF(String fileName){

        File file = new File(fileName);

        file.delete();
    }


}