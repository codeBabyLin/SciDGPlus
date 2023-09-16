import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.chronos.chronograph.api.branch.ChronoGraphBranchManager;
import org.chronos.chronograph.api.branch.GraphBranch;
import org.chronos.chronograph.api.structure.ChronoGraph;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class ChronoGraphTest {

    @Test
    public void testWrite(){

        String path = "./ChronoGraphTest";
        if(!new File(path).exists()) new File(path).mkdirs();


        ChronoGraph graph = ChronoGraph.FACTORY.create()
                .exodusGraph("./ChronoGraphTest")
                //.withTransactionAutoStart(false)
                .build();
        ChronoGraph txGraph = graph.tx().createThreadedTx();
        Vertex v = txGraph.addVertex("person");
        v.property("name","joejoe");
        long t1 = txGraph.tx().commitAndReturnTimestamp(1l);

        txGraph = graph.tx().createThreadedTx();
        v = txGraph.addVertex("person");
        v.property("name","momo");
        long t2 = txGraph.tx().commitAndReturnTimestamp(2l);

        txGraph = graph.tx().createThreadedTx();
        v = txGraph.addVertex("person");
        v.property("name","an");
        long t3 = txGraph.tx().commitAndReturnTimestamp(3l);


        System.out.println(graph.countCommitTimestamps());
        System.out.println(t1);
        System.out.println(t2);
        System.out.println(t3);
       // graph.tx().commit();

    }

    @Test
    public void testRead(){

       /* 3
        1694861397416
        1694861397427
        1694861397431*/


       //long t1 = 1694861397416;


        String path = "./ChronoGraphTest";
        if(!new File(path).exists()) new File(path).mkdirs();


        ChronoGraph graph = ChronoGraph.FACTORY.create()
                .exodusGraph("./ChronoGraphTest")
                //.withTransactionAutoStart(false)
                .build();

       // graph.get


        ChronoGraph txGraph = graph.tx().createThreadedTx(1694861397416l);
        int size = txGraph.traversal().V().toList().size();
        System.out.println(" version 1 : "+size);

        txGraph = graph.tx().createThreadedTx(1694861397427l);
        size = txGraph.traversal().V().toList().size();
        System.out.println(" version 2 : "+size);

        txGraph = graph.tx().createThreadedTx(1694861397431l);
        size = txGraph.traversal().V().toList().size();
        System.out.println(" version 3 : "+size);

    }

    @Test
    public void testTime(){
        long yesterday = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1);
        System.out.println(yesterday);
    }


    @Test
    public void testBranchWrite(){

        String path = "./ChronoGraphTest";
        if(!new File(path).exists()) new File(path).mkdirs();


        ChronoGraph graph = ChronoGraph.FACTORY.create()
                .exodusGraph("./ChronoGraphTest")
                //.withTransactionAutoStart(false)
                .build();
        ChronoGraphBranchManager branchManager = graph.getBranchManager();

        branchManager.createBranch("master","1");

        ChronoGraph txGraph = graph.tx().createThreadedTx("1");
        Vertex v = txGraph.addVertex("person");
        v.property("name","joejoe");
        long t1 = txGraph.tx().commitAndReturnTimestamp(1l);


        branchManager.createBranch("master","2");

        txGraph = graph.tx().createThreadedTx("2");
        v = txGraph.addVertex("person");
        v.property("name","momo");
        long t2 = txGraph.tx().commitAndReturnTimestamp(2l);


        branchManager.createBranch("master","3");
        txGraph = graph.tx().createThreadedTx("3");
        v = txGraph.addVertex("person");
        v.property("name","an");
        long t3 = txGraph.tx().commitAndReturnTimestamp(3l);


        System.out.println(graph.countCommitTimestamps());
        System.out.println(t1);
        System.out.println(t2);
        System.out.println(t3);
        // graph.tx().commit();

    }


    @Test
    public void testBranchRead(){


        String path = "./ChronoGraphTest";
        if(!new File(path).exists()) new File(path).mkdirs();


        ChronoGraph graph = ChronoGraph.FACTORY.create()
                .exodusGraph("./ChronoGraphTest")
                //.withTransactionAutoStart(false)
                .build();

        ChronoGraphBranchManager branchManager = graph.getBranchManager();
        branchManager.getBranches().forEach(graphBranch->{
            //graphBranch.getOrigin()
            System.out.print("current: "+ graphBranch.getName() +"->");
            graphBranch.getOriginsRecursive().forEach(graphBranch1 -> {
                System.out.print(graphBranch1.getName()+"->");
            });
            System.out.println();
        });
        //System.out.println(branchManager.getBranches().);


        ChronoGraph txGraph = graph.tx().createThreadedTx("1");
        int size = txGraph.traversal().V().toList().size();
        System.out.println(" version 1 : "+size);

        txGraph = graph.tx().createThreadedTx("2");
        size = txGraph.traversal().V().toList().size();
        System.out.println(" version 2 : "+size);

        txGraph = graph.tx().createThreadedTx("3");
        size = txGraph.traversal().V().toList().size();
        System.out.println(" version 3 : "+size);

    }

}
