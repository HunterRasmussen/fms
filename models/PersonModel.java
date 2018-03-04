package fms.models;


/**
 * the class that represents a person in the database
 */
public class PersonModel {



    public PersonModel(String personId, String descendantId, String firstName,
                       String lastName, char gender, String fatherId,
                       String motherId, String spouseId) {
        this.descendantId = descendantId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherId = fatherId;
        this.motherId = motherId;
        this.spouseId = spouseId;
        this.personId = personId;
    }
    public PersonModel(){} //empty constructor


    private String descendantId;
    private String firstName;
    private String lastName;
    private char gender;
    private String fatherId;
    private String motherId;
    private String spouseId;
    private String personId;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getDescendantId() {
        return descendantId;
    }

    public void setDescendantId(String descendantId) {
        this.descendantId = descendantId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    public String getMotherId() {
        return motherId;
    }

    public void setMotherId(String motherId) {
        this.motherId = motherId;
    }

    public String getSpouseId() {
        return spouseId;
    }

    public void setSpouseId(String spouseId) {
        this.spouseId = spouseId;
    }
}
