package ru.antelit.fiskabinet.service.dao;

import liquibase.pro.packaged.P;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import ru.antelit.fiskabinet.domain.Organization;
import ru.antelit.fiskabinet.domain.Tradepoint;

import java.util.List;

@Mapper
public interface TradepointDao {

    List<Tradepoint> getTradepointByOrg(@Param("organizationId") Integer orgId);
    List<Tradepoint> listSiblings(@Param("tradepointId") Integer tpId);
    Tradepoint get(@Param("id") Integer id);
}
