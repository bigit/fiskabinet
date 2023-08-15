package ru.antelit.fiskabinet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.antelit.fiskabinet.domain.OfdProvider;
import ru.antelit.fiskabinet.service.repository.OfdRepository;

import java.util.List;

@Service
public class OfdService {

    @Autowired
    private OfdRepository repository;

    public OfdProvider get(Integer id) {
        return repository.getReferenceById(id);
    }
    public List<OfdProvider> list() {
        return repository.findAll();
    }
}
