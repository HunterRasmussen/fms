package fms.test;


import org.junit.*;
import static org.junit.Assert.*;

import fms.models.AuthTokModel;

public class AuthTokModelTest {
    private AuthTokModel model1;
    private AuthTokModel model2;


    @Before
    public void setup() {
        model1 = new AuthTokModel("12345b", "myUsername");
        model2 = new AuthTokModel("12345b", "myUsername");
    }

    @Test
    public void testCompare(){
        assertEquals(model1.getUsername(), model2.getUsername());
        assertEquals(model1.getAuthTok(), model2.getAuthTok());
    }
}
