package com.cms.backend.service.impl;

import com.cms.backend.mapper.UserMapper;
import com.cms.backend.pojo.User;
import com.cms.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User findByUserName(Integer id) {
        return userMapper.findByUserName(id);
    }
}
