package fms.test;


import org.junit.*;

import fms.models.EventModel;

import static org.junit.Assert.*;

public class EventModelTest {
    private EventModel model1;
    private EventModel model2;


    @Before
    public void setup() {
        model1 = new EventModel("eventId","descendant","person",1111,2222,"france","vannes","birth",1);
    }


    @Test
    public void testCompare(){
        model2 = new EventModel("eventId","descendant","person",1111,2222,"france","vannes","birth",1);
        assertEquals(model1.getEventID(),model2.getEventID());
        assertEquals(model1.getDescendant(), model2.getDescendant());
        assertEquals(model1.getPersonID(), model2.getPersonID());
        assertEquals(model1.getCountry(), model2.getCountry());
        assertEquals(model1.getCity(), model2.getCity());
        assertEquals(model1.getEventType(), model2.getEventType());
        assertEquals(model1.getYear(), model2.getYear());
    }

}
