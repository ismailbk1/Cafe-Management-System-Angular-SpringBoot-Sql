package com.inn.cafe.rest;

import com.inn.cafe.model.Category;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/category")
public interface CategoryRest {

    @PostMapping(path="/add")
    public ResponseEntity<String> addCategory(@RequestBody(required = true) Map<String,String> requestmap);

@GetMapping(path = "/get")
ResponseEntity<List<Category>> getAllCategory(@RequestParam(required = false) String filterValue);

@PostMapping(path = "update")
    ResponseEntity<String> updateCategory(@RequestBody(required = true) Map<String,String> requestmap);
}
