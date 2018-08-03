package view.btnOper.oper;


import com.mingchun.mu.mushiny.kiva.path.TriangleRotateAreaNew;
import com.mingchun.mu.mushiny.kiva.path.TriangleRotateAreaNewManager;
import com.mushiny.kiva.map.KivaMap;
import com.mushiny.kiva.map.MapManager;
import view.AutoPathView;
import view.PathConfigMainServer;
import view.bean.PathBean;
import view.bean.ViewAGVPathContainer;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Laptop-6 on 2017/12/23.
 */
public class PathConfigCommandAction extends DefaultButtonAction {

    private Pattern pathPattern;
    private String pathRex = "^(\\s*[1-9][0-9]*\\s*,)+(\\s*[1-9][0-9]*\\s*)$"; // 路径匹配正则表达式

    public PathConfigCommandAction(AutoPathView autoPathView) {
        super(autoPathView);
        init();
    }
    private void init(){
        pathPattern = Pattern.compile(pathRex);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        long robotId;
        long upAddressCodeId = 0;
        long downAddressCodeId = 0;
        long stopAddressCodeId = 0;
        long waitTime = 0;
        long rotationAddressCodeId = 0;
        int rotationAngle = 0;
        int podCodeId = 0;

        String pathText = autoPathView.getPathConfigPanel().getPathText().getText().trim();
        String robotIdText = autoPathView.getPathConfigPanel().getRobotIdText().getText().trim();
        String upAddressCodeIdText = autoPathView.getPathConfigPanel().getUpAddressCodeIdText().getText().trim();
        String downAddressCodeIdText = autoPathView.getPathConfigPanel().getDownAddressCodeIdText().getText().trim();
        String stopAddressCodeIdText = autoPathView.getPathConfigPanel().getStopAddressCodeIdText().getText().trim();
        String waitTimeText = autoPathView.getPathConfigPanel().getStopTimeText().getText().trim();
        String rotationAddressCodeIdText = autoPathView.getPathConfigPanel().getRotationAddressCodeIdText().getText().trim();
        String rotationAngleText = autoPathView.getPathConfigPanel().getChargerTowardText().getText().trim();
        String podCodeIdText = autoPathView.getPathConfigPanel().getPodCodeIdText().getText().trim();

        if(!pathPattern.matcher(pathText).matches()){
            autoPathView.printLogAndClearLogBySize("输入路径格式不正确，正确实例: 1,2,3,4！");
            return;
        }
        if(!intPattern.matcher(robotIdText).matches()){
            autoPathView.printLogAndClearLogBySize("请输入正确的‘小车ID’！");
            return;
        }
        if(!intPattern.matcher(upAddressCodeIdText).matches()){
            upAddressCodeId = 0;
        }else{
            upAddressCodeId = Long.parseLong(upAddressCodeIdText);
        }
        if(!intPattern.matcher(downAddressCodeIdText).matches()){
            downAddressCodeId = 0;
        }else{
            downAddressCodeId = Long.parseLong(downAddressCodeIdText);
        }
        if(!intPattern.matcher(stopAddressCodeIdText).matches()){
            stopAddressCodeId = 0;
        }else{
            stopAddressCodeId = Long.parseLong(stopAddressCodeIdText);
        }
        if(!intPattern.matcher(waitTimeText).matches()){
            waitTime = 0;
        }else{
            waitTime = Long.parseLong(waitTimeText);
        }
        if(!intPattern.matcher(podCodeIdText).matches()){
            podCodeId = 0;
        }else{
            podCodeId = Integer.parseInt(podCodeIdText);
        }
        if(!intPattern.matcher(rotationAddressCodeIdText).matches()){
            rotationAddressCodeId = 0;
        }else{
            rotationAddressCodeId = Long.parseLong(rotationAddressCodeIdText);
        }
        if(!intPattern.matcher(rotationAngleText).matches()){
            rotationAngle = 0;
        }else{
            rotationAngle = Integer.parseInt(rotationAngleText);
        }

        // 根据旋转点虚拟旋转区
        if(rotationAddressCodeId != 0 && TriangleRotateAreaNewManager.getInstance().getTriangleRotateAreaNewByCellNode(rotationAddressCodeId) == null){
            KivaMap kivaMap = MapManager.getInstance().getMap();
            String[] paths = pathText.split(",");
            List<Long> pathList = new LinkedList<>();
            for(String temp : paths){
                pathList.add(Long.parseLong(temp.trim()));
            }
            long enterAddressCodeId = pathList.get(pathList.indexOf(rotationAddressCodeId) - 1);
            TriangleRotateAreaNew triangleRotateAreaNew = new TriangleRotateAreaNew(rotationAddressCodeId, kivaMap.getMapCellByAddressCodeID(enterAddressCodeId), kivaMap.getMapCellByAddressCodeID(rotationAddressCodeId));
            TriangleRotateAreaNewManager.getInstance().addTriangleRotateAreaNew(triangleRotateAreaNew);
        }

        robotId = Long.parseLong(robotIdText);
        Map<Long, ViewAGVPathContainer> agvPathContainerMap = PathConfigMainServer.getInstance().getAgvPathContainerMap();
        PathBean pathBean;
        ViewAGVPathContainer viewAGVPathContainer = agvPathContainerMap.get(robotId);
        if(viewAGVPathContainer == null){
            viewAGVPathContainer = new ViewAGVPathContainer();
            viewAGVPathContainer.setRobotId(robotId);
            agvPathContainerMap.put(robotId, viewAGVPathContainer);
        }else{
            PathBean tempPathBean = viewAGVPathContainer.getPathList().get(viewAGVPathContainer.getPathList().size() - 1);
            String firstAddr = pathText.split(",")[0].trim();
            String lastAddr = tempPathBean.getPathList().get(tempPathBean.getPathList().size()-1) + "";
            if(!firstAddr.trim().equals(lastAddr)){
                autoPathView.printLogAndClearLogBySize("路径不可执行：已配置路径的最后一点("+lastAddr+")和将配置路径的起点("+firstAddr+")不一致！");
                return;
            }
        }
        if(pathText.indexOf(stopAddressCodeIdText) != -1){
            String path1 = pathText.substring(0, pathText.indexOf(stopAddressCodeIdText) + stopAddressCodeIdText.length());
            String path2 = pathText.substring(pathText.indexOf(stopAddressCodeIdText));
            if("".equals(path1.trim()) || (path1.trim().split(",")).length == 1){
                createPathBean(robotId, upAddressCodeId, podCodeId, rotationAngle, downAddressCodeId, path2, waitTime, viewAGVPathContainer);
            }else if("".equals(path2.trim()) || (path2.trim().split(",")).length == 1){
                createPathBean(robotId, upAddressCodeId, podCodeId, rotationAngle, downAddressCodeId, path1, 0, viewAGVPathContainer);
            }else{
                createPathBean(robotId, upAddressCodeId, podCodeId, rotationAngle, downAddressCodeId, path1, 0, viewAGVPathContainer);
                createPathBean(robotId, upAddressCodeId, podCodeId, rotationAngle, downAddressCodeId, path2, waitTime, viewAGVPathContainer);
            }
        }else{
            createPathBean(robotId, upAddressCodeId, podCodeId, rotationAngle, downAddressCodeId, pathText, 0, viewAGVPathContainer);
        }
    }

    private void createPathBean(long robotId, long upAddressCodeId, int podCodeId, int rotationAngle, long downAddressCodeId, String pathText, long waitTime, ViewAGVPathContainer viewAGVPathContainer) {
        PathBean pathBean;
        String[] pathArr = pathText.split(",");
        LinkedList<Long> pathList = new LinkedList<>();
        for(String temp : pathArr){
            pathList.add(Long.parseLong(temp.trim()));
        }
        pathBean = new PathBean();
        pathBean.setRobotId(robotId);
        pathBean.setUpAddressCodeId(upAddressCodeId);
        pathBean.setDownAddressCodeId(downAddressCodeId);
        pathBean.setPathList(pathList);
        pathBean.setWaitTime(waitTime * 1000);
        pathBean.setPodCodeId(podCodeId);
        pathBean.setRotateTheta(rotationAngle);
        viewAGVPathContainer.addPath(pathBean);
        autoPathView.printLogAndClearLogBySize("配置路径成功："+pathBean);
    }
}
