package ru.antelit.fiskabinet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.antelit.fiskabinet.domain.Organization;
import ru.antelit.fiskabinet.domain.Tradepoint;
import ru.antelit.fiskabinet.service.dao.TradepointDao;

import java.util.List;

@Service
public class TradepointService {

    @Autowired
    private TradepointDao tradepointDao;

    public List<Tradepoint> listTradepointsByOrganization(Organization organization) {
        return tradepointDao.getTradepointByOrg(organization);
    }
//    public List<Tradepoint> listTradepointsByOrganization(Organization organization) {
//        return tradepointDao.getTradepointByOrg(organization.getId());
//    }

}
