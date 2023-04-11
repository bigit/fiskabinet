package ru.antelit.fiskabinet.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.antelit.fiskabinet.domain.KkmModel;
import ru.antelit.fiskabinet.domain.Vendor;

import java.util.List;

public interface ModelRepository extends JpaRepository<KkmModel, Integer> {

    List<KkmModel> getModelsByVendor(Vendor vendor);

    List<KkmModel> getModelsByVendorId(Integer vendorId);
}
