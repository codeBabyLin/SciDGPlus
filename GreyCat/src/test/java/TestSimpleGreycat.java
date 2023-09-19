import greycat.*;
import greycat.leveldb.LevelDBStorage;
import org.junit.Test;

public class TestSimpleGreycat {



    @Test
    public void testWrite(){
        Graph graph = new GraphBuilder()
                .withMemorySize(1000)
                .withStorage(new LevelDBStorage("./greycat_db_1"))
                .build();
        graph.connect(isConnected->{
            long t1 = 1;
            Node n1 = graph.newNode(0, 0);
            n1.set("name", Type.STRING, "momo");
            Node n2 = graph.newNode(0, 3);
            n2.set("name", Type.STRING, "jojo");
            n1.addToRelation("know", n2);

            Node finalN1 = n1;
            Node finalN2 = n2;
            graph.index(0, 1, "test", nodeIndex -> {
                nodeIndex.addToIndex(finalN1);
                //nodeIndex.re
                //nodeIndex.addToIndex(finalN1);
                graph.save(res -> {
                });
            });

            //graph.index();
            graph.index(0, 2, "test", nodeIndex -> {
                Node n3 = graph.newNode(0, 3);
                n3.set("name", Type.STRING, "anan");
               // nodeIndex.addToIndex(finalN);
                nodeIndex.addToIndex(finalN2);
                nodeIndex.addToIndex(n3);
                graph.save(res -> {
                });
            });
        });
    }

    @Test
    public void testRead(){
        Graph graph = new GraphBuilder()
                .withMemorySize(1000)
                .withStorage(new LevelDBStorage("./greycat_db_1"))
                .build();
        graph.connect(isConnected->{

            graph.index(0, 3, "test", nodeIndex -> {
                nodeIndex.find(new Callback<Node[]>() {
                    @Override
                    public void on(Node[] nodes) {
                        for(Node n: nodes){
                            System.out.println(n.get("name")+":"+n.world());
                            n.relation("", new Callback<Node[]>() {
                                @Override
                                public void on(Node[] nodes) {
                                    for(Node n1: nodes){
                                        String name1 = (String) n.get("name");
                                        String name2 = (String) n1.get("name");
                                        String  res = String.format("%s->%s",name1,name2);
                                        System.out.println(res);
                                    }
                                    //System.out.println()
                                }
                            });
                        }
                    }
                });
            });
        });
    }


}
