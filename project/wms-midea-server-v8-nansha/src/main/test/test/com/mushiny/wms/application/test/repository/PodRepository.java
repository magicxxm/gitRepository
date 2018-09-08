package test.com.mushiny.wms.application.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import test.com.mushiny.wms.application.test.domain.Pod;

public interface PodRepository extends JpaRepository<Pod, String> {

    @Query(value = "SELECT MAX(p.POD_INDEX) FROM MD_POD p", nativeQuery = true)
    Integer getMaxIndexOfPod();



}
