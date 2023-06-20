package ru.antelit.fiskabinet.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.antelit.fiskabinet.domain.UserInfo;
import ru.antelit.fiskabinet.security.Role;
import ru.antelit.fiskabinet.service.repository.RoleRepository;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

}
