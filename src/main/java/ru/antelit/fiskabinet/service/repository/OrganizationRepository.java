package ru.antelit.fiskabinet.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.antelit.fiskabinet.domain.Organization;

import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {

    Organization findByInn(@NonNull String inn);

    @Query("select o from organization o where upper(o.name) like upper(?1)")
    List<Organization> findByNameIgnoreCase(String name);
}
