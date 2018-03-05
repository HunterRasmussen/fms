package fms.facade;


import java.sql.SQLException;
import java.util.Random;

import fms.dao.Database;
import fms.models.AuthTokModel;
import fms.models.UserModel;
import fms.results.LoginRegisterResult;

/**
 * This class contains all the methods to which access use the DAO's to add and get data from the database.
 * It is the intermediary between the webHandlers and the DAO's
 */
public class ServerFacade {

    private Database database = new Database();
    private int currentPersonIdCount = 0;
    public String createAuthToken(String username){
        String chars = "abccdefghijklnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder authTokBuild = new StringBuilder(username + "_");
        for(int i =0; i < 6;i++){
            //append their user id to the front
            Random rand = new Random();
            int randomIndex = rand.nextInt(chars.length()-1); // chars.length()-1 is the largest index, and 0 is the smallest index
            authTokBuild.append(chars.charAt(randomIndex));
        }
        return authTokBuild.toString();
    }

    public String generatePersonId(){
        currentPersonIdCount++;
        return Integer.toString(currentPersonIdCount);
    }

    public LoginRegisterResult registerUser(UserModel user){
        user.setPersonId(generatePersonId());
        LoginRegisterResult result = new LoginRegisterResult();
        try{
            result = database.usersTable.addUser(user);
            if(result.isSuccessFlag()){
               AuthTokModel authTokModel = new AuthTokModel(createAuthToken(user.getUserName()),user.getUserName(), user.getPersonId());
               result = database.authTokTable.addAuthTok(authTokModel);
            }
            return result;
        }
        catch (SQLException e){
            e.printStackTrace();
            result.setErrorMessage(e.getMessage());
            result.setSuccessFlag(false);
            return result;
        }
    }

    /**
     *
     * @param userName to search for in database
     * @return true if username isn't currently being used.  False if it is found in the database/the username is unavailable
     */
    public boolean checkUserNameAvailability(String userName){
        UserModel userToCheck = database.usersTable.getUser(userName);
        if(userToCheck == null){
            return true;
        }
        return false;
    }





}
