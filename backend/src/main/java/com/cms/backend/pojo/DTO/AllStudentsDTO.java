package com.cms.backend.pojo.DTO;

import com.cms.backend.pojo.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AllStudentsDTO {

    private List<User> users;

    public AllStudentsDTO(List<User> users) {
        this.users = users;
    }

}
