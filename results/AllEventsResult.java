package fms.results;

import org.w3c.dom.events.EventTarget;

import fms.models.EventModel;
import java.lang.*;
import java.util.*;

/**
 * Created by hrazi94 on 10/16/17.
 */



public class AllEventsResult {

    public AllEventsResult() {
        this.arrayToReturn = new ArrayList<EventModel>();
    }

    /**
     * this flag will be marked false if the request was unsuccessful in any way.  True if it
     * was successful.
     */
    private boolean successFlag;

    /**
     * If request fails, contains a message with description of the error
     */
    private String errorMessage;

    /**
     * This will be a JSON object that contains all the events that are related to the userId found in the request
     */
    private List<EventModel> arrayToReturn;



}
