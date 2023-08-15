package ru.antelit.fiskabinet.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.antelit.fiskabinet.domain.Kkm;
import ru.antelit.fiskabinet.domain.Organization;
import ru.antelit.fiskabinet.domain.Tradepoint;

import java.util.List;
import java.util.Optional;

@Repository
public interface KkmRepository extends JpaRepository<Kkm, Integer> {

   List<Kkm> getKkmByTradepoint(Tradepoint tradepoint);

   List<Kkm> findByOrganization(Organization organization);

   List<Kkm> findByTradepoint_Organization(Organization organization);

   Optional<Kkm> getKkmBySerialNumber(String serialNumber);

   Kkm getKkmByFnNumber(String fnNumber);
}
