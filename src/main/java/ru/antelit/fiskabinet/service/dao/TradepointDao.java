package ru.antelit.fiskabinet.service.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import ru.antelit.fiskabinet.domain.Organization;
import ru.antelit.fiskabinet.domain.Tradepoint;

import java.util.List;

@Mapper
public interface TradepointDao {

    List<Tradepoint> getTradepointByOrg(@Param("organization")Organization organization);
//    List<Tradepoint> getTradepointByOrg(@Param("id") Integer id);
}
