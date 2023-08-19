package com.inn.cafe.restImpl;

import com.inn.cafe.constents.CafeConstents;
import com.inn.cafe.model.Category;
import com.inn.cafe.rest.CategoryRest;
import com.inn.cafe.serviceimpl.CategoryServiceImpl;
import com.inn.cafe.utils.Cafeutils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
public class CategoryRestImpl implements CategoryRest {

    @Autowired
    CategoryServiceImpl categoryService;
    @Override
    public ResponseEntity<String> addCategory(Map<String, String> requestmap) {
        try{
            log.info("in the add category function ");

            return categoryService.addCategory(requestmap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Cafeutils.getResonseEntity(CafeConstents.Something_went_wrong, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {

        log.info(filterValue);

        try {
         return  categoryService.getAllCategory(filterValue);
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestmap) {
        try {
         return    categoryService.updateCategory(requestmap);
        }catch (Exception e){
            e.printStackTrace();
        }

        return Cafeutils.getResonseEntity(CafeConstents.Something_went_wrong,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
