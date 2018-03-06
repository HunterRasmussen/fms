package fms.main;

import com.sun.net.httpserver.*;
import org.junit.runner.JUnitCore;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import fms.dao.Database;
import fms.facade.ServerFacade;
import fms.models.AuthTokModel;
import fms.models.UserModel;
import fms.results.LoginRegisterResult;
import fms.test.TestJunit;
import java.util.Random;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;


public class Main {

    private static int PORT_NUMBER;
    private HttpServer myServer;
    private static final int MAX_WAITING_CONNECTIONS = 12;
    private Gson gson = new Gson();
    private ServerFacade facade;


    Connection connection = null;   //------
    PreparedStatement stmt = null;  //-------
    ResultSet results = null;       //--------

    public static void main(String[] args) throws Exception{

        Database theDatabase = new Database();
        theDatabase.clearDb();
        PORT_NUMBER = Integer.parseInt(args[0]);
        new Main().runServer();

        //new Main().runJunitTests();
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
        myServer.createContext("/fill", fillHandler);
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

        }
    };

    private HttpHandler fillHandler = new HttpHandler(){
        @Override
        public void handle(HttpExchange exchange){

        }
    };

    private HttpHandler loginHandler = new HttpHandler(){
        @Override
        public void handle(HttpExchange exchange){

        }
    };

    private HttpHandler registerHandler = new HttpHandler(){
        @Override
        public void handle(HttpExchange httpExchange){
            LoginRegisterResult result = new LoginRegisterResult();
            try{


                if (httpExchange.getRequestMethod().toLowerCase().equals("post")){
                    Headers reqHeaders= httpExchange.getRequestHeaders();
                    System.out.println(reqHeaders);
                    InputStream reqBody = httpExchange.getRequestBody();
                    String reqData= readString(reqBody);
                    System.out.println("    Request Body: " + reqData);
                    JsonObject json = gson.fromJson(reqData, JsonObject.class);
                    UserModel user = new UserModel();
                    if(json!= null){
                        Boolean validUserCheck = true;
                        if(json.has("userName")){
                            user.setUserName(json.get("userName").getAsString());
                        }
                        else{
                            validUserCheck = false;
                            JsonObject toSend = createJsonMessagFromString("Error.  No userName data found in the register request");
                            sendData(toSend,httpExchange);
                        }

                        if(json.has("password")){
                            user.setPassword(json.get("password").getAsString());
                        }
                        else{
                            validUserCheck = false;
                            JsonObject toSend = createJsonMessagFromString("Error.  No password data found in the register request");
                            sendData(toSend,httpExchange);
                        }
                        if(json.has("email")){
                            user.setEmail(json.get("email").getAsString());
                        }
                        else{
                            validUserCheck = false;
                            JsonObject toSend = createJsonMessagFromString("Error.  No Email data found in the register request");
                            sendData(toSend,httpExchange);
                        }
                        if(json.has("firstName")){
                            user.setFirstName(json.get("firstName").getAsString());
                        }
                        else{
                            validUserCheck = false;
                            JsonObject toSend = createJsonMessagFromString("Error.  No First Name data found in the register request");
                            sendData(toSend,httpExchange);
                        }
                        if(json.has("lastName")){
                            user.setLastName(json.get("lastName").getAsString());
                        }
                        else{
                            validUserCheck = false;
                            JsonObject toSend = createJsonMessagFromString("Error.  No Last Name data found in the register request");
                            sendData(toSend,httpExchange);
                        }
                        if(json.has("gender")){
                            String gender = (json.get("gender").getAsString());
                            if(gender.length() >1){
                                validUserCheck = false;
                                JsonObject toSend = createJsonMessagFromString("Error.  Gender data length is longer than one. Enter either an m or an f");
                                sendData(toSend,httpExchange);
                            }
                            else{
                                gender.toLowerCase();
                                user.setGender(gender.charAt(0));
                            }
                        }
                        else{
                            validUserCheck = false;
                            JsonObject toSend = createJsonMessagFromString("Error.  No Gender data found in the register request");
                            sendData(toSend,httpExchange);
                        }
                        if(validUserCheck) {
                            LoginRegisterResult registerResult = facade.registerUser(user);
                            if (registerResult.isSuccessFlag() == false) {
                                if(registerResult.getErrorMessage().contains("column USERNAME is not unique")){
                                    JsonObject toSend = createJsonMessagFromString("That UserName is already in use.  Try a different one.");
                                    sendData(toSend, httpExchange);
                                }
                                else {
                                    JsonObject toSend = createJsonMessagFromString(registerResult.getErrorMessage());
                                    sendData(toSend, httpExchange);
                                }
                            }
                            else {
                                AuthTokModel toSend = new AuthTokModel();
                                toSend.setAuthTok(registerResult.getAuthTok());
                                toSend.setPersonId(registerResult.getPersonId());
                                toSend.setUserName(registerResult.getUserName());
                                sendData(toSend, httpExchange);
                            }
                        }
                        /*System.out.println("Sending the completed user back to the web server");
                        sendData(user, httpExchange);*/
                    }
                    else{
                        JsonObject toSend = createJsonMessagFromString("Error with json Object from request body.  It seems to be null.");
                        sendData(toSend,httpExchange);
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

        }
    };

    private HttpHandler eventHandler = new HttpHandler(){
        @Override
        public void handle(HttpExchange exchange){

        }
    };

    private HttpHandler clearHandler = new HttpHandler(){
        @Override
        public void handle(HttpExchange exchange){

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

    private JsonObject createJsonMessagFromString (String messageToSend){
        JsonObject toReturn = new JsonObject();
        toReturn.addProperty("message",messageToSend);
        return toReturn;
    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(sr);
        for(String line = reader.readLine(); line != null; line = reader.readLine()) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }




    public void runJunitTests(){
        JUnitCore.main(
                "fms.test.TestJunit",
                "fms.test.AuthTokModelTest",
                "fms.test.UserDaoTest",
                "fms.test.EventDaoTest",
                "fms.test.AuthTokDaoTest",
                "fms.test.PersonDaoTest"
        );
    }
}
