import org.junit.Test;
import welding.property.PropertyDyStore;

public class PropertyStoreTest {


    @Test
    public void testPropertyWrite(){
        PropertyDyStore propertyDyStore = new PropertyDyStore("./property");

        propertyDyStore.updateEntityProperty(1,"name","joejoe",2012);
        propertyDyStore.updateEntityProperty(1,"name","haha",2013);
        propertyDyStore.updateEntityProperty(1,"name","lala",2023);
        propertyDyStore.updateEntityProperty(1,"nana","kaka",2023);

    }
    @Test
    public void testPropertyRead(){
        PropertyDyStore propertyDyStore = new PropertyDyStore("./property");

        Object v1 = propertyDyStore.getEntityProperty(1,"name",2013);
        System.out.println(v1);

        Object v2 = propertyDyStore._getEntityProperty(1,"name",2012);
        System.out.println(v2);
    }

}
