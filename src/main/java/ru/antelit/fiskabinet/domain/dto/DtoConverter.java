package ru.antelit.fiskabinet.domain.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.antelit.fiskabinet.domain.ActivationCode;
import ru.antelit.fiskabinet.domain.CodeStatus;
import ru.antelit.fiskabinet.domain.OfdProvider;
import ru.antelit.fiskabinet.service.OfdService;

@Component
public class DtoConverter {
    
    @Autowired
    private OfdService ofdService;

    public ActivationCode convert(ActivationCodeDto dto) {
        ActivationCode code = new ActivationCode();
        if (dto.getId() != null) {
            code.setId(dto.getId());
        }
        if (dto.getValue() != null) {
            code.setValue(dto.getValue());
        }
        code.setDuration(dto.getDuration());

        OfdProvider provider = ofdService.get(dto.getProviderId());
        code.setProvider(provider);

        if (dto.getStatus() != null) {
            code.setStatus(CodeStatus.getById(dto.getStatus()));
        } else {
            code.setStatus(CodeStatus.NEW);
        }
        return code;
    }
}
