package ru.antelit.fiskabinet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.antelit.fiskabinet.domain.Vendor;
import ru.antelit.fiskabinet.service.repository.VendorRepository;

import java.util.List;

@Service
public class VendorService {

    @Autowired
    VendorRepository vendorRepository;

    public List<Vendor> list() {
        return vendorRepository.findAll();
    }
}
