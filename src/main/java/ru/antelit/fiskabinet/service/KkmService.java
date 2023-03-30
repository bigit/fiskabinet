package ru.antelit.fiskabinet.service;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.antelit.fiskabinet.domain.Kkm;
import ru.antelit.fiskabinet.domain.Model;
import ru.antelit.fiskabinet.domain.Tradepoint;
import ru.antelit.fiskabinet.domain.Vendor;
import ru.antelit.fiskabinet.service.dao.KkmDao;
import ru.antelit.fiskabinet.service.repository.KkmRepository;

import java.util.List;

@Service
public class KkmService {

//    @Autowired
//    private KkmDao kkmDao;

    @Autowired
    private KkmRepository repository;

    public Kkm save(Kkm kkm) {
        return repository.save(kkm);
    }

    public List<Kkm> listKkmByTradepoint(Tradepoint tradepoint) {
        return repository.getKkmByTradepoint(tradepoint);
    }

//    public List<Kkm> listKkmByTradepoint(Tradepoint tradepoint) {
//        return kkmDao.listKkmByTradepoint(tradepoint);
//    }
//    public List<Vendor> listVendors() {
//        return kkmDao.listVendors();
//    }
//    public List<Model> listModelsByVendor(@NonNull Vendor vendor) {
//        return kkmDao.listModelsByVendor(vendor);
//    }

}
