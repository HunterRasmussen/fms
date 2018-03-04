package fms.services;

import fms.requests.LoadRequest;

/**
 * Created by hrazi94 on 10/16/17.
 */

public class Load {
    /**
     * Creates the users in the given loadrequest object and uses the dao to add them to the user
     *      table in the database.
     *  creates the persons objects in the given loadrequest and uses the dao to add them to the
     *      person table
     * Creates the events in the given loadrequest object then uses the dao to add them to the event
     *      table in the database.
     * @param r see loadrequest object class
     * @return  A string detailing if the load was succesfull and what it added to the database
     *      or a message detailing what went wrong;
     */
    String load(LoadRequest r){
      return "";
    }

}
