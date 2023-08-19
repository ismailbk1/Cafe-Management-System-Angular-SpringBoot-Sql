package com.inn.cafe.dao;

import com.inn.cafe.model.Category;
import org.apache.coyote.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryDao extends JpaRepository<Category,Integer> {


    public List<Category> getAllCategory();
}
