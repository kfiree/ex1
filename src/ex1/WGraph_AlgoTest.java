package ex1;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_AlgoTest {

    @Test
    void isConnected() {
        weighted_graph g0 = WGraph_DSTest.graph_creator(0,0,1);

        weighted_graph_algorithms ag0 = new WGraph_Algo();

        ag0.init(g0);
        toString(ag0.getGraph());
        toStringWithWeight(ag0.getGraph());
        assertTrue(ag0.isConnected());

        g0 = WGraph_DSTest.graph_creator(1,0,1);
        ag0 = new WGraph_Algo();
        ag0.init(g0);
        toString(ag0.getGraph());
        toStringWithWeight(ag0.getGraph());
        assertTrue(ag0.isConnected());

        g0 = WGraph_DSTest.graph_creator(2,0,1);
        ag0 = new WGraph_Algo();
        ag0.init(g0);
        toString(ag0.getGraph());
        toStringWithWeight(ag0.getGraph());
        assertFalse(ag0.isConnected());

        g0 = WGraph_DSTest.graph_creator(2,1,1);
        ag0 = new WGraph_Algo();
        ag0.init(g0);
        toString(ag0.getGraph());
        toStringWithWeight(ag0.getGraph());
        assertTrue(ag0.isConnected());

        g0 = WGraph_DSTest.graph_creator(10,30,1);
        ag0.init(g0);
        toString(ag0.getGraph());
        toStringWithWeight(ag0.getGraph());
        boolean b = ag0.isConnected();
        assertTrue(b);
    }

    @Test
    void shortestPathDist() {
        weighted_graph g0 = small_graph();
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());
//        toString(ag0.getGraph());
//        toStringWithWeight(ag0.getGraph());
        double d = ag0.shortestPathDist(0,10);
        assertEquals(d, 5.1);
    }

    @Test
    void shortestPath() {
        weighted_graph g0 = small_graph();
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        List<node_info> sp = ag0.shortestPath(0,10);
        //double[] checkTag = {0.0, 1.0, 2.0, 3.1, 5.1};
        int[] checkKey = {0, 1, 5, 7, 10};
        int i = 0;
        for(node_info n: sp) {
            //assertEquals(n.getTag(), checkTag[i]);
            assertEquals(n.getKey(), checkKey[i]);
            i++;
        }
    }

    @Test
    void save_load() {
        WGraph_DS graphObject= new WGraph_DS();
        WGraph_DS.NodeInfo node = graphObject.new NodeInfo(1);



         weighted_graph g0 = WGraph_DSTest.graph_creator(10,30,1);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        toStringWithWeight(ag0.getGraph());
        toString(ag0.getGraph());

        String str = "g0.obj";
        deleteF(str);
        ag0.save(str);
        weighted_graph g1 = WGraph_DSTest.graph_creator(10,30,1);
        ag0.load(str);
        assertEquals(g0,g1);
        g0.removeNode(0);
        assertNotEquals(g0,g1);
    }

    private weighted_graph small_graph() {
        weighted_graph g0 = WGraph_DSTest.graph_creator(11,0,1);
        g0.connect(0,1,1);
        g0.connect(0,2,2);
        g0.connect(0,3,3);

        g0.connect(1,4,17);
        g0.connect(1,5,1);
        g0.connect(2,4,1);
        g0.connect(3, 5,10);
        g0.connect(3,6,100);
        g0.connect(5,7,1.1);
        g0.connect(6,7,10);
        g0.connect(7,10,2);
        g0.connect(6,8,30);
        g0.connect(8,10,10);
        g0.connect(4,10,30);
        g0.connect(3,9,10);
        g0.connect(8,10,10);

        return g0;
    }


    public static void toString(weighted_graph g){
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

    public static void toStringWithWeight(weighted_graph g){
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

    public static void deleteF(String fileName){

        File file = new File(fileName);

        if(file.delete())
        {
            System.out.println("File deleted successfully");
        }
        else
        {
            System.out.println("Failed to delete the file");
        }
    }
}