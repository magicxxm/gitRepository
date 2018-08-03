package com.mushiny.wcs.application.domain;


import javax.persistence.*;

@Entity
@Table(name = "ICQA_STOCKTAKINGSTATION", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "WAREHOUSE_ID"
        })
})
public class StocktakingStation extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TYPE_ID")
    private StocktakingStationType stocktakingStationType;

    @ManyToOne
    @JoinColumn(name = "OPERATOR_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "WORKSTATION_ID", nullable = false)
    private WorkStation workstation;

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

    public StocktakingStationType getStocktakingStationType() {
        return stocktakingStationType;
    }

    public void setStocktakingStationType(StocktakingStationType stocktakingStationType) {
        this.stocktakingStationType = stocktakingStationType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public WorkStation getWorkstation() {
        return workstation;
    }

    public void setWorkstation(WorkStation workstation) {
        this.workstation = workstation;
    }

}
