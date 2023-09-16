import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Random;
import statistic.statistic;
public class HellTest {
    public static void main(String []args){
        Random random = new Random();
        for(int i =1;i<=20;i++){
            int x = random.nextInt(5)+1;
            System.out.println(x);
        }

        //OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        //String res = operatingSystemMXBean.toString();
        //res = operatingSystemMXBean.getName();
        new statistic().testSystemUsage();
        System.out.println("hello world");
        //System.out.println(res);
    }

}
