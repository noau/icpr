package com.cms.backend.service;

import com.cms.backend.pojo.DTO.FollowDTO;
import com.cms.backend.pojo.DTO.SubscriptionDTO;
import com.cms.backend.pojo.Favorite;
import com.cms.backend.pojo.Folder;
import com.cms.backend.pojo.User;

import java.util.List;

public interface UserService {
    User findByUserName(Integer id);

    List<FollowDTO> getUserFollowers(Integer id);

    void changePassword(Integer id, String newPassword);

    List<SubscriptionDTO> getUserSubscriptions(Integer id);

    List<Folder> getUserFolders(Integer id);

    List<Favorite> getUserFavorites(Integer id);

    void changeInfo(Integer id, String name, String userClass, String academy, String gender);

    void addUser(Integer id, String name, String password, String userClass, String academy, String gender);

    void addFolder(Integer userId, String name, String createdAt, Integer isDefault);

    void addFavorite(Integer userId, Integer threadId, Integer folderId, String createdAt);

    void deleteFolder(Integer id);

    void changeFolder(Integer id, String name, Integer isPrivate);

    void deleteFavorite(Integer id);

    void changeFavorite(Integer id, Integer favoriteId);

    void uploadAvatar(Integer id, String avatar);

    void makeSubscription(Integer followingId, Integer subscriptionId, String followingName, String subscriptionName);

    void addFollower(Integer followingId);

    void addSubscriber(Integer subscriptionId);

    void deleteSubscription(Integer followingId, Integer subscriptionId);

    void deleteFollower(Integer followingId);

    void deleteSubscriber(Integer subscriptionId);

    void like(Integer userId, Integer threadId, String courseId, Integer replyId, String createdAt);

    void addLikeThread(Integer ThreadId);

    void addLikeReply(Integer replyId);

    void addFavorites(Integer threadId);

    void deleteLikeThread(Integer id);

    void deleteLikeReply(Integer id);

}
