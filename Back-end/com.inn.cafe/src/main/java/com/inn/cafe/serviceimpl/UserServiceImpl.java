package com.inn.cafe.serviceimpl;

import com.google.common.base.Strings;
import com.inn.cafe.constents.CafeConstents;
import com.inn.cafe.dao.UserDao;
import com.inn.cafe.jwt.JwtFilter;
import com.inn.cafe.model.User;
import com.inn.cafe.service.UserService;
import com.inn.cafe.utils.Cafeutils;
import com.inn.cafe.utils.EmailUtils;
import com.inn.cafe.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ConcreteTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    EmailUtils emailUtils;
    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<String> signup(Map<String, String> requestmap) {
        log.info(requestmap.toString() );
        try {
            if (validateRequestMap(requestmap)) {
                User user = userDao.findByEmailId(requestmap.get("email"));
                     log.info(user.toString());
                if (Objects.isNull(user)) {
                    userDao.save(getUserFromMap(requestmap));
                    return Cafeutils.getResonseEntity("Successfully Register", HttpStatus.OK);
                } else {
                    return Cafeutils.getResonseEntity("Email alreadyy existe .", HttpStatus.BAD_REQUEST);
                }


            } else {
                return Cafeutils.getResonseEntity(CafeConstents.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Cafeutils.getResonseEntity(CafeConstents.Something_went_wrong,HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUSer() {
        try {

log.info(String.valueOf(jwtFilter.isAdmin()));
            if (jwtFilter.isAdmin()) {
                return new ResponseEntity<>(userDao.getAllUser(),HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String,String> requestmap) {
        try {
if (jwtFilter.isAdmin()) {

    Optional<User> optional = userDao.findById(Integer.parseInt(requestmap.get("id")));
    if (!optional.isEmpty()) {
        userDao.updateStatus(requestmap.get("status"), Integer.parseInt(requestmap.get("id")));
        // send email a all admin

     sendMailToAllAdmin(requestmap.get("status"),optional.get().getEmail(),userDao.getAllAdmin());


        return Cafeutils.getResonseEntity("User Status Update Successfuly", HttpStatus.OK);
    } else {
           return Cafeutils.getResonseEntity("User id doesn't exist in the data base",HttpStatus.OK);
    }


}else{
    return Cafeutils.getResonseEntity(CafeConstents.UNAUTORIZED_ACCESS,HttpStatus.UNAUTHORIZED);

}
        }catch (Exception e){
            e.printStackTrace();
        }
        return Cafeutils.getResonseEntity(CafeConstents.Something_went_wrong,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {


        log.info("inside the checkToken methode");
        return   Cafeutils.getResonseEntity("true",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestmap) {
        try {
            User userObject=userDao.findByEmail(jwtFilter.getCurrentUser());
            if(!userObject.equals(null) ){
                if(passwordEncoder.matches(requestmap.get("oldPassword"), userObject.getPassword())){
                    userObject.setPassword(passwordEncoder.encode(requestmap.get("newPassword")));
                    userDao.save(userObject);
                    return Cafeutils.getResonseEntity("Password Updated Successfully",HttpStatus.OK);
                }
                return Cafeutils.getResonseEntity("Incorrect Old Passowrd",HttpStatus.BAD_REQUEST);
            }
            return Cafeutils.getResonseEntity(CafeConstents.Something_went_wrong,HttpStatus.INTERNAL_SERVER_ERROR);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Cafeutils.getResonseEntity(CafeConstents.Something_went_wrong,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestmap) {
        try {
            User user = userDao.findByEmail(requestmap.get("email"));
            if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail())) {
                // Generate a random temporary password
                String temporaryPassword = generateRandomPassword();

                // Update the user's password
                user.setPassword(passwordEncoder.encode(temporaryPassword));
                userDao.save(user);

                // Send the temporary password via email
                emailUtils.forgotMail(user.getEmail(), "Credentials by Cafe Management System", temporaryPassword);

                return Cafeutils.getResonseEntity("Check your email for Credentials", HttpStatus.OK);
            }
            return Cafeutils.getResonseEntity("User not found or email is empty", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Cafeutils.getResonseEntity("Problem occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String generateRandomPassword() {
        // Define the characters that can be used in the temporary password
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        int passwordLength = 10; // Adjust the length as needed

        StringBuilder randomPassword = new StringBuilder();

        // Generate a random password using allowed characters
        Random random = new Random();
        for (int i = 0; i < passwordLength; i++) {
            int randomIndex = random.nextInt(allowedChars.length());
            randomPassword.append(allowedChars.charAt(randomIndex));
        }

        return randomPassword.toString();
    }


    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {

        allAdmin.remove(jwtFilter.getCurrentUser());
        log.info(jwtFilter.getCurrentUser());
        if(status!=null && status.equalsIgnoreCase("true")){
            emailUtils.sendsimpleMessage(jwtFilter.getCurrentUser(),"Account approuved","User:- "+user+" \n isapproved by \n Admin:-" +jwtFilter.getCurrentUser(),allAdmin);
        }else{
            emailUtils.sendsimpleMessage(jwtFilter.getCurrentUser(),"Account Disabled","User:- "+user+" \n isDisabled by \n Admin:-" +jwtFilter.getCurrentUser(),allAdmin);

        }

    }

    private boolean validateRequestMap(Map<String,String> requestmap){
        if(requestmap.containsKey("name") && requestmap.containsKey("email") &&
                requestmap.containsKey("password")
                && requestmap.containsKey("contactNumber")){
                     return true;
        }
        return false;

    }

    private User getUserFromMap(Map<String,String> maprequest){

        User user= null;
                /*User.builder()
                .name(maprequest.get("name"))
                .email(maprequest.get("email"))
                .contactNumber(maprequest.get("contactNumber"))
                .password(maprequest.get("password"))
                .status("false")
        .role("user").build();*/






        return user;
    }
}
