package models;

/**
 * The MenuItem class represents an item on a menu.
 * It holds details such as the item's ID, name, type (e.g., appetizer, main course, dessert), and price.
 * 
 * This class provides getter methods to access the details of a menu item,
 * and it overrides the toString() method to return a simple string representation of the item.
 * 
 * @author Nicholas Griffin
 * @author Daniel Warren
 * @author Joseph Dillard
 * @author Keshav Dharshan
 * @author Richard Huynh
 */
public class MenuItem {
    private int menuID;
    private String menuName;
    private String itemType;
    private double price;

    /**
     * Constructs a new MenuItem with the specified details.
     * 
     * @param menuID the unique ID of the menu item
     * @param menuName the name of the menu item
     * @param itemType the type of menu item (e.g., appetizer, main course, dessert)
     * @param price the price of the menu item
     */
    public MenuItem(int menuID, String menuName, String itemType, double price) {
        this.menuID = menuID;
        this.menuName = menuName;
        this.itemType = itemType;
        this.price = price;
    }

    /**
     * Gets the menu item's ID.
     * 
     * @return the menu ID
     */
    public int getMenuID() {
        return menuID;
    }

    /**
     * Gets the name of the menu item.
     * 
     * @return the menu name
     */
    public String getMenuName() {
        return menuName;
    }

    /**
     * Gets the type of menu item (e.g., appetizer, main course, dessert).
     * 
     * @return the item type
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * Gets the price of the menu item.
     * 
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns a string representation of the menu item.
     * 
     * @return a string representation of the menu item
     */
    @Override
    public String toString() {
        return "" + menuName + ": " + price;
    }
}
