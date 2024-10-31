package com.cms.backend.pojo.DTO;

import lombok.Data;

@Data
public class UserTeacherDTO {

    private Integer id;

    private String name;

    private String password;

    private String userClass;

    private String academy;

    private String gender;

    private String email;

    private String phoneNumber;

    private String idCardNumber;

    private String address;

    private String title;

    private String brief;

    public UserTeacherDTO(Integer id, String name, String password, String userClass, String academy, String gender, String email, String phoneNumber, String idCardNumber, String address, String title, String brief) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.userClass = userClass;
        this.academy = academy;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.idCardNumber = idCardNumber;
        this.address = address;
        this.title = title;
        this.brief = brief;
    }

    public UserTeacherDTO() {

    }

}
