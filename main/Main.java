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
import fms.results.LoginRegisterResult;
import fms.test.TestJunit;
import java.util.Random;


public class Main {

    private static int PORT_NUMBER;
    private HttpServer myServer;
    private static final int MAX_WAITING_CONNECTIONS = 12;

    Connection connection = null;   //------
    PreparedStatement stmt = null;  //-------
    ResultSet results = null;       //--------

    public static void main(String[] args) throws Exception{

        Database theDatabase = new Database();
        String chars = "abccdefghijklnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder authTokBuild = new StringBuilder();
        for(int i =0; i < 6;i++){
            //append their user id to the front
            Random rand = new Random();
            int randomIndex = rand.nextInt(chars.length()-1); // chars.length()-1 is the largest index, and 0 is the smalles index
            authTokBuild.append(chars.charAt(randomIndex));
        }
        System.out.println(authTokBuild.toString());

        theDatabase.clearDb();

        PORT_NUMBER = Integer.parseInt(args[0]);

        new Main().runServer();

        //new Main().runJunitTests();


        System.out.println("finished tests?");
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

                /*if (httpExchange.getRequestMethod().toLowerCase().equals("get")) {
                    Headers reqHeaders = httpExchange.getRequestHeaders();
                    System.out.println("Hey.  Made it to default Handler good job");
                    System.out.println(reqHeaders);
                    String respData = "Welcome to the default Handler.  We're trying to get this so it actually does what you and I want.  Please be patient.  Try back in 1 year.";
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream respBody = httpExchange.getResponseBody();
                    writeString(respData, respBody);
                    respBody.close();
                    success = true;*/
                //if (httpExchange.getRequestMethod().toLowerCase().equals("post")){
                    Headers reqHeaders= httpExchange.getRequestHeaders();
                    System.out.println("Hey made it to the register handler.");
                    System.out.println(reqHeaders);
                    InputStream reqBody = httpExchange.getRequestBody();
                    String reqData = readString(reqBody);
                    System.out.println(reqData);
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    String respData = "username: hunter \n ";
                    OutputStream respBody = httpExchange.getResponseBody();
                    writeString(respData, respBody);
                    respBody.close();
                //}

            }
            catch(Exception e){

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
                    os.write(response.getBytes()); os.close();
                }
                else if (!file.isFile()) {
                    // Object does not exist or is not a file: reject with 404 error.
                    String response = "404 (Not Found)\n";
                    httpExchange.sendResponseHeaders(404, response.length());
                    OutputStream os = httpExchange.getResponseBody();
                    os.write(response.getBytes()); os.close();
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

               /* if (httpExchange.getRequestMethod().toLowerCase().equals("get")) {
                    Headers reqHeaders = httpExchange.getRequestHeaders();
                    System.out.println("Hey.  Made it to default Handler good job");
                    System.out.println(reqHeaders);
                    String respData = "Welcome to the default Handler.  We're trying to get this so it actually does what you and I want.  Please be patient.  Try back in 1 year.";
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream respBody = httpExchange.getResponseBody();
                    writeString(respData, respBody);
                    respBody.close();
                    success = true;
                }
            }*/
            catch (Exception e) {
                httpExchange.getResponseBody().close();
                // Display/log the stack trace
                e.printStackTrace();
            }
        }
    };

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
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
