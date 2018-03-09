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
    public String addEvent(EventModel toAdd){

        PreparedStatement addEventStmt = null;
        String addEventSql = "insert into Event (EVENTID, DESCENDANT, EVENTTYPE, PERSONID," +
                " LATITUDE, LONGITUDE, COUNTRY, CITY, YEAR) VALUES(?,?,?,?,?,?,?,?,?)";
        String toReturn = null;

        try{
            addEventStmt = database.connection.prepareStatement(addEventSql);
            addEventStmt.setString(1,toAdd.getEventID());
            addEventStmt.setString(2, toAdd.getDescendant());
            addEventStmt.setString(3, toAdd.getEventType());
            addEventStmt.setString(4, toAdd.getPersonID());
            addEventStmt.setDouble(5, toAdd.getLatitude());
            addEventStmt.setDouble(6, toAdd.getLongitude());
            addEventStmt.setString(7, toAdd.getCountry());
            addEventStmt.setString(8, toAdd.getCity());
            addEventStmt.setInt   (9, toAdd.getYear());

            if(addEventStmt.executeUpdate() == 1){
                toReturn = "success";
                if(addEventStmt != null){
                    addEventStmt.close();
                }
                return toReturn;
            }
        }
        catch(SQLException e){
            toReturn = e.getMessage();
            return toReturn;
        }

        toReturn = "Got to the end of the addEvent method in the EventDao.  Shouldn't have.  Take a look at it." ;
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

    public String removeAllEvents(){
        PreparedStatement removeAllEventsStmt = null;
        try{
            String removeAllEventsSql = "delete from Event";
            removeAllEventsStmt = database.connection.prepareStatement(removeAllEventsSql);
            removeAllEventsStmt.executeUpdate();
            removeAllEventsStmt.close();
            return "success";
        }
        catch(SQLException e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String removeEventsbyDescendant(String Descendant){
        PreparedStatement removeEventsStmt = null;
        String toReturn;
        try{
            String removeEventSql = "delete from Event where DESCENDANT = ?";
            removeEventsStmt = database.connection.prepareStatement(removeEventSql);
            removeEventsStmt.setString(1,Descendant);
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
            removeEventsStmt.setString(1, personId);
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
            String getEventsSql = "select * from Event where DESCENDANT = ?";
            getEventsStmt = database.connection.prepareStatement(getEventsSql);
            getEventsStmt.setString(1,username);
            resultSet = getEventsStmt.executeQuery();
            toReturn = new ArrayList<EventModel>();
            while(resultSet.next()){
                EventModel eventToAdd = new EventModel();
                eventToAdd.setEventID(resultSet.getString(1));
                eventToAdd.setDescendant(resultSet.getString(2));
                eventToAdd.setEventType(resultSet.getString(3));
                eventToAdd.setPersonID(resultSet.getString(4));
                eventToAdd.setLatitude(resultSet.getDouble(5));
                eventToAdd.setLongitude(resultSet.getDouble(6));
                eventToAdd.setCountry(resultSet.getString(7));
                eventToAdd.setCity(resultSet.getString(8));
                eventToAdd.setYear(resultSet.getInt(9));
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
                toAdd.setEventID(resultSet.getString(1));
                toAdd.setDescendant(resultSet.getString(2));
                toAdd.setEventType(resultSet.getString(3));
                toAdd.setPersonID(resultSet.getString(4));
                toAdd.setLatitude(resultSet.getDouble(5));
                toAdd.setLongitude(resultSet.getDouble(6));
                toAdd.setCountry(resultSet.getString(7));
                toAdd.setCity(resultSet.getString(8));
                toAdd.setYear(resultSet.getInt(9));
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
                toReturn.setEventID(resultSet.getString(1));
                toReturn.setDescendant(resultSet.getString(2));
                toReturn.setEventType(resultSet.getString(3));
                toReturn.setPersonID(resultSet.getString(4));
                toReturn.setLatitude(resultSet.getDouble(5));
                toReturn.setLongitude(resultSet.getDouble(6));
                toReturn.setCountry(resultSet.getString(7));
                toReturn.setCity(resultSet.getString(8));
                toReturn.setYear(resultSet.getInt(9));
            }
            getEventStmt.close();
            return toReturn;
        }
        catch(SQLException e){
            System.out.println("EventDAO getEventbyID sql exception:   " + e.getMessage());
            e.printStackTrace();
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
