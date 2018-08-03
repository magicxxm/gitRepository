package com.mushiny.wcs.application.domain;

import com.mushiny.wcs.application.utils.JSONUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/12/29.
 */
public class Test2 {
    public void buildPath(List<Integer> stack, Path2 root, List<String> pathList) {

        if (root != null) {
            stack.add(root.getCurrent());
            if (root.getNext().size() == 0) {
                changeToPath(stack, pathList); // 把值栈中的值转化为路径
            } else {
                List<Path2> items = root.getNext();
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
    public void changeToPath(List<Integer> path, List<String> pathList) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < path.size(); i++) {
            if (path.get(i) != null) {
                sb.append(path.get(i) + " ");
            }

        }
        pathList.add(sb.toString().trim());


    }

    public static void main(String[] args) {
        Test2 te=new Test2();
        Path2 root=new Path2(1);
        Path2 s1=new Path2(2);
        Path2 s11=new Path2(3);
        root.getNext().add(s1);
        root.getNext().add(s11);
        Path2 s2=new Path2(4);
        Path2 s21=new Path2(5);
        Path2 s23=new Path2(6);
        Path2 s24=new Path2(7);
        Path2 s25=new Path2(8);
        s1.getNext().add(s2);
        s1.getNext().add(s21);
        s11.getNext().add(s23);
        s11.getNext().add(s24);
        s11.getNext().add(s25);

        List<Integer> stack=new ArrayList<>();
        List<String> pathList=new ArrayList<>();
        te.buildPath(stack,root,pathList);
        System.out.println(JSONUtil.toJSon(pathList));

    }

}
