package fms.test;

import org.junit.*;
import org.junit.runners.MethodSorters;

/**
 *
 */
import fms.dao.Database;
import fms.models.AuthTokModel;
import fms.results.LoginRegisterResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuthTokDaoTest {

    private AuthTokModel model1;
    private AuthTokModel model2;
    private Database theDatabase;
    private LoginRegisterResult addAuthTokResult;
    private String removeAuthTokResult;
    private String getUserNameResult;


    @Before
    public void setup(){
        model1 = new AuthTokModel("1a2b3c","hunter");
        model2 = new AuthTokModel();
        model2.setAuthTok("222221");
        theDatabase = new Database();
    }

    @Test
    public void testAddAuthTok(){
        addAuthTokResult = theDatabase.authTokTable.addAuthTok(model1);
        assertEquals(addAuthTokResult.getUsername(),"hunter" );
        assertEquals(addAuthTokResult.getAuthTok(), "1a2b3c");
    }

    @Test
    public void testAddInvalidAuthTok(){
        addAuthTokResult = theDatabase.authTokTable.addAuthTok(model2);
        assertEquals(addAuthTokResult.getErrorMessage(), "failure");
    }

    @Test
    public void testGetUsername(){
        getUserNameResult = theDatabase.authTokTable.getUser("1a2b3c");
        assertEquals(getUserNameResult, "hunter");
    }

    @Test
    public void testGetNonPresentUsername(){
        String result = theDatabase.authTokTable.getUser("2");
        assertNull(result);
    }


    @Test
    public void testRemoveAuthTok(){
        removeAuthTokResult = theDatabase.authTokTable.removeAuthTok("1a2b3c");
        assertEquals(removeAuthTokResult, "success");
    }



}
