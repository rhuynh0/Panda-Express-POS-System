package models;

public class InventoryUsage {
    private String itemName;
    private float quantityUsed;
    private String units;

    public InventoryUsage(String itemName, float quantityUsed, String units) {
        this.itemName = itemName;
        this.quantityUsed = quantityUsed;
        this.units = units;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public float getQuantityUsed() {
        return quantityUsed;
    }

    public void setQuantityUsed(float quantityUsed) {
        this.quantityUsed = quantityUsed;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    @Override
    public String toString() {
        return "InventoryUsage{" +
                "itemName='" + itemName + '\'' +
                ", quantityUsed=" + quantityUsed +
                ", units='" + units + '\'' +
                '}';
    }
}