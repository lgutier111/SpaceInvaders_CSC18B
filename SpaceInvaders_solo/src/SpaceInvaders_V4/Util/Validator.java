package SpaceInvaders_V4.Util;

public class Validator {

    /**
     * valid name alpha 1-20 characters long
     *
     * @param name String value to check
     * @return true if regex patter match
     */
    public static boolean matchName(String name) {

        return name.matches("^[-a-zA-Z ]{1,20}$");
    }

    /**
     * valid email (name@site.domain)
     *
     * @param email string value to check against
     * @return true if regex patter match
     */
    public static boolean matchEmail(String email) {

        return email.matches("^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$");
    }

    /**
     * match Password must be 6-20 characters long, AlphaNumeric or @ # $ %
     *
     * @param password string value to check against
     * @return true if regex patter match
     */
    public static boolean matchPassword(String password) {

        return password.matches("^([a-zA-Z0-9@#$%]{6,20})$");
    }

    /**
     * match username must be 3-20 characters long, AlphaNumeric or @ # $ %
     *
     * @param handle password string value to check against
     * @return true if regex patter match
     */
    public static boolean matchUser(String handle) {

        return handle.matches("^([ a-zA-Z0-9@#$%]{3,20})$");
    }

}
