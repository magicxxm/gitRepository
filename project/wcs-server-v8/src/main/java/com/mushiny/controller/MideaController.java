package com.mushiny.controller;

import com.mushiny.beans.Pod;
import com.mushiny.beans.Section;
import com.mushiny.business.PodManager;
import com.mushiny.business.WareHouseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/3.
 */
@RestController
@RequestMapping(value="/midea")
public class MideaController {
    @Autowired
    private PodManager podManager;
    @Autowired
    private WareHouseManager wareHouseManager;

    @GetMapping(path = "/allPodsInfo")
    public String checkWorkStationAddrs(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        Section section = this.wareHouseManager.getSectionById(sectionId);
        if(section == null){
            section = this.wareHouseManager.getSectionByRcsSectionId(Long.parseLong(sectionId));
        }
        //获取所有信息
        List<Pod> pods = this.podManager.getPods(section);

        return "";
    }

}
