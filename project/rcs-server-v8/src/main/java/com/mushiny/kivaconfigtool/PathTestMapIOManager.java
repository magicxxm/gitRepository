package com.mushiny.kivaconfigtool;

import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.MapCell;
import com.mushiny.rcs.listener.MapIOListener;
import com.mushiny.kiva.map.MapViewCommonOperation;
import com.mushiny.kiva.map.MapWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;

/**
 *
 * @author aricochen
 */
public class PathTestMapIOManager extends MapViewCommonOperation implements MapIOListener {

    private static Logger LOG = LoggerFactory.getLogger(PathTestMapIOManager.class.getName());
    private MapWindow mapWindow;
    private LinkedList<CellNode> selectedCellPathList = new LinkedList();

    public PathTestMapIOManager(MapWindow mapWindow) {
        this.mapWindow = mapWindow;
        installMapWindow(mapWindow);
        mapWindow.installMapIOListener(this);
        mapWindow.registerMapViewChangedListener(this);
    }

    public void clearPath() {
        for (CellNode mapCell : selectedCellPathList) {
            mapCell.setSelected(false);
            mapCell.getSeriesPath().clear();
            resetSelectedCellAsPath(mapCell);
        }
        getSelectedCellPathList().clear();
    }

    public void mousePressed(MouseEvent e) {
        if (mapWindow.getMapModel() == mapWindow.VIEW_MODEL) {//地图处于非编辑状态
            return;
        }
        Point screenP = e.getPoint();
        MapCell cell = mapWindow.getMap().getMapCellByScreenPoint(screenP);
        if (cell != null) {
            if (!cell.isWalkable()) {
                return;
            }
            if (!selectedCellPathList.contains(cell)) {
                getSelectedCellPathList().addLast(cell);
                cell.setSelected(true);
                selectedCellAsPath(cell);
            } else {
                getSelectedCellPathList().remove(cell);
                cell.setSelected(false);
                resetSelectedCellAsPath(cell);
            }
        }
    }

    /*
     选择指定的CELL作为路径
     */
    public void selectedCellAsPath(CellNode selectedCell) {
        updateCellNodeView(selectedCell);
        Toolkit.getDefaultToolkit().beep();
    }

    /*
      取消指定的CELL作为路径点的资格
     */
    public void resetSelectedCellAsPath(CellNode noSelectedCell) {
        updateCellNodeView(noSelectedCell);
    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseDragged(MouseEvent e) {

    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void onZoom(AffineTransform transform) {

    }

    @Override
    public void onFlip(int x, int y, double theta) {

    }

    @Override
    public void onLoadingMapData() {
        clearPath();
    }

    /**
     * @return the selectedCellPathList
     */
    public LinkedList<CellNode> getSelectedCellPathList() {
        return selectedCellPathList;
    }

}
