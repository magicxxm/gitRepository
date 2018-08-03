package com.mushiny.wms.system.domain;

import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SYS_MODULE")
public class Module extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "MODULE_TYPE", nullable = false)
    private String moduleType;

    @Column(name = "RF_ACTIVE", nullable = false)
    private boolean rfActive;

    @Column(name = "DK_ACTIVE", nullable = false)
    private boolean dkActive;

    @Column(name = "FORM_PATH")
    private String formPath;

    @Column(name = "REPORT_FILENAME")
    private String reportFilename;

    @Column(name = "REPORT_TYPE")
    private String reportType;

    @Column(name = "PRINT_PREVIEW", nullable = false)
    private boolean printPreview;

    @Column(name = "PRINT_DIALOG", nullable = false)
    private boolean printDialog;

    @Column(name = "PRINT_COPIES", nullable = false)
    private int printCopies;

    @Column(name = "RESOURCE_KEY")
    private String resourceKey;

    @ManyToMany
    @OrderBy("name")
    @JoinTable(
            name = "SYS_ROLE_MODULE",
            joinColumns = @JoinColumn(name = "MODULE_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private Set<Role> roles = new HashSet<>();

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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
