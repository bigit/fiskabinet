package ru.antelit.fiskabinet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.antelit.fiskabinet.domain.Vendor;
import ru.antelit.fiskabinet.service.repository.OfdRepository;

import java.util.List;

@Service
public class OfdService {

    @Autowired
    private OfdRepository repository;

    public Vendor.OfdProvider get(Integer id) {
        return repository.getReferenceById(id);
    }
    public List<Vendor.OfdProvider> list() {
        return repository.findAll();
    }
}
