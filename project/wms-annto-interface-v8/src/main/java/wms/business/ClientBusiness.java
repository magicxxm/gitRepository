package wms.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import wms.domain.common.Client;

import javax.persistence.EntityManager;

/**
 * Created by 123 on 2018/1/3.
 */
@Component
public class ClientBusiness {
    private final Logger log = LoggerFactory.getLogger(WarehouseBusiness.class);
    private final EntityManager manager;

    public ClientBusiness(EntityManager manager){
        this.manager = manager;
    }

    public Client saveClient(String clientNo){
        Client client = new Client();
        client.setClientNo(clientNo);
        client.setName(clientNo);
        manager.persist(client);
        return client;
    }
}
