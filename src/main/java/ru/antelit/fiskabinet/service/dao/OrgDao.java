package ru.antelit.fiskabinet.service.dao;

import liquibase.pro.packaged.P;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import ru.antelit.fiskabinet.domain.Activity;
import ru.antelit.fiskabinet.domain.Organization;
import ru.antelit.fiskabinet.domain.UserInfo;

import java.util.List;

@Mapper
public interface OrgDao {

    List<Organization> list();
    List<Organization> getUserOrganizations(@Param("user") UserInfo user);

    Integer add(@Param("org") Organization organization);
    void update(@Param("org") Organization organization);

    List<Activity> getOrgActivities(@Param("org") Organization organization);
    void addOrgActivity(@Param("org") Organization organization, @Param("activity") Activity activity);
    void addOrgActivities(@Param("org") Organization organization, @Param("activities") List<Activity> activities);
    void removeOrgActivity(@Param("org") Organization organization, @Param("activity") Activity activity);
    void removeOrgActivities(@Param("org") Organization organization, @Param("activities") List<Activity> activities);

    boolean checkInnExists(@Param("inn") String inn);
}
