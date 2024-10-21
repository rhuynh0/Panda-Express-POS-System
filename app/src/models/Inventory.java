package models;

public class Inventory {
    private int inventoryID;
    private String itemname;
    private double quantityInStock;
    private String quantityUnits;
    private double reorderThreshold;
    private double unitPrice;

    public Inventory(int inventoryID, String itemname, double quantityInStock, String quantityUnits, double reorderThreshold, double unitPrice) {
        this.inventoryID = inventoryID;
        this.itemname = itemname;
        this.quantityInStock = quantityInStock;
        this.quantityUnits = quantityUnits;
        this.reorderThreshold = reorderThreshold;
        this.unitPrice = unitPrice;
    }

    public int getInventoryID() {
        return inventoryID;
    }

    public String getItemname() {
        return itemname;
    }

    public double getQuantityInStock() {
        return quantityInStock;
    }

    public String getQuantityUnits() {
        return quantityUnits;
    }   

    public double getReorderThreshold() {
        return reorderThreshold;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setInventoryID(int inventoryID) {
        this.inventoryID = inventoryID;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public void setQuantityInStock(double quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public void setQuantityUnits(String quantityUnits) {
        this.quantityUnits = quantityUnits;
    }

    public void setReorderThreshold(double reorderThreshold) {
        this.reorderThreshold = reorderThreshold;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

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

