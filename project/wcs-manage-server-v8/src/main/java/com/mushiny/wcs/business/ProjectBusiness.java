package com.mushiny.wcs.business;

import com.mushiny.wcs.entity.Project;
import com.mushiny.wcs.respository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/12/20.
 */
@Component
public class ProjectBusiness {
    @Autowired
    private ProjectRepository projectRepository;

    public List<Map<String, Object>> getAllProject() {
        List<Map<String, Object>> results = new ArrayList<>();
        List<Project> projects = projectRepository.findAll();
        if (!CollectionUtils.isEmpty(projects)) {

            for (Project pj : projects) {
                Map<String, Object> te = null;
                for (Map<String, Object> tt : results) {
                    if (tt.get("projectVersion").equals(pj.getProjectVersion())) {
                        te = tt;
                        break;

                    }
                }
                if (CollectionUtils.isEmpty(te)) {
                    Map<String, Object> temp = new HashMap();
                    temp.put("projectVersion", pj.getProjectVersion());
                    List sub = new ArrayList();
                    Map subModule = new HashMap();
                    subModule.put("moduleName", pj.getModuleName());
                    subModule.put("modulePort", pj.getModulePort());
                    sub.add(subModule);
                    temp.put("subModules", sub);
                    results.add(temp);

                } else {

                    Map subModule2 = new HashMap();
                    subModule2.put("moduleName", pj.getModuleName());
                    subModule2.put("modulePort", pj.getModulePort());
                    List<Map> subTemp = (List<Map>) te.get("subModules");
                    subTemp.add(subModule2);
                }

            }

        }
        return results;
    }
}
