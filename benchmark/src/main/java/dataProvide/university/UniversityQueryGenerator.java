package dataProvide.university;

//import dataProvide.dppin.DPPINQueryGenerator;
import dataConfig.QueryGenerator;
import dataConfig.QueryGeneratorConfig;
import dataProvide.dppin.PPINDataTestConfig;

public class UniversityQueryGenerator {

    public static void main(String[]args){
        QueryGeneratorConfig qgc = new UniversityDataTestConfig();
        QueryGenerator qg = new QueryGenerator(qgc);
        qg.generate();
    }
}
