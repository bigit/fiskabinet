package ru.antelit.fiskabinet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.antelit.fiskabinet.domain.ActivationCode;
import ru.antelit.fiskabinet.domain.OfdProvider;
import ru.antelit.fiskabinet.service.repository.ActivationCodeRepository;

import java.util.List;

@Service
public class ActivationCodeService {

    @Autowired
    private ActivationCodeRepository repository;

    public ActivationCode getById(Long id) {
        return repository.getReferenceById(id);
    }

    public ActivationCode getByValue(String value) {
        return repository.findActivationCodesByValue(value).orElse(null);
    }

    @SuppressWarnings("unused")
    public List<ActivationCode> getFreeCodesByProvider(OfdProvider provider) {
        return repository.getFreeCodesByProvider(provider);
    }

    @SuppressWarnings("unused")
    public List<ActivationCode> getFreeCodesByProvider(Integer providerId) {
        return repository.getFreeCodesByProvider(providerId);
    }

    public List<ActivationCode> getCodesByProvider(Integer providerId) {
        return repository.getActivationCodeByProvider(providerId);
    }

    @SuppressWarnings("unused")
    public ActivationCode getFreeCode(OfdProvider provider) {
        return repository.getFreeCodesByProvider(provider).get(0);
    }

    public void addActivationCode(ActivationCode activationCode) {
        repository.save(activationCode);
    }

    public ActivationCode save(ActivationCode code) {
        return repository.save(code);
    }
}
