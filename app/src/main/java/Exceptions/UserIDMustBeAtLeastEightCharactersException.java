package Exceptions;

/**
 * UserIDMustBeAtLeastEightCharactersException
 * Thrown when user id < 8 characters
 * @version 2.0
 * @see com.cmput301f18t20.medicalphotorecord.User
 */
public class UserIDMustBeAtLeastEightCharactersException extends Exception {
    public UserIDMustBeAtLeastEightCharactersException() {
        super("User id needs to be at least 8 characters");
    }
}
