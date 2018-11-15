package wms.domain;

import wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-4 on 2017/8/3.
 */
@Entity
@Table(name = "ICQA_STOCKTAKING", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "STOCKTAKING_NO"
        })
})
public class Stocktaking extends BaseWarehouseAssignedEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "STOCKTAKING_NO", nullable = false)
    private String stocktakingNo;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "ZONE")
    private String zone;

    @Column(name = "AMOUNT", nullable = false)
    private int amount;

    @Column(name = "STOCKTAKING_TYPE")
    private String stocktakingType;

    @Column(name = "ENDED")
    private LocalDateTime ended;

    @Column(name = "STARTED")
    private LocalDateTime started;

    @OneToMany(mappedBy = "stocktaking", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<StocktakingOrder> positions = new ArrayList<>();

    public void addPosition(StocktakingOrder position) {
        getPositions().add(position);
        position.setStocktaking(this);
    }

    public String getStocktakingNo() {
        return stocktakingNo;
    }

    public void setStocktakingNo(String stocktakingNo) {
        this.stocktakingNo = stocktakingNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getStocktakingType() {
        return stocktakingType;
    }

    public void setStocktakingType(String stocktakingType) {
        this.stocktakingType = stocktakingType;
    }

    public LocalDateTime getEnded() {
        return ended;
    }

    public void setEnded(LocalDateTime ended) {
        this.ended = ended;
    }

    public LocalDateTime getStarted() {
        return started;
    }

    public void setStarted(LocalDateTime started) {
        this.started = started;
    }

    public List<StocktakingOrder> getPositions() {
        return positions;
    }

    public void setPositions(List<StocktakingOrder> positions) {
        this.positions = positions;
    }
}
