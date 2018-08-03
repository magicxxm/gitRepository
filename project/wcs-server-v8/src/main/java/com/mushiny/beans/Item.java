package com.mushiny.beans;

/**
 * Created by Tank.li on 2017/6/26.
 */
public class Item implements java.io.Serializable{
    private String itemId;
    private String itemName;
    private int amount;
    private String uniType;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUniType() {
        return uniType;
    }

    public void setUniType(String uniType) {
        this.uniType = uniType;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", amount=" + amount +
                ", uniType='" + uniType + '\'' +
                '}';
    }
}
