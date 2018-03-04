package fms.results;

import fms.models.PersonModel;

/**
 * Created by hrazi94 on 10/16/17.
 */

public class SinglePersonResult {

    /**
     * this is the person object found with the personId from the request
     */
    public PersonModel person;

    /**
     * this flag will be marked false if the request was unsuccessful in any way.  True if it
     * was successful.
     */
    private boolean flag;

    /**
     * Contains a message if the request failed detailing why it failed
     */
    private String errorMessage;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}


