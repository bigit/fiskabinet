package ru.antelit.fiskabinet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import ru.antelit.fiskabinet.domain.Vendor;
import ru.antelit.fiskabinet.service.repository.VendorRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VendorService {

    @Autowired
    VendorRepository vendorRepository;

    public List<Vendor> list() {
        return vendorRepository.findAll();
    }

    public Map<Integer, String> getVendorsNames() {
        return vendorRepository.findAll().stream()
                .collect(Collectors.toMap(Vendor::getId, Vendor::getName));
    }
}
