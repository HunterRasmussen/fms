package fms.test;

import org.junit.*;

import java.sql.SQLException;

import fms.dao.Database;
import fms.models.UserModel;
import fms.results.LoginRegisterResult;

import static org.junit.Assert.*;

public class UserDaoTest {

    private UserModel model1;
    private UserModel model2;
    private Database theDatabase;
    private LoginRegisterResult ourResult;
    private String stringResult;

    @Before
    public void setup(){
        model1 = new UserModel("username" ,"password" , "email@gmail.com", "firstname", "lastName", 'm', "personID");
        theDatabase = new Database();


    }


    @Test
    public void testAddUser(){
        try{
           ourResult = theDatabase.usersTable.addUser(model1);
        }
        catch(SQLException e){
            System.out.println("Line 48 User Dao Test, shouldn't be here...  Exceptions should be handled below this level");
        }
        assertEquals(ourResult.getUsername(), "username");
        assertEquals(ourResult.isSuccessFlag(), true);
        assertEquals(ourResult.getPersonId(), "personId");
    }

    @Test
    public void testGetUser(){
        model2 = theDatabase.usersTable.getUser("username");
        if(model2.successFlag == true){

            assertEquals(model1.getUsername(),model2.getUsername());
            assertEquals(model1.getPassword(), model2.getPassword());
            assertEquals(model1.getEmail(),model2.getEmail());
            assertEquals(model1.getFirstName(),model2.getFirstName());
            assertEquals(model1.getLastName(), model2.getLastName());
            assertEquals(model1.getGender(), model2.getGender());
            assertEquals(model1.getPersonId(), model2.getPersonId());
        }

    }

    @Test
    public void testGetNonExistantUser(){
        model2 = theDatabase.usersTable.getUser("freddy");
        assertEquals(model2.getUsername(), null);
        assertEquals(model2.successFlag, false);
        assertEquals(model2.successMessage, "[SQLITE_ERROR] SQL error or missing database (no such table: users)");
    }



    @Test
    public void testRemoveUser(){
        stringResult = theDatabase.usersTable.removeUser("username");
        assertEquals(stringResult,"good");
    }







}
