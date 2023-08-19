package com.inn.cafe.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "Authorization")
@RequestMapping(path = "/dashboard")
public interface DashboardRest {
    @GetMapping(path = "/details")
    @CrossOrigin(origins = "http://localhost:4200")
    ResponseEntity<Map<String,Object>>getCount();
}
