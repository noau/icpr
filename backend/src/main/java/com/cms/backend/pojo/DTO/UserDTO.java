package com.cms.backend.pojo.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Integer id;

    private String name;

    private String password;

    private String userClass;

    private String gender;

    private String academy;

    public UserDTO(Integer id, String name, String password, String userClass, String gender, String academy) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.userClass = userClass;
        this.gender = gender;
        this.academy = academy;
    }

}
