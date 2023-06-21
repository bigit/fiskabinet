package ru.antelit.fiskabinet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.antelit.fiskabinet.domain.Kkm;
import ru.antelit.fiskabinet.domain.Organization;
import ru.antelit.fiskabinet.domain.Tradepoint;
import ru.antelit.fiskabinet.domain.dto.KkmDto;
import ru.antelit.fiskabinet.service.repository.KkmRepository;

import java.util.List;

@Service
public class KkmService {

//    @Autowired
//    private KkmDao kkmDao;

    @Autowired
    private KkmRepository repository;

    @Autowired
    private ModelService modelService;
    @Autowired
    private TradepointService tradepointService;

    public Kkm save(Kkm kkm) {
        return repository.save(kkm);
    }

    public List<Kkm> getKkmByTradepoint(Tradepoint tradepoint) {
        return repository.getKkmByTradepoint(tradepoint);
    }

    public List<Kkm> getByOrganization(Organization organization) {
        return repository.findByTradepoint_Organization(organization);
    }

    public Kkm get(Integer id) {
        return repository.getReferenceById(id);
    }

    public Kkm fromDto(KkmDto dto) {
        var kkm = new Kkm();
        kkm.setId(dto.getId());
        kkm.setFnNumber(dto.getFnNumber());
        kkm.setFnEnd(dto.getFnEnd());
        kkm.setInnerName(dto.getInnerName());
        var model = modelService.get(dto.getModelId());
        kkm.setKkmModel(model);

        var tp = tradepointService.get(dto.getTradepointId());
        kkm.setTradepoint(tp);

        return kkm;
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
