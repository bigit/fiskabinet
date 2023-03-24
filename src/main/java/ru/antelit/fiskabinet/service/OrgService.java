package ru.antelit.fiskabinet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.antelit.fiskabinet.domain.Organization;
import ru.antelit.fiskabinet.domain.UserInfo;
import ru.antelit.fiskabinet.service.dao.OrgDao;

import java.util.List;

@Service
public class OrgService {

    @Autowired
    private OrgDao orgDao;

    public List<Organization> list() {
        return orgDao.list();
    }

    public List<Organization> getUserOrganizations(UserInfo userInfo) {
        return orgDao.getUserOrganizations(userInfo);
    }
}
