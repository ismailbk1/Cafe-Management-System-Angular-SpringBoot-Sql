package com.inn.cafe.restImpl;

import com.inn.cafe.constents.CafeConstents;
import com.inn.cafe.rest.UserRest;
import com.inn.cafe.service.UserService;
import com.inn.cafe.utils.Cafeutils;
import com.inn.cafe.wrapper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserRestImpl implements UserRest {
@Autowired
    UserService userService;
    @Override
    public ResponseEntity<String> signup(Map<String, String> requestmap) {
        try {
            return  userService.signup(requestmap);
        }catch (Exception e){
            e.printStackTrace();
        }
     return   Cafeutils.getResonseEntity(CafeConstents.Something_went_wrong,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
         try{
             return  userService.getAllUSer();
         }catch (Exception e){
             e.printStackTrace();
         }

         return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestmap) {
          try {
              return userService.update(requestmap);
          }catch(Exception e){
              e.printStackTrace();
          }

          return
                  Cafeutils.getResonseEntity(CafeConstents.Something_went_wrong,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        try {
         return userService.checkToken();

        }catch (Exception e){
            e.printStackTrace();
        }
        return Cafeutils.getResonseEntity(CafeConstents.Something_went_wrong,HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestmap) {
        try {
            return userService.changePassword(requestmap);
        }catch(Exception e){
            e.printStackTrace();
        }
        return Cafeutils.getResonseEntity(CafeConstents.Something_went_wrong,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestmap) {
        try{
            userService.forgotPassword(requestmap);
            return Cafeutils.getResonseEntity("check your Email",HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
        }
        return  Cafeutils.getResonseEntity("probleme fil user impl",HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
