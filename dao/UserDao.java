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
            addUserStmt.setString(1,toAdd.getUserName());
            addUserStmt.setString(2,toAdd.getPassword());
            addUserStmt.setString(3,toAdd.getEmail());
            addUserStmt.setString(4,toAdd.getFirstName());
            addUserStmt.setString(5,toAdd.getLastName());
            addUserStmt.setString(6,Character.toString(toAdd.getGender()));
            addUserStmt.setString(7,toAdd.getPersonID());

            if(addUserStmt.executeUpdate() == 1){
                toReturn.setSuccessFlag(true);
                toReturn.setUserName(toAdd.getUserName());
                toReturn.setPersonId(toAdd.getPersonID());
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
    public String removeUser(String userName){
        PreparedStatement removeUserStmt = null;
        String toReturn;
        try{
            String removeUserSql = "delete from User where USERNAME = ?";
            removeUserStmt = database.connection.prepareStatement(removeUserSql);
            removeUserStmt.setString(1,userName);
            removeUserStmt.executeUpdate();
            removeUserStmt.close();
            toReturn = "success";
        }
        catch(SQLException e){
            toReturn = e.getMessage();
            return toReturn;
        }


        //toReturn = "good";
        return toReturn;
    }


    public String removeAllUsers(){
        PreparedStatement removeAllStmt = null;
        try{
            String removeAllSql = "delete from User";
            removeAllStmt = database.connection.prepareStatement(removeAllSql);
            removeAllStmt.executeUpdate();
            removeAllStmt.close();
            return "success";
        }
        catch (SQLException e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * Gets a user and it's info from the table and returns it in a userModel object.
     */
    public UserModel getUser(String username){
        PreparedStatement getUserStmt = null;
        UserModel toReturn = null;
        ResultSet results =  null;
        try{
            String getUserSql = "select * from User where USERNAME = ?";
            getUserStmt = database.connection.prepareStatement(getUserSql);
            getUserStmt.setString(1,username);
            results = getUserStmt.executeQuery();

            while(results.next()){
                toReturn = new UserModel();
                toReturn.setUserName(results.getString(1));
                toReturn.setPassword(results.getString(2));
                toReturn.setEmail(results.getString(3));
                toReturn.setFirstName(results.getString(4));
                toReturn.setLastName(results.getString(5));
                toReturn.setGender(results.getString(6).charAt(0));
                toReturn.setPersonID(results.getString(7));
                return toReturn;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
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
        return null;
    }

}



