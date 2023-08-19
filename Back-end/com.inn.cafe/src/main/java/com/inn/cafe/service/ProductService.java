package com.inn.cafe.service;

import com.inn.cafe.wrapper.ProductWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ProductService {
    public ResponseEntity<String> addNewProduct(Map<String, String> requestmap);

   public ResponseEntity<List<ProductWrapper>> gatAllProduct();

    ResponseEntity<String> updateProduct(Map<String, String> requestMap);

    ResponseEntity<String> deleteProduct(Integer id);

    ResponseEntity<String> updateStatus(Map<String, String> requestMap);

    ResponseEntity<List<ProductWrapper>> getProductByCategory(Integer id);

    ResponseEntity<ProductWrapper> getProductById(Integer id);
}
