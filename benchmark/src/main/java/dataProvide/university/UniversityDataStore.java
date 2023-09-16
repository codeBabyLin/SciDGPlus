package dataProvide.university;

//import dataProvide.coautor.CoauthorQueryGenerator;
//import dataProvide.coautor.CoauthorQueryOperation;
import dataConfig.DataStore;
import operation.VersionGraphStore;
import org.apache.commons.lang3.tuple.Pair;
import sampleGraph.SampleGraph;
import dataProvide.university.Instance.*;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

public class UniversityDataStore implements DataStore{

    private int versionSize;
    private String dataPath;
    public UniversityDataStore(int versionSize){
        this.versionSize =versionSize;
        this.dataPath = System.getProperty("user.dir")+"\\Csv";
    }

    void readCourse(String fileName, HashMap<Integer, Course> courses){
        try {
            RandomAccessFile fp = new RandomAccessFile(new File(fileName), "r");
            String str;
            while ((str = fp.readLine()) != null) {
                if(str.contains("id")){
                    continue;
                }
                String []data = str.split(",");
                Course c= new Course();
                c.setName(data[1]);
                courses.put(new Integer(data[0]),c);
            }
            fp.close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    void readProfessor(String fileName,HashMap<Integer, Professor> professors){
        try {
            RandomAccessFile fp = new RandomAccessFile(new File(fileName), "r");
            String str;
            while ((str = fp.readLine()) != null) {
                if(str.contains("id")){
                    continue;
                }
                String []data = str.split(",");
                Professor p= new Professor();
                p.setName(data[1]);
                professors.put(new Integer(data[0]),p);
            }
            fp.close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    void readStudents(String fileName,HashMap<Integer, Student> students){
        try {
            RandomAccessFile fp = new RandomAccessFile(new File(fileName), "r");
            String str;
            while ((str = fp.readLine()) != null) {
                if(str.contains("id")){
                    continue;
                }
                String []data = str.split(",");
                Student p= new Student();
                p.setName(data[1]);
                students.put(new Integer(data[0]),p);
            }
            fp.close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    void readPubs(String fileName,HashMap<Integer, Publication> pubs){
        try {
            RandomAccessFile fp = new RandomAccessFile(new File(fileName), "r");
            String str;
            while ((str = fp.readLine()) != null) {
                if(str.contains("id")){
                    continue;
                }
                String []data = str.split(",");
                Publication p= new Publication();
                p.setName(data[1]);
                pubs.put(new Integer(data[0]),p);
            }
            fp.close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    void readRelations(String fileName,HashMap<Integer, ArrayList<Integer>> relations){
        try {
            RandomAccessFile fp = new RandomAccessFile(new File(fileName), "r");
            String str;
            while ((str = fp.readLine()) != null) {
                if(str.contains("id")){
                    continue;
                }
                String []data = str.split(",");
                if(data.length<=1){
                    continue;
                }
                Integer eId = new Integer(data[0]);
                String []strIds = data[1].split(" ");
                ArrayList<Integer> ids = new ArrayList<>();
                for(int i = 0;i<strIds.length;i++){
                    ids.add(new Integer(strIds[i]));
                }
                relations.put(eId,ids);
            }
            fp.close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void writeNamedEntity(RandomAccessFile fp, HashMap<Integer, ? extends NamedEntity> entities) throws IOException {
        fp.writeInt(entities.size());
        entities.forEach((integer, entity) -> {
            try {
                fp.writeInt(integer);
                fp.writeUTF(entity.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    private void readNamedEntity(RandomAccessFile fp, HashMap<Integer, NamedEntity> entities) throws IOException {
        int size = fp.readInt();
        for(int i = 0;i<size;i++){

            int id = fp.readInt();
            String name = fp.readUTF();
            NamedEntity ne = new NamedEntity();
            ne.setName(name);
            entities.put(id,ne);
        }
    }
    private void writeRelations(RandomAccessFile fp,HashMap<Integer, ArrayList<Integer>> relations) throws IOException {
        fp.writeInt(relations.size());
        relations.forEach((integer, integers) -> {
            try {
                fp.writeInt(integer);
                fp.writeInt(integers.size());
                integers.forEach(integer1 -> {
                    try {
                        fp.writeInt(integer1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });
    }
    private void readRelations(RandomAccessFile fp,HashMap<Integer, ArrayList<Integer>> relations) throws IOException {
        int size = fp.readInt();
        for(int i = 0;i<size;i++){
            int id = fp.readInt();
            int idSize = fp.readInt();
            ArrayList<Integer> ids = new ArrayList<>();
            for(int j= 0;j<idSize;j++){
                ids.add(fp.readInt());
            }
            relations.put(id,ids);
        }
    }


    public SampleGraph getSamppleGraph(int version,String path){
        HashMap<Integer, Course> courses = new HashMap<>();
        HashMap<Integer, Professor> professors = new HashMap<>();
        HashMap<Integer, Student> students = new HashMap<>();
        HashMap<Integer, Publication> pubs = new HashMap<>();

        HashMap<Integer, Course> coursesAdd = new HashMap<>();
        HashMap<Integer, Professor> professorsAdd = new HashMap<>();
        HashMap<Integer, Student> studentsAdd = new HashMap<>();
        HashMap<Integer, Publication> pubsAdd = new HashMap<>();

        HashMap<Integer, Course> coursesDel = new HashMap<>();
        HashMap<Integer, Professor> professorsDel = new HashMap<>();
        HashMap<Integer, Student> studentsDel = new HashMap<>();
        HashMap<Integer, Publication> pubsDel = new HashMap<>();

        HashMap<Integer, ArrayList<Integer>> takeCourses = new HashMap<>();
        HashMap<Integer, ArrayList<Integer>> teachCourses = new HashMap<>();
        HashMap<Integer, ArrayList<Integer>> authorsOf = new HashMap<>();

        HashMap<Integer, ArrayList<Integer>> takeCoursesAdd = new HashMap<>();
        HashMap<Integer, ArrayList<Integer>> teachCoursesAdd = new HashMap<>();
        HashMap<Integer, ArrayList<Integer>> authorsOfAdd = new HashMap<>();

        HashMap<Integer, ArrayList<Integer>> takeCoursesDel = new HashMap<>();
        HashMap<Integer, ArrayList<Integer>> teachCoursesDel = new HashMap<>();
        HashMap<Integer, ArrayList<Integer>> authorsOfDel = new HashMap<>();

        HashMap<Integer,Integer> nodeIds = new HashMap<>(); //id1 -> nodeId
        HashMap<Pair<Integer,Integer>,Integer>  relIds = new HashMap<>(); //id1_id2 -> relId

        String []fileNames = new String[]{"course.csv","authors.csv","professor.csv","publication.csv","student.csv","takeCourse.csv","teach.csv","webCourse.csv"};


        for(int j = 0;j<fileNames.length;j++){
            String fileName = String.format("%s\\V%d\\University0_0\\%s",path,version,fileNames[j]);
            switch (fileNames[j]){
                case "course.csv": readCourse(fileName,courses);break;
                case "authors.csv": readRelations(fileName,authorsOf);break;
                case "professor.csv":readProfessor(fileName,professors);break;
                case "publication.csv":readPubs(fileName,pubs);break;
                case "student.csv":readStudents(fileName,students);break;
                case "takeCourse.csv": readRelations(fileName,takeCourses);break;
                case "teach.csv": readRelations(fileName,teachCourses);break;
                case "webCourse.csv": readCourse(fileName,courses);break;
            }
        }

        SampleGraph sg = new SampleGraph();
        courses.forEach((integer, entity) -> {
            String label = "course";
            HashMap<String,Object> p = new HashMap<>();
            p.put("name",entity.getName());
            sg.addNode(integer,label,p);
        });
        professors.forEach((integer, entity) -> {
            String label = "professor";
            HashMap<String,Object> p = new HashMap<>();
            p.put("name",entity.getName());
            sg.addNode(integer,label,p);
        });

        students.forEach((integer, entity) -> {
            String label = "student";
            HashMap<String,Object> p = new HashMap<>();
            p.put("name",entity.getName());
            sg.addNode(integer,label,p);
        });
        pubs.forEach((integer, entity) -> {
            String label = "pubs";
            HashMap<String,Object> p = new HashMap<>();
            p.put("name",entity.getName());
            sg.addNode(integer,label,p);
        });

        authorsOf.forEach((integer, integers) -> {
            String type = "coauthor";
            integers.forEach(integer1 -> {
                sg.addRel(Pair.of(integer,integer1),type,new HashMap<>());
            });
        });
        teachCourses.forEach((integer, integers) -> {
            String type = "teachCourse";
            integers.forEach(integer1 -> {
                sg.addRel(Pair.of(integer,integer1),type,new HashMap<>());
            });
        });
        takeCourses.forEach((integer, integers) -> {
            String type = "takeCourse";
            integers.forEach(integer1 -> {
                sg.addRel(Pair.of(integer,integer1),type,new HashMap<>());
            });
        });
        return sg;

/*        if(i>=1){
            String []deltaFileNames;
            String [] add = new String[]{"courseADD.csv","authorsAdd.csv","professorADD.csv","publicationADD.csv","studentsADD.csv","takeCourseAdd.csv","teachAdd.csv","webCourseAdd.csv"};
            String [] del = new String[]{"authorsDel.csv","publicationDel.csv","takeCourseDel.csv"};

            deltaFileNames = i%2==0?add:del;
            for(int j = 0;j<deltaFileNames.length;j++){
                String fileName = String.format("%s\\V%d\\University0_0\\change\\%s",path,i,deltaFileNames[j]);
                switch (deltaFileNames[j]){
                    case "courseADD.csv": readCourse(fileName,coursesAdd);break;
                    case "authorsAdd.csv": readRelations(fileName,authorsOfAdd);break;
                    case "professorADD.csv":readProfessor(fileName,professorsAdd);break;
                    case "publicationADD.csv":readPubs(fileName,pubsAdd);break;
                    case "studentsADD.csv":readStudents(fileName,studentsAdd);break;
                    case "takeCourseAdd.csv": readRelations(fileName,takeCoursesAdd);break;
                    case "teachAdd.csv": readRelations(fileName,teachCoursesAdd);break;
                    case "webCourseAdd.csv": readCourse(fileName,coursesAdd);break;
                    case "authorsDel.csv":readRelations(fileName,authorsOfDel);break;
                    case "publicationDel.csv": readPubs(fileName,pubsDel);break;
                    case "takeCourseDel.csv": readRelations(fileName,takeCoursesDel);break;
                }
            }
        }*/
    }



    @Override
    public void storeVersionGraph(VersionGraphStore graphDb) {
        graphDb.begin();
        for(int i = 0;i<this.versionSize;i++){
            SampleGraph sg = getSamppleGraph(i,this.dataPath);
            graphDb.storeGraph(sg,i);
        }
        graphDb.finish();

    }
}
