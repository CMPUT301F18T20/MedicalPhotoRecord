package Exceptions;

public class UserIDMustBeAtLeastEightCharactersException extends Exception {
    public UserIDMustBeAtLeastEightCharactersException() {
        super("User id needs to be at least 8 characters");
    }
}
