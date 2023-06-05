package ru.antelit.fiskabinet.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.antelit.fiskabinet.domain.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
}
