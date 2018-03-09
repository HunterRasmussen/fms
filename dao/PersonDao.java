package fms.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fms.models.EventModel;
import fms.models.PersonModel;
import fms.results.SinglePersonResult;

/**
*This class interacts with the Person database table
 */

public class PersonDao {


    private Database database;


    PersonDao(Database db){
        database = db;
    }

    /**
     * adds a person to the Person database table
     *
     * @return If adding a person is successful,
     *      it will create a PersonModel and a Single PersonResult object.
     *      If it is unsuccessful,
     *      It will create a SinglePersonResult object and mark the flag false and give an error message
     */
    public String addPerson(PersonModel toAdd ){
        PreparedStatement addPersonStmt = null;
        String addPersonSql = "insert into Person (PERSONID, DESCENDANT, FIRSTNAME, LASTNAME," +
                " GENDER, FATHERID, MOTHERID, SPOUSEID) VALUES(?,?,?,?,?,?,?,?)";
        String toReturn = null;

        try{
            addPersonStmt = database.connection.prepareStatement(addPersonSql);
            addPersonStmt.setString(1, toAdd.getPersonID());
            addPersonStmt.setString(2, toAdd.getDescendant());
            addPersonStmt.setString(3, toAdd.getFirstName());
            addPersonStmt.setString(4, toAdd.getLastName());
            addPersonStmt.setString(5, Character.toString(toAdd.getGender()));
            addPersonStmt.setString(6, toAdd.getFatherID());
            addPersonStmt.setString(7, toAdd.getMotherID());
            addPersonStmt.setString(8, toAdd.getSpouseID());

            if(addPersonStmt.executeUpdate() == 1){

                toReturn = "success";
                if(addPersonStmt != null){
                    addPersonStmt.close();
                }
                return toReturn;
            }
        }
        catch(SQLException e){
            toReturn = e.getMessage();
            return toReturn;
        }

        toReturn = "Got to the end of the method.  Shouldn't have.  Look into this";
        return toReturn;
    }

    /**
     *
     *
     * @return a string declaring wether it succeeded or not.
     */
    public String removePerson(String personId){
        PreparedStatement removePersonStmt = null;
        String toReturn;
        try{
            String removePersonSql = "delete from Person where PERSONID = ?";
            removePersonStmt = database.connection.prepareStatement(removePersonSql);
            removePersonStmt.setString(1,personId);
            removePersonStmt.close();
            toReturn= "success";
            return toReturn;

        }
        catch(SQLException e){
            toReturn = e.getMessage();
            return toReturn;
        }

    }

    public String removePersonsByDescendant(String userName){
        PreparedStatement removePersonStmt = null;
        String toReturn;
        try{
            String removePersonSql = "delete from Person where descendant = ?";
            removePersonStmt = database.connection.prepareStatement(removePersonSql);
            removePersonStmt.setString(1,userName);
            removePersonStmt.executeUpdate();
            removePersonStmt.close();
            toReturn= "success";
            return toReturn;

        }
        catch(SQLException e){
            toReturn = e.getMessage();
            return toReturn;
        }
    }

    public String removeAllPersons(){
        PreparedStatement removeAllPersonsStmt = null;
        try{
            String removeAllPersonsSql = "delete from Person";
            removeAllPersonsStmt = database.connection.prepareStatement(removeAllPersonsSql);
            removeAllPersonsStmt.executeUpdate();
            removeAllPersonsStmt.close();
            return "success";

        }
        catch (SQLException e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * Gets a person and it's info from the table and returns it in a PersonModel object.
     */
    public PersonModel getPerson(String personID){
        PersonModel toReturn = null;
        PreparedStatement getPersonStmt = null;
        ResultSet results = null;
        try{
            String getPersonSql = "select * from Person where PERSONID = ?";
            getPersonStmt = database.connection.prepareStatement(getPersonSql);
            getPersonStmt.setString(1, personID);
            results  = getPersonStmt.executeQuery();
            toReturn = new PersonModel();
            while(results.next()){
                toReturn.setPersonID(results.getString(1));
                toReturn.setDescendant(results.getString(2));
                toReturn.setFirstName(results.getString(3));
                toReturn.setLastName(results.getString(4));
                toReturn.setGender(results.getString(5).charAt(0));
                toReturn.setFatherID(results.getString(6));
                toReturn.setMotherID(results.getString(7));
                toReturn.setSpouseID(results.getString(8));
                return toReturn;
            }
        }
        catch(SQLException e){
            System.out.println(e.getStackTrace());
            System.out.println(e.getMessage());
            return null;
        }
        return null;
    }

    public List<PersonModel> getAllPeopleByUserName(String userName){
        List<PersonModel> toReturn = null;
        PreparedStatement getPeopleStmt = null;
        ResultSet resultSet = null;
        try{
            String getPeopleSql = "select * from Person where DESCENDANT = ?";
            getPeopleStmt = database.connection.prepareStatement(getPeopleSql);
            getPeopleStmt.setString(1,userName);
            resultSet = getPeopleStmt.executeQuery();
            toReturn = new ArrayList<PersonModel>();
            while(resultSet.next()){
                PersonModel personToAdd = new PersonModel();
                personToAdd.setPersonID(resultSet.getString(1));
                personToAdd.setDescendant(resultSet.getString(2));
                personToAdd.setFirstName(resultSet.getString(3));
                personToAdd.setLastName(resultSet.getString(4));
                personToAdd.setGender(resultSet.getString(5).charAt(0));
                personToAdd.setFatherID(resultSet.getString(6));
                personToAdd.setMotherID(resultSet.getString(7));
                personToAdd.setSpouseID(resultSet.getString(8));
                toReturn.add(personToAdd);
            }
            return toReturn;
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Takes an PersonModel object, looks at it's id, then updates the data for that id.
     *
     * @return a string detailing success or error
     */
    public String updateParents(PersonModel toUpdate){
        PreparedStatement updatePersonStmt = null;
        try{
            String updatePersonSql = "UPDATE Person SET FATHERID = ? , MOTHERID = ? WHERE PERSONID =?";
            updatePersonStmt = database.connection.prepareStatement(updatePersonSql);
            updatePersonStmt.setString(1,toUpdate.getFatherID());
            updatePersonStmt.setString(2, toUpdate.getMotherID());
            updatePersonStmt.setString(3 ,toUpdate.getPersonID());
            updatePersonStmt.executeUpdate();
                if(updatePersonStmt != null){
                    updatePersonStmt.close();
                }
                return "success";
        }
        catch(SQLException e){
            return e.getMessage();
        }
    }
}
