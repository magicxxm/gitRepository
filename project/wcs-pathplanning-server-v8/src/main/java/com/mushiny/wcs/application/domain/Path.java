package com.mushiny.wcs.application.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/12/27.
 */
public class Path {
    private MapNode current;
    private List<Path> next=new ArrayList<>();

    public Path(MapNode current) {
        this.current = current;
    }

    public MapNode getCurrent() {
        return current;
    }

    public void setCurrent(MapNode current) {
        this.current = current;
    }

    public List<Path> getNext() {
        return next;
    }

    public void setNext(List<Path> next) {
        this.next = next;
    }
}
