package fms.results;


import java.util.ArrayList;
import java.util.List;

import fms.models.PersonModel;

public class AllPersonsResult {

    public AllPersonsResult(){
        this.arrayToReturn = new ArrayList<PersonModel>();
    }

    /**
     * If request fails, this is marked false or true if it succeeds.
     */
    private boolean successFlag;

    /**
     * If request fails, contains a message with description of the error
     */
    private String errorMessage;


    /**
     * This will be a json object with all the data of all the persons found with the userID from the request
     */
    private List<PersonModel> arrayToReturn;
}
