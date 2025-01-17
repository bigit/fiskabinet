package ru.antelit.fiskabinet.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.antelit.fiskabinet.domain.UserInfo;

public interface UserRepository extends JpaRepository<UserInfo, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM security.user where username = :login or email = :login or phonenumber = :login")
    UserInfo findUserInfosByLogin(@Param("login") String login);
}
