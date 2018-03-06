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
    public SinglePersonResult addPerson(PersonModel toAdd ){PreparedStatement addEventStmt = null;
        String addEventSql = "insert into Person (PERSONID, DESCENDANTID, FIRSTNAME, LASTNAME," +
                " GENDER, FATHERID, MOTHERID, SPOUSEID) VALUES(?,?,?,?,?,?,?,?)";
        SinglePersonResult toReturn = new SinglePersonResult();

        try{
            addEventStmt = database.connection.prepareStatement(addEventSql);
            addEventStmt.setString(1, toAdd.getPersonId());
            addEventStmt.setString(2, toAdd.getDescendantId());
            addEventStmt.setString(3, toAdd.getFirstName());
            addEventStmt.setString(4, toAdd.getLastName());
            addEventStmt.setString(5, Character.toString(toAdd.getGender()));
            addEventStmt.setString(6, toAdd.getFatherId());
            addEventStmt.setString(7, toAdd.getMotherId());

            if(addEventStmt.executeUpdate() == 1){
                toReturn.setFlag(true);
                toReturn.person = toAdd;
                if(addEventStmt != null){
                    addEventStmt.close();
                }
                return toReturn;
            }
        }
        catch(SQLException e){
            toReturn.setFlag(false);
            toReturn.setErrorMessage(e.getMessage());
            toReturn.person = null;
            return toReturn;
        }

        toReturn.setErrorMessage("Got to the end of the method.  Shouldn't have.  Look into this");
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
    String updatePerson(PersonModel toUpdate){
        return "";
    }
}
