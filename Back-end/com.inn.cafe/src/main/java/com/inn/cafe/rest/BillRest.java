package com.inn.cafe.rest;

import com.inn.cafe.model.Bill;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/bill")
public interface BillRest {

    @PostMapping(path = "/generatReport")
    ResponseEntity<String> generatReport(@RequestBody Map<String, Object> requestMap);

    @GetMapping(path = "/getBills")
    ResponseEntity<List<Bill>> getBills();

    @PostMapping(path = "/getPdf")
    ResponseEntity<byte[]> getpdf(@RequestBody Map<String, Object> requestMap);

@PostMapping(path="/delete/{id}")
    ResponseEntity<String> deleteBill(@PathVariable Integer id);
}
