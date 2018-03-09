package fms.facade;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;


import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import fms.dao.Database;
import fms.models.AuthTokModel;
import fms.models.EventModel;
import fms.models.PersonModel;
import fms.models.UserModel;
import fms.results.LoginRegisterResult;

/**
 * This class contains all the methods to which access use the DAO's to add and get data from the database.
 * It is the intermediary between the webHandlers and the DAO's
 */
public class ServerFacade {

    private Database database;
    private int currentPersonIdCount = 0;
    private List<String> femaleNames;
    private List<String> maleNames;
    private List<String> lastNames;
    private List<Location> locations;

    public ServerFacade(){
        database = new Database();
        femaleNames = new ArrayList<String>();
        maleNames = new ArrayList<String>();
        lastNames = new ArrayList<String>();
        locations = new ArrayList<Location>();
        loadFemaleNames();
        loadMaleNames();
        loadLastNames();
        loadLocations();

    }

    private void loadFemaleNames(){
        try{
            JsonReader reader = new JsonReader(new FileReader("/Users/AthleticsVideoMacPro/Documents/Hunter_Rasmussen/family map server/MyApplication/lib/src/main/java/fms/facade/fnames.json"));
            //consume opening bracket
            reader.beginObject();
            String type = reader.nextName();
            if(type.equals("data")){
                //consume array bracket
                reader.beginArray();
                while(reader.hasNext()){
                    femaleNames.add(reader.nextString());
                }
            }

        }
        catch (IOException e){
            System.out.println("Unable to get the fnames file.  Check filePath");
        }
        System.out.println("FemaleNames loaded");
    }
    private void loadMaleNames(){
        try{
            JsonReader reader = new JsonReader(new FileReader("/Users/AthleticsVideoMacPro/Documents/Hunter_Rasmussen/family map server/MyApplication/lib/src/main/java/fms/facade/mnames.json"));
            //consume opening bracket
            reader.beginObject();
            String type = reader.nextName();
            if(type.equals("data")){
                //consume array bracket
                reader.beginArray();
                while(reader.hasNext()){
                    maleNames.add(reader.nextString());
                }
            }

        }
        catch (IOException e){
            System.out.println("Unable to get the mnames file.  Check filePath");
        }
        System.out.println("MaleNames loaded");
    }
    private void loadLastNames() {
        try{
            JsonReader reader = new JsonReader(new FileReader("/Users/AthleticsVideoMacPro/Documents/Hunter_Rasmussen/family map server/MyApplication/lib/src/main/java/fms/facade/snames.json"));
            //consume opening bracket
            reader.beginObject();
            String type = reader.nextName();
            if(type.equals("data")){
                //consume array bracket
                reader.beginArray();
                while(reader.hasNext()){
                    lastNames.add(reader.nextString());
                }
                reader.endArray();
            }
            reader.endObject();
        }
        catch (IOException e){
            System.out.println("Unable to get the snames file.  Check filePath");
        }
        System.out.println("LastNames loaded");
    }
    private void loadLocations(){
        try{
            JsonReader reader = new JsonReader(new FileReader("/Users/AthleticsVideoMacPro/Documents/Hunter_Rasmussen/family map server/MyApplication/lib/src/main/java/fms/facade/locations.json"));
            //consume opening bracket
            reader.beginObject();
            String type = reader.nextName();
            if(type.equals("data")){
                //consume array bracket
                reader.beginArray();
                                String country = null;
                String city = null;
                Double latitude = null;
                Double longitude = null;
                Boolean isNewObject = true;
                while(reader.hasNext()){
                    //consume opening bracket
                    if(isNewObject){
                        reader.beginObject();
                        isNewObject = false;
                    }
                    String name = reader.nextName();
                    if(name.equals("country")){
                        country = reader.nextString();
                    }
                    else if(name.equals("city")){
                        if(country == null){
                            System.out.println("Error with the locations file.  Check data is arranged properly");
                            return;
                        }
                        city = reader.nextString();
                    }
                    else if(name.equals("latitude")){
                        if(city == null){
                            System.out.println("Error with the locations file.  Check data is arranged properly");
                            return;
                        }
                        latitude = reader.nextDouble();
                    }
                    else if(name.equals("longitude")){
                        if(latitude == null){
                            System.out.println("Error with the locations file.  Check data is arranged properly");
                            return;
                        }
                        longitude = reader.nextDouble();
                        Location toAdd = new Location(country, city, latitude, longitude);
                        locations.add(toAdd);
                        country = null;
                        city = null;
                        latitude = null;
                        longitude = null;
                        isNewObject = true;
                        reader.endObject();
                    }
                     else{
                        System.out.println("Error with the locations.json file.  Contains Data field that is invalid for a location");
                        return;
                    }
                }
            }
        }
        catch (IOException e){
            System.out.println("Unable to get the locations file.  Check filePath");
        }
        System.out.println("Locations Loaded");
    }



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

    public String generatePersonId(String name){
        currentPersonIdCount++;
        String toReturn = name + currentPersonIdCount;
        return toReturn;
    }

    public String checkRegisteredUser(String userName){
        UserModel user = database.usersTable.getUser(userName);
        if (user == null){
            return "Error.  Couldn't find that userName.  Is that userName registered?";
        }
        else{
            return "success";
        }
    }

    public String checkValidAuthToken(String authTok){
        return database.authTokTable.getUser(authTok);

    }




 //------------------------------------------- login ------------------------------------------//
    public LoginRegisterResult loginUser(String userName, String password){
        UserModel user = database.usersTable.getUser(userName);
        LoginRegisterResult toReturn = new LoginRegisterResult();
        if(user == null){
            toReturn.setSuccessFlag(false);
            toReturn.setErrorMessage("Invalid Username.  Not in database");
            return toReturn;
        }
        if(!password.equals(user.getPassword())){
            toReturn.setSuccessFlag(false);
            toReturn.setErrorMessage("Incorrect password.");
            return toReturn;
        }
        else{
            AuthTokModel toAdd = new AuthTokModel(createAuthToken(user.getUserName()), user.getUserName(), user.getPersonID());
            toReturn = database.authTokTable.addAuthTok(toAdd);
            return toReturn;
        }
    }

 //--------
 //
 // ----------------------------------- register ------------------------------------------//
    public LoginRegisterResult registerUser(UserModel user){
        user.setPersonID(generatePersonId(user.getUserName()));
        LoginRegisterResult addUserResult = new LoginRegisterResult();
        PersonModel toAdd = new PersonModel(user.getPersonID(), user.getUserName(), user.getFirstName(), user.getLastName(), user.getGender(), null, null, null);
        try{
            addUserResult = database.usersTable.addUser(user);
            if(addUserResult.isSuccessFlag()){
                String addPersonResult = database.personTable.addPerson(toAdd);
                if(addPersonResult.contains("success")) {
                    AuthTokModel authTokModel = new AuthTokModel(createAuthToken(user.getUserName()), user.getUserName(), user.getPersonID());
                    addUserResult = database.authTokTable.addAuthTok(authTokModel);
                    this.fill(4,user.getUserName(), toAdd);
                }
                else{
                    addUserResult.setErrorMessage(addPersonResult);
                    addUserResult.setSuccessFlag(false);
                    return addUserResult;
                }
            }
            //this.fill();
            return addUserResult;
        }
        catch (SQLException e){
            e.printStackTrace();
            addUserResult.setErrorMessage(e.getMessage());
            addUserResult.setSuccessFlag(false);
            return addUserResult;
        }
    }



 //------------------------------------------- fill ------------------------------------------//
    public String fill(int numberOfGenerations, String userName, PersonModel startingPerson){
        if(numberOfGenerations == 0) return "Successfully added " + 0 + " persons and " + 0 + " events to the database.";
        if (startingPerson == null){
            startingPerson = this.getPersonFromUsername(userName);
            if(startingPerson == null){
                return " error. couldn't find person with that username in database";
            }
        }




        //remove current events for the StartingPerson;
        String result = database.eventsTable.removeEventsbyDescendant(userName);
        if(result != "success"){
            return result;
        }
        //remove current associated People from StartingPerson
        result = database.personTable.removePersonsByDescendant(userName);
        if(result != "success"){
            return result;
        }
        int personCount = 1;
        int eventCount = 0;

        String addPersonResult = database.personTable.addPerson(startingPerson);
        //generate events for starting person
        String UserDataResult = generateUserData(userName,startingPerson.getPersonID());
        if(!UserDataResult.equals("success")){
            return UserDataResult;
        }
        eventCount += 2;
        int ancestorsResult =  createAncestry_r( userName, startingPerson, 0, numberOfGenerations);
        if(ancestorsResult == -1){
            return "Error in geneerating ancestry.  Try again later";
        }
        personCount += calculateNumberofPeople(numberOfGenerations);
        eventCount+= ancestorsResult;
       return "successfully added " + eventCount + " events and " + personCount + " people";
    }

    //returns total events created
    private int createAncestry_r(String descendant, PersonModel child, Integer currentGeneration, int generationMax){
        if(currentGeneration >= generationMax){
            return 0;
        }
        String result = generateParents(descendant, child);
        if(!result.equals("success")){
            return -1;
        }
        int count = 0;
        result = generateParentLifeEvents(child.getFatherID(), child.getMotherID(), child.getPersonID(), descendant);
        if(result.contains("Error")){
            return -1;
        }
        if(result.contains("6")){
            count += 6;
        }
        else if(result.contains("7")){
            count += 7;
        }
        else if (result.contains("8")){
            count += 8;
        }
        PersonModel father = database.personTable.getPerson(child.getFatherID());
        PersonModel mother = database.personTable.getPerson(child.getMotherID());
        int fatherAncestryResult = createAncestry_r(descendant,  father , currentGeneration+1, generationMax);
        int  motherAncestryResult = createAncestry_r(descendant,  mother , currentGeneration+1, generationMax);
        if(fatherAncestryResult == -1){
            return fatherAncestryResult;
        }
        count += fatherAncestryResult;
        if(motherAncestryResult == -1){
            return motherAncestryResult;
        }
        count += motherAncestryResult;
        return count;
    }

    private int calculateNumberofPeople(int maxGenerations){
        int personCount = 0;
        for(int i = 1; i <= maxGenerations; i++){
            personCount += Math.pow(2,i);
            //System.out.println("CurrentPerson count in calculating = " + personCount);
        }
        return personCount;
    }

    /**gets a person model out of a username
    *first grabs the user from user table
    *then grabs the person out of the persontable using that personId
    * */
    private PersonModel getPersonFromUsername(String userName){
        UserModel user = database.usersTable.getUser(userName);
        if (user == null){
            return null;
        }
        PersonModel personToReturn = database.personTable.getPerson(user.getPersonID());
        return personToReturn;
    }



 //------------------------------------------- load ------------------------------------------//
    public String load(JsonReader reader){
        int userCount = 0;
        int personCount = 0;
        int eventCount = 0;
        Gson gson = new Gson();
        LoadRequest dataToAdd = gson.fromJson(reader,LoadRequest.class);
        for (UserModel user : dataToAdd.users){
            try{
                LoginRegisterResult result = database.usersTable.addUser(user);
                if(!result.isSuccessFlag()){
                    return result.getErrorMessage();
                }
                userCount++;
            }
            catch (SQLException e){
                e.printStackTrace();
                return e.getMessage();
            }
        }
        for(PersonModel person : dataToAdd.persons){
            String result = database.personTable.addPerson(person);
            if(!result.equals("success")){
                return result;
            }
            personCount++;
        }
        for(EventModel event : dataToAdd.events){
            String result = database.eventsTable.addEvent(event);
            if(!result.equals("success")){
                return result;
            }
            eventCount ++;
        }
        return "Successfully added " + userCount + " users, " + personCount + " people and " + eventCount + " events";
    }




 //------------------------------------------- person ------------------------------------------//
    public PersonModel getSinglePerson(String userName, String personId){
       PersonModel person = database.personTable.getPerson(personId);
       if(person == null){
           return person;
       }
       if(!person.getDescendant().equals(userName)){
            person.setFirstName("invalid");
       }
       return person;
    }

    public List<PersonModel> getAllPeople(String userName){
        List<PersonModel> ancestors = database.personTable.getAllPeopleByUserName(userName);
        return ancestors;
    }


 //------------------------------------------- event ------------------------------------------//
    public EventModel getSingleEvent(String userName, String eventID){
        EventModel event = database.eventsTable.getEventbyEventID(eventID);
        if(event == null){
            return null;
        }
        if(!event.getDescendant().equals(userName)){
            event.setEventID("invalid");
        }
        return event;
    }

    public List<EventModel> getAllEvents(String userName){
        List<EventModel> events = database.eventsTable.getAllEventsByUsername(userName);
        return events;
    }


 //------------------------------------------- clear ------------------------------------------//
    public String clearDatabase(){
        String result = database.usersTable.removeAllUsers();
        if(!result.equals("success")){
            return result;
        }
        result = database.personTable.removeAllPersons();
        if(!result.equals("success")){
            return result;
        }
        result = database.eventsTable.removeAllEvents();
        if(!result.equals("success")){
            return result;
        }
        result = database.authTokTable.removeAllTokens();
        return result;

    }





    //----------------------------------- GET JSON METHODS -------------------------------------//
        //contains all the methods for getting values originally from the json files


    private Location getRandomLocation(){
        Random rand = new Random();
        int index = rand.nextInt(locations.size());//gives a random number inbetween 0 and the length of the locations arrayList
        Location toReturn = locations.get(index);
        return toReturn;
    }
    private String getRandomFemaleName(){
        Random rand = new Random();
        int index = rand.nextInt(femaleNames.size());
        String toReturn = femaleNames.get(index);
        return toReturn;
    }
    private String getRandomMaleName(){
        Random rand = new Random();
        int index = rand.nextInt(maleNames.size());
        String toReturn = maleNames.get(index);
        return toReturn;
    }
    private String getRandomLastName(){
        Random rand = new Random();
        int index = rand.nextInt(lastNames.size());
        String toReturn = lastNames.get(index);
        return toReturn;
    }


    //-------------------------------------  GENERATE METHODS ------------------------------------//
    //all the methods required for the fill method above

    //generates parents, and events for each of them, for the given user
    private String generateParents(String userName, PersonModel child){
        PersonModel mother = new PersonModel();
        PersonModel father = new PersonModel();
        father.setDescendant(userName);
        father.setFirstName(getRandomMaleName());
        father.setLastName(child.getLastName());
        father.setGender('m');
        father.setPersonID(generatePersonId(father.getFirstName()+ "_" + father.getLastName()));
        mother.setDescendant(userName);
        mother.setFirstName(getRandomFemaleName());
        mother.setLastName(getRandomLastName());
        mother.setGender('f');
        mother.setPersonID(generatePersonId(mother.getFirstName() + "_" + mother.getLastName()));
        father.setSpouseID(mother.getPersonID());
        mother.setSpouseID(father.getPersonID());
        //String lifeEventsresult = generateParentLifeEvents(father, mother, child, userName);
        //if adding life events failed for whatever reason, just return that reason;
        //if(!lifeEventsresult.contains("success")){
            //return lifeEventsresult;
        //}
        // add father to Database
        String addFatherResult = database.personTable.addPerson(father);
        if(!addFatherResult.equals("success")){
            return addFatherResult;
        }
        //add Mother to database
        String addMotherResult = database.personTable.addPerson(mother);
        if(!addMotherResult.equals("success")){
            return addMotherResult;
        }

        //assign the parents to the child
        child.setFatherID(father.getPersonID());
        child.setMotherID(mother.getPersonID());
        String updateParentsResult = database.personTable.updateParents(child);
        return updateParentsResult;

    }

    private String generateParentLifeEvents(String fatherId, String motherId, String childId, String descendant){
        List<EventModel> childEvents = database.eventsTable.getEventbyPersonId(childId);
        if(childEvents.size() == 0 || childEvents == null){
            return "failure.  In generateParentLifeEvents, the child didn't have any events." +
                    "  Perhaps the child didn't have life events generated for him/her yet";
        }
        int childBirthYear = -1;
        for(EventModel event : childEvents){
            if (event.getEventType().equals("BIRTH")){
                childBirthYear = event.getYear();
            }
        }
        if(childBirthYear == -1){
            return "failure.  Child doesn't have a birth event when checking in generateParentLifeEvents";
        }
        int fatherBirthYear = generateYear(childBirthYear-38, childBirthYear -22); //father can only have a child between ages 22 and 38;
        int motherBirthYear =  generateYear(fatherBirthYear -8, fatherBirthYear + 8); //mom has to be born within 8 years of dad
        //have to be married at least one year before child is born and max of ten years before child is born
        int marriageYear = generateYear(childBirthYear-10, childBirthYear-1);
        int fatherDeathYear = generateYear(childBirthYear+20, childBirthYear +40);
        int motherDeathYear = generateYear(childBirthYear+20, childBirthYear +40);


        Location marriageLocation = getRandomLocation();

        List<EventModel> parentEvents = new ArrayList<EventModel>();

        parentEvents.add(generateEvent(fatherId, "BIRTH", descendant, fatherBirthYear, null));
        parentEvents.add(generateEvent(motherId, "BIRTH", descendant, motherBirthYear, null));
        parentEvents.add(generateEvent(fatherId, "BAPTISM", descendant, fatherBirthYear+8, null));
        parentEvents.add(generateEvent(motherId, "BAPTISM" , descendant, motherBirthYear+8, null));
        parentEvents.add(generateEvent(fatherId, "MARRIAGE", descendant, marriageYear, marriageLocation));
        parentEvents.add(generateEvent(motherId, "MARRIAGE", descendant, marriageYear, marriageLocation));
        EventModel fatherDeath = null;
        EventModel motherDeath = null;
        Calendar currentDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        if(currentYear > fatherDeathYear){
            parentEvents.add(generateEvent(fatherId, "DEATH", descendant, fatherDeathYear, null));
        }
        if(currentYear > motherDeathYear){
            parentEvents.add(generateEvent(motherId, "DEATH", descendant, motherDeathYear, null));
        }
        //add all the events
        int numAdded = addEvents(parentEvents);

        if (numAdded == -1){
            return "Error adding parent's events to the database.  Error happened in addEvents under facade class";
        }
        else if(numAdded == 6){
            return "success. 6 events added";
        }
        else if (numAdded == 7){
            return "success.  7 events added";
        }
        else if(numAdded == 8){
            return "success. 8 events added";
        }
        else{
            return "Error.  Number of events added shouldn't be what it is.  Number: " + numAdded;
        }
    }

    private int addEvents(List<EventModel> eventList){
        int count = 0;
        for(EventModel event : eventList){
            String results = database.eventsTable.addEvent(event);
            if(!results.equals("success")){
                System.out.print(results);
                return -1; //error
            }
            count++;
        }
        return count;
    }

    private EventModel generateEvent(String personId, String eventType, String descendant , int year, Location eventLocation){
        if(eventLocation == null){
            eventLocation = getRandomLocation();
        }
        EventModel toReturn = new EventModel(personId+"_"+eventType, descendant,
                personId, eventLocation.latitude, eventLocation.longitude, eventLocation.country, eventLocation.city, eventType, year);
        return toReturn;
    }

    //creates and adds a birth event and a baptism event for the user
    private String generateUserData(String userName, String personId){
        int yearOfBirth =  getUserBirthYear();
        String eventIdToAdd = userName + "_birth";
        Location birthLocation = getRandomLocation();
        EventModel userBirthEvent = new EventModel(eventIdToAdd, userName,personId,birthLocation.latitude,
                birthLocation.longitude, birthLocation.country, birthLocation.city, "BIRTH", yearOfBirth);

        Location userBaptismLocation = getRandomLocation();
        EventModel userBaptismEvent = new EventModel(userName+"_baptism", userName, personId, userBaptismLocation.latitude,
                userBaptismLocation.longitude, userBaptismLocation.country, userBaptismLocation.city, "BAPTISM", yearOfBirth+8);




        String result = database.eventsTable.addEvent(userBirthEvent);
        if(!result.equals("success")){
            return result;
        }
        result = database.eventsTable.addEvent(userBaptismEvent);
        return result;
    }

    //returns a random year from 10 to 25 years ago.
    private int getUserBirthYear(){
        Calendar currentDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        Random rand = new Random();
        int presentAge = rand.nextInt(15) + 10; //starting age is 10.  Max age is twenty five for a current user.
        return currentYear - presentAge; // return birth year
    }

    private int generateYear(int min, int max){ // generates a random number inbetween two numbers.
        Random r = new Random();
        int toReturn = r.nextInt(max-min) + min;
        return toReturn;
    }
}
