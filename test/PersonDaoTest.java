package fms.test;

import org.junit.*;
import org.junit.runners.MethodSorters;

import java.sql.SQLException;


import fms.dao.Database;
import fms.models.PersonModel;
import fms.results.SinglePersonResult;

import static org.junit.Assert.assertEquals;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PersonDaoTest {

    private PersonModel model1;
    private PersonModel model2;
    private String addPersonResult;
    private String removePersonResult;
    private Database theDatabase;

    @Before
    public void setup(){
        model1 = new PersonModel();
        model1.setPersonID("personid1");
        model1.setDescendant("hunter");
        model1.setFirstName("Cort");
        model1.setLastName("Rasmussen");
        model1.setGender('m');
        model1.setFatherID("10");
        model1.setMotherID(null);
        //model1.setSpouseId(null);
        theDatabase = new Database();
    }

    @Test
    public void testAddPerson(){
        addPersonResult = theDatabase.personTable.addPerson(model1);
        assertEquals(addPersonResult, "success");
    }


    @Test
    public void testRemovePerson(){
        removePersonResult = theDatabase.personTable.removePerson("personid1");
        assertEquals(removePersonResult, "success");
    }
}
