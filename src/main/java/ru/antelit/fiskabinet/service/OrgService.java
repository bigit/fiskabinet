package ru.antelit.fiskabinet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.antelit.fiskabinet.domain.Organization;
import ru.antelit.fiskabinet.domain.UserInfo;
import ru.antelit.fiskabinet.service.dao.OrgDao;
import ru.antelit.fiskabinet.service.repository.OrganizationRepository;

import java.util.List;

@Service
public class OrgService {

    @Autowired
    private OrgDao orgDao;

    @Autowired
    private OrganizationRepository organizationRepository;

    public List<Organization> list() {
        return organizationRepository.findAll();
    }

    public Organization get(Integer id) {
        return organizationRepository.getReferenceById(id);
    }

    public Integer save(Organization organization) {
        Organization org = organizationRepository.save(organization);
        return org.getId();
    }

    public Organization findOrganizationBySourceId(String id) {
        return orgDao.findOrganizationBySourceId(id);
    }

    public Organization findByInn(String inn) {
        return organizationRepository.findByInn(inn);
    }

    public List<Organization> findByName(String query) {
        return organizationRepository.findByNameIgnoreCase("%"+query+"%");
    }

    public List<Organization> getUserOrganizations(UserInfo userInfo) {
        return orgDao.getUserOrganizations(userInfo);
    }

    public List<String> findImportedFromBitrixId(List<String> externalIds) {
        return organizationRepository.findImportedIds(externalIds);
    }

    public boolean checkInnExists(String inn) {
        return orgDao.checkInnExists(inn);
    }
}
