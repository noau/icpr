package com.cms.backend.mapper;

import com.cms.backend.pojo.DTO.FollowDTO;
import com.cms.backend.pojo.DTO.SubscriptionDTO;
import com.cms.backend.pojo.Favorites;
import com.cms.backend.pojo.Folders;
import com.cms.backend.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Results({
            @Result(property = "userClass", column = "class"),
            @Result(property = "defaultFolderId", column = "default_folder_id"),
            @Result(property = "subscriptionsNumber", column = "subscriptions_number"),
            @Result(property = "fansNumber", column = "fans_number")
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
    @Select("select * from folders where user_id = #{id}")
    List<Folders> getUserFolders(Integer id);

    @Results({
            @Result(property = "userId", column = "user_id"),
            @Result(property = "threadId", column = "thread_id"),
            @Result(property = "folderId", column = "folder_id"),
            @Result(property = "createdAt", column = "created_at")
    })
    @Select("select * from favorites where folder_id = #{id}")
    List<Favorites> getUserFavorites(Integer id);

    @Results({
            @Result(property = "userClass", column = "class"),
    })
    @Update("update user set name = #{name}, class = #{userClass}, academy = #{academy}, gender = #{gender} where id = #{id}")
    void changeInfo(Integer id, String name, String userClass, String academy, String gender);
}
