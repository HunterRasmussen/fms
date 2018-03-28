package fms.test;

import org.junit.*;
import org.junit.runners.MethodSorters;

import java.sql.SQLException;
import java.util.List;


import fms.dao.Database;
import fms.dao.PersonDao;
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
    public void testAddInvalidPerson(){
        String result = theDatabase.personTable.addPerson(new PersonModel(null,null,null,null,'f',null,null, null));
        Assert.assertNotEquals(result,"success");
    }

    @Test
    public void testGetPerson(){
        PersonModel result = theDatabase.personTable.getPerson(model1.getPersonID());
        assertEquals(result.getFirstName(), model1.getFirstName());
    }

    @Test
    public void testInvalidGetPerson(){
        PersonModel result = theDatabase.personTable.getPerson("GenericPersonID");
        assertEquals(result, null);
    }

    @Test
    public void testGetAllPeopleByUsername(){
        theDatabase.personTable.addPerson(new PersonModel("randomPerson", model1.getDescendant(), "randomFirstName", "randomLastName", 'f',null,null,null));
        List<PersonModel> results = theDatabase.personTable.getAllPeopleByUserName(model1.getDescendant());
        assertEquals(results.size(), 2);
    }

    @Test
    public void testUpdateParents(){
        theDatabase.personTable.addPerson(model1);
        PersonModel updatedModel = new PersonModel(model1.getPersonID(),
                model1.getDescendant(), model1.getFirstName(), model1.getLastName(),
                model1.getGender(), "newFather", "newMother", null);
        String result = theDatabase.personTable.updateParents(updatedModel);
        assertEquals(result, "success");
        PersonModel resultPerson = theDatabase.personTable.getPerson(model1.getPersonID());
        assertEquals(resultPerson.getFatherID(), "newFather");
        assertEquals(resultPerson.getMotherID(), "newMother");

    }

    @Test
    public void testRemovePerson(){
        removePersonResult = theDatabase.personTable.removePerson("personid1");
        assertEquals(removePersonResult, "success");
    }

    @Test
    public void testRemovePersonsByDescendant(){
        String result = theDatabase.personTable.removePersonsByDescendant("genericUsername");
        assertEquals(result,"success");
    }

    @Test
    public void testRemoveAllPersons(){
        String result = theDatabase.personTable.removeAllPersons();
        assertEquals(result, "success");
    }
}
