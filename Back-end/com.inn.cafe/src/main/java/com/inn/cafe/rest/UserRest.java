package com.inn.cafe.rest;

import com.inn.cafe.wrapper.UserWrapper;
import org.springframework.beans.factory.ListableBeanFactoryExtensionsKt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path= "/user")
public interface UserRest {
@PostMapping(path="/signup")
    public ResponseEntity<String> signup(@RequestBody(required = true) Map<String,String> requestmap);


@GetMapping(path="/get")
    public ResponseEntity<List<UserWrapper>> getAllUser();


@PostMapping(path="/update")
    public ResponseEntity<String> update(@RequestBody(required = true) Map<String,String> requestmap);


@GetMapping(path = "/checkToken")
public ResponseEntity<String> checkToken();

@PostMapping(path = "/changePassword")

    public ResponseEntity<String> changePassword(@RequestBody(required = true) Map<String,String> requestmap);

@PostMapping(path = "/forgotPassword")

    public ResponseEntity<String> forgotPassword(@RequestBody(required = true) Map<String,String> requestmap);
}





