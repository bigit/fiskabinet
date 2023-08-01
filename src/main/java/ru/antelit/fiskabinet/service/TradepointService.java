package ru.antelit.fiskabinet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.antelit.fiskabinet.domain.Organization;
import ru.antelit.fiskabinet.domain.Tradepoint;
import ru.antelit.fiskabinet.service.dao.TradepointDao;
import ru.antelit.fiskabinet.service.repository.TradepointRepository;

import java.util.List;

@Service
public class TradepointService {

    @Autowired
    private TradepointDao tradepointDao;

    @Autowired
    private TradepointRepository tradepointRepository;

    public List<Tradepoint> listTradepointsByOrganization(Organization organization) {
        return listTradepointsByOrganization(organization.getId());
    }

    public List<Tradepoint> listTradepointsByOrganization(Integer organizationId) {
        return tradepointDao.getTradepointByOrg(organizationId);
    }

    public Tradepoint findTradepointByName(Organization organization, String name) {
        return tradepointRepository.findByOrganizationAndName(organization, name);
    }

    public List<Tradepoint> listSiblings(Tradepoint tradepoint) {
        return listSiblings(tradepoint.getId());
    }

    public List<Tradepoint> listSiblings(Integer tradepointId) {
        return tradepointDao.listSiblings(tradepointId);
    }

    public Tradepoint get(Integer id) {
        return tradepointDao.get(id);
    }

    public Integer save(Tradepoint tradepoint) {
        return tradepointRepository.save(tradepoint).getId();
    }

    public Tradepoint createDefaultTradepoint(Organization organization) {
        Tradepoint tradepoint = new Tradepoint();
        tradepoint.setOrganization(organization);
        tradepoint.setName("Без торговой точки");
        return tradepoint;
    }

    public Tradepoint getDefaultTradepoint(Organization organization) {
        return tradepointRepository.findByOrganizationAndName(organization, "Без торговой точки");
    }
}
