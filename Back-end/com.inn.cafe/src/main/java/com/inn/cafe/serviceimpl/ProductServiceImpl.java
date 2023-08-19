package com.inn.cafe.serviceimpl;

import com.inn.cafe.constents.CafeConstents;
import com.inn.cafe.dao.ProductDao;
import com.inn.cafe.jwt.JwtFilter;
import com.inn.cafe.model.Category;
import com.inn.cafe.model.Product;
import com.inn.cafe.service.ProductService;
import com.inn.cafe.utils.Cafeutils;
import com.inn.cafe.wrapper.ProductWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    ProductDao productDao;

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestmap) {
        try {
            if (jwtFilter.isAdmin()) {
                log.info("is Admin");
                if (valditeMap(requestmap, false)) {
                    productDao.save(extractProductFromMap(requestmap, false));
                    return Cafeutils.getResonseEntity("Product Add Successfully", HttpStatus.OK);
                } else {
                    return Cafeutils.getResonseEntity(CafeConstents.INVALID_DATA, HttpStatus.BAD_REQUEST);
                }


            } else {

                return Cafeutils.getResonseEntity(CafeConstents.UNAUTORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return Cafeutils.getResonseEntity(CafeConstents.Something_went_wrong, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> gatAllProduct() {
        try {
            return new ResponseEntity<>(productDao.gatAllProduct(), HttpStatus.OK);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                log.info(String.valueOf(valditeMap(requestMap, true)));
                if (valditeMap(requestMap, true)) {
                    Optional<Product> optional = productDao.findById(Integer.parseInt(requestMap.get("id")));

                    if (!optional.isEmpty()) {
                        Product product = extractProductFromMap(requestMap, true);
                        product.setStatus(optional.get().getStatus());
                        productDao.save(product);
                        return Cafeutils.getResonseEntity("Product Updated Successfully ", HttpStatus.OK);

                    } else {
                        return Cafeutils.getResonseEntity("Product Id don't Found In The Data Base", HttpStatus.OK);
                    }


                } else {
                    return Cafeutils.getResonseEntity(CafeConstents.INVALID_DATA, HttpStatus.BAD_REQUEST);
                }

            } else {

                return Cafeutils.getResonseEntity(CafeConstents.UNAUTORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Cafeutils.getResonseEntity(CafeConstents.Something_went_wrong, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try {
            if (jwtFilter.isAdmin()) {

                Optional<Product> optional = productDao.findById(id);
                if (!optional.isEmpty()) {
                    productDao.deleteById(id);

                    return Cafeutils.getResonseEntity("Product Delete Successfully ", HttpStatus.OK);

                } else {
                    return Cafeutils.getResonseEntity("Product id Does not exist in the data base ", HttpStatus.OK);
                }

            } else {
                return Cafeutils.getResonseEntity(CafeConstents.UNAUTORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Cafeutils.getResonseEntity(CafeConstents.Something_went_wrong, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<Product> optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
                if (!optional.isEmpty()) {
                    productDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    return Cafeutils.getResonseEntity("Product Status Updated Successfully ", HttpStatus.OK);
                } else {
                    return Cafeutils.getResonseEntity("Product id does not exist ", HttpStatus.OK);

                }

            } else {

                return Cafeutils.getResonseEntity(CafeConstents.UNAUTORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Cafeutils.getResonseEntity(CafeConstents.Something_went_wrong, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getProductByCategory(Integer id) {
        try {
            return new ResponseEntity<>(productDao.getProductByCategory(id), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<ProductWrapper> getProductById(Integer id) {
        try{

            return new ResponseEntity<>(productDao.getProductById(id),HttpStatus.OK);
        }catch (Exception e){

            e.printStackTrace();
        }

        return new ResponseEntity<>(new ProductWrapper(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Product extractProductFromMap(Map<String, String> requestmap, boolean isadd) {

        Category category = new Category();

        category.setId(Integer.parseInt(requestmap.get("categoryId")));
        Product product = new Product();
        if (isadd) {
            product.setId(Integer.parseInt(requestmap.get("id")));
        } else {
            product.setStatus("true");
        }
        product.setName(requestmap.get("name"));
        product.setDescription(requestmap.get("description"));
        product.setPrice(Integer.parseInt(requestmap.get("price")));
        product.setCategory(category);
        return product;


    }

    private boolean valditeMap(Map<String, String> requestmap, boolean validateid) {
        if (requestmap.containsKey("name") && requestmap.containsKey("description")) { //you can verfied all the champ
            if (requestmap.containsKey("id") && validateid) {
                return true;
            } else if (!validateid) {
                return true;
            }
        }
        return false;

    }
}
