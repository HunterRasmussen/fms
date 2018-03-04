package fms.results;

import fms.models.EventModel;

/**
 * Created by hrazi94 on 10/16/17.
 */

public class SingleEventResult {




    /**
     * this flag will be marked false if the request was unsuccessful in any way.  True if it
     * was successful.
     */
    private boolean successFlag;


    /**
     * If request fails, contains a message with description of the error
     */
    private String errorMessage;

    public EventModel event;



    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean getSuccessFlag() {
        return successFlag;
    }

    public void setSuccessFlag(boolean successFlag) {
        this.successFlag = successFlag;
    }


}


