package fms.test;

import org.junit.*;
import static org.junit.Assert.*;
import fms.models.UserModel;

public class UserModelTest {


    private UserModel model1;
    private UserModel model2;

    @Before
    public void setup() {
        model1 = new UserModel("userName","password","email","firstName","lastName", 'f',"personId");
    }

    @Test
    public void testCompare(){
        model2 = new UserModel("userName","password","email","firstName","lastName", 'f',"personId");
        assertEquals(model1.getUserName(), model2.getUserName());
        assertEquals(model1.getPassword(), model2.getPassword());
        assertEquals(model1.getEmail(), model2.getEmail());
        assertEquals(model1.getFirstName(), model2.getFirstName());
        assertEquals(model1.getLastName(), model2.getLastName());
        assertEquals(model1.getGender(),model2.getGender());
        assertEquals(model1.getPersonID(),model2.getPersonID());

    }
}
