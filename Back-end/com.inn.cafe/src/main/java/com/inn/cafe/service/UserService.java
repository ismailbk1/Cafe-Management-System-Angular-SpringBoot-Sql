package com.inn.cafe.service;

import com.inn.cafe.wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService {

   public ResponseEntity<String> signup(Map<String,String > requestmap);

   public ResponseEntity<List<UserWrapper>> getAllUSer();

   public ResponseEntity<String> update(Map<String,String > requestmap);

   public ResponseEntity<String> checkToken();

   public ResponseEntity<String> changePassword(Map<String,String> requestmap);

   public  ResponseEntity<String > forgotPassword(Map<String,String> requestmap);



}
