package com.cms.backend.pojo.DTO;

import lombok.Data;

@Data
public class UserDTO {

    private Integer id;

    private String name;

    private String password;

    private String userClass;

    private String academy;

    private String gender;

    private String email;

    private String phoneNumber;

    private String idCardNumber;

    public UserDTO(Integer id, String name, String password, String userClass, String academy, String gender, String email, String phoneNumber, String idCardNumber) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.userClass = userClass;
        this.academy = academy;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.idCardNumber = idCardNumber;
    }

    public UserDTO() {
    }

}
