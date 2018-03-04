package fms.test;

import org.junit.*;
import static org.junit.Assert.*;


public class TestJunit {
    private String str;
    @Before
    public void setup(){
        str = "Junit is working fine";
    }
    @Test
    public void testAdd() {
        assertEquals("Junit is working fine",str);
    }
}


