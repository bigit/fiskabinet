package ru.antelit.fiskabinet.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.antelit.fiskabinet.domain.UserInfo;
import ru.antelit.fiskabinet.service.dao.UserDao;
import ru.antelit.fiskabinet.service.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRepository repository;
    @Autowired
    private RoleService roleService;

    public UserInfo findUser(String username) {
        return userDao.getUserByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        log.debug("Login: " + login);
        UserInfo userInfo = repository.findUserInfosByLogin(login);
        if (userInfo == null) {
            throw new UsernameNotFoundException("User not found");
        }


        List <GrantedAuthority> auth = new ArrayList<>();
        userInfo.getRoles().forEach(role -> auth.add(new SimpleGrantedAuthority(role.getName())));

        return new User(userInfo.getUsername(), userInfo.getPassword(), auth);

    }

    public UserInfo getUserByName(String username) {
        return repository.findUserInfosByLogin(username);
    }

    public UserInfo getCurrentUser() {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return repository.findUserInfosByLogin(user.getUsername());
    }
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
