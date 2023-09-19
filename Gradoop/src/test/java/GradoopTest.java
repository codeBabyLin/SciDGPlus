import org.gradoop.common.model.impl.id.GradoopId;
import org.junit.Test;

public class GradoopTest {

    @Test
    public void testHaha(){
        for(int i = 0;i<10;i++){
            String id = GradoopId.get().toString();
            System.out.println(id);
        }

    }

}
