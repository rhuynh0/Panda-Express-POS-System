package models;

    /**
     * The Inventory class represents an item in the inventory system.
     * It holds details about the item's ID, name, quantity in stock, 
     * unit of measurement, reorder threshold, and price per unit.
     * 
     * This class provides getter and setter methods to access and modify the inventory details.
     * It also overrides the toString() method to provide a string representation of the inventory item.
     * 
     * @author Nicholas Griffin
     * @author Daniel Warren
     * @author Joseph Dillard
     * @author Keshav Dharshan
     * @author Richard Huynh
     */
public class Inventory {
    private int inventoryID;
    private String itemname;
    private double quantityInStock;
    private String quantityUnits;
    private double reorderThreshold;
    private double unitPrice;

     /**
     * Constructs a new Inventory item with the specified details.
     * 
     * @param inventoryID the unique ID of the inventory item
     * @param itemname the name of the inventory item
     * @param quantityInStock the quantity of the item in stock
     * @param quantityUnits the units in which the quantity is measured
     * @param reorderThreshold the threshold at which the item needs to be reordered
     * @param unitPrice the price per unit of the item
     */
    public Inventory(int inventoryID, String itemname, double quantityInStock, String quantityUnits, double reorderThreshold, double unitPrice) {
        this.inventoryID = inventoryID;
        this.itemname = itemname;
        this.quantityInStock = quantityInStock;
        this.quantityUnits = quantityUnits;
        this.reorderThreshold = reorderThreshold;
        this.unitPrice = unitPrice;
    }

    /**
     * Gets the inventory ID.
     * 
     * @return the inventory ID
     */
    public int getInventoryID() {
        return inventoryID;
    }

    /**
     * Gets the name of the item.
     * 
     * @return the item name
     */
    public String getItemname() {
        return itemname;
    }

    /**
     * Gets the quantity of the item in stock.
     * 
     * @return the quantity in stock
     */
    public double getQuantityInStock() {
        return quantityInStock;
    }

    /**
     * Gets the units in which the quantity is measured.
     * 
     * @return the quantity units
     */
    public String getQuantityUnits() {
        return quantityUnits;
    }   

    /**
     * Gets the reorder threshold.
     * 
     * @return the reorder threshold
     */
    public double getReorderThreshold() {
        return reorderThreshold;
    }

    /**
     * Gets the price per unit of the item.
     * 
     * @return the unit price
     */
    public double getUnitPrice() {
        return unitPrice;
    }

    /**
     * Sets the inventory ID.
     * 
     * @param inventoryID the new inventory ID
     */
    public void setInventoryID(int inventoryID) {
        this.inventoryID = inventoryID;
    }

    /**
     * Sets the name of the item.
     * 
     * @param itemname the new item name
     */
    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    /**
     * Sets the quantity of the item in stock.
     * 
     * @param quantityInStock the new quantity in stock
     */
    public void setQuantityInStock(double quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    /**
     * Sets the units in which the quantity is measured.
     * 
     * @param quantityUnits the new quantity units
     */
    public void setQuantityUnits(String quantityUnits) {
        this.quantityUnits = quantityUnits;
    }

    /**
     * Sets the reorder threshold.
     * 
     * @param reorderThreshold the new reorder threshold
     */
    public void setReorderThreshold(double reorderThreshold) {
        this.reorderThreshold = reorderThreshold;
    }

    /**
     * Sets the price per unit of the item.
     * 
     * @param unitPrice the new unit price
     */
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * Returns a string representation of the inventory item.
     * 
     * @return a string representation of the inventory item
     */
    @Override
    public String toString() {
        return "Inventory{" +
                "itemname='" + itemname + '\'' +
                ", quantityInStock=" + quantityInStock +
                ", quantityUnits='" + quantityUnits + '\'' +
                ", reorderThreshold=" + reorderThreshold +
                ", unitPrice=" + unitPrice +
                '}';
    }
}

