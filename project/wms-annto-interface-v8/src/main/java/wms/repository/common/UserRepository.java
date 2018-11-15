package wms.repository.common;

import wms.common.respository.BaseRepository;
import wms.domain.common.User;

import java.util.List;

public interface UserRepository extends BaseRepository<User, String> {

    User findByUsername(String username);

    List<User> findByEntityLockOrderByUsername(Integer entityLock);

}
