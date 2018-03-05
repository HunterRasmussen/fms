package fms.test;


import org.junit.*;
import static org.junit.Assert.*;

import fms.models.AuthTokModel;

public class AuthTokModelTest {
    private AuthTokModel model1;
    private AuthTokModel model2;


    @Before
    public void setup() {
        model1 = new AuthTokModel("12345b", "myUsername", "personid1");
        model2 = new AuthTokModel("12345b", "myUsername", "personid2");
    }

    @Test
    public void testCompare(){
        assertEquals(model1.getUserName(), model2.getUserName());
        assertEquals(model1.getAuthTok(), model2.getAuthTok());
    }
}
