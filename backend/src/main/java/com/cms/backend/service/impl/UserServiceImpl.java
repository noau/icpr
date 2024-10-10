package com.cms.backend.service.impl;

import com.cms.backend.mapper.UserMapper;
import com.cms.backend.pojo.DTO.FollowDTO;
import com.cms.backend.pojo.DTO.SubscriptionDTO;
import com.cms.backend.pojo.Favorites;
import com.cms.backend.pojo.Folders;
import com.cms.backend.pojo.User;
import com.cms.backend.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<FollowDTO> getUserFollowers(Integer id) {
        return userMapper.getUserFollowers(id);
    }

    @Override
    public void changePassword(Integer id, String newPassword) {
        userMapper.changePassword(id, newPassword);
    }

    @Override
    public List<SubscriptionDTO> getUserSubscriptions(Integer id) {
        return userMapper.getUserSubscriptions(id);
    }

    @Override
    public List<Folders> getUserFolders(Integer id) {
        return userMapper.getUserFolders(id);
    }

    @Override
    public List<Favorites> getUserFavorites(Integer id) {
        return userMapper.getUserFavorites(id);
    }

    @Override
    public void changeInfo(Integer id, String name, String userClass, String academy, String gender) {
        userMapper.changeInfo(id, name, userClass, academy, gender);
    }

    @Override
    public void addUser(Integer id, String name, String password, String userClass, String academy, String gender) {
        userMapper.addUser(id, name, password, userClass, academy, gender);
    }


}
