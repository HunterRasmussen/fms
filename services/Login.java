package fms.services;

import fms.requests.LoginRequest;
import fms.results.LoginRegisterResult;


public class Login {


    /**
     * creates a user model and checks if that is in database.  If it is, a new authtoken model
     * is created and then added to the database using the UserDao.
     * @param l LoginRequest object
     * @return LoginRegisterResult object
     */
    LoginRegisterResult loginUser(LoginRequest l){
        return null;
    }
}
