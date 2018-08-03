package com.mushiny.websocket.TaskSchedul;

import com.mushiny.comm.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;

/**
 * @author:
 * @Description: Created by wangjianwei on 2017/8/29.
 */

public class TaskSchedule {

    private static final ExecutorService exec = Executors.newFixedThreadPool(1000);
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskSchedule.class);
    private static Map<Object, TaskThread> pickPackWallTask = new HashMap<>(12);

    public static  void schedule(Map task) {

        if (!CollectionUtils.isEmpty(task)) {
            Object pickPackWall = task.get("PICKPACKWALL_ID");
            if (!ObjectUtils.isEmpty(pickPackWall)) {
                try {
                    LOGGER.info("开始给工作站推送任务");

                    if (!pickPackWallTask.containsKey(pickPackWall)) {
                        LOGGER.info("新建pickPackWall {} 的线程", pickPackWall);
                        TaskThread run = new TaskThread();
                        final LinkedBlockingQueue temp = new LinkedBlockingQueue(1000);
                        temp.put(task);
                        run.setPickTasks(temp);
                        run.setPickPackWall((String) pickPackWall);
                        pickPackWallTask.put(pickPackWall, run);
                        exec.submit(run);

                    } else {
                        TaskThread taskThread = pickPackWallTask.get(pickPackWall);
                        if (taskThread.getPickTasks().offer(task)) {
                            LOGGER.info("往pickPackWall{}推送任务{}成功", pickPackWall, JSONUtil.mapToJSon(task));
                            synchronized (Lock.class)
                            {
                                Lock.class.notify();
                            }

                        } else {
                            LOGGER.info("往pickPackWall{}推送任务{}失败", pickPackWall, JSONUtil.mapToJSon(task));

                        }

                    }
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            } else {
                LOGGER.info("发送的任务不和法 ,未包含pickPackWall\n 任务为---->{}", JSONUtil.mapToJSon(task));
            }


        }


    }


}
