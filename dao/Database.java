package fms.dao;

import java.io.File;
import java.sql.*;



public class Database {

    public PersonDao personTable;
    public EventDao eventsTable;
    public UserDao usersTable;
    public AuthTokDao authTokTable;
    public Connection connection;



    public Database(){
        loadDriver();
        openConnection();
        usersTable = new UserDao(this);
        eventsTable = new EventDao(this);
        personTable = new PersonDao(this);
        authTokTable = new AuthTokDao(this);
        try{
            createTable();
        }
        catch(SQLException e){
            System.out.println("Error when creating the database tables\n");
            return;
        }
        //System.out.println("Database and tables created");
    }


    //loads the driver to talk to the database
    private void loadDriver(){
        try{
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        }
        catch(ClassNotFoundException e){
            System.out.println("invalid string. Class not found exception!");
        }
    }

    private void openConnection(){
        String dbname = "jdbc:sqlite:myDatabase.sqlite";
        try {
            connection = DriverManager.getConnection(dbname);
        }
        catch(SQLException e){
            System.out.println("Error opening connection to database\n");
            return;
        }
    }

    private void createTable() throws SQLException{

        String createUserTable = "create table if not exists User\n" +
                "    (USERNAME text PRIMARY KEY,\n" +
                "    PASSWORD text NOT NULL,\n" +
                "    EMAIL text NOT NULL,\n" +
                "    FIRSTNAME text NOT NULL,\n" +
                "    LASTNAME text NOT NULL,\n" +
                "    GENDER char NOT NULL,\n" +
                "    PERSONID text NOT NULL)";

        String createPersonTable = "create table if not exists Person\n" +
                "    (PERSONID text  PRIMARY KEY,\n" +
                "    DESCENDANT text NOT NULL,\n" +
                "    FIRSTNAME text NOT NULL,\n" +
                "    LASTNAME text NOT NULL,\n" +
                "    GENDER char NOT NULL,\n" +
                "    FATHERID text,\n" +
                "    MOTHERID text, \n" +
                "    SPOUSEID text)";

        String createEventTable = "create table if not exists Event\n" +
                "    (EVENTID text PRIMARY KEY,\n" +
                "    DESCENDANT TEXT NOT NULL,\n" +
                "    EVENTTYPE text NOT NULL,\n" +
                "    PERSONID TEXT NOT NULL,\n" +
                "    LATITUDE REAL NOT NULL,\n" +
                "    LONGITUDE REAL NOT NULL,\n" +
                "    COUNTRY text NOT NULL,\n" +
                "    CITY text NOT NULL,\n" +
                "    YEAR INT NOT NULL)";

        String createAuthTokTable = "create table if not exists AuthTok\n" +
                "    (USERNAME TEXT NOT NULL,\n" +
                "    AUTHTOK TEXT NOT NULL)";


        //System.out.println(createUserTable);
        PreparedStatement UserTableStmt = connection.prepareStatement(createUserTable);
        UserTableStmt.executeUpdate();
        UserTableStmt.close();

        PreparedStatement PersonTableStmt = connection.prepareStatement(createPersonTable);
        PersonTableStmt.executeUpdate();
        PersonTableStmt.close();

        PreparedStatement EventTableStmt = connection.prepareStatement(createEventTable);
        EventTableStmt.executeUpdate();
        EventTableStmt.close();

        PreparedStatement AuthTokTableStmt = connection.prepareStatement(createAuthTokTable);
        AuthTokTableStmt.executeUpdate();
        AuthTokTableStmt.close();
    }

    public void clearDb() throws SQLException{
        String dropEvents = "drop table if exists Event; ";
        String dropUsers = "drop table if exists User; ";
        String dropPersons = "drop table if exists Person; ";
        String dropAuthToks = "drop table if exists AuthTok";


        PreparedStatement dropEventsStmt = connection.prepareStatement(dropEvents);
        dropEventsStmt.executeUpdate();
        dropEventsStmt.close();

        PreparedStatement dropUsersStmt = connection.prepareStatement(dropUsers);
        dropUsersStmt.executeUpdate();
        dropUsersStmt.close();

        PreparedStatement dropPersonsStmt = connection.prepareStatement(dropPersons);
        dropPersonsStmt.executeUpdate();
        dropPersonsStmt.close();

        PreparedStatement dropAuthTokStmt = connection.prepareStatement(dropAuthToks);
        dropAuthTokStmt.executeUpdate();
        dropAuthTokStmt.close();

    }
}
