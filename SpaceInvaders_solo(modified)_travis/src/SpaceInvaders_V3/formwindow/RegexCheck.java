/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SpaceInvaders_V3.formwindow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author rcc
 */
public class RegexCheck {
    
    
// Validate first name.
public static boolean validateFirstName(String firstName){
return firstName.matches("^[a-zA-Z]{1,20}$");
}
// Validate last name.
public static boolean validateLastName(String lastName){
return lastName.matches("^[a-zA-z]+([ '-][a-zA-Z]+)*$");
}
// Validate email
public static boolean validateEmail(String email){
return email.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
}

public static boolean validatePassword(String password){
return password.matches("^\\w*(?=\\w*\\d)(?=\\w*[a-z])(?=\\w*[A-Z])\\w*$");
}

public static boolean validateUsername(String username){
return username.matches("^[a-zA-Z0-9]*([._](?![._])|[a-zA-Z0-9]){5,18}[a-zA-Z0-9]*$");
}


    
}
