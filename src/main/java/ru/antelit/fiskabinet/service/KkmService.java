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

    @Autowired
    private KkmRepository repository;
    @Autowired
    private ModelService modelService;
    @Autowired
    private OrgService orgService;
    @Autowired
    private TradepointService tradepointService;
    @Autowired
    private OfdService ofdService;

    public Kkm save(Kkm kkm) {
        return repository.save(kkm);
    }

    public List<Kkm> getKkmByTradepoint(Tradepoint tradepoint) {
        return repository.getKkmByTradepoint(tradepoint);
    }

    public List<Kkm> getByOrganization(Organization organization) {
        return repository.findByOrganization(organization);
    }

    public Kkm getBySerialNumber(String serialNumber) {
        return repository.getKkmBySerialNumber(serialNumber).orElse(null);
    }

    public Kkm getByFnNumber(String fnNumber) {
        return repository.getKkmByFnNumber(fnNumber);
    }

    public Kkm get(Integer id) {
        return repository.getReferenceById(id);
    }

    public Kkm fromDto(KkmDto dto) {
        var kkm = new Kkm();
        kkm.setId(dto.getId());
        if (!dto.getFnNumber().isBlank()) {
            kkm.setFnNumber(dto.getFnNumber());
        }
        kkm.setFnEnd(dto.getFnEnd());
        kkm.setInnerName(dto.getInnerName());
        kkm.setSerialNumber(dto.getSerialNumber());
        var model = modelService.get(dto.getModelId());
        kkm.setKkmModel(model);
        kkm.setOfdSubEnd(dto.getOfdEnd());

        if (dto.getOfdId() != 0) {
            var ofd = ofdService.get(dto.getOfdId());
            kkm.setOfdProvider(ofd);
        }

        var org = orgService.get(dto.getOrgId());
        kkm.setOrganization(org);

        var tp = tradepointService.get(dto.getTradepointId());
        kkm.setTradepoint(tp);

        return kkm;
    }
}
