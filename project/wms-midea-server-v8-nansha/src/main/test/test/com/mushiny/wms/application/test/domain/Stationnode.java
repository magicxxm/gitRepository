package test.com.mushiny.wms.application.test.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @program: wms-midea-server
 * @description: midea 工作站
 * @author: mingchun.mu@mushiny.com
 * @create: 2018-07-10 17:04
 **/

@Entity
@Table(name = "MD_STATIONNODE")
public class Stationnode {
    @Id
    @GeneratedValue(generator = "internaltool-uuid")
    @GenericGenerator(name = "internaltool-uuid", strategy = "uuid2")
    @Column(name = "ID")
    private String id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "IS_CALL_POD")
    private Boolean isCallPod;
    @Column(name = "TYPE")
    private Integer type;
    @Column(name = "ADDRS")
    private String addrs;
    @Column(name = "MAP_ID")
    private String mapId;
    @Column(name = "MAP_NAME")
    private String mapName;
    @Column(name = "SECTION_ID")
    private String sectionId;
    @Column(name = "SECTION_NAME")
    private String sectionName;
    @Column(name = "RCS_SECTIONID")
    private String rcsSectionId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getCallPod() {
        return isCallPod;
    }

    public void setCallPod(Boolean callPod) {
        isCallPod = callPod;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAddrs() {
        return addrs;
    }

    public void setAddrs(String addrs) {
        this.addrs = addrs;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getRcsSectionId() {
        return rcsSectionId;
    }

    public void setRcsSectionId(String rcsSectionId) {
        this.rcsSectionId = rcsSectionId;
    }
}
