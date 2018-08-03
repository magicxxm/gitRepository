package com.mushiny.wms.masterdata.obbasics.business.order.util.boxpacker;

import java.util.ArrayList;
import java.util.List;

/**
 * Fit items into container, i.e. perform bin packing to a single container.
 * <p>
 * Thread-safe implementation.
 */
public class Packager {

    private final Dimension[] containers;

    /**
     * Constructor
     *
     * @param containers Dimensions of supported containers
     */

    public Packager(List<? extends Dimension> containers) {
        this.containers = containers.toArray(new Dimension[containers.size()]);
    }

    /**
     * Return a container which holds all the items in the argument
     *
     * @return null if no match
     */

    public Container pack(List<Item> items) {
        int volume = 0;
        for (Item item : items) {
            volume += item.getVolume();
        }

        items:
        for (Dimension containerBox : containers) {
            if (containerBox.getVolume() < volume) {
                continue;
            }

            for (Item item : items) {
                if (!containerBox.canHold(item)) {
                    continue items;
                }
            }

            List<Item> containerProducts = new ArrayList<>(items);

            Container holder = new Container(containerBox);

            while (!containerProducts.isEmpty()) {
                // choose the item with the largest surface area, that fits
                // if the same then the one with minimum height
                Item space = holder.getRemainingFreeSpace(containerBox);

                Item currentBox = null; // i
                for (Item item : containerProducts) {
                    if (item.rotateLargestFootprint(space)) {
                        if (currentBox == null) {
                            currentBox = item;
                        } else if (currentBox.getFootprint() < item.getFootprint()) {
                            currentBox = item;
                        } else if (currentBox.getFootprint() == item.getFootprint() && currentBox.getHeight() > item.getHeight()) {
                            currentBox = item;
                        }
                    }
                }

                if (currentBox == null) {
                    continue items;
                }

                holder.addLevel();
                holder.add(currentBox);
                containerProducts.remove(currentBox);

                // currentitem should have the optimal orientation already
                if (containerProducts.isEmpty()) {
                    break;
                }

                Dimension freeSpace = new Dimension(containerBox.getWidth(), containerBox.getDepth(), currentBox.getHeight()); // ak, bk, ck

                fit(containerProducts, holder, currentBox, freeSpace);
            }

            return holder;
        }

        return null;
    }

    private void fit(List<Item> containerProducts, Container holder, Item usedSpace, Dimension freespace) {
        Dimension r1 = new Dimension(freespace.getWidth() - usedSpace.getWidth(), freespace.getDepth(), freespace.getHeight()); // right
        Dimension t1 = new Dimension(freespace.getWidth(), freespace.getDepth() - usedSpace.getDepth(), freespace.getHeight()); // top

        Dimension r2 = new Dimension(freespace.getWidth() - usedSpace.getDepth(), freespace.getDepth(), freespace.getHeight()); // right
        Dimension t2 = new Dimension(freespace.getWidth(), freespace.getDepth() - usedSpace.getWidth(), freespace.getHeight()); // top

        List<Dimension> spaces = new ArrayList<>();
        if (!r1.isEmpty()) {
            spaces.add(r1);
        }
        if (!t1.isEmpty()) {
            spaces.add(t1);
        }
        if (!r2.isEmpty()) {
            spaces.add(r2);
        }
        if (!t2.isEmpty()) {
            spaces.add(t2);
        }

        if (spaces.isEmpty()) {
            // no items
            return;
        }
        Item nextBox = bestVolume(containerProducts, spaces);

        if (nextBox == null) {
            // no fit
            return;
        }

        nextBox.rotateSmallestFootprint(freespace);

        holder.add(nextBox);
        containerProducts.remove(nextBox);

        if (nextBox.getFootprint() == freespace.getFootprint()) {
            // no more space left
            return;
        }

        // maximize free space
        Dimension nextFreeSpace = null;

        // determine stacking within free space to maximize leftover area
        for (Dimension space : spaces) {
            Dimension free = getBest(space, nextBox);
            if (free != null) {
                if (nextFreeSpace == null || free.getFootprint() > nextFreeSpace.getFootprint()) {
                    nextFreeSpace = free;
                }
            }
        }

        if (nextFreeSpace == null) {
            // nothing left
            return;
        }

        // so what about the other remaining space?
        Dimension parent = nextFreeSpace.getParent();

        Dimension leftOverFreespace = null;
        if (parent == r1) {
            // free space in depth the used space
            leftOverFreespace = new Dimension(usedSpace.getWidth(), freespace.getDepth() - usedSpace.getDepth(), freespace.getHeight());
        } else if (parent == t1) {
            // free space in width of the used space
            leftOverFreespace = new Dimension(freespace.getWidth() - usedSpace.getWidth(), usedSpace.getDepth(), freespace.getHeight());
        } else if (parent == r2) {
            // free space in depth the used space (rotated used space)
            leftOverFreespace = new Dimension(usedSpace.getDepth(), freespace.getDepth() - usedSpace.getWidth(), freespace.getHeight());
        } else if (parent == t2) {
            // free space in width of the used space (rotated used space)
            leftOverFreespace = new Dimension(freespace.getWidth() - usedSpace.getDepth(), usedSpace.getWidth(), freespace.getHeight());
        } else {
            throw new IllegalArgumentException();
        }
        fitNext(containerProducts, holder, nextFreeSpace);

        fitNext(containerProducts, holder, leftOverFreespace);
    }

    private void fitNext(List<Item> containerProducts, Container holder, Dimension nextFreeSpace) {
        if (!nextFreeSpace.isEmpty()) {
            Item nextUsedSpace = bestVolume(containerProducts, nextFreeSpace);

            if (nextUsedSpace != null) {
                holder.add(nextUsedSpace);
                containerProducts.remove(nextUsedSpace);

                nextUsedSpace.rotateSmallestFootprint(nextFreeSpace);

                fit(containerProducts, holder, nextUsedSpace, nextFreeSpace);
            }
        }
    }


    private Item bestVolume(List<Item> containerProducts, Dimension space) {
        Item best = null;
        for (Item item : containerProducts) {

            if (item.canFitInside(space)) {
                if (best == null) {
                    best = item;
                } else if (best.getVolume() < item.getVolume()) {
                    best = item;
                } else if (best.getVolume() == item.getVolume()) {
                    // determine lowest fit
                    best.rotateSmallestFootprint(space);

                    item.rotateSmallestFootprint(space);

                    if (item.getFootprint() < best.getFootprint()) {
                        best = item;
                    }
                }
            }
        }

        return best;
    }

    private Item bestVolume(List<Item> containerProducts, List<Dimension> spaces) {
        Item best = null;
        for (Dimension space : spaces) {
            for (Item item : containerProducts) {
                if (item.canFitInside(space)) {
                    if (best == null) {
                        best = item;
                    } else if (best.getVolume() < item.getVolume()) {
                        best = item;
                    } else if (best.getVolume() == item.getVolume()) {
                        // determine lowest fit
                        best.rotateSmallestFootprint(space);

                        item.rotateSmallestFootprint(space);

                        if (item.getFootprint() < best.getFootprint()) {
                            best = item;
                        }
                    }
                }
            }
        }

        return best;
    }

    private Dimension getBest(Dimension freespace, Item item) {
        Dimension alignment1 = null;
        if (item.fitsInside(freespace)) {
            if ((freespace.getDepth() - item.getDepth()) * freespace.getWidth() > (freespace.getWidth() - item.getWidth()) * freespace.getDepth()) {
                alignment1 = new Dimension(freespace, freespace.getWidth(), freespace.getDepth() - item.getDepth(), freespace.getHeight());
            } else {
                alignment1 = new Dimension(freespace, freespace.getWidth() - item.getWidth(), freespace.getDepth(), freespace.getHeight());
            }
        }

        Dimension alignment2 = null;
        item.rotate2D();
        if (item.fitsInside(freespace)) {
            if ((freespace.getDepth() - item.getDepth()) * freespace.getWidth() > (freespace.getWidth() - item.getWidth()) * freespace.getDepth()) {
                alignment2 = new Dimension(freespace, freespace.getWidth(), freespace.getDepth() - item.getDepth(), freespace.getHeight());
            } else {
                alignment2 = new Dimension(freespace, freespace.getWidth() - item.getWidth(), freespace.getDepth(), freespace.getHeight());
            }
        }

        if (alignment1 != null && alignment2 != null) {
            if (alignment1.getFootprint() > alignment2.getFootprint()) {
                return alignment1;
            } else {
                return alignment2;
            }
        } else if (alignment1 != null) {
            return alignment1;
        } else if (alignment2 != null) {
            return alignment2;
        }

        return null;
    }
}
