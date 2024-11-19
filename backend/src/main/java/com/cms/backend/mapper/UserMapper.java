package com.cms.backend.mapper;

import com.cms.backend.controller.UserController;
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
    User findById(Integer id);

    @Results({
            @Result(property = "subscriptionId", column = "subscription_id"),
            @Result(property = "followingId", column = "following_id"),
            @Result(property = "followingName", column = "following_name"),
            @Result(property = "userClass", column = "class")
    })
    @Select("select following_id, following_name, u.class, u.academy, u.avatar from follow join icpr.user u on u.id = follow.following_id where subscription_id = #{id}")
    List<FollowDTO> getUserFollowers(Integer id);

    @Update("update user set password = #{newPassword} where id = #{id}")
    void changePassword(Integer id, String newPassword);

    @Results({
            @Result(property = "subscriptionId", column = "subscription_id"),
            @Result(property = "subscriptionName", column = "subscription_name"),
            @Result(property = "followingId", column = "following_id"),
            @Result(property = "userClass", column = "class")
    })
    @Select("select subscription_id, subscription_name, u.class, u.academy, u.avatar from follow join icpr.user u on u.id = follow.subscription_id where following_id = #{id}")
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
    @Select("select *, dt.title from favorite join icpr.discussion_thread dt on dt.id = favorite.thread_id where folder_id = #{id}")
    List<UserController.FavoriteGet> getUserFavorites(Integer id);

    @Results({
            @Result(property = "phoneNumber", column = "phone_number"),
    })
    @Update("update user set email = #{email}, phone_number = #{phoneNumber} where id = #{id}")
    void changeInfo(Integer id, String email, String phoneNumber);

    @Results({
            @Result(property = "userClass", column = "class"),
            @Result(property = "phoneNumber", column = "phone_number"),
            @Result(property = "idCardNumber", column = "id_card_number")
    })
    @Insert("insert into user (id, name, password, class, academy, gender, avatar, email, phone_number, id_card_number) values (#{id}, #{name}, #{password}, #{userClass}, #{academy}, #{gender}, 'http://localhost:65/maomao.png', #{email}, #{phoneNumber}, #{idCardNumber})")
    void addUser(Integer id, String name, String password, String userClass, String academy, String gender, String email, String phoneNumber, String idCardNumber);

    @Results({
            @Result(property = "userId", column = "user_id"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at"),
            @Result(property = "isDefault", column = "is_default"),
            @Result(property = "isPrivate", column = "is_private")
    })
    @Insert("insert into folder (user_id, name, created_at, is_default, is_private) values (#{userId}, #{name}, #{createdAt}, #{isDefault}, #{isPrivate})")
    void addFolder(Integer userId, String name, String createdAt, Integer isDefault, Integer isPrivate);

    @Results({
            @Result(property = "userId", column = "user_id"),
            @Result(property = "threadId", column = "thread_id"),
            @Result(property = "folderId", column = "folder_id"),
            @Result(property = "createdAt", column = "created_at")
    })
    @Insert("insert into favorite (user_id, thread_id, folder_id, created_at) values (#{userId}, #{threadId}, #{folderId}, #{createdAt})")
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

    @Results({
            @Result(property = "userId", column = "user_id"),
            @Result(property = "threadId", column = "thread_id"),
            @Result(property = "courseId", column = "course_id"),
            @Result(property = "replyId", column = "reply_id"),
            @Result(property = "createdAt", column = "created_at")
    })
    @Insert("insert into discussion_like (user_id, thread_id, course_id, reply_id, created_at) values (#{userId}, #{threadId}, #{courseId}, #{replyId}, #{createdAt})")
    void like(Integer userId, Integer threadId, String courseId, Integer replyId, String createdAt);

    @Results({
            @Result(property = "threadId", column = "id")
    })
    @Update("update discussion_thread set likes = likes + 1 where id = #{threadId}")
    void addLikeThread(Integer threadId);

    @Results({
            @Result(property = "replyId", column = "id")
    })
    @Update("update discussion_reply set likes = likes + 1 where id = #{replyId}")
    void addLikeReply(Integer replyId);

    @Results({
            @Result(property = "threadId", column = "id")
    })
    @Update("update discussion_thread set favorites = favorites + 1 where id = #{threadId}")
    void addFavorites(Integer threadId);

    @Update("update discussion_thread set likes = likes - 1 where id = #{id}")
    void deleteLikeThread(Integer id);

    @Update("update discussion_reply set likes = likes - 1 where id = #{id}")
    void deleteLikeReply(Integer id);

    @Insert("insert into teacher_info values (#{id}, #{address}, #{title}, #{brief})")
    void addUserTeacher(Integer id, String address, String title, String brief);

    @Update("update user set thread_number = thread_number + 1 where id = #{id}")
    void addThreadNumber(Integer id);

    @Update("update user set thread_number = thread_number - 1 where id = #{id}")
    void deleteThreadNumber(Integer id);

    @Results({
            @Result(property = "id", column = "thread_id"),
            @Result(property = "userId", column = "user_id")
    })
    @Delete("delete from discussion_like where thread_id = #{id} and user_id = #{userId}")
    void deleteLikeThreadInfo(Integer id, Integer userId);

    @Results({
            @Result(property = "id", column = "reply_id"),
            @Result(property = "userId", column = "user_id")
    })
    @Delete("delete from discussion_like where reply_id = #{id} and user_id = #{userId}")
    void deleteLikeReplyInfo(Integer id, Integer userId);

    @Insert("insert into discussion_collect (user_id, thread_id, course_id, created_at) values (#{userId}, #{threadId}, #{courseId}, #{createdAt})")
    void addThreadCollect(Integer userId, Integer threadId, String courseId, String createdAt);

    @Select("select * from favorite where id = #{id}")
    Favorite getFavorite(Integer id);

    @Results({
            @Result(property = "id", column = "thread_id"),
            @Result(property = "userId", column = "user_id")
    })
    @Delete("delete from discussion_collect where thread_id = #{threadId} and user_id = #{userId}")
    void deleteDiscussionCollect(Integer threadId, Integer userId);

    @Update("update user set mark = mark + #{mark} where id = #{id}")
    void addMark(Integer id, int mark);

    @Update("update user set mark = mark - #{mark} where id = #{id}")
    void dropMark(Integer id, int mark);

}
