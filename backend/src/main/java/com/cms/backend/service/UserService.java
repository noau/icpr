package com.cms.backend.service;

import com.cms.backend.pojo.DTO.FollowDTO;
import com.cms.backend.pojo.DTO.SubscriptionDTO;
import com.cms.backend.pojo.Favorites;
import com.cms.backend.pojo.Folders;
import com.cms.backend.pojo.User;

import java.util.List;

public interface UserService {
    User findByUserName(Integer id);

    List<FollowDTO> getUserFollowers(Integer id);

    void changePassword(Integer id, String newPassword);

    List<SubscriptionDTO> getUserSubscriptions(Integer id);

    List<Folders> getUserFolders(Integer id);

    List<Favorites> getUserFavorites(Integer id);

    void changeInfo(Integer id, String name, String userClass, String academy, String gender);

    void addUser(Integer id, String name, String password, String userClass, String academy, String gender);
}
