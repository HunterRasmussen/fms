package fms.test;


import org.junit.runner.JUnitCore;

import java.sql.SQLException;

import fms.dao.Database;

public class UnitTests {

    public void runUnitTests(){
        Database theDatabase = new Database();
        try{
            theDatabase.clearDb();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        JUnitCore.main(
                "fms.test.AuthTokModelTest",
                "fms.test.UserDaoTest",
                "fms.test.EventDaoTest",
                "fms.test.AuthTokDaoTest",
                "fms.test.PersonDaoTest",
                "fms.test.EventModelTest",
                "fms.test.PersonModelTest",
                "fms.test.UserModelTest"
        );
    }
}
