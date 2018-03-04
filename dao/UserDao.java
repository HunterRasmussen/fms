package fms.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import fms.models.UserModel;
import fms.results.LoginRegisterResult;
import java.sql.*;


/**
 * This class interacts with the User database Table
 */

public class UserDao {


    private Database database;


    UserDao(Database db){
        database = db;
    }

    /**
     *  This will add a new user to the database
     *
     * @param toAdd
     * @return  The method will create a LoginRegisterResult object
     *          If the add is successful, it will give it the appropriate data
     *          If it is unsuccessful, it will mark the flag false and put in a description or the error;
     */
    public LoginRegisterResult addUser(UserModel toAdd) throws SQLException{

        PreparedStatement addUserStmt = null;
        String addUserSql =  "insert into User (USERNAME, PASSWORD, EMAIL, FIRSTNAME," +
            "LASTNAME, GENDER, PERSONID) values ( ?, ?, ?, ?, ?, ?, ?)";

        LoginRegisterResult toReturn = new LoginRegisterResult();

        try{
            addUserStmt = database.connection.prepareStatement(addUserSql);
            addUserStmt.setString(1,toAdd.getUsername());
            addUserStmt.setString(2,toAdd.getPassword());
            addUserStmt.setString(3,toAdd.getEmail());
            addUserStmt.setString(4,toAdd.getFirstName());
            addUserStmt.setString(5,toAdd.getLastName());
            addUserStmt.setString(6,Character.toString(toAdd.getGender()));
            addUserStmt.setString(7,toAdd.getPersonId());

            if(addUserStmt.executeUpdate() == 1){
                toReturn.setSuccessFlag(true);
                toReturn.setUsername(toAdd.getUsername());
                toReturn.setPersonId("personId");
                return toReturn;

            }
        }
        catch(SQLException e){
            toReturn.setSuccessFlag(false);
            toReturn.setErrorMessage(e.getMessage());
            return toReturn;
        }
        finally{
            if(addUserStmt != null){
                addUserStmt.close();
            }
        }
        return toReturn;
    }

    /**
     *
     *
     * @return a string declaring wether it succeeded or not.
     */
    public String removeUser(String username){
        PreparedStatement removeUserStmt = null;
        String toReturn;
        try{
            String removeUserSql = "delete from User where USERNAME = ?";
            removeUserStmt = database.connection.prepareStatement(removeUserSql);
            removeUserStmt.setString(1,username);
            removeUserStmt.executeUpdate();
            removeUserStmt.close();
            toReturn = "good";
        }
        catch(SQLException e){
            toReturn = e.getMessage();
            return toReturn;
        }


        //toReturn = "good";
        return toReturn;
    }


    /**
     * Gets a user and it's info from the table and returns it in a userModel object.
     */
    public UserModel getUser(String username){
        PreparedStatement getUserStmt = null;
        UserModel toReturn = null;
        ResultSet results =  null;
        try{
            String getUserSql = "select * from users where USERNAME = ?";
            getUserStmt = database.connection.prepareStatement(getUserSql);
            getUserStmt.setString(1,username);
            results = getUserStmt.executeQuery();

            while(results.next()){
                toReturn = new UserModel();
                toReturn.setUsername(results.getString(1));
                toReturn.setPassword(results.getString(2));
                toReturn.setEmail(results.getString(3));
                toReturn.setFirstName(results.getString(4));
                toReturn.setLastName(results.getString(5));
                toReturn.setGender(results.getString(6).charAt(0));
                toReturn.setPersonId(results.getString(7));
                toReturn.successFlag = true;
            }
        }
        catch(SQLException e){
            toReturn = new UserModel();
            toReturn.successFlag = false;
            toReturn.successMessage = e.getMessage();
            return toReturn;
        }
        return toReturn;
    }


    /**
     * Takes an UserModel object, looks at it's username, then updates the data for that username.
     *
     * @return a string detailing success or error
     */
    String updateUser(UserModel toUpdate){
        //UserModel currentInfo = getUser(toUpdate.getUsername());



        return "";
    }

}



