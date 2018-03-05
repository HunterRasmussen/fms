package fms.models;

/**
 * Class that represents an authTok in the database
 */

public class AuthTokModel {

    public AuthTokModel(String authTok, String userName, String personId){
        this.authTok = authTok;
        this.userName = userName;
        this.personId = personId;

    }
    public AuthTokModel(){

    }

    public void setAuthTok(String authTok) {
        this.authTok = authTok;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    private String authTok;
    private String userName;
    private String personId;

    public String getAuthTok(){
        return authTok;
    }
    public String getUserName(){
        return userName;
    }
    public String getPersonId(){return personId;}

}
