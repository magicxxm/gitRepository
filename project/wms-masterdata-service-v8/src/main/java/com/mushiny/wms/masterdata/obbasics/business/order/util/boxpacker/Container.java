package com.mushiny.wms.masterdata.obbasics.business.order.util.boxpacker;

import java.util.ArrayList;
import java.util.List;

public class Container extends Item {

    private int stackHeight = 0;
    private List<Level> levels = new ArrayList<>();

    public Container(Dimension item) {
        super(item.getName(), item.getWidth(), item.getDepth(), item.getHeight());
    }

    public boolean add(Level element) {
        if (!levels.isEmpty()) {
            stackHeight += currentLevelStackHeight();
        }

        return levels.add(element);
    }

    public int getStackHeight() {
        return stackHeight + currentLevelStackHeight();
    }

    public void add(int index, Level element) {
        if (!levels.isEmpty()) {
            stackHeight += currentLevelStackHeight();
        }

        levels.add(index, element);
    }

    public int currentLevelStackHeight() {
        if (levels.isEmpty()) {
            return 0;
        }
        return levels.get(levels.size() - 1).getHeight();
    }

    public void add(Item item) {
        levels.get(levels.size() - 1).add(item);
    }

    public void addLevel() {
        add(new Level());
    }

    public Item getRemainingFreeSpace(Dimension item) {
        int spaceHeight = item.getHeight() - currentLevelStackHeight();
        if (spaceHeight < 0) {
            throw new IllegalArgumentException();
        }
        return new Item(item.getWidth(), item.getDepth(), spaceHeight);
    }

    public String printLevels() {
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < levels.size(); i++) {
            Level level = levels.get(i);
            buffer.append(i + " " + level.size() + " element / " + level.getHeight() + " height");
            buffer.append(level);
            buffer.append("\n");
        }

        return buffer.toString();
    }

    public List<Level> getLevels() {
        return levels;
    }
}
