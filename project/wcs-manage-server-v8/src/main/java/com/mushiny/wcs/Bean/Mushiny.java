package com.mushiny.wcs.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/12/14.
 */
public class Mushiny {
    private String projectName;
    private String projectVersion;
    private List<Module> modules = new ArrayList<>();

    public void addModule(Module module) {
        modules.add(module);
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules.addAll(modules);
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectVersion() {
        return projectVersion;
    }

    public void setProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
    }
}
