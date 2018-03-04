package fms.services;


import fms.requests.RegisterRequest;
import fms.results.LoginRegisterResult;

public class Register {
/**
 *
 * checks each of the data members in the request object.  If they are all valid, this method creates a user
 * and person model to pass to the UserDao to add to the database
 *
 * @param r which contains
 *          username
 *          password
 *          email
 *          firstname
 *          lastname
 *          gender
 *
 * @return A RegisterLoginResult Object which contains
 *          an auth token
 *          username
 *          person id
 */
    LoginRegisterResult registerUser(RegisterRequest r){
        return null;
    }

}
