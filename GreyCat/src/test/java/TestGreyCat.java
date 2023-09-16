import greycat.*;
import greycat.leveldb.LevelDBStorage;
import org.junit.Test;

public class TestGreyCat {

    @Test
    public void testWrite(){

        Graph graph = new GraphBuilder()
                .withMemorySize(1000)
                .withStorage(new LevelDBStorage("./greycat_db"))
                .build();
        graph.connect(isConnected->{
            long t1 = 1;
            Node n1 = graph.newNode(0,t1);
            n1.set("name", Type.STRING,"momo");
            Node n2 = graph.newNode(0,t1);
            n2.set("name", Type.STRING,"jojo");
            n1.addToRelation("know",n2);

            long t2 = 2;
            n1 = graph.newNode(0,t2);
            n1.set("name", Type.STRING,"haha");
            n2 = graph.newNode(0,t1);
            n2.set("name", Type.STRING,"lala");


            long t3 = 3;
            n1 = graph.newNode(0,t3);
            n1.set("name", Type.STRING,"anan");
            n2 = graph.newNode(0,t1);
            n2.set("name", Type.STRING,"zukou");

        });
    }
    @Test
    public void testRead(){

        Graph graph = new GraphBuilder()
                .withMemorySize(1000)
                .withStorage(new LevelDBStorage("./greycat_db"))
                .build();
        long t1 = 1;
        long t2 = 2;
        long t3 = 3;
        graph.connect(isConnected->{

           // graph.storage().
            //lookupAll(long world, long time, long[] ids, Callback<Node[]> callback)
            long[] ids = new long[1];
           graph.lookupAll(0,t2,ids, nodes->{
               for(Node n: nodes){
                  // n.get("name");
                   System.out.print(n.get("name"));
               }

           });

        });
    }
}





























