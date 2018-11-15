package com.mushiny.wcs.Bean;

/**
 * @author:
 * @Description: Created by wangjianwei on 2017/12/14.
 */
public class Module {
    private String moduleName;
    private String modulePort;
    private String moduleDir;
    private String moduleLog;
    private String fileSaveDir;

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

    public String getFileSaveDir() {
        return fileSaveDir;
    }

    public void setFileSaveDir(String fileSaveDir) {
        this.fileSaveDir = fileSaveDir;
    }
}
