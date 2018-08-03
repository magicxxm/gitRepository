package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.general.crud.mapper.ClientMapper;
import com.mushiny.wms.masterdata.general.domain.Client;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.ItemGroupRepository;
import com.mushiny.wms.masterdata.general.crud.dto.ClientDTO;
import com.mushiny.wms.masterdata.obbasics.business.dto.SemblenceDTO;
import com.mushiny.wms.masterdata.obbasics.business.dto.SemblenceListDTO;
import com.mushiny.wms.masterdata.obbasics.domain.Semblence;
import com.mushiny.wms.masterdata.obbasics.repository.SemblenceRepository;
import com.mushiny.wms.masterdata.obbasics.service.SemblenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prestonmax on 2017/6/1.
 */
@Service
public class SemblenceServiceImpl implements SemblenceService {
    private final SemblenceRepository semblenceRepository;
    private final ApplicationContext applicationContext;
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    protected final ItemGroupRepository itemGroupRepository;
    @Autowired
    public SemblenceServiceImpl(SemblenceRepository semblenceRepository,
                                ApplicationContext applicationContext,
                                ClientRepository clientRepository,
                                ClientMapper clientMapper, ItemGroupRepository itemGroupRepository) {
        this.semblenceRepository = semblenceRepository;
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.itemGroupRepository = itemGroupRepository;
    }

    @Override
    public List<SemblenceListDTO> getAll() {
        List<Semblence> semblences = semblenceRepository.getAll(Constant.NOT_LOCKED);
        List<SemblenceListDTO> semblenceListDTOS = new ArrayList<>();
        for (Semblence semblence:semblences){
            SemblenceListDTO semblenceListDTO = new SemblenceListDTO();
            semblenceListDTO.setId(semblence.getId());
            Client client = clientRepository.retrieve(semblence.getClientId());
            semblenceListDTO.setClientName(client.getClientNo());
            semblenceListDTO.setClientId(client.getClientNo());
            semblenceListDTO.setSemblence(semblence.getSemblence());
            semblenceListDTOS.add(semblenceListDTO);
        }
        return semblenceListDTOS;
    }

    @Override
    public Semblence getBySerch(String search) {
        return semblenceRepository.getBySearch(search);
    }

    @Override
    public SemblenceDTO getById(String id) {
        SemblenceDTO semblenceDTO = new SemblenceDTO();
        Semblence semblence = semblenceRepository.getById(id);
        semblenceDTO.setClientId(semblence.getClientId());
        semblenceDTO.setSemblence(semblence.getSemblence());
        semblenceDTO.setSemblenceId(semblence.getId());
        Client client = clientRepository.retrieve(semblence.getClientId());
        semblenceDTO.setClientName(client.getName());
        return semblenceDTO;
    }

    public void insertSemblence(SemblenceDTO semblenceDTO){
        if(semblenceDTO.getClientId().equalsIgnoreCase("ALL")){
            List<Client> clients = clientRepository.findByEntityLockOrderByName(Constant.NOT_LOCKED);
            if(clients!=null){
                for (Client client:clients){
                    Semblence semblence = null;
                    semblence = semblenceRepository.getByClientId(client.getId());
                    if(semblence==null){
                        semblence = new Semblence();
                        semblence.setClientId(client.getId());
                        semblence.setSemblence(semblenceDTO.getSemblence());
                        semblenceRepository.saveAndFlush(semblence);
                    }
                }
//                semblenceRepository.save(semblences);
            }
        }else{
            Semblence sem = semblenceRepository.getByClientIdAndSemblence(semblenceDTO.getClientId(),semblenceDTO.getSemblence());
            if(sem==null){
                Semblence semblence = new Semblence();
                semblence.setClientId(semblenceDTO.getClientId());
                semblence.setSemblence(semblenceDTO.getSemblence());
                semblenceRepository.saveAndFlush(semblence);
            }
        }
    }
    @Override
    public List<ClientDTO> getAllClient(){
        List<Client> clients = clientRepository.findByEntityLockOrderByName(Constant.NOT_LOCKED);
        List<ClientDTO> clientDTOS = new ArrayList<ClientDTO>();
        for (Client client:clients){
            clientDTOS.add(clientMapper.toDTO(client));
        }
        return clientDTOS;
    }

    @Override
    public void update(SemblenceDTO semblenceDTO) {
        Semblence semblence = semblenceRepository.getById(semblenceDTO.getSemblenceId());
        semblence.setVersion(semblence.getVersion()+1);
        semblence.setSemblence(semblenceDTO.getSemblence());
        semblence.setClientId(semblenceDTO.getClientId());
        semblenceRepository.saveAndFlush(semblence);
    }

    @Override
    public void deleteSemblence(String id) {
        semblenceRepository.delete(id);
    }
}
