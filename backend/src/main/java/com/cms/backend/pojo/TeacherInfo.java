package com.cms.backend.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeacherInfo {

    @TableId
    private Integer teacherId;

    private String address;

    private String title;

    private String brief;

}
