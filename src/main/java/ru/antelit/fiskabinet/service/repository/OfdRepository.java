package ru.antelit.fiskabinet.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.antelit.fiskabinet.domain.Vendor;

@Repository
public interface OfdRepository extends JpaRepository<Vendor.OfdProvider, Integer> {
}
