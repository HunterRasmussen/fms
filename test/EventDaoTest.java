package fms.test;


import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

import fms.dao.Database;
import fms.models.EventModel;

import static org.junit.Assert.assertEquals;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EventDaoTest {


    private EventModel model1;
    private EventModel model2;
    private EventModel invalidModel;
    private List<EventModel> getEventsByPersonID;
    private List<EventModel> modelList;
    private Database theDatabase;
    private String stringResult;


    @Before
    public void setup(){
        model1 = new EventModel("EventId1", "descendantId", "PersonId2", 1122, 1123, "Denmark", "Paris", "Birth", 1994);
        invalidModel = new EventModel(null, null, "personID", 2233, 3344, "France","Grenoble", "Death", 10);
        theDatabase = new Database();

    }

    @Test
    public void testAddEvent(){
        String addEventResult = theDatabase.eventsTable.addEvent(model1);
        assertEquals(addEventResult, "success");
    }

    @Test
    public void testInvalidAddEvent(){
        String invalidAddResult = theDatabase.eventsTable.addEvent(invalidModel);
        Assert.assertNotEquals("success", invalidAddResult);
    }

    @Test
    public void testGetEventsByPersonId(){
        getEventsByPersonID = theDatabase.eventsTable.getEventbyPersonId(model1.getPersonID());
        assertEquals( getEventsByPersonID.get(0).getDescendant(), "descendantId");
        assertEquals(getEventsByPersonID.size() , 2);
    }
    @Test
    public void testGetEventsByNonExistantPersonID(){
        List<EventModel> result = theDatabase.eventsTable.getEventbyPersonId("nonExistantUser");
        assertEquals(result.size(),0);
    }
    @Test
    public void testGetAllEventsByUsername(){
        EventModel testModel1 = new EventModel("EventId2", "hunter", "PersonId2", 1234, 4321, "USA" , "Auberry", "Graduation", 2012);
        String test1 = theDatabase.eventsTable.addEvent(testModel1);
        ///////----------------------------------------check me please!            ------------ ---------------               -------------------        assertEquals(test1);
    }
    @Test
    public void testGetAllEventsByInvalidUserName(){
        List<EventModel> result = theDatabase.eventsTable.getAllEventsByUsername("nonExistantUser");
        assertEquals(0, result.size());
    }

    @Test
    public void testGetEventByEventId(){
        model2 = theDatabase.eventsTable.getEventbyEventID("EventId1");
        assertEquals(model2.getEventID(), "EventId1");
        assertEquals(model2.getPersonID(), model1.getPersonID());
        assertEquals(model2.getCity(), model1.getCity());

    }
    @Test
    public void testInvalidGetEventbyEventId(){
        EventModel result = theDatabase.eventsTable.getEventbyEventID("nonExistantEvent");
        assertEquals(null,result);
    }

    @Test
    public void testRemoveEvent(){
        stringResult = theDatabase.eventsTable.removeEvent("EventId1");
        assertEquals(stringResult,"success");
        stringResult = theDatabase.eventsTable.removeEvent("EventId2");
        assertEquals(stringResult, "success");
    }

    @Test
    public void testRemoveAllEvents(){
        String result = theDatabase.eventsTable.removeAllEvents();
        assertEquals(result, "success");
    }

    @Test
    public void testRemoveAllEventsByUserName(){
        String result = theDatabase.eventsTable.removeEventsbyDescendant("genericUsername");
        assertEquals(result, "success");
    }

    @Test
    public void testRemoveAllEventsbyPersonID(){
        String result = theDatabase.eventsTable.removeEventsbyPersonId("genericPersonID");
        assertEquals(result,"success");
    }

}
