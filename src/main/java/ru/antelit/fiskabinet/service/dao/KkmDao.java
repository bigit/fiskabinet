package ru.antelit.fiskabinet.service.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import ru.antelit.fiskabinet.domain.Kkm;
import ru.antelit.fiskabinet.domain.KkmModel;
import ru.antelit.fiskabinet.domain.Tradepoint;
import ru.antelit.fiskabinet.domain.Vendor;

import java.util.List;

@Mapper
public interface KkmDao {

    List<Vendor> listVendors();
    List<KkmModel> listModelsByVendor(@Param("vendor") Vendor vendor);
    List<Kkm> listKkmByTradepoint(@Param("tradepoint") Tradepoint tradepoint);

    Vendor getVendor(Integer id);
}
