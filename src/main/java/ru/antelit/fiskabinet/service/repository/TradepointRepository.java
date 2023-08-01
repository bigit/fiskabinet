package ru.antelit.fiskabinet.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.antelit.fiskabinet.domain.Organization;
import ru.antelit.fiskabinet.domain.Tradepoint;

@Repository
public interface TradepointRepository extends JpaRepository<Tradepoint, Integer> {

    Tradepoint findByOrganizationAndName(Organization organization, String name);
}
