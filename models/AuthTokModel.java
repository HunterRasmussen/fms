package fms.models;

/**
 * Class that represents an authTok in the database
 */

public class AuthTokModel {

    public AuthTokModel(String authTok, String username){
        this.authTok = authTok;
        this.username = username;
    }
    public AuthTokModel(){

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

    private String authTok;
    private String username;
    private String personId;

    public String getAuthTok(){
        return authTok;
    }
    public String getUsername(){
        return username;
    }
    public String getPersonId(){return personId;}

}
