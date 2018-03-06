package fms.facade;


import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fms.dao.Database;
import fms.models.AuthTokModel;
import fms.models.PersonModel;
import fms.models.UserModel;
import fms.results.LoginRegisterResult;
import fms.results.SinglePersonResult;
import fms.services.SinglePerson;

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

    public String generatePersonId(String username){
        currentPersonIdCount++;
        String toReturn = username + currentPersonIdCount;
        return toReturn;
    }

    public LoginRegisterResult registerUser(UserModel user){
        user.setPersonId(generatePersonId(user.getUserName()));
        LoginRegisterResult addUserResult = new LoginRegisterResult();
        PersonModel toAdd = new PersonModel(user.getPersonId(), user.getUserName(), user.getFirstName(), user.getLastName(), user.getGender(), null, null, null);
        try{
            addUserResult = database.usersTable.addUser(user);
            if(addUserResult.isSuccessFlag()){
                SinglePersonResult addPersonResult = database.personTable.addPerson(toAdd);
                if(addPersonResult.isFlag()) {
                    AuthTokModel authTokModel = new AuthTokModel(createAuthToken(user.getUserName()), user.getUserName(), user.getPersonId());
                    addUserResult = database.authTokTable.addAuthTok(authTokModel);
                    this.fill(4,user.getUserName(), toAdd);
                }
                else{
                    addUserResult.setErrorMessage(addPersonResult.getErrorMessage());
                    addUserResult.setSuccessFlag(addPersonResult.isFlag());
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

    public String fill(int numberOfGenerations, String userName, PersonModel startingPerson){
        if(numberOfGenerations == 0) return "Successfully added " + 0 + " persons and " + 0 + " events to the database.";

        int personCount = 1;
        int eventCount = 0;

        //remove current events for the StartingPerson;
        String result = database.eventsTable.removeEventsbyPersonId(startingPerson.getPersonId());
        if(result != "success"){
            return result;
        }
        //remove current associated People from StartingPerson
        result = database.personTable.removePersonsByDescendantId(userName);
        if(result != "success"){
            return result;
        }


        return null;

    }
}
