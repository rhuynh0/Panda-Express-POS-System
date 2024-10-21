package models;

/**
 * Represents the usage of an item from inventory.
 * This class holds the item name, quantity used, and the units in which the quantity is measured.
 * 
 * @author Nicholas Griffin
 * @author Daniel Warren
 * @author Joseph Dillard
 * @author Keshav Dharshan
 * @author Richard Huynh
 */
public class InventoryUsage {
    private String itemName;
    private float quantityUsed;
    private String units;

    /**
     * Constructs an InventoryUsage object with the specified item name, quantity used, and units.
     *
     * @param itemName      the name of the item used
     * @param quantityUsed  the quantity of the item used
     * @param units         the units in which the quantity is measured
     */
    public InventoryUsage(String itemName, float quantityUsed, String units) {
        this.itemName = itemName;
        this.quantityUsed = quantityUsed;
        this.units = units;
    }

     /**
     * Gets the name of the item.
     *
     * @return the name of the item used
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Sets the name of the item.
     *
     * @param itemName the name of the item to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * Gets the quantity of the item used.
     *
     * @return the quantity of the item used
     */
    public float getQuantityUsed() {
        return quantityUsed;
    }

    /**
     * Sets the quantity of the item used.
     *
     * @param quantityUsed the quantity to set
     */
    public void setQuantityUsed(float quantityUsed) {
        this.quantityUsed = quantityUsed;
    }

    /**
     * Gets the units in which the quantity is measured.
     *
     * @return the units in which the quantity is measured
     */
    public String getUnits() {
        return units;
    }

    /**
     * Sets the units in which the quantity is measured.
     *
     * @param units the units to set
     */
    public void setUnits(String units) {
        this.units = units;
    }

    /**
     * Returns a string representation of the InventoryUsage object.
     *
     * @return a string containing the item name, quantity used, and units
     */
    @Override
    public String toString() {
        return "InventoryUsage{" +
                "itemName='" + itemName + '\'' +
                ", quantityUsed=" + quantityUsed +
                ", units='" + units + '\'' +
                '}';
    }
}