package com.cms.backend.mapper;

import com.cms.backend.pojo.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("select * from user where id = #{id}")
    User findByUserName(Integer id);

}
