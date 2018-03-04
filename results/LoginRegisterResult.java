package fms.results;

/**
 * Created by hrazi94 on 10/16/17.
 */

public class LoginRegisterResult {

    private String authTok;
    private String username;
    private String personId;
    private boolean successFlag;
    /**
     * If request fails, this contains a message with description of error
     */
    private String errorMessage;

    public String getAuthTok() {
        return authTok;
    }

    public String getUsername() {
        return username;
    }

    public String getPersonId() {
        return personId;
    }

    public void setAuthTok(String authTok) {
        this.authTok = authTok;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    /**
     * If request fails, this flag is marked false.  If it succeeds, it is marked true.
     */



    public boolean isSuccessFlag() {
        return successFlag;
    }

    public void setSuccessFlag(boolean successFlag) {
        this.successFlag = successFlag;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


}
