package com.mushiny.wcs.application.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/12/29.
 */
public class Path2 {
    private Integer current;
    private List<Path2> next=new ArrayList<>();

    public Path2(int current) {
        this.current = current;
    }

    public List<Path2> getNext() {
        return next;
    }

    public void setNext(List<Path2> next) {
        this.next = next;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }


}
