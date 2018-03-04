package fms.models;

/**
 * Class that represents an event in the database
 */

public class EventModel {

    public EventModel(String EventId, String descendantId, String personId , String latitude, String longitude,
                      String country , String city , String eventType , String year){

        this.EventId = EventId;
        this.descendantId = descendantId;
        this.personId = personId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }
    public EventModel(){}; // empty constructor


    private String EventId;
    private String descendantId;
    private String personId;
    private String latitude;
    private String longitude;
    private String country;
    private String city;
    private String eventType;
    private String year;


    public String getEventId() {
        return EventId;
    }

    public void setEventId(String eventId) {
        EventId = eventId;
    }

    public String getDescendantId() {
        return descendantId;
    }

    public void setDescendantId(String descendantId) {
        this.descendantId = descendantId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
