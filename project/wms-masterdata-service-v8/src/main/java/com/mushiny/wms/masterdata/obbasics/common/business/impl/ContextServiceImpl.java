package com.mushiny.wms.masterdata.obbasics.common.business.impl;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.common.entity.BaseEntity;
import com.mushiny.wms.config.security.SecurityUtils;
import com.mushiny.wms.masterdata.general.domain.Client;
import com.mushiny.wms.masterdata.general.domain.User;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
import com.mushiny.wms.masterdata.general.repository.UserRepository;
import com.mushiny.wms.masterdata.general.repository.WarehouseRepository;
import com.mushiny.wms.masterdata.obbasics.common.business.ContextService;
import com.mushiny.wms.masterdata.obbasics.common.domain.Resource;
import com.mushiny.wms.masterdata.obbasics.common.util.TypeResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ContextServiceImpl implements ContextService {

    private final Logger log = LoggerFactory.getLogger(ContextServiceImpl.class);

    private static List<String> collectionVetoList = new ArrayList<>();

    protected static final String WAREHOUSE_HEADER = "Warehouse";

    private final HttpServletRequest request;

    private final WarehouseRepository warehouseRepository;

    private final UserRepository userRepository;

    public ContextServiceImpl(HttpServletRequest request,
                              WarehouseRepository warehouseRepository,
                              UserRepository userRepository) {
        this.request = request;
        this.warehouseRepository = warehouseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public User getCallersUser() {
        return userRepository.findByUsername(SecurityUtils.getCurrentUsername());
    }

    @Override
    public String getCallerUsername() {
        return SecurityUtils.getCurrentUsername();
    }

    @Override
    public Warehouse getCallersWarehouse() {
//        User user = getCallersUser();
//        if (user != null) {
//            return user.getWarehouse();
//        }
//        return null;
        String warehouseId = getWarehouseHeader();
        if (warehouseId != null) {
            return warehouseRepository.findOne(warehouseId);
        }
        return null;
    }

    @Override
    public Client getCallersClient() {
        User callersUser = getCallersUser();
        if (callersUser != null) {
            return callersUser.getClient();
        }
        return null;
    }

    @Override
    public String getCallersLocale() {
        User callersUser = getCallersUser();
        if (callersUser != null) {
            return callersUser.getLocale();
        }
        return Resource.LOCALE_DEFAULT;
    }

    protected String getWarehouseHeader() {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getHeader(WAREHOUSE_HEADER);
    }

    @Override
    public boolean checkClient(BaseEntity be) {
        String logStr = "checkClient ";
        if (be == null) {
            throw new NullPointerException("bo must not be null");
        }

        User callersUser = getCallersUser();

        if (callersUser == null) {
            log.error(logStr + "Cannot identify callers User");
            return false;
        }

//        if (callersUser.hasRole(com.mushiny.wms.common.constants.Role.ADMIN)) {
//            return true;
//        }

        if (be instanceof BaseClientAssignedEntity) {
            BaseClientAssignedEntity bcae = (BaseClientAssignedEntity) be;
            if (bcae.getClientId() == null) {
                return true;
            }
            if (callersUser.getClient() == null) {
                return false;
            }
            if (bcae.getClientId().equals(callersUser.getClient())) {
                return true;
            } else if (callersUser.getClient().isSystemClient()) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * Reads every property to initialize values from LAZY loading.
     */
    @Override
    @SuppressWarnings({"unused", "rawtypes"})
    public BaseEntity eagerRead(BaseEntity e) {
        try {
            BeanInfo infoTo = Introspector.getBeanInfo(e.getClass());
            PropertyDescriptor[] d = infoTo.getPropertyDescriptors();

            for (int i = 0; i < d.length; i++) {
                try {
                    if (d[i].getReadMethod() == null) {
                        continue;
                    }

                    Object value = d[i].getReadMethod().invoke(e, new Object[0]);

                    if (value == null) {
                        continue;
                    } else if (TypeResolver.isPrimitiveType(d[i].getPropertyType())) {
                        // should be loaded now
                    } else if (TypeResolver.isEnumType(d[i].getPropertyType())) {
                        // should be loaded now
                    } else if (TypeResolver.isDateType(d[i].getPropertyType())) {
                        // should be loaded now
                    } else if (value instanceof BaseEntity) {
                        ((BaseEntity) value).toUniqueString();
                    } else if (value instanceof Collection) {
                        String name = e.toUniqueString().toLowerCase();
                        if (collectionVetoList.contains(name)) {
                            log.info("Do not eager read veto collection. id=" + e.getId() + ", name=" + e.toUniqueString() + ", property=" + d[i].getName());
                        } else {
                            Collection c = (Collection) value;
                            if (c == null) {
                                log.warn("Collection is null " + d[i].getName());
                            } else if (c.size() == 0) {
                                try {
                                    log.debug("Try init Collection of length 0: " + d[i].getName());
                                    // just to perform any operation on collection to read it eagerly - hopefully
                                    c.contains(d[i].getName());
                                    d[i].getWriteMethod().invoke(e, new Object[]{value});
                                } catch (Throwable t) {
                                    log.error(t.getMessage(), t);
                                }
                            } else {
                                for (Object elem : c) {
                                    log.debug("-- read element: " + elem.toString());
                                }
                            }
                        }

                    } else {
                        log.warn("Unsupported value type for eager reading: " + value.getClass().getName());
                    }
                } catch (Throwable ex) {
                    log.error(ex.getMessage(), ex);
                    continue;
                }
            }

            return e;
        } catch (Throwable ex) {
            log.error(ex.getMessage(), ex);
        }

        return e;
    }
}
