package ru.antelit.fiskabinet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.antelit.fiskabinet.api.Bitrix24;
import ru.antelit.fiskabinet.domain.UserInfo;
import ru.antelit.fiskabinet.service.dao.UserDao;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public UserInfo findUser(String username) {
        return userDao.findUser(username);
    }

    public UserDetails getUserDetails(String username) {
        UserInfo userInfo = findUser(username);
        if (userInfo == null) {
            return null;
        }
        UserDetails details = (UserDetails) userInfo;
        return details;
    }

//    public Integer saveUser(UserInfo userInfo) {
//           if (userInfo.getId() == null) {
//               return createUser(userInfo);
//           } else {
//               return updateUserInfo(userInfo);
//           }
//    }
//
//    private Integer createUser(UserInfo userInfo) {
//        return userDao.createUser(userInfo);
//    }

    /**
     * Обновляет информацию о пользователе (ФИО, телефон, email)
     * @param userInfo -
     * @return id пользователя
     */
//    private Integer updateUserInfo(UserInfo userInfo) {
//        userDao.updateUserInfo(userInfo);
//        return userInfo.getId();
//    }
}
