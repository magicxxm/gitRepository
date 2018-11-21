package com.mushiny.auth.domain;

import javax.persistence.*;

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
    private boolean rfActive = false;

    @Column(name = "DK_ACTIVE", nullable = false)
    private boolean dkActive = false;

    @Column(name = "FORM_PATH")
    private String formPath;

    @Column(name = "REPORT_FILENAME")
    private String reportFilename;

    @Column(name = "REPORT_TYPE")
    private String reportType;

    @Column(name = "PRINT_PREVIEW")
    private boolean printPreview = false;

    @Column(name = "PRINT_DIALOG")
    private boolean printDialog = false;

    @Column(name = "PRINT_COPIES")
    private int printCopies = 0;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "RESOURCE_KEY")
    private String resourceKey;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    @Override
    public String toUniqueString() {
        return getName();
    }
}
