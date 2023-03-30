package ru.antelit.fiskabinet.service.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import ru.antelit.fiskabinet.domain.UserInfo;

@Mapper
public interface UserDao {

    UserInfo getUserByUsername(@Param("username") String username);

    UserInfo findUserByLogin(String login);

//    Integer createUser(@Param("user")UserInfo userInfo);
//    void updateUserInfo(@Param("user") UserInfo userInfo);
}
