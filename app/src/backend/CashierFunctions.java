package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import models.MenuItem;

/**
 * Functions that are needed for cashier including managing orders
 * 
 * @author Nicholas Griffin
 * @author Daniel Warren
 * @author Joseph Dillard
 * @author Keshav Dharshan
 * @author Richard Huynh
 */
public class CashierFunctions {
    private double total;
    private int cashierID;
    private List<MenuItem> itemsOrdered;
    private Connection conn;

    /**
     * Cashier constructor.
     * 
     * @param conn The database connection
     * @param cashierID The ID of the cashier
     */
    public CashierFunctions(Connection conn, int cashierID) {
        this.conn = conn;
        this.cashierID = cashierID;
        this.itemsOrdered = new ArrayList<>();
        this.total = 0.0;
    }

    /**
     * Returns list of items ordered.
     * 
     * @return The list of items ordered
     */
    public List<MenuItem> getItemsOrdered() {
        return itemsOrdered;
    }

    /**
     * Returns menu items from the database.
     * 
     * @return A list of all menu items
     * @throws SQLException If there's an error retrieving menu items
     */
    public List<MenuItem> getMenuItems() throws SQLException {
        List<MenuItem> menuItems = new ArrayList<>();
        String query = "SELECT menuID, menuName, itemType, price FROM MenuItem";
    
        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet result = pstmt.executeQuery()) {           
            while (result.next()) {
                int menuID = result.getInt("menuID");
                String menuName = result.getString("menuName");
                String itemType = result.getString("itemType");
                double price = result.getDouble("price");
                menuItems.add(new MenuItem(menuID, menuName, itemType, price));
            }
        }
    
        return menuItems;
    }
    
    /**
     * Gets price for specific menu item.
     * 
     * @param menuItemID The ID of the menu item
     * @return The price of the menu item
     * @throws SQLException If the menu item is not found
     */
    public double getPrice(int menuItemID) throws SQLException {
        String query = "SELECT price FROM MenuItem WHERE menuID = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, menuItemID);
            try (ResultSet result = pstmt.executeQuery()) {           
                if (result.next()) {
                    return result.getDouble("price");
                }
            }
        }
        throw new SQLException("Menu item not found: " + menuItemID);
    }

    /**
     * Removes item from current order.
     * 
     * @param item The menu item to remove from the order
     */
    public void removeFromOrder(MenuItem item) {
        itemsOrdered.remove(item);
    }

    /**
     * Returns subtotal of current order.
     * 
     * @return The subtotal of the current order
     */
    public double getSubtotal() {
        total = 0;
        
        for (MenuItem item : itemsOrdered) {
            total += item.getPrice();
        }
        return total;
    }

    /**
     * Returns the taxes for current order.
     * 
     * @return The tax amount for the current order
     */
    public double getTaxes() {
        return getSubtotal() * 0.08;
    }

    /**
     * Returns total for current order.
     * 
     * @return The total amount for the current order, including taxes
     */
    public double getTotal() {
        return getSubtotal() + getTaxes();
    }

    /**
     * Adds item to current order.
     * 
     * @param item The menu item to add to the order
     */
    public void addToOrder(MenuItem item) {
        itemsOrdered.add(item);
    }

    /**
     * Submits and processes order. Updates associated inventory 
     * item quantities in the inventory database.
     * 
     * @param items The list of menu items to submit as an order
     * @throws SQLException If there's an error submitting the order
     * @throws IllegalStateException If the order is empty
     */
    public void submitOrder(List<MenuItem> items) throws SQLException {
        if (items.isEmpty()) {
            throw new IllegalStateException("Cannot submit an empty order");
        }
    
        conn.setAutoCommit(false);
        try {
            String orderQuery = "INSERT INTO Orders (orderTime, orderDate, employeeID, orderPrice, paymentMethod) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS)) {
                Timestamp now = new Timestamp(new Date().getTime());
                pstmt.setTimestamp(1, now);
                pstmt.setDate(2, new java.sql.Date(now.getTime()));
                pstmt.setInt(3, cashierID);
                pstmt.setDouble(4, getTotal());
                pstmt.setString(5, "Cash");
                updateInventory();
    
                pstmt.executeUpdate();
    
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int orderID = generatedKeys.getInt(1);
    
                        String orderMenuItemQuery = "INSERT INTO OrderMenuItem (orderID, menuItemID) VALUES (?, ?)";
                        try (PreparedStatement orderMenuItemStmt = conn.prepareStatement(orderMenuItemQuery)) {
                            for (MenuItem item : items) {
                                orderMenuItemStmt.setInt(1, orderID);
                                orderMenuItemStmt.setInt(2, item.getMenuID());
                                orderMenuItemStmt.addBatch();
                            }
                            orderMenuItemStmt.executeBatch();
                        }
                    } else {
                        throw new SQLException("Creating order failed, no ID obtained.");
                    }
                }
            }
    
            conn.commit();
    
            items.clear();
            total = 0.0;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    /**
     * Updates the inventory quantities by subtracting the quantities used in each menu item.
     * For each MenuItem in the list, this method will update the associated
     * inventory records, reducing the current inventory by the specified quantity.
     */
    public void updateInventory() {
        String orderQuery = "UPDATE inventory SET quantityinstock = inventory.quantityinstock - mi.quantityused FROM menuiteminventory mi WHERE inventory.inventoryid = mi.inventoryid AND mi.menuid = ?;";

        try (PreparedStatement preparedStatement = conn.prepareStatement(orderQuery)) {
            for (MenuItem item : itemsOrdered) {
                // Set the menuid for each menu item
                preparedStatement.setInt(1, item.getMenuID());

                // Execute the update query
                int rowsAffected = preparedStatement.executeUpdate();
                conn.commit();
                
                if (rowsAffected > 0) {
                    System.out.println("Inventory updated for menu item: " + item.getMenuID());
                } else {
                    System.out.println("No inventory update for menu item: " + item.getMenuID());
                }
            }
        } catch (SQLException e) {
            // Handle SQL exception (e.g., log the error)
            System.err.println("Error updating inventory: " + e.getMessage());
        }
    }

    /**
     * Fetches the name from te menu item id.
     * 
     * @param menuItemID The ID of the menu item
     * @return The name of the menu item
     * @throws SQLException If the menu item is not found
     */
    public String getMenuItemName(int menuItemID) throws SQLException {
        String query = "SELECT menuName FROM MenuItem WHERE menuID = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, menuItemID);
            try (ResultSet result = pstmt.executeQuery()) {           
                if (result.next()) {
                    return result.getString("menuName");
                }
            }
        }
        throw new SQLException("Menu item not found: " + menuItemID);
    }
}