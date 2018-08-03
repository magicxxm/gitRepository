/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.listener;

import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.path.SeriesPath;

/**
 * 格子监听
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public interface CellListener {

    public void OnCellWalkable(CellNode cellNode);

    public void OnCellUnWalkable(CellNode cellNode);

    public void OnCellUnLocked(CellNode unLockedCellNode);

    public void OnCellLocked(CellNode lockedCellNode);

    public void OnCellInSeriesPath(CellNode cellNode, SeriesPath globalSeriesPath);

    public void OnCellNoInSeriesPath(CellNode cellNode, SeriesPath globalSeriesPath);

    public void OnCellCommonUpdate(CellNode cellNode);//一般的普通信息更新时调用
}
