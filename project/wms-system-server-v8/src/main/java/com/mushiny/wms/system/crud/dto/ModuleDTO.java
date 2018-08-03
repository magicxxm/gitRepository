package com.mushiny.wms.system.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.system.domain.Module;

import javax.validation.constraints.NotNull;

public class ModuleDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private String moduleType;

    @NotNull
    private boolean rfActive;

    @NotNull
    private boolean dkActive;

    private String formPath;

    private String reportFilename;

    private String reportType;

    @NotNull
    private boolean printPreview;

    @NotNull
    private boolean printDialog;

    @NotNull
    private int printCopies;

    private String resourceKey;

    public ModuleDTO() {
    }

    public ModuleDTO(Module entity) {
        super(entity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public boolean isRfActive() {
        return rfActive;
    }

    public void setRfActive(boolean rfActive) {
        this.rfActive = rfActive;
    }

    public boolean isDkActive() {
        return dkActive;
    }

    public void setDkActive(boolean dkActive) {
        this.dkActive = dkActive;
    }

    public String getFormPath() {
        return formPath;
    }

    public void setFormPath(String formPath) {
        this.formPath = formPath;
    }

    public String getReportFilename() {
        return reportFilename;
    }

    public void setReportFilename(String reportFilename) {
        this.reportFilename = reportFilename;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public boolean isPrintPreview() {
        return printPreview;
    }

    public void setPrintPreview(boolean printPreview) {
        this.printPreview = printPreview;
    }

    public boolean isPrintDialog() {
        return printDialog;
    }

    public void setPrintDialog(boolean printDialog) {
        this.printDialog = printDialog;
    }

    public int getPrintCopies() {
        return printCopies;
    }

    public void setPrintCopies(int printCopies) {
        this.printCopies = printCopies;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }
}
