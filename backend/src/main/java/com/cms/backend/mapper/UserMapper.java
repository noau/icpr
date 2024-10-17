package com.cms.backend.mapper;

import com.cms.backend.pojo.DTO.FollowDTO;
import com.cms.backend.pojo.DTO.SubscriptionDTO;
import com.cms.backend.pojo.Favorite;
import com.cms.backend.pojo.Folder;
import com.cms.backend.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Results({
            @Result(property = "userClass", column = "class"),
            @Result(property = "subscriptionsNumber", column = "subscriptions_number"),
            @Result(property = "fansNumber", column = "fans_number"),
            @Result(property = "threadNumber", column = "thread_number"),
            @Result(property = "phoneNumber", column = "phone_number"),
            @Result(property = "idCardNumber", column = "id_card_number")
    })
    @Select("select * from user where id = #{id}")
    User findByUserName(Integer id);

    @Results({
            @Result(property = "subscriptionId", column = "subscription_id"),
            @Result(property = "followingId", column = "following_id"),
            @Result(property = "followingName", column = "following_name")
    })
    @Select("select following_id, following_name from follow where subscription_id = #{id}")
    List<FollowDTO> getUserFollowers(Integer id);

    @Update("update user set password = #{newPassword} where id = #{id}")
    void changePassword(Integer id, String newPassword);

    @Results({
            @Result(property = "subscriptionId", column = "subscription_id"),
            @Result(property = "subscriptionName", column = "subscription_name"),
            @Result(property = "followingId", column = "following_id")
    })
    @Select("select subscription_id, subscription_name from follow where following_id = #{id}")
    List<SubscriptionDTO> getUserSubscriptions(Integer id);

    @Results({
            @Result(property = "userId", column = "user_id"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at"),
            @Result(property = "isDefault", column = "is_default")
    })
    @Select("select * from folder where user_id = #{id}")
    List<Folder> getUserFolders(Integer id);

    @Results({
            @Result(property = "userId", column = "user_id"),
            @Result(property = "threadId", column = "thread_id"),
            @Result(property = "folderId", column = "folder_id"),
            @Result(property = "createdAt", column = "created_at")
    })
    @Select("select * from favorite where folder_id = #{id}")
    List<Favorite> getUserFavorites(Integer id);

    @Results({
            @Result(property = "userClass", column = "class"),
    })
    @Update("update user set name = #{name}, class = #{userClass}, academy = #{academy}, gender = #{gender} where id = #{id}")
    void changeInfo(Integer id, String name, String userClass, String academy, String gender);

    @Results({
            @Result(property = "userClass", column = "class"),
            @Result(property = "subscriptionsNumber", column = "subscriptions_number"),
            @Result(property = "fansNumber", column = "fans_number")
    })
    @Insert("insert into user (id, name, password, class, academy, gender, avatar) values (#{id}, #{name}, #{password}, #{userClass}, #{academy}, #{gender}, 'default_avatar.png')")
    void addUser(Integer id, String name, String password, String userClass, String academy, String gender);

    @Results({
            @Result(property = "userId", column = "user_id"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at"),
            @Result(property = "isDefault", column = "is_default")
    })
    @Insert("insert into folder (user_id, name, created_at, is_default) values (#{userId}, #{name}, #{createdAt}, #{isDefault})")
    void addFolder(Integer userId, String name, String createdAt, Integer isDefault);

    @Insert("insert into favorite (user_id, thread_id, folder_id, created_at) values (#{userId}, #{threadId}, #{folderid}, #{createdAt})")
    void addFavorite(Integer userId, Integer threadId, Integer folderId, String createdAt);

    @Delete("delete from folder where id = #{id}")
    void deleteFolder(Integer id);

    @Update("update folder set name = #{name}, is_private = #{isPrivate} where id = #{id}")
    void changeFolder(Integer id, String name, Integer isPrivate);

    @Delete("delete from favorite where id = #{id}")
    void deleteFavorite(Integer id);

    @Results({
            @Result(property = "id", column = "folder_id"),
            @Result(property = "favoriteId", column = "id")
    })
    @Update("update favorite set folder_id = #{id} where id = #{favoriteId}")
    void changeFavorite(Integer id, Integer favoriteId);

    @Update("update user set avatar = #{avatar} where id = #{id}")
    void uploadAvatar(Integer id, String avatar);

    @Results({
            @Result(property = "subscriptionId", column = "subscription_id"),
            @Result(property = "followingId", column = "following_id"),
            @Result(property = "followingName", column = "following_name"),
            @Result(property = "subscriptionName", column = "subscription_name")
    })
    @Insert("insert into follow values (#{subscriptionId}, #{followingId}, #{followingName}, #{subscriptionName})")
    void makeSubscription(Integer followingId, Integer subscriptionId, String followingName, String subscriptionName);

    @Results({
            @Result(property = "followingId", column = "id")
    })
    @Update("update user set subscriptions_number = user.subscriptions_number + 1 where id = #{followingId}")
    void addFollower(Integer followingId);

    @Results({
            @Result(property = "subscriptionId", column = "id")
    })
    @Update("update user set fans_number = user.fans_number + 1 where id = #{subscriptionId}")
    void addSubscriber(Integer subscriptionId);

    @Results({
            @Result(property = "subscriptionId", column = "subscription_id"),
            @Result(property = "followingId", column = "following_id")
    })
    @Insert("delete from follow where subscription_id = #{subscriptionId} and following_id = #{followingId}")
    void deleteSubscription(Integer followingId, Integer subscriptionId);

    @Results({
            @Result(property = "followingId", column = "id")
    })
    @Update("update user set subscriptions_number = user.subscriptions_number - 1 where id = #{followingId}")
    void deleteFollower(Integer followingId);

    @Results({
            @Result(property = "subscriptionId", column = "id")
    })
    @Update("update user set fans_number = user.fans_number - 1 where id = #{subscriptionId}")
    void deleteSubscriber(Integer subscriptionId);

}
