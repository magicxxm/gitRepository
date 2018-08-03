package com.mushiny.websocket.TaskSchedul;

import com.mushiny.comm.DateTimeUtil;
import com.mushiny.comm.JSONUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author:
 * @Description: Created by wangjianwei on 2017/8/29.
 */
public class DigitallabelMessage {

    private static volatile int i=1;

    public synchronized static String getDigitallabelMess(String digitalId, String workStation) {
        i++;
        Map messageTample = new HashMap();
        messageTample.put("labelId", digitalId);
        messageTample.put("url", "/light/onOff");
        messageTample.put("onOffFlag", true);
        messageTample.put("shootLight", true);
        messageTample.put("workStationId", workStation);
        messageTample.put("times", i);
        messageTample.put("time", DateTimeUtil.getDateFormat(new Date(System.currentTimeMillis()),"yyyy-MM-dd HH:mm:ss"));

        return JSONUtil.toJSon(messageTample);

    }

    public static String replyWorkStationDigMess(String digitalId, String workStation) {
        i++;
        Map<String, Object> map = new HashMap<>();
        map.put("cmd", "2");
        map.put("labelId", digitalId);
        map.put("description", "确认");
        map.put("state", 0);
        map.put("resTime", System.currentTimeMillis());
        map.put("workStationId", workStation);
        return JSONUtil.toJSon(map);

    }
}
