package fms.services;

/**
 * Created by hrazi94 on 10/16/17.
 */

public class Fill {

    /**
     * First verifies the username given exists in the database.  If it does, then will create the
     * requisite number of persons and events for each of those persons in the model classes then
     * add them into the database through the dao classes.
     *
     *
     * @param username must contain a username that is in the database
     * @param generations is optional.  If no number is given, default is 4
     * @return String message saying if the fill was successful and who and what it added or a
     * message detailing why it wasn't successful
     */
    String fill(String username, int generations){
    return  "";
    }
}
