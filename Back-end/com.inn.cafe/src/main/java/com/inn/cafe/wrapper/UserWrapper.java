package com.inn.cafe.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
public class UserWrapper {

    private Integer id;

    private  String name;

    private String email;

    private String conatactNumber;

    private String status;


    public UserWrapper(Integer id, String name, String email, String conatactNumber, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.conatactNumber = conatactNumber;
        this.status = status;
    }
}
