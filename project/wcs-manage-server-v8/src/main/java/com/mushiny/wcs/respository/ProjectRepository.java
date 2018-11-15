package com.mushiny.wcs.respository;

import com.mushiny.wcs.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
public interface ProjectRepository extends JpaRepository<Project, String> {

    @Query(" select p from Project p " +
            " where p.moduleName=:moduleName and p.projectVersion=:projectVersion"
    )
    Project getProjectByVersion(@Param("projectVersion") String projectVersion, @Param("moduleName") String moduleName);

}
