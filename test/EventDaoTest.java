package fms.test;


import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.sql.SQLException;
import java.util.List;

import fms.dao.Database;
import fms.models.EventModel;
import fms.results.SingleEventResult;

import static org.junit.Assert.assertEquals;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EventDaoTest {


    private EventModel model1;
    private EventModel model2;
    private List<EventModel> getEventsByPersonID;
    private List<EventModel> modelList;
    private Database theDatabase;
    private SingleEventResult addEventResult;
    private String stringResult;


    @Before
    public void setup(){
        model1 = new EventModel("EventId1", "descendantId", "PersonId2", 1122, 1123, "Denmark", "Paris", "Birth", 1994);
        theDatabase = new Database();

    }

    @Test
    public void testAddEvent(){
        String addResult = theDatabase.eventsTable.addEvent(model1);
        assertEquals(addEventResult.event.getEventID(), "EventId1");
        assertEquals(addEventResult.getSuccessFlag(), true);
        assertEquals(addEventResult.event.getPersonID(), "PersonId2");
        assertEquals(addEventResult.event.getLatitude(), 1122);
    }

    @Test
    public void testGetEventsByPersonId(){
        getEventsByPersonID = theDatabase.eventsTable.getEventbyPersonId(model1.getPersonID());
        assertEquals( getEventsByPersonID.get(0).getDescendant(), "descendantId");
        assertEquals(getEventsByPersonID.size() , 2);
    }

    @Test
    public void testGetAllEventsByUsername(){
        EventModel testModel1 = new EventModel("EventId2", "hunter", "PersonId2", 1234, 4321, "USA" , "Auberry", "Graduation", 2012);
        String test1 = theDatabase.eventsTable.addEvent(testModel1);
    }

    @Test
    public void testGetEventByEventId(){
        model2 = theDatabase.eventsTable.getEventbyEventID("EventId1");
        assertEquals(model2.getEventID(), "EventId1");
        assertEquals(model2.getPersonID(), model1.getPersonID());
        assertEquals(model2.getCity(), model1.getCity());

    }




    @Test
    public void testRemoveEvent(){
        stringResult = theDatabase.eventsTable.removeEvent("EventId1");
        assertEquals(stringResult,"success");
        stringResult = theDatabase.eventsTable.removeEvent("EventId2");
        assertEquals(stringResult, "success");
    }


}
