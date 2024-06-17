package ru.antelit.fiskabinet.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.antelit.fiskabinet.domain.Vendor;
import ru.antelit.fiskabinet.ofd.ActivationCode;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivationCodeRepository extends JpaRepository<ActivationCode, Long> {

    @Query("select ac from ActivationCode as ac where ac.status = 1 and ac.provider = :provider")
    List<ActivationCode> getFreeCodesByProvider(@Param("provider") Vendor.OfdProvider provider);

    @Query("select ac from ActivationCode as ac where ac.status = 0 and ac.provider.id = :provider")
    List<ActivationCode> getFreeCodesByProvider(@Param("provider") Integer providerId);

    @SuppressWarnings("unused")
    @Query(value = "select ac from ActivationCode as ac where ac.status = 1 and ac.provider = :provider")
    List<ActivationCode> getFreeCode(@Param("provider") Vendor.OfdProvider providerId);

    @Query("select ac from ActivationCode as ac where ac.provider.id = :provider")
    List<ActivationCode> getActivationCodeByProvider(@Param("provider") Integer providerId);

    Optional<ActivationCode> findActivationCodesByValue(String value);
}
