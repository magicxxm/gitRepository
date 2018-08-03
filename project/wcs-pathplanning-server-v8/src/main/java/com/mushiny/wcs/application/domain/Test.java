package com.mushiny.wcs.application.domain;

import com.mushiny.wcs.application.utils.JSONUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/12/29.
 */
public class Test {
    public void buildPath(List<MapNode> stack, Path root, List<String> pathList) {

        if (root != null) {
            stack.add(root.getCurrent());
            if (root.getNext().size() == 0) {
                changeToPath(stack, pathList); // 把值栈中的值转化为路径
            } else {
                List<Path> items = root.getNext();
                for (int i = 0; i < items.size(); i++) {
                    buildPath(stack, items.get(i), pathList);
                }
            }
            stack.remove(stack.size() - 1);
        }
    }

    /**
     * @param path
     * @param pathList
     */
    public void changeToPath(List<MapNode> path, List<String> pathList) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < path.size(); i++) {
            if (path.get(i) != null) {
                sb.append(path.get(i).getAddressCodeId() + " ");
            }

        }
        pathList.add(sb.toString().trim());


    }



}
