package ru.antelit.fiskabinet.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.antelit.fiskabinet.domain.Kkm;
import ru.antelit.fiskabinet.domain.Organization;
import ru.antelit.fiskabinet.domain.Tradepoint;

import java.util.List;

@Repository
public interface KkmRepository extends JpaRepository<Kkm, Integer> {

   List<Kkm> getKkmByTradepoint(Tradepoint tradepoint);

   @Query("select kkm from kkm where tradepoint.id in (" +
           "select id from tradepoint where tradepoint.organization.id = #{organization.id})")
   List<Kkm> getByOrganization(Organization organization);
}
