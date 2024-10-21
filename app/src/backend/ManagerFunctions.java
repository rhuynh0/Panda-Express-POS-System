package backend;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import models.Employee;
import models.Inventory;
import models.InventoryUsage;
import models.MenuItem;

/**
 * Functions that are needed for a manager including getting and setting databases
 * 
 * @author Nicholas Griffin
 * @author Daniel Warren
 * @author Joseph Dillard
 * @author Keshav Dharshan
 * @author Richard Huynh
 */
public class ManagerFunctions {
    private final Connection conn;

    /**
     * Manager constructor.
     * 
     * @param conn The database connection
     */
    public ManagerFunctions(Connection conn) {
        this.conn = conn;
    }

    /**
     * Sets the price of a menu item in the database.
     * 
     * @param menuItem The name of the menu item
     * @param price The new price for the menu item
     */
    public void setPrice(String menuItem, double price) {
        String query = "UPDATE menuitem SET price = ? WHERE menuname = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDouble(1, price);
            pstmt.setString(2, menuItem);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Menu item not found: " + menuItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches price for specific menu item.
     * 
     * @param id The ID of the menu item
     * @return The price of the menu item
     * @throws SQLException If the menu item is not found
     */
    public double getPrice(int id) throws SQLException {
        String query = "SELECT price FROM MenuItem WHERE menuID = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet result = pstmt.executeQuery()) {           
                if (result.next()) {
                    return result.getDouble("price");
                }
            }
        }
        throw new SQLException("Menu item not found: " + id);
    }

    /**
     * Returns list of menu items.
     * 
     * @return A list of all menu items
     * @throws SQLException If there's an error retrieving menu items
     */
    public List<MenuItem> getMenuItems() throws SQLException {
        List<MenuItem> menuItems = new ArrayList<>();
        String query = "SELECT menuID, menuName, price, itemType FROM MenuItem";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet result = pstmt.executeQuery()) {
            while (result.next()) {
                int id = result.getInt("menuID");
                String name = result.getString("menuName");
                double price = result.getDouble("price");
                String category = result.getString("itemType");
                MenuItem menuItem = new MenuItem(id, name, category, price);
                menuItems.add(menuItem);
            }
        }
        return menuItems;
    }
    
    /**
     * Sets new information for menu items.
     * 
     * @param menuName The new name of the menu item
     * @param type The new type of the menu item
     * @param price The new price of the menu item
     */
    public void setMenuItem(String menuName, String type, double price) {
        String query = "UPDATE menuitem SET price = ?, itemType = ? WHERE menuname = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDouble(1, price);
            pstmt.setString(2, type);
            pstmt.setString(3, menuName);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Menu item not found: " + menuName); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds menu item to database.
     * 
     * @param menuName The name of the new menu item
     * @param type The type of the new menu item
     * @param price The price of the new menu item
     */
    public void addMenuItem(String menuName, String type, double price) {
        String query = "INSERT INTO menuitem (menuName, itemType, price) VALUES (?, ?, ?)";
    
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, menuName);
            pstmt.setString(2, type);
            pstmt.setDouble(3, price);
    
            int affectedRows = pstmt.executeUpdate();
    
            if (affectedRows == 0) {
                throw new SQLException("Failed to add menu item: " + menuName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes menu item from database.
     * 
     * @param menuname The name of the menu item to remove
     */
    public void removeMenuItem(String menuname) {
        String query = "DELETE FROM MenuItem WHERE menuname = ?";
    
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, menuname);
    
            int affectedRows = pstmt.executeUpdate(); 
    
            if (affectedRows == 0) {
                throw new SQLException("No menu item found with menuname: " + menuname);  
            }
        } catch (SQLException e) {
            e.printStackTrace();  
        }
    }

    /**
     * Gets inventory items from database.
     * 
     * @return A list of all inventory items
     * @throws SQLException If there's an error retrieving inventory items
     */
    public List<Inventory> getInventoryItems() throws SQLException {
        List<Inventory> inventoryList = new ArrayList<>();
        String query = "SELECT inventoryID, itemName, quantityInStock, quantityUnits, reorderThreshold, unitPrice FROM Inventory";

        try (PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet result = pstmt.executeQuery()) {
            while (result.next()) {
                int inventoryID = result.getInt("inventoryID");
                String itemName = result.getString("itemName");
                double quantityInStock = result.getDouble("quantityInStock");
                String quantityUnits = result.getString("quantityUnits");
                double reorderThreshold = result.getDouble("reorderThreshold");
                double unitPrice = result.getDouble("unitPrice");

                Inventory inventoryItem = new Inventory(inventoryID, itemName, quantityInStock, quantityUnits, reorderThreshold, unitPrice);
                inventoryList.add(inventoryItem);
            }
        }
        return inventoryList;
    }

    /**
     * Gets in stock quantity from item in database.
     * 
     * @param inventoryId The ID of the inventory item
     * @return The quantity in stock of the inventory item
     * @throws SQLException If the inventory item is not found
     */
    public int getInventory(int inventoryId) throws SQLException {
        String query = "SELECT quantityInStock FROM Inventory WHERE inventoryID = ?"; 
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, inventoryId);
            try (ResultSet result = pstmt.executeQuery()) {           
                if (result.next()) {
                    return result.getInt("quantityInStock");
                }
            }
        }
        throw new SQLException("Inventory item not found: " + inventoryId);
    }

    /**
     * Sets new information for inventory items.
     * 
     * @param itemName The new name of the inventory item
     * @param quantityInStock The new quantity in stock
     * @param quantityUnits The new quantity units
     * @param reorderThreshold The new reorder threshold
     * @param unitPrice The new unit price
     */
    public void setInventory(String itemName, double quantityInStock, String quantityUnits, double reorderThreshold, double unitPrice) {
        String query = "UPDATE Inventory SET quantityinstock = ?, quantityunits = ?, reorderthreshold = ?, unitprice = ? WHERE itemname = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDouble(1, quantityInStock);
            pstmt.setString(2, quantityUnits);
            pstmt.setDouble(3, reorderThreshold);
            pstmt.setDouble(4, unitPrice); 
            pstmt.setString(5, itemName);

            int affectedRows = pstmt.executeUpdate();
            conn.commit();

            if (affectedRows == 0) {
                throw new SQLException("Inventory item not found: " + itemName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes item from inventory.
     * 
     * @param itemname The name of the inventory item to remove
     */
    public void removeInventory(String itemname) {
        String query = "DELETE FROM inventory WHERE itemname = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, itemname);
    
            int affectedRows = pstmt.executeUpdate(); 
    
            if (affectedRows == 0) {
                throw new SQLException("No menu item found with menu name: " + itemname);  
            }
        } catch (SQLException e) {
            e.printStackTrace();  
        }

    }


    /**
     * Adds new item to inventory database.
     * 
     * @param itemName The name of the new inventory item
     * @param quantityInStock The quantity in stock of the new item
     * @param quantityUnits The quantity units of the new item
     * @param reorderThreshold The reorder threshold of the new item
     * @param unitPrice The unit price of the new item
     */
    public void addInventory(String itemName, double quantityInStock, String quantityUnits, double reorderThreshold, double unitPrice) {
        String query = "INSERT INTO Inventory (itemname, quantityinstock, quantityunits, reorderthreshold, unitprice) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, itemName);
            pstmt.setDouble(2, quantityInStock);
            pstmt.setString(3, quantityUnits);
            pstmt.setDouble(4, reorderThreshold);
            pstmt.setDouble(5, unitPrice);
        
            int affectedRows = pstmt.executeUpdate();
        
            if (affectedRows > 0) {
                System.out.println("Inventory item added successfully!");
            } else {
                System.out.println("No inventory item was added.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets employee from database by id.
     * 
     * @param id The ID of the employee
     * @return The Employee object
     * @throws SQLException If the employee is not found
     */
    public Employee getEmployeeById(int id) throws SQLException {
        String query = "SELECT employeeid, employeename, jobtitle, startdate, hourlywage FROM Employees WHERE employeeid = ?";
        Employee employee = null;
    
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet result = pstmt.executeQuery();
    
            if (result.next()) {
                int employeeId = result.getInt("employeeid");
                String employeeName = result.getString("employeename");
                String jobTitle = result.getString("jobtitle");
                Date startDate = result.getDate("startdate");
                double hourlyWage = result.getDouble("hourlywage");
                employee = new Employee(employeeId, employeeName, jobTitle, startDate, hourlyWage);
            }
        }
    
        return employee;
    }

    /**
     * Returns list of employees from database.
     * 
     * @return A list of all employees
     * @throws SQLException If there's an error retrieving employees
     */
    public List<Employee> getEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT employeeid, employeename, jobtitle, startdate, hourlywage FROM Employees";

        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet result = pstmt.executeQuery()) {
            while (result.next()) {
                int employeeId = result.getInt("employeeid");
                String employeeName = result.getString("employeename");
                String jobTitle = result.getString("jobtitle");
                Date startDate = result.getDate("startdate");
                double hourlyWage = result.getDouble("hourlywage");
                
                employees.add(new Employee(employeeId, employeeName, jobTitle, startDate, hourlyWage));
            }
        }

        return employees;
    }

    /**
     * Sets information for employees.
     * 
     * @param employeeName The new name of the employee
     * @param jobTitle The new job title of the employee
     * @param startDate The new start date of the employee
     * @param hourlyWage The new hourly wage of the employee
     */
    public void setEmployee(String employeeName, String jobTitle, Date startDate, double hourlyWage) {
        String query = "UPDATE Employees SET jobtitle = ?, startdate = ?, hourlywage = ? WHERE employeename = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, jobTitle);
            pstmt.setDate(2, startDate);
            pstmt.setDouble(3, hourlyWage);
            pstmt.setString(4, employeeName);
    
            int affectedRows = pstmt.executeUpdate();
    
            if (affectedRows == 0) {
                throw new SQLException("Employee not found: " + employeeName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds employee to database.
     * 
     * @param employeeName The name of the new employee
     * @param jobTitle The job title of the new employee
     * @param startDate The start date of the new employee
     * @param hourlyWage The hourly wage of the new employee
     */
    public void addEmployee(String employeeName, String jobTitle, Date startDate, double hourlyWage) {
        String query = "INSERT INTO Employees (employeename, jobtitle, startdate, hourlywage) VALUES (?, ?, ?, ?)";
    
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, employeeName);
            pstmt.setString(2, jobTitle);
            pstmt.setDate(3, startDate);
            pstmt.setDouble(4, hourlyWage);
    
            int affectedRows = pstmt.executeUpdate();
    
            if (affectedRows == 0) {
                throw new SQLException("Failed to add employee: " + employeeName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes employee from database.
     * 
     * @param employeeName The name of the employee to remove
     */
    public void removeEmployee(String employeeName) {
        String query = "DELETE FROM Employees WHERE employeename = ?";
    
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, employeeName);
    
            int affectedRows = pstmt.executeUpdate();
    
            if (affectedRows == 0) {
                throw new SQLException("No employee found with name: " + employeeName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns employees in a specfic shift.
     * 
     * @param shiftId The ID of the shift
     * @return A list of employee information for the given shift
     */
    public List<String> getEmployeesForShift(int shiftId) {
        List<String> employees = new ArrayList<>();
        String query = "SELECT e.employeeid, e.employeename, e.jobtitle, e.hourlywage " +
                       "FROM Employees e " +
                       "JOIN EmployeeShifts es ON e.employeeid = es.employeeid " +
                       "WHERE es.shiftid = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, shiftId);
            
            ResultSet result = pstmt.executeQuery();
            while (result.next()) {
                int employeeId = result.getInt("employeeid");
                String employeeName = result.getString("employeename");
                String jobTitle = result.getString("jobtitle");
                double hourlyWage = result.getDouble("hourlywage");
                
                String employeeInfo = String.format("ID: %d, Name: %s, Job Title: %s, Wage: $%.2f", 
                                                    employeeId, employeeName, jobTitle, hourlyWage);
                employees.add(employeeInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return employees;
    }
    
    /**
     * Gets shift ID from time and date.
     * 
     * @param shiftDate The date of the shift
     * @param shiftStartTime The start time of the shift
     * @return The shift ID, or null if not found
     */
    public Integer getShiftID(LocalDate shiftDate, String shiftStartTime) {
        String query = "SELECT shiftid FROM shifts WHERE shiftdate = ? AND shiftStartTime = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDate(1, java.sql.Date.valueOf(shiftDate));
            pstmt.setTime(2, java.sql.Time.valueOf(shiftStartTime));
            
            ResultSet result = pstmt.executeQuery();
            
            if (result.next()) {
                return result.getInt("shiftid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Returns employee hours in list.
     * 
     * @param startDate The start date of the report period
     * @param endDate The end date of the report period
     * @return A list of employee hours data
     */
    public List<String[]> getEmployeeHoursReport(Date startDate, Date endDate) {
        List<String[]> employeeHoursData = new ArrayList<>();
    
        String query = "SELECT e.employeeid, e.employeename, SUM(EXTRACT(EPOCH FROM (s.shiftendtime - s.shiftstarttime)) / 3600) AS total_hours " +
                       "FROM EmployeeShifts es " +
                       "JOIN Employees e ON es.employeeid = e.employeeid " +
                       "JOIN Shifts s ON es.shiftid = s.shiftid " +
                       "WHERE s.shiftdate BETWEEN ? AND ? " +
                       "GROUP BY e.employeeid, e.employeename";
    
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDate(1, startDate);
            pstmt.setDate(2, endDate);
    
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String employeeId = rs.getString("employeeid");
                String employeeName = rs.getString("employeename");
                String totalHours = String.format("%.2f", rs.getDouble("total_hours"));
    
                employeeHoursData.add(new String[] { employeeId, employeeName, totalHours });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return employeeHoursData;
    }
    
    /**
     * Gets sales report for a specific time frame.
     * 
     * @param startDate The start date of the report period
     * @param endDate The end date of the report period
     * @return A list of sales report data by item
     */
    public List<String> getSalesReportByItem(Date startDate, Date endDate) {
        String query = "SELECT mi.menuname, COUNT(omi.menuitemid) AS totalQuantity, SUM(o.orderprice) AS totalSales " +
                   "FROM Orders o " +
                   "JOIN OrderMenuItem omi ON o.orderid = omi.orderid " +
                   "JOIN MenuItem mi ON omi.menuitemid = mi.menuid " +
                   "WHERE o.orderdate >= ? AND o.orderdate < ? " +
                   "GROUP BY mi.menuid, mi.menuname " +
                   "ORDER BY totalQuantity DESC";
        List<String> returnArray = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDate(1, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(2, new java.sql.Date(endDate.getTime()));           
    
            try (ResultSet rs = pstmt.executeQuery()) {
                returnArray.add("Sales Report by Item from " + startDate + " to " + endDate + "\n");
                returnArray.add("--------------------------------------------------------------\n");
                returnArray.add(String.format("%-30s %-15s %-15s", "Item ID", "Quantity Sold", "Total Sales ($)\n"));
                returnArray.add("--------------------------------------------------------------\n");
    
                while (rs.next()) {
                    String menuName = rs.getString("menuname");
                    int totalQuantity = rs.getInt("totalQuantity");
                    double totalSales = rs.getDouble("totalSales");
    
                    returnArray.add(String.format("%-30s %-15d %-15.2f\n", menuName, totalQuantity, totalSales));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnArray;
    }

    /**
     * Returns a list of popular items in a specific time period.
     * 
     * @param startDate The start date of the report period
     * @param endDate The end date of the report period
     * @return A list of menu items by popularity
     */
    public List<String> getMenuItemsPopularityAnalysis(Date startDate, Date endDate) {
        String query = "SELECT mi.menuname, COUNT(omi.menuitemid) AS totalQuantity " +
                       "FROM Orders o " +
                       "JOIN OrderMenuItem omi ON o.orderid = omi.orderid " +
                       "JOIN MenuItem mi ON omi.menuitemid = mi.menuid " +
                       "WHERE o.orderdate >= ? AND o.orderdate < ? " +
                       "GROUP BY mi.menuid, mi.menuname " +
                       "ORDER BY totalQuantity DESC";
        List<String> report = new ArrayList<>();
    
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDate(1, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(2, new java.sql.Date(endDate.getTime()));
    
            try (ResultSet rs = pstmt.executeQuery()) {
                // Add header for the report
                report.add("Menu Items Popularity Analysis from " + startDate + " to " + endDate + "\n");
                report.add("----------------------------------------------------");
                report.add(String.format("%-30s %-15s", "Item Name", "Quantity Sold"));
                report.add("----------------------------------------------------");
    
                // Loop through each result and format it
                while (rs.next()) {
                    String menuName = rs.getString("menuname");
                    int totalQuantity = rs.getInt("totalQuantity");
    
                    report.add(String.format("%-30s %-15d", menuName, totalQuantity));
                }
    
                // Add footer if no results
                if (report.size() == 4) { // Only headers are present
                    report.add("No popularity data found for the specified period.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return report;
    }

    /**
     * Returns a list of inventory usage in a time frame.
     * 
     * @param startDate The start date of the report period
     * @param endDate The end date of the report period
     * @return A list of inventory usage
     */
    public List<InventoryUsage> getInventoryUsage(Date startDate, Date endDate) {
        String query = "SELECT i.itemname, SUM(mii.quantityUsed) AS totalQuantity, i.quantityUnits " +
                       "FROM Orders o " +
                       "JOIN OrderMenuItem omi ON o.orderid = omi.orderid " +
                       "JOIN MenuItemInventory mii ON omi.menuItemID = mii.menuID " +
                       "JOIN Inventory i ON mii.inventoryid = i.inventoryid " +
                       "WHERE o.orderdate >= ? AND o.orderdate < ? " +
                       "GROUP BY i.inventoryid, i.itemname, i.quantityUnits";
        List<InventoryUsage> inventoryUsage = new ArrayList<>();
    
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDate(1, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(2, new java.sql.Date(endDate.getTime()));
    
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String itemName = rs.getString("itemname");
                    float totalQuantity = rs.getFloat("totalQuantity");
                    String units = rs.getString("quantityUnits");
    
                    inventoryUsage.add(new InventoryUsage(itemName, totalQuantity, units));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return inventoryUsage;
    }

    /**
     * Returns a list of sales numbers by hour
     * 
     * @return A list of sales numbers by hour
     */
    public List<String> getXReport() {
        String query = "SELECT EXTRACT(HOUR FROM ordertime) AS hour, " +
                       "COUNT(orderid) AS totalOrders, " +
                       "SUM(orderprice) AS totalSales " +
                       "FROM Orders " +
                       "WHERE orderdate = ? " +
                       "GROUP BY EXTRACT(HOUR FROM ordertime) " +
                       "ORDER BY hour";
        List<String> report = new ArrayList<>();
        Date today = Date.valueOf(LocalDate.now());
    
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDate(1, today);
            try (ResultSet rs = pstmt.executeQuery()) {
                int grandTotalOrders = 0;
                double grandTotalSales = 0.0;
                while (rs.next()) {
                    int hour = rs.getInt("hour");
                    int totalOrders = rs.getInt("totalOrders");
                    double totalSales = rs.getDouble("totalSales");
                    grandTotalOrders += totalOrders;
                    grandTotalSales += totalSales;
                    
                    String hourRange = String.format("%02d:00 - %02d:59", hour, hour);
                    report.add(String.format("%-15s Orders: %-5d Sales: $%.2f", hourRange, totalOrders, totalSales));
                }
                // Add grand total at the end
                report.add(String.format("\nGrand Total:   Orders: %-5d Sales: $%.2f", grandTotalOrders, grandTotalSales));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            report.add("Error generating X Report: " + e.getMessage());
        }
        return report;
    }

    /**
     * Returns a string that shows full day sales data.
     * 
     * @return A list of menu items by popularity
     */
    public String getZReport(){
        String query = "SELECT COUNT(orderid) AS totalOrders, SUM(orderprice) AS totalSales FROM Orders WHERE orderdate = ?";
        String report = "";
        Date today = Date.valueOf(LocalDate.now());

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDate(1, today);
            try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                int totalOrders = rs.getInt("totalOrders");
                double totalSales = rs.getDouble("totalSales");
                report = String.format("Total Orders: %d, Total Sales: $%.2f", totalOrders, totalSales);
            }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return report;
    }
}