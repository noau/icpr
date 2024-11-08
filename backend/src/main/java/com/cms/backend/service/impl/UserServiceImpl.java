package com.cms.backend.service.impl;

import com.cms.backend.controller.UserController;
import com.cms.backend.mapper.UserMapper;
import com.cms.backend.pojo.DTO.FollowDTO;
import com.cms.backend.pojo.DTO.SubscriptionDTO;
import com.cms.backend.pojo.Folder;
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
    public List<Folder> getUserFolders(Integer id) {
        return userMapper.getUserFolders(id);
    }

    @Override
    public List<UserController.FavoriteGet> getUserFavorites(Integer id) {
        return userMapper.getUserFavorites(id);
    }

    @Override
    public void changeInfo(Integer id, String email, String phoneNumber) {
        userMapper.changeInfo(id, email, phoneNumber);
    }

    @Override
    public void addUser(Integer id, String name, String password, String userClass, String academy, String gender, String email, String phoneNumber, String idCardNumber) {
        userMapper.addUser(id, name, password, userClass, academy, gender, email, phoneNumber, idCardNumber);
    }

    @Override
    public void addFolder(Integer userId, String name, String createdAt, Integer isDefault, Integer isPrivate) {
        userMapper.addFolder(userId, name, createdAt, isDefault, isPrivate);
    }

    @Override
    public void addFavorite(Integer userId, Integer threadId, Integer folderId, String createdAt) {
        userMapper.addFavorite(userId, threadId, folderId, createdAt);
    }

    @Override
    public void deleteFolder(Integer id) {
        userMapper.deleteFolder(id);
    }

    @Override
    public void changeFolder(Integer id, String name, Integer isPrivate) {
        userMapper.changeFolder(id, name, isPrivate);
    }

    @Override
    public void deleteFavorite(Integer id) {
        userMapper.deleteFavorite(id);
    }

    @Override
    public void changeFavorite(Integer id, Integer favoriteId) {
        userMapper.changeFavorite(id, favoriteId);
    }

    @Override
    public void uploadAvatar(Integer id, String avatar) {
        userMapper.uploadAvatar(id, avatar);
    }

    @Override
    public void makeSubscription(Integer followingId, Integer subscriptionId, String followingName, String subscriptionName) {
        userMapper.makeSubscription(followingId, subscriptionId, followingName, subscriptionName);
    }

    @Override
    public void addFollower(Integer followingId) {
        userMapper.addFollower(followingId);
    }

    @Override
    public void addSubscriber(Integer subscriptionId) {
        userMapper.addSubscriber(subscriptionId);
    }

    @Override
    public void deleteSubscription(Integer followingId, Integer subscriptionId) {
        userMapper.deleteSubscription(followingId, subscriptionId);
    }

    @Override
    public void deleteFollower(Integer followingId) {
        userMapper.deleteFollower(followingId);
    }

    @Override
    public void deleteSubscriber(Integer subscriptionId) {
        userMapper.deleteSubscriber(subscriptionId);
    }

    @Override
    public void like(Integer userId, Integer threadId, String courseId, Integer replyId, String createdAt) {
        userMapper.like(userId, threadId, courseId, replyId, createdAt);
    }

    @Override
    public void addLikeThread(Integer threadId) {
        userMapper.addLikeThread(threadId);
    }

    @Override
    public void addLikeReply(Integer replyId) {
        userMapper.addLikeReply(replyId);
    }

    @Override
    public void addFavorites(Integer threadId) {
        userMapper.addFavorites(threadId);
    }

    @Override
    public void deleteLikeThread(Integer id) {
        userMapper.deleteLikeThread(id);
    }

    @Override
    public void deleteLikeReply(Integer id) {
        userMapper.deleteLikeReply(id);
    }

    @Override
    public void addUserTeacher(Integer id, String address, String title, String brief) {
        userMapper.addUserTeacher(id, address, title, brief);
    }

    @Override
    public void addThreadNumber(Integer id) {
        userMapper.addThreadNumber(id);
    }

    @Override
    public void deleteThreadNumber(Integer id) {
        userMapper.deleteThreadNumber(id);
    }

}
