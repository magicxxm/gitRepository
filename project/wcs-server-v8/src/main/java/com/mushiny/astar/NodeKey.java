package com.mushiny.astar;

/**
 * Created by Tank.li on 2017/7/4.
 */
public class NodeKey implements java.io.Serializable{
    private int x;
    private int y;

    public NodeKey(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeKey nodeKey = (NodeKey) o;

        if (x != nodeKey.x) return false;
        return y == nodeKey.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
