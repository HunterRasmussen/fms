package fms.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        String addPersonSql = "insert into Person (PERSONID, DESCENDANTID, FIRSTNAME, LASTNAME," +
                " GENDER, FATHERID, MOTHERID, SPOUSEID) VALUES(?,?,?,?,?,?,?,?)";
        String toReturn = null;

        try{
            addPersonStmt = database.connection.prepareStatement(addPersonSql);
            addPersonStmt.setString(1, toAdd.getPersonId());
            addPersonStmt.setString(2, toAdd.getDescendantId());
            addPersonStmt.setString(3, toAdd.getFirstName());
            addPersonStmt.setString(4, toAdd.getLastName());
            addPersonStmt.setString(5, Character.toString(toAdd.getGender()));
            addPersonStmt.setString(6, toAdd.getFatherId());
            addPersonStmt.setString(7, toAdd.getMotherId());

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

    public String removePersonsByDescendantId(String userName){
        PreparedStatement removePersonStmt = null;
        String toReturn;
        try{
            String removePersonSql = "delete from Person where DESCENDANTID = ?";
            removePersonStmt = database.connection.prepareStatement(removePersonSql);
            removePersonStmt.setString(1,userName);
            removePersonStmt.close();
            toReturn= "success";
            return toReturn;

        }
        catch(SQLException e){
            toReturn = e.getMessage();
            return toReturn;
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
                toReturn.setPersonId(results.getString(1));
                toReturn.setDescendantId(results.getString(2));
                toReturn.setFirstName(results.getString(3));
                toReturn.setLastName(results.getString(4));
                toReturn.setGender(results.getString(5).charAt(0));
                toReturn.setFatherId(results.getString(6));
                toReturn.setMotherId(results.getString(7));
                toReturn.setSpouseId(results.getString(8));
            }
        }
        catch(SQLException e){
            System.out.println(e.getStackTrace());
            System.out.println(e.getMessage());
            return null;
        }
        return null;
    }


    /**
     * Takes an PersonModel object, looks at it's id, then updates the data for that id.
     *
     * @return a string detailing success or error
     */
    public String updateParents(PersonModel toUpdate){
        PreparedStatement updatePersonStmt = null;
        try{
            String updatePersonSql = "UPDATE Person" +
                    "SET FATHERID = \'" + toUpdate.getFatherId() + "\'," +
                    "MOTHERID = \'" + toUpdate.getMotherId() + "\' " +
                    "WHERE PERSONID = \'" + toUpdate.getPersonId() + "\';";
            updatePersonStmt = database.connection.prepareStatement(updatePersonSql);
            if(updatePersonStmt.executeUpdate() == 1){
                if(updatePersonStmt != null){
                    updatePersonStmt.close();
                }
                return "success";
            }

        }
        catch(SQLException e){
            return e.getMessage();
        }

        return "Error.  Got to the end of updateParents method in PersonDao." +
                "  Didn't account for that.  Use breakpoints and replicate last attempt.";
    }
}
