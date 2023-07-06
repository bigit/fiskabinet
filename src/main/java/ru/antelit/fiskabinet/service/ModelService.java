package ru.antelit.fiskabinet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.antelit.fiskabinet.domain.KkmModel;
import ru.antelit.fiskabinet.service.repository.ModelRepository;

import java.util.List;

@Service
public class ModelService {

    @Autowired
    private ModelRepository modelRepository;

//    public List<KkmModel> getModelsByVendor(Vendor vendor) {
//        return modelRepository.getModelsByVendor(vendor);
//    }

    public List<KkmModel> getModelsByVendorId(Integer vendorId) {
        return modelRepository.getModelsByVendorId(vendorId);
    }

    public KkmModel get(Integer id) {
        return modelRepository.findById(id).get();
    }

    public List<KkmModel> list() {
        return modelRepository.findAll();
    }
}
