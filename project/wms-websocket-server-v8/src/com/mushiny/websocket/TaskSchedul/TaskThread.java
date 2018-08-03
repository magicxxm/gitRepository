package com.mushiny.websocket.TaskSchedul;


import com.mushiny.comm.JSONUtil;
import com.mushiny.comm.SpringUtil;
import com.mushiny.jdbc.service.JdbcService;
import com.mushiny.rabbitmq.RabbitMessageSender;
import com.mushiny.rabbitmq.RabbitMqReceiver;
import com.mushiny.websocket.WSServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author:
 * @Description: Created by wangjianwei on 2017/8/29.
 */
public class TaskThread implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskThread.class);
    private static final RabbitMessageSender sender = SpringUtil.getBean(RabbitMessageSender.class);
    private String pickPackWall;
    private LinkedBlockingQueue pickTasks = new LinkedBlockingQueue(100);
    private List<Station> stations = new ArrayList<>(10);
    private JdbcService service = SpringUtil.getBean(JdbcService.class);
    private int current = 0;

    public String getPickPackWall() {
        return pickPackWall;
    }

    public void setPickPackWall(String pickPackWall) {
        this.pickPackWall = pickPackWall;
    }

    private void initPackpackwallStation(Map task) {
        List list = service.gePackWallStation();
        for (int i = 0; i < list.size(); i++) {
            Map row = (Map) list.get(i);
            if (row.get("PICKPACKWALL_ID") == null
                    || "".equals(row.get("PICKPACKWALL_ID"))) {
                continue;
            }
            if (pickPackWall.equalsIgnoreCase((String) row.get("PICKPACKWALL_ID"))) {
                Station station = new Station();
                station.setPickPackWall((String) row.get("PICKPACKWALL_ID"));
                station.setPhyStation((String) row.get("PHYSICS"));
                station.setLogicalStation((String) row.get("ID"));

                if (!stations.contains(station)) {
                    stations.add(station);
                }
            }
            LOGGER.debug("加载pickpackwall包装工作站 {}\n{}", pickPackWall, JSONUtil.toJSon(stations));
        }

    }

    public void setPickTasks(LinkedBlockingQueue pickTasks) {
        this.pickTasks.addAll(pickTasks);
    }

    public LinkedBlockingQueue getPickTasks() {
        return pickTasks;
    }

    public void setStations(List<Station> stations) {
        this.stations.addAll(stations);
    }

    public List<Station> getStations() {
        return stations;
    }

    @Override

    public void run() {
        while (true) {
            try {
                while (!pickTasks.isEmpty()) {
                    Map task = (Map) pickTasks.peek();
                    TimeUnit.SECONDS.sleep(2);
                    initPackpackwallStation(task);
                    int stationLen = CollectionUtils.isEmpty(stations) ? 0 : stations.size();
                    if (stationLen > 0) {
                        Station station = stations.get(current);
                        //如果包裝灯泡不亮，並且沒有任務時分配任務
                        //当前pickPackWall对应包装工作站的灯只有一个亮
                        if (service.getPackWallDiaState(station.getPickPackWall()) < 1) {
                            //当前工作站是否有任务
                            boolean currentStationTask = station.getHasTask().get();
                            LOGGER.info("工作站{} 是否有任务?{}", station.getPhyStation(), currentStationTask);
                            // 没有任务，并且工作站登录时分配任务
                            boolean hasLogin = CollectionUtils.isEmpty(service.getPackWorkStationState(station.getLogicalStation(), station.getPickPackWall()));
                            boolean isStartPack=service.isStopPack(station.getPhyStation());
                            if ((!currentStationTask) && (!hasLogin)&&(isStartPack)) {
                                station.getTask().put(pickTasks.poll());

                                service.updateDigitallabelShipment(station.getLogicalStation(), 1, (String) task.get("SHIPMENT_ID"));
                                Session session = getStationSession(station.getPhyStation());
                                LOGGER.info("线程{}\n给灯{}分配工作站\n物理工作={}逻辑工作站={}", Thread.currentThread().getName(), task.get("DIGITALLABEL2"), station.getPhyStation(), station.getLogicalStation());
                                if (!ObjectUtils.isEmpty(session)) {
                                    RabbitMqReceiver.sendMessage(station.getPhyStation(), session, DigitallabelMessage.replyWorkStationDigMess((String) task.get("DIGITALLABEL2"), station.getPhyStation()));
                                } else {
                                    LOGGER.debug("线程{}\n未找到工作站{} 对应的Session\nWSServer.workStations-->{}", Thread.currentThread().getName(), station.getPhyStation(), JSONUtil.mapToJSon(WSServer.workStations));
                                }
                                LOGGER.debug("线程{}\n----设置灯和工作站的对应关系\n灯id={}物理工作={}逻辑工作站={}", Thread.currentThread().getName(), task.get("DIGITALLABEL2"), station.getPhyStation(), station.getLogicalStation());
                                if (!WSServer.diglabs.containsKey(task.get("DIGITALLABEL2"))) {
                                    WSServer.diglabs.put(task.get("DIGITALLABEL2"), station.getPhyStation());
                                } else {
                                    WSServer.diglabs.replace(task.get("DIGITALLABEL2"), station.getPhyStation());
                                }
                                if (LOGGER.isDebugEnabled()) {

                                    String[] message = new String[]{
                                            "线程{}",
                                            "WSServer 保存灯和工作站对应关系为\nWSServer.diglabs===>{}"
                                    };

                                    LOGGER.debug(StringUtils.arrayToDelimitedString(message, "\n")
                                            , new Object[]{Thread.currentThread().getName(),
                                                    JSONUtil.mapToJSon(WSServer.diglabs)
                                            }
                                    );


                                }
                                //亮灯
                                sender.sendMessage(DigitallabelMessage.getDigitallabelMess((String) task.get("DIGITALLABEL2"), station.getPhyStation()));


                            } else {
                                TimeUnit.SECONDS.sleep(2);
                                if (LOGGER.isDebugEnabled()) {
                                    String[] message = new String[]{
                                            "线程{}",
                                            "工作站 {} 有任务或未登录,未分配任务",
                                            "工作站任务标志currentStationTask={}",
                                            "isStartPack {}",
                                            "WsServer所有登录的工作站为{}",
                                            "工作站{}是否退出:{}"
                                    };

                                    LOGGER.debug(StringUtils.arrayToDelimitedString(message, "\n")
                                            , new Object[]{Thread.currentThread().getName(),
                                                    station.getPhyStation(),
                                                    currentStationTask,
                                                    isStartPack,
                                                    WSServer.workStations,
                                                    station.getPhyStation(),
                                                    hasLogin
                                            }
                                    );

                                }
                            }

                        } else {
                            TimeUnit.SECONDS.sleep(2);
                            LOGGER.info("pickPackWall:{} 已经有灯在亮,未分配任务{}给工作站{} ", station.getPickPackWall(), JSONUtil.mapToJSon(task), station.getPhyStation());
                        }
                        current++;
                        if (current == stationLen) {
                            current = 0;
                        }

                    }
                }
                synchronized (Lock.class)
                {
                    try {
                        Lock.class.wait();
                    } catch (InterruptedException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }

        }


    }

    private Session getStationSession(String station) {
        return WSServer.workStations.get(station);
    }
}

