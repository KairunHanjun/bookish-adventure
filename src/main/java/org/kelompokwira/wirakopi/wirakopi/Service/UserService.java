package org.kelompokwira.wirakopi.wirakopi.Service;

import org.kelompokwira.wirakopi.wirakopi.WirakopiApplication.Something;

import org.kelompokwira.wirakopi.wirakopi.Entity.User;

public interface UserService {
    String register(User user);
    String changeSomething(Something something, String valueWannaChange, String searchBy);
    Iterable<User> getUserByEmail(String email);
    /**
     *  This is deprecated because you take all data to database which is take long time
     *  
     * @return Iterable/List all of user credential
     * @since 0.0.1
     */
    @Deprecated
    Iterable<User> getAllUser();
    boolean login(String passHash, String userEmail);
    Iterable<String> allUsernamebyEmail(String email);
    public String sendOTP(String toWhere, String OTP);
}
