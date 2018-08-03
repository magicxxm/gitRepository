package com.mushiny.wms.system.service;


import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.system.crud.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService extends BaseService<UserDTO> {

    void changePassword(String password, String newPassword);

    void resetPassword(String id, String password);

    UserDTO getCurrentUser();

    void importFile(MultipartFile file);
}
