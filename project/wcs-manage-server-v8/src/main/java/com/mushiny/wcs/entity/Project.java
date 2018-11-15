package com.mushiny.wcs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/12/20.
 */

@Entity
@Table(name = "wms_project")
public class Project extends BaseEntity {
    @Column(name = "PROJECT_NAME")
    private String projectName;
    @Column(name = "PROJECT_VERSION")
    private String projectVersion;
    @Column(name = "MODULE_NAME")
    private String moduleName;
    @Column(name = "MODULE_PORT")
    private String modulePort;
    @Column(name = "MODULE_DIR")
    private String moduleDir;
    @Column(name = "MODULE_LOG")
    private String moduleLog;
    @Column(name = "FILE_SAVE_DIR")
    private String fileSaveDir;

    public String getFileSaveDir() {
        return fileSaveDir;
    }

    public void setFileSaveDir(String fileSaveDir) {
        this.fileSaveDir = fileSaveDir;
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

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModulePort() {
        return modulePort;
    }

    public void setModulePort(String modulePort) {
        this.modulePort = modulePort;
    }

    public String getModuleDir() {
        return moduleDir;
    }

    public void setModuleDir(String moduleDir) {
        this.moduleDir = moduleDir;
    }

    public String getModuleLog() {
        return moduleLog;
    }

    public void setModuleLog(String moduleLog) {
        this.moduleLog = moduleLog;
    }
}
