package fms.requests;

import java.util.Set;

import fms.models.*;

public class LoadRequest {
    /**
     * Contains any number of user objects to add to the database
     */
    private Set<UserModel> userstoAdd;
    /**
     * contains any number of Person objects to add to the database
     */
    private Set<PersonModel> personsToAdd;
    /**
     * contains any number of Event Objects to add to the database.
     */
    private Set<EventModel> eventsToAdd;


    Set<UserModel> getUsersToAdd(){
        return userstoAdd;
    }

    Set<PersonModel> getPersonsToAdd(){
        return personsToAdd;
    }

    Set<EventModel> getEventsToAdd(){
        return eventsToAdd;
    }

}
