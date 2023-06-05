package ru.antelit.fiskabinet.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.antelit.fiskabinet.domain.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

}
