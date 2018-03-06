package fms.dao;

import java.awt.Event;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fms.models.EventModel;
import fms.results.SingleEventResult;

/**
 * This class interacts with the Event database Table
 */
public class EventDao {

    private Database database;


    EventDao(Database db){
        database = db;
    }


    /**
     * Will add a new event to the Event database table.
     *
     * @return If adding a person is successful,
     *              it will create a PersonModel and a Single PersonResult object.
     *      If it is unsuccessful,
     *          It will create a SinglePersonResult object and mark the flag false and give an error message
     */
    public SingleEventResult addEvent(EventModel toAdd){

        PreparedStatement addEventStmt = null;
        String addEventSql = "insert into Event (EVENTID, DESCENDANTID, EVENTTYPE, PERSONID," +
                " LATITUDE, LONGITUDE, COUNTRY, CITY, YEAR) VALUES(?,?,?,?,?,?,?,?,?)";
        SingleEventResult toReturn = new SingleEventResult();

        try{
            addEventStmt = database.connection.prepareStatement(addEventSql);
            addEventStmt.setString(1,toAdd.getEventId());
            addEventStmt.setString(2, toAdd.getDescendantId());
            addEventStmt.setString(3, toAdd.getEventType());
            addEventStmt.setString(4, toAdd.getPersonId());
            addEventStmt.setString(5, toAdd.getLatitude());
            addEventStmt.setString(6, toAdd.getLongitude());
            addEventStmt.setString(7, toAdd.getCountry());
            addEventStmt.setString(8, toAdd.getCity());
            addEventStmt.setString(9, toAdd.getYear());

            if(addEventStmt.executeUpdate() == 1){
                toReturn.setSuccessFlag(true);
                toReturn.event = toAdd;
                if(addEventStmt != null){
                    addEventStmt.close();
                }
                return toReturn;
            }
        }
        catch(SQLException e){
            toReturn.setSuccessFlag(false);
            toReturn.setErrorMessage(e.getMessage());
            toReturn.event = null;
            return toReturn;
        }

        toReturn.setErrorMessage("Got to the end of the method.  Shouldn't have.");
        return toReturn;
    }


    /**
     *
     *
     * @return a string declaring wether it succeeded or not.
     */
    public String removeEvent(String eventId){
        PreparedStatement removeEventStmt = null;
        String toReturn;
        try{
            String removeEventSql = "delete from Event where EVENTID = ?";
            removeEventStmt = database.connection.prepareStatement(removeEventSql);
            removeEventStmt.setString(1,eventId);
            removeEventStmt.executeUpdate();
            removeEventStmt.close();
            toReturn = "success";
            return toReturn;
        }
        catch(SQLException e){
            toReturn = e.getMessage();
            return toReturn;
        }



    }

    public String removeEventsbyDescendantId(String descendantId){
        PreparedStatement removeEventsStmt = null;
        String toReturn;
        try{
            String removeEventSql = "delete from Event where DESCENDANTID = ?";
            removeEventsStmt = database.connection.prepareStatement(removeEventSql);
            removeEventsStmt.setString(1,descendantId);
            removeEventsStmt.executeUpdate();
            removeEventsStmt.close();
            toReturn = "success";
            return toReturn;
        }
        catch(SQLException e){
            toReturn = e.getMessage();
            return toReturn;
        }
    }

    public String removeEventsbyPersonId(String personId){
        PreparedStatement removeEventsStmt = null;
        String toReturn;
        try{
            String removeEventSql = "delete from Event where PERSONID = ?";
            removeEventsStmt = database.connection.prepareStatement(removeEventSql);
            removeEventsStmt.setString(1,personId);
            removeEventsStmt.executeUpdate();
            removeEventsStmt.close();
            toReturn = "success";
            return toReturn;
        }
        catch(SQLException e){
            toReturn = e.getMessage();
            return toReturn;
        }
    }


    public List<EventModel> getAllEventsByUsername (String username){
        List<EventModel> toReturn = null;
        PreparedStatement getEventsStmt = null;
        ResultSet resultSet = null;
        try{
            String getEventsSql = "select * from Event where DESCENDANTID = ?";
            getEventsStmt = database.connection.prepareStatement(getEventsSql);
            getEventsStmt.setString(1,username);
            resultSet = getEventsStmt.executeQuery();
            toReturn = new ArrayList<EventModel>();
            while(resultSet.next()){
                EventModel eventToAdd = new EventModel();
                eventToAdd.setEventId(resultSet.getString(1));
                eventToAdd.setDescendantId(resultSet.getString(2));
                eventToAdd.setEventType(resultSet.getString(3));
                eventToAdd.setPersonId(resultSet.getString(4));
                eventToAdd.setLatitude(resultSet.getString(5));
                eventToAdd.setLongitude(resultSet.getString(6));
                eventToAdd.setCountry(resultSet.getString(7));
                eventToAdd.setCity(resultSet.getString(8));
                eventToAdd.setYear(resultSet.getString(9));
                toReturn.add(eventToAdd);
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return toReturn;
    }

    public List<EventModel> getEventbyPersonId(String PersonId){
        List<EventModel> toReturn = null;
        PreparedStatement getEventStmt = null;
        ResultSet resultSet = null;
        try{
            String getEventSql = "select * from Event where PERSONID = ?";
            getEventStmt = database.connection.prepareStatement(getEventSql);
            getEventStmt.setString(1,PersonId);
            resultSet = getEventStmt.executeQuery();
            toReturn = new ArrayList<EventModel>();
            while(resultSet.next()){
                EventModel toAdd = new EventModel();
                toAdd.setEventId(resultSet.getString(1));
                toAdd.setDescendantId(resultSet.getString(2));
                toAdd.setEventType(resultSet.getString(3));
                toAdd.setPersonId(resultSet.getString(4));
                toAdd.setLatitude(resultSet.getString(5));
                toAdd.setLongitude(resultSet.getString(6));
                toAdd.setCountry(resultSet.getString(7));
                toAdd.setCity(resultSet.getString(8));
                toAdd.setYear(resultSet.getString(9));
                toReturn.add(toAdd);
            }
            return toReturn;
        }
        catch(SQLException e){
            System.out.println("EventDAo getEventbyID sql exception:   " + e.getMessage());
            return null;

        }
    }

    public EventModel getEventbyEventID(String EventId){
        EventModel toReturn = null;
        PreparedStatement getEventStmt = null;
        ResultSet resultSet = null;
        try{
            String getEventSql = "select * from Event where EVENTID = ?";
            getEventStmt = database.connection.prepareStatement(getEventSql);
            getEventStmt.setString(1,EventId);
            resultSet = getEventStmt.executeQuery();
            toReturn = new EventModel();
            while(resultSet.next()){
                toReturn.setEventId(resultSet.getString(1));
                toReturn.setDescendantId(resultSet.getString(2));
                toReturn.setEventType(resultSet.getString(3));
                toReturn.setPersonId(resultSet.getString(4));
                toReturn.setLatitude(resultSet.getString(5));
                toReturn.setLongitude(resultSet.getString(6));
                toReturn.setCountry(resultSet.getString(7));
                toReturn.setCity(resultSet.getString(8));
                toReturn.setYear(resultSet.getString(9));
            }
            getEventStmt.close();
            return toReturn;
        }
        catch(SQLException e){
            System.out.println("EventDAo getEventbyID sql exception:   " + e.getMessage());
            return null;

        }
    }



    /**
     * Takes an EventModel object, looks at it's id, then updates the data for that id.
     *
     * @return a string detailing success or error
     */
    String updateEvent(EventModel toUpdate){
        return "";
    }
}
