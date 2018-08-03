package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.masterdata.general.crud.dto.ClientDTO;
import com.mushiny.wms.masterdata.general.domain.Client;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemGroup;
import com.mushiny.wms.masterdata.obbasics.business.dto.SemblenceDTO;
import com.mushiny.wms.masterdata.obbasics.business.dto.SemblenceListDTO;
import com.mushiny.wms.masterdata.obbasics.domain.Semblence;

import java.util.List;

/**
 * Created by prestonmax on 2017/6/1.
 */

public interface SemblenceService {
    //查询所有
    List<SemblenceListDTO> getAll();
    //查询单个
    Semblence getBySerch(String search);

    void update(SemblenceDTO semblenceDTO);
    //新增
    void insertSemblence(SemblenceDTO semblenceDTO);

    void deleteSemblence(String id);

    List<ClientDTO> getAllClient();

    SemblenceDTO getById(String id);
}
