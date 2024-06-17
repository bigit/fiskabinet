package ru.antelit.fiskabinet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.antelit.fiskabinet.domain.UserInfo;
import ru.antelit.fiskabinet.service.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserInfo createUser(UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userInfo.setEnabled(true);

        return userRepository.save(userInfo);
    }

    public boolean updatePassword(UserInfo userInfo, String password) {
        if (passwordEncoder.matches(password, userInfo.getPassword())) {
            return false;
        }
        userInfo.setPassword(passwordEncoder.encode(password));
        userRepository.save(userInfo);
        return true;
    }
}
