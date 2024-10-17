package com.cms.backend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Teaching {

    private Integer userId;

    private String address;

    private String professionalTitle;

    private String content;

}
