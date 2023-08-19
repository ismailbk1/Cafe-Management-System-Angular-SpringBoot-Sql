package com.inn.cafe.utils;

import com.google.common.base.Strings;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Cafeutils {

    private Cafeutils(){

    }

    public static ResponseEntity<String > getResonseEntity(String responsemsg ,HttpStatus httpstatus){
        return new ResponseEntity<String>("{\"message\":\""+responsemsg+"\"}", httpstatus);

    }
    public  static  String getUUID(){
        Date date=new Date();
        long time=date.getTime();
        return "BILL-" + time;
    }
    public static JSONArray getJsonArrayFromString(String data) throws JSONException {
       JSONArray jsonArray=new JSONArray(data);

       return jsonArray;

    }

    public static Map<String,Object> getMapFromJson(String data){

        if(!Strings.isNullOrEmpty(data)){
            return new Gson().fromJson(data,new TypeToken<Map<String , Object>>(){

            }.getType());



        }
        return new HashMap<>();
    }


    public  static  Boolean isFileExist(String path){
        try {
            File file=new File(path);

            return (file!=null && file.exists()) ? Boolean.TRUE :Boolean.FALSE;


        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
}
