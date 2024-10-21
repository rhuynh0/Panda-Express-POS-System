package models;

public class MenuItem {
    private int menuID;
    private String menuName;
    private String itemType;
    private double price;

    public MenuItem(int menuID, String menuName, String itemType, double price) {
        this.menuID = menuID;
        this.menuName = menuName;
        this.itemType = itemType;
        this.price = price;
    }

    public int getMenuID() {
        return menuID;
    }

    public String getMenuName() {
        return menuName;
    }

    public String getItemType() {
        return itemType;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "" + menuName + ": " + price;
    }
}
