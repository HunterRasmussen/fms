package fms.test;


import org.junit.*;
import static org.junit.Assert.*;
import fms.models.PersonModel;

public class PersonModelTest {

    private PersonModel model1;
    private PersonModel model2;

    @Before
    public void setup() {
        model1 = new PersonModel("person", "descendant", "firstname", "lastnme", 'f', "father","mother","spouse");
    }

    @Test
    public void testCompare(){
        model2 = new PersonModel("person", "descendant", "firstname", "lastnme", 'f', "father","mother","spouse");
        assertEquals(model1.getPersonID(), model2.getPersonID());
        assertEquals(model1.getDescendant(), model2.getDescendant());
        assertEquals(model1.getFirstName(),model2.getFirstName());
        assertEquals(model1.getLastName(), model2.getLastName());
        assertEquals(model1.getGender(), model2.getGender());
        assertEquals(model1.getFatherID(),model2.getFatherID());
        assertEquals(model1.getMotherID(), model2.getMotherID());
        assertEquals(model1.getSpouseID(), model2.getSpouseID());
    }
}
