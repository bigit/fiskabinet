package ru.antelit.fiskabinet.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.antelit.fiskabinet.domain.UserInfo;
import ru.antelit.fiskabinet.service.dao.UserDao;
import ru.antelit.fiskabinet.service.repository.UserRepository;

import java.util.Collections;
import java.util.List;

@Log4j2
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRepository repository;

    public UserInfo findUser(String username) {

        return userDao.getUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        log.debug("Login: " + login);
//        UserInfo user = userDao.findUserByLogin(login);
        UserInfo user = repository.findUserInfosByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return getDetails(user);
    }


    private static UserDetails getDetails(UserInfo userInfo) {
        List<GrantedAuthority> list = Collections.singletonList(new SimpleGrantedAuthority("READ"));
        return new User(userInfo.getUsername(), userInfo.getPassword(), list );
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
