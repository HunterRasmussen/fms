package fms.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fms.models.AuthTokModel;
import fms.models.UserModel;
import fms.results.LoginRegisterResult;

/**
 * This class interacts with the AuthTok database table
 */

public class AuthTokDao {

    private Database database;


    AuthTokDao(Database db){
        database = db;
    }

       //Logs a user in by putting them in the database
    //Returns a loginRegisterResult.  if unsuccesful, flag is marked false and error message given
    public LoginRegisterResult addAuthTok(AuthTokModel toAdd){
        PreparedStatement addUserStmt = null;
        String addUserSql = "insert into AuthTok (USERNAME, AUTHTOK) VALUES(?,?)";
        LoginRegisterResult toReturn = new LoginRegisterResult();
        try{
            addUserStmt = database.connection.prepareStatement(addUserSql);
            addUserStmt.setString(1,toAdd.getUserName());
            addUserStmt.setString(2,toAdd.getAuthTok());

            if(addUserStmt.executeUpdate() == 1){
                toReturn.setSuccessFlag(true);
                toReturn.setAuthTok(toAdd.getAuthTok());
                toReturn.setUserName(toAdd.getUserName());
                toReturn.setPersonId(toAdd.getPersonId());
                if(addUserStmt != null){
                    addUserStmt.close();
                }
                return toReturn;
            }
        }
        catch(SQLException e){
            toReturn.setSuccessFlag(false);
            toReturn.setErrorMessage("failure");
            return toReturn;
        }
        toReturn.setErrorMessage("Error.  Got to the end of the method.  Shouldn't have");
        return toReturn;
    }


    /**
     *
     *
     * @return a string declaring wether it succeeded or not.
     */
    public String removeAuthTok(String authTok){

        PreparedStatement removeAuthTokStmt = null;
        String toReturn = "failure";
        try{
            String removeAuthTokSql = "delete from AuthTok where AUTHTOK = ?";
            removeAuthTokStmt = database.connection.prepareStatement(removeAuthTokSql);
            removeAuthTokStmt.setString(1, authTok);
            if(removeAuthTokStmt.executeUpdate() == 1) {
                toReturn = "success";
                removeAuthTokStmt.close();
                return toReturn;
            }
        }
        catch(SQLException e){
            toReturn = e.getMessage();
            return toReturn;
        }
        return "Got to the end of the removeAuthTok from authTok table.  Shouldn't";
    }

    public String removeAllTokens(){
        PreparedStatement removeAll = null;
        try{
            String removeAllTokensSql = "delete from AuthTok";
            removeAll = database.connection.prepareStatement(removeAllTokensSql);
            removeAll.executeUpdate();
            removeAll.close();
            return "success";
        }
        catch (SQLException e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * Gets a username from the table and returns it
     */
    public String getUser(String authTok){

        String toReturn = null;
        PreparedStatement getUserStmt = null;
        ResultSet resultSet = null;
        try{
            String getUserSql = "select * from AuthTok where AUTHTOK = ?";
            getUserStmt = database.connection.prepareStatement(getUserSql);
            getUserStmt.setString(1,authTok);
            resultSet = getUserStmt.executeQuery();
            while(resultSet.next()){
                toReturn = resultSet.getString(1);
                return toReturn;
            }
            getUserStmt.close();
        }
        catch(SQLException e){
            return "failure";
        }
        return null;
    }
}
