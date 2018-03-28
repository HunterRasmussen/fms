package fms.main;

import com.google.gson.stream.JsonReader;
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;

import fms.dao.Database;
import fms.test.UnitTests;
import fms.facade.ServerFacade;
import fms.models.AuthTokModel;
import fms.models.EventModel;
import fms.models.PersonModel;
import fms.models.UserModel;
import fms.results.LoginRegisterResult;

import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;


public class Main {

    private static int PORT_NUMBER;
    private HttpServer myServer;
    private static final int MAX_WAITING_CONNECTIONS = 12;
    private Gson gson = new Gson();
    private ServerFacade facade;

    public static void main(String[] args) throws Exception{
        UnitTests junitTest = new UnitTests();
        //junitTest.runUnitTests();
        PORT_NUMBER = Integer.parseInt(args[0]);
        new Main().runServer();
    }


    private void runServer() throws Exception{
        System.out.println("Initializing server on port " + PORT_NUMBER);
        try{
            myServer = HttpServer.create(new InetSocketAddress(PORT_NUMBER), MAX_WAITING_CONNECTIONS);
            System.out.println("Server created");
        }
        catch(IOException e){
            System.out.println("Couldn't create an HTTP server");
            System.out.println(e.getMessage());
            if(e.getMessage().contains("Address already in use")){
                System.out.println("You have another server already running on this port. Close it and try again");
            }
            return;
        }

        myServer.setExecutor(null);
        System.out.println("Creating contexts");
        myServer.createContext("/user/login", loginHandler);
        myServer.createContext("/user/register", registerHandler);
        myServer.createContext("/clear", clearHandler);
        myServer.createContext("/fill/", fillHandler);
        myServer.createContext("/load", loadHandler);
        myServer.createContext("/person", personHandler);
        myServer.createContext("/event", eventHandler);
        myServer.createContext("/", defaultHandler);

        System.out.println("Starting server");
        myServer.start();
        System.out.println("Server started");
        facade = new ServerFacade();
        System.out.println("Facade created");
    }

    private HttpHandler loadHandler = new HttpHandler(){
        @Override
        public void handle(HttpExchange exchange){
            if(exchange.getRequestMethod().toLowerCase().equals("post")){
                String clearResult = facade.clearDatabase();
                if(!clearResult.equals("success")){
                    JsonObject toSend = createJsonMessagFromString(clearResult);
                    sendData(toSend, exchange);
                    return;
                }
                InputStream requestBody = exchange.getRequestBody();
                InputStreamReader streamReader = new InputStreamReader(requestBody);
                JsonReader jsonReader = new JsonReader(streamReader);
                String result  = facade.load(jsonReader);
                JsonObject toSend = createJsonMessagFromString(result);
                sendData(toSend, exchange);
                try{
                    requestBody.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                    JsonObject error = createJsonMessagFromString(e.getMessage());
                    sendData(error, exchange);
                }
            }
        }
    };

    private HttpHandler fillHandler = new HttpHandler(){
        @Override
        public void handle(HttpExchange exchange){
            if(exchange.getRequestMethod().toLowerCase().equals("post")){
                String path = exchange.getRequestURI().getPath();
                String[] arguments  = parsePath(path);
                if (arguments[1] == null){
                    // that means they forgot the required userName parameter in the request header
                    JsonObject toSend = createJsonMessagFromString("Error.  No Username in request path");
                    sendData(toSend, exchange);
                }
                String registeredUserCheck = facade.checkRegisteredUser(arguments[1]);
                if(!registeredUserCheck.equals("success")){
                    JsonObject toSend = createJsonMessagFromString(registeredUserCheck);
                    sendData(toSend, exchange);
                    }
                else{
                    if(arguments[2] == null) {
                        //fill with 4 gens
                        String fillResult = facade.fill(4, arguments[1], null);
                        JsonObject toSend = createJsonMessagFromString(fillResult);
                        sendData(toSend, exchange);
                    }
                    else{
                        int generations = 0;
                        try{
                            generations = Integer.parseInt(arguments[2]);
                            //fill with however many gens they asked for
                            String fillResult = facade.fill(generations, arguments[1], null);
                            JsonObject toSend = createJsonMessagFromString(fillResult);
                            sendData(toSend, exchange);
                        }
                        catch (NumberFormatException e){
                            JsonObject toSend = createJsonMessagFromString("Error," +
                                    " second parameter should be a number (int) for number of generations.");
                            sendData(toSend, exchange);
                        }

                    }
                }
            }
        }
    };

    private HttpHandler loginHandler = new HttpHandler(){
        @Override
        public void handle(HttpExchange exchange){
            try {
                if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                    InputStream reqBody = exchange.getRequestBody();
                    String reqData = readString(reqBody);
                    JsonObject json = gson.fromJson(reqData, JsonObject.class);
                    String userName = null;
                    String password = null;
                    if(json.has("userName")){
                        userName = json.get("userName").getAsString();
                    }
                    else{
                        JsonObject toSend = createJsonMessagFromString("Error. No userName datafound in login request.");
                        sendData(toSend, exchange);
                        return;
                    }
                    if(json.has("password")){
                        password = json.get("password").getAsString();
                    }
                    else{
                        JsonObject toSend = createJsonMessagFromString("Error. No password datafound in login request.");
                        sendData(toSend, exchange);
                        return;
                    }
                    LoginRegisterResult loginResult = facade.loginUser(userName, password);
                    if(!loginResult.isSuccessFlag()){
                        JsonObject toSend = createJsonMessagFromString(loginResult.getErrorMessage());
                        sendData(toSend, exchange);
                        return;
                    }
                    AuthTokModel toSend = new AuthTokModel();
                    toSend.setAuthTok(loginResult.getAuthTok());
                    toSend.setPersonId(loginResult.getPersonId());
                    toSend.setUserName(loginResult.getUserName());
                    sendData(toSend, exchange);

                }
            }
            catch (IOException e){
                System.out.println(e.getMessage());
            }
        }
    };

    private HttpHandler registerHandler = new HttpHandler(){
        @Override
        public void handle(HttpExchange httpExchange){
            LoginRegisterResult result = new LoginRegisterResult();
            try{
                if (httpExchange.getRequestMethod().toLowerCase().equals("post")){
                    //Headers reqHeaders= httpExchange.getRequestHeaders();
                    //System.out.println(reqHeaders);
                    InputStream reqBody = httpExchange.getRequestBody();
                    String reqData= readString(reqBody);
                    //System.out.println("    Request Body: " + reqData);
                    JsonObject json = gson.fromJson(reqData, JsonObject.class);
                    UserModel user = new UserModel();
                    String validUserCheck = null;
                    if(json!= null){
                        validUserCheck = checkValidUser(user, json);

                        if(validUserCheck.equals("success")) {
                            // register the user with the facade
                            LoginRegisterResult registerResult = facade.registerUser(user);
                            //if not successful
                            if (registerResult.isSuccessFlag() == false) {
                                //send error message
                                JsonObject toSend = createJsonMessagFromString(registerResult.getErrorMessage());
                                sendData(toSend, httpExchange);

                            }
                            else {
                                //send back Authtoken, personId and userName
                                AuthTokModel toSend = new AuthTokModel();
                                toSend.setAuthTok(registerResult.getAuthTok());
                                toSend.setPersonId(registerResult.getPersonId());
                                toSend.setUserName(registerResult.getUserName());
                                sendData(toSend, httpExchange);
                                reqBody.close();
                            }
                        }
                        else{
                            JsonObject toSend = createJsonMessagFromString(validUserCheck);
                            sendData(toSend,httpExchange);
                            reqBody.close();
                        }
                    }
                    else{
                            JsonObject toSend = createJsonMessagFromString("Error.  No jsonObject in the request body");
                            sendData(toSend,httpExchange);
                            reqBody.close();
                    }

                    /*httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    String respData = "username: hunter \n ";
                    OutputStream respBody = httpExchange.getResponseBody();
                    writeString(respData, respBody);
                    System.out.println("Line 132 " + respBody);
                    respBody.close();*/
                }

            }
            catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
    };

    private HttpHandler personHandler = new HttpHandler(){
        @Override
        public void handle(HttpExchange exchange){
            if(exchange.getRequestMethod().toLowerCase().equals("get")){
                Headers reqHeaders = exchange.getRequestHeaders();
                //is there an auth token?
                if(!reqHeaders.containsKey("Authorization")){
                    JsonObject toSend = createJsonMessagFromString("Error.  This request requires an authorization token.  Please include yours");
                    sendData(toSend,exchange);
                }
                else{
                    //get authToken
                    String authToken = reqHeaders.getFirst("Authorization");

                    //get uri path
                    String path = exchange.getRequestURI().getPath();
                    //get different arguments
                    String[] arguments  = parsePath(path);

                    String userName = facade.checkValidAuthToken(authToken);
                    if(userName == null){
                        JsonObject toSend = createJsonMessagFromString("Error. Invalid AuthToken.  Are you logged in?");
                        sendData(toSend,exchange);
                    }
                    else{
                        // there is no personId attatched. That means get all the people associated with the user
                        if(arguments[1] == null) {
                            List<PersonModel> ancestors = facade.getAllPeople(userName);
                            if(ancestors.size() == 0){
                                JsonObject toSend = createJsonMessagFromString("Didn't find any people with you as their descendant.");
                                sendData(toSend,exchange);
                            }
                            else{
                                sendData(ancestors, exchange);
                            }
                        }
                        //get that specific individual
                        else{

                            PersonModel person =  facade.getSinglePerson(userName, arguments[1]);
                            if(person == null){
                                JsonObject toSend = createJsonMessagFromString("Error getting that person from the database.  Perhaps they don't exist");
                                sendData(toSend,exchange);
                            }
                            else if(person.getFirstName().equals("invalid")){
                                JsonObject toSend = createJsonMessagFromString("Error. That person does not belong to you.");
                                sendData(toSend,exchange);
                            }
                            else{
                                sendData(person, exchange);
                            }
                        }

                    }
                }
                //System.out.println(reqHeaders);

            }


        }
    };

    private HttpHandler eventHandler = new HttpHandler(){
        @Override
        public void handle(HttpExchange exchange){
            if(exchange.getRequestMethod().toLowerCase().equals("get")){
                Headers reqHeaders = exchange.getRequestHeaders();
                //is there an auth token?
                if(!reqHeaders.containsKey("Authorization")){
                    JsonObject toSend = createJsonMessagFromString("Error.  This request requires an authorization token.  Please include yours");
                    sendData(toSend,exchange);
                    return;
                }
                //get authToken and check to see if it is valid
                String userName = facade.checkValidAuthToken(reqHeaders.getFirst("Authorization"));
                if(userName == null){
                    JsonObject toSend = createJsonMessagFromString("Error. Invalid AuthToken.  Are you logged in?");
                    sendData(toSend,exchange);
                    return;
                }

                //get uri path
                String path = exchange.getRequestURI().getPath();
                //is the client requesting all events or just one event?  get the arguments
                String[] arguments  = parsePath(path);

                // there is no eventID attatched. That means get all the people associated with the user
                if(arguments[1] == null) {
                    List<EventModel> events = facade.getAllEvents(userName);
                    if(events.size() == 0){
                        JsonObject toSend = createJsonMessagFromString("Didn't find any events associated with you or there was some internal server error");
                        sendData(toSend,exchange);
                    }
                    else{
                        sendData(events, exchange);
                    }
                }
                //there is an eventID attatched.  Get that one.
                else{
                    EventModel event = facade.getSingleEvent(userName, arguments[1]);
                    if (event == null){
                        JsonObject toSend = createJsonMessagFromString("Error getting that event from the database.  Perhaps it doesn't exist");
                        sendData(toSend,exchange);
                        return;
                    }
                    else if (event.getEventID().equals("invalid")){
                        JsonObject toSend = createJsonMessagFromString("Error. That event does not belong to you or your ancestors. ");
                        sendData(toSend,exchange);
                    }
                    else{
                        sendData(event, exchange);
                    }

                }
            }
        }
    };

    private HttpHandler clearHandler = new HttpHandler(){
        @Override
        public void handle(HttpExchange exchange){
           String result =  facade.clearDatabase();

           if(result.equals("success")){

               JsonObject toSend = createJsonMessagFromString("Clear succeeded");
               sendData(toSend,exchange);

           }
           else{
               JsonObject toSend = createJsonMessagFromString(result);
               sendData(toSend,exchange);
           }

        }
    };

    private HttpHandler defaultHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            boolean success = false;
            try {
                String root = "/Users/AthleticsVideoMacPro/Documents/Hunter_Rasmussen/family map server/MyApplication";
                URI uri = httpExchange.getRequestURI();
                System.out.println(uri);
                File file = new File(root + uri.getPath()).getCanonicalFile();
                if (!file.getPath().startsWith(root)) {
                    // Suspected path traversal attack: reject with 403 error.
                    String response = "403 (Forbidden)\n";
                    httpExchange.sendResponseHeaders(403, response.length());
                    OutputStream os = httpExchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
                else if (!file.isFile()) {
                    // Object does not exist or is not a file: reject with 404 error.
                    String response = "404 (Not Found)\n";
                    httpExchange.sendResponseHeaders(404, response.length());
                    OutputStream os = httpExchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
                else {
                    // Object exists and is a file: accept with response code 200.
                    httpExchange.sendResponseHeaders(200, 0);
                    OutputStream os = httpExchange.getResponseBody();
                    FileInputStream fs = new FileInputStream(file);
                    final byte[] buffer = new byte[0x10000];
                    int count = 0;
                    while ((count = fs.read(buffer)) >= 0) {
                        os.write(buffer, 0, count);
                    }
                    fs.close();
                    os.close();
                }
            }
            catch (Exception e) {
                httpExchange.getResponseBody().close();
                // Display/log the stack trace
                e.printStackTrace();
            }
        }
    };



    //------------------------------------------------------//

    /**
     *      sends data from the server to the client using http.  Converts the object given, t
     *      turns it to json, sends it.
     * @param obj  -- the object you want to send through http
     * @param httpExchange -- the current exchange object so it knows what vessel through which to
     *                     send the data
     *
     * Catches IOException
     */
    private void sendData(Object obj, HttpExchange httpExchange){
        try{
            if(obj == null){
               httpExchange .sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
            }
            else{
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStreamWriter os = new OutputStreamWriter(httpExchange.getResponseBody());
                Gson gsonToSend = new GsonBuilder().disableHtmlEscaping().create();
                String jsonToSend = gsonToSend.toJson(obj);
                os.write(jsonToSend);
                os.close();
            }
        }
        catch(IOException e){
            //e.printStackTrace();
            System.out.println("\n Error sending data from sendData method");
        }
    }

    JsonObject createJsonMessagFromString (String messageToSend){
        JsonObject toReturn = new JsonObject();
        toReturn.addProperty("message",messageToSend);
        return toReturn;
    }

    String checkValidUser(UserModel user, JsonObject json){
        if(json.has("userName")){
            user.setUserName(json.get("userName").getAsString());
        }
        else{
            return "Error. No userName datafound in register request.";
        }

        if(json.has("password")){
            user.setPassword(json.get("password").getAsString());
        }
        else{
            return "Error.  NO email datafound in register request.";
        }
        if(json.has("email")){
            user.setEmail(json.get("email").getAsString());
        }
        else{
            return "Error.  No Email data found in the register request";

        }
        if(json.has("firstName")){
            user.setFirstName(json.get("firstName").getAsString());
        }
        else{

            return "Error.  No First Name data found in the register request";

        }
        if(json.has("lastName")){
            user.setLastName(json.get("lastName").getAsString());
        }
        else{
            return"Error.  No Last Name data found in the register request";
        }
        if(json.has("gender")){
            String gender = (json.get("gender").getAsString());
            if(gender.length() >1){
                return "Error.  Gender data length is longer than one. Enter either an m or an f";
            }
            else{
                gender.toLowerCase();
                user.setGender(gender.charAt(0));
            }
        }
        else{
            return "Error.  No Gender data found in the register request";
        }
        return "success";
    }

    String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(sr);
        for(String line = reader.readLine(); line != null; line = reader.readLine()) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }

    String[] parsePath(String path){
        String[] toReturn = new String[3]; //all requests for this project have a maximum of three arguments  eg fill/username/numberofGenerations
        Scanner s = new Scanner(path).useDelimiter("/");
        int index = 0;
        while (s.hasNext()){
            String argument = s.next();
            //System.out.println("Argument is " + argument);
            toReturn[index] = argument;
            index++;
        }
        return toReturn;
    }

}
