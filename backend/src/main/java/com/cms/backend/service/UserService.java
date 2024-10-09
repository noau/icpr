package com.cms.backend.service;

import com.cms.backend.pojo.User;

public interface UserService {
    User findByUserName(Integer id);
}
