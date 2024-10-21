package frontend;

import backend.CashierFunctions;
import backend.ManagerFunctions;
import backend.dbSetup;
import models.Employee;
import models.Inventory;
import models.InventoryUsage;
import models.MenuItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * POSSystem class represents a Point of Sale (POS) system that allows users 
 * to manage inventory, employees, and shifts while providing cashier and manager functionalities.
 * The system interacts with a database to fetch, add, and update data related to sales, employees, and inventory.
 * 
 * @author Nicholas Griffin
 * @author Daniel Warren
 * @author Joseph Dillard
 * @author Keshav Dharshan
 * @author Richard Huynh
 */
public class POSSystem {
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private Connection conn;
    private CashierFunctions cashier;
    private JLabel subtotalField;
    private JLabel taxesField;
    private JLabel tipField;
    private JLabel totalField;
    private String userName;
    private ManagerFunctions managerFunctions;

    // Create shifts calendar of manager POS
    private JPanel shiftsCalendarPanel;
    private LocalDate currentWeekStartDate = LocalDate.of(2024, 7, 1).with(DayOfWeek.MONDAY);
    
    
     /**
     * Constructor to initialize the POS system, establish a database connection, 
     * and create the GUI components like login page, cashier, and manager POS system.
     */
    public POSSystem() {
        establishDatabaseConnection();
        this.cashier = new CashierFunctions(conn, 1);
        this.managerFunctions = new ManagerFunctions(conn);
        
        frame = new JFrame("POS System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel loginPage = createLoginPage();

        JPanel initialPage = createInitialPage();
        
        mainPanel.add(loginPage, "LoginPage");
        mainPanel.add(initialPage, "InitialPage");

        cardLayout.show(mainPanel, "LoginPage");

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    /**
     * Creates the login page where the user enters their name to access the system.
     * 
     * @return JPanel representing the login page.
     */
    private JPanel createLoginPage() {
        JPanel loginPage = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel nameLabel = new JLabel("Enter your name:");
        JTextField nameField = new JTextField(15);
        JButton loginButton = new JButton("Login");

        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPage.add(nameLabel, gbc);

        gbc.gridx = 1;
        loginPage.add(nameField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        loginPage.add(loginButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                if (!name.isEmpty()) {
                    userName = name;
                    JOptionPane.showMessageDialog(frame, "Welcome, " + name + "!");
                    cardLayout.show(mainPanel, "InitialPage");

                    JPanel managerPOSPanel = createManagerPOSPanel();
                    JPanel cashierPOSPanel = createCashierPOSPanel();
                    mainPanel.add(managerPOSPanel, "ManagerPOS");
                    mainPanel.add(cashierPOSPanel, "CashierPOS");
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter your name to continue.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        return loginPage;
    }

    /**
     * Creates the initial page where the user can choose between the cashier 
     * and manager POS systems.
     * 
     * @return JPanel representing the initial selection page.
     */
    private JPanel createInitialPage() {
        JPanel initialPage = new JPanel();
        initialPage.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JButton cashierButton = new JButton("Cashier POS System");
        JButton managerButton = new JButton("Manager POS System");

        // Cashier Button
        cashierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "CashierPOS");
            }
        });

        // Manager Button
        managerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "ManagerPOS");
            }
        });

        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.gridx = 0;
        gbc.gridy = 0;
        initialPage.add(cashierButton, gbc);

        gbc.gridy = 1;
        initialPage.add(managerButton, gbc);

        return initialPage;
    }

    /**
     * Creates the manager POS panel that includes options for menu management, 
     * inventory management, employee management, reporting, and shifts calendar.
     * 
     * @return JPanel representing the manager POS panel.
     */
    private JPanel createManagerPOSPanel() {
        JPanel managerPOSPanel = new JPanel(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel nameLabel = new JLabel("Welcome, " + userName); 
        JLabel titleLabel = new JLabel("Manager POS", JLabel.CENTER); 
        JButton backButton = new JButton("Return to Initial Page");

        // Style header elements
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add header components
        headerPanel.add(nameLabel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(backButton, BorderLayout.EAST);

        // Back button  to return to the Initial Page
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "InitialPage"));

        // Sidebar with navigation buttons
        JPanel sidebarPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        sidebarPanel.setPreferredSize(new Dimension(200, frame.getHeight()));
        sidebarPanel.setBackground(new Color(70, 130, 180)); 

        // Create buttons for the sidebar
        JButton menuManagementButton = new JButton("Menu Management");
        JButton inventoryManagementButton = new JButton("Inventory Management");
        JButton employeeManagementButton = new JButton("Employee Management");
        JButton reportingButton = new JButton("Reporting");
        JButton shiftsCalendarButton = new JButton("Shifts Calendar");

        // Add buttons to the sidebar
        sidebarPanel.add(menuManagementButton);
        sidebarPanel.add(inventoryManagementButton);
        sidebarPanel.add(employeeManagementButton);
        sidebarPanel.add(reportingButton);
        sidebarPanel.add(shiftsCalendarButton);

        // Main content area where different management views will appear
        JPanel contentPanel = new JPanel(new CardLayout());

        // Menu management panel
        JPanel menuManagementPanel = createMenuManagementPanel();
        contentPanel.add(menuManagementPanel, "MenuManagement");

        JPanel inventoryPanel = createInventoryPanel();
        contentPanel.add(inventoryPanel, "InventoryManagement");

        JPanel employeePanel = createEmployeePanel();
        contentPanel.add(employeePanel, "EmployeeManagement");

        JPanel reportingPanel = createReportingPanel();
        contentPanel.add(reportingPanel, "Reporting");

        JPanel shiftsCalendarPanel = createShiftsCalendarPanel();
        contentPanel.add(shiftsCalendarPanel, "ShiftsCalendar");

        // Action Listeners for sidebar buttons
        menuManagementButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) contentPanel.getLayout();
            cl.show(contentPanel, "MenuManagement");
        });

        inventoryManagementButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) contentPanel.getLayout();
            cl.show(contentPanel, "InventoryManagement");
        });

        employeeManagementButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) contentPanel.getLayout();
            cl.show(contentPanel, "EmployeeManagement");
        });

        reportingButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) contentPanel.getLayout();
            cl.show(contentPanel, "Reporting");
        });

        shiftsCalendarButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) contentPanel.getLayout();
            cl.show(contentPanel, "ShiftsCalendar");
        });

        // Add header, sidebar, and content panels to the main manager panel
        managerPOSPanel.add(headerPanel, BorderLayout.NORTH);
        managerPOSPanel.add(sidebarPanel, BorderLayout.WEST);
        managerPOSPanel.add(contentPanel, BorderLayout.CENTER);

        return managerPOSPanel;
    }

    /**
     * Creates the menu management panel where the manager can add, remove, 
     * and update menu items.
     * 
     * @return JPanel representing the menu management panel.
     */
    private JPanel createMenuManagementPanel() {
        JPanel menuManagementPanel = new JPanel(new BorderLayout());

        // Table to display the current menu items
        String[] columnNames = {"Item Name", "Price", "Category"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable menuTable = new JTable(tableModel);
        menuTable.setRowHeight(30);
        menuTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Allows selection of a single row
        JScrollPane tableScrollPane = new JScrollPane(menuTable);

        // Load menu items into the table
        loadMenuItems(tableModel);

        // Panel for adding new menu items
        JPanel addItemPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // JLabel idLabel = new JLabel("Menu ID:");
        // JTextField idField = new JTextField(10);
        JLabel nameLabel = new JLabel("Item Name:");
        JTextField nameField = new JTextField(15);
        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField(10);
        JLabel categoryLabel = new JLabel("Category:");
        JTextField categoryField = new JTextField(10);

        JButton addButton = new JButton("Add Item");

        // Layout for Add Item form
        // gbc.gridx = 0;
        // gbc.gridy = 0;
        // addItemPanel.add(idLabel, gbc);
        // gbc.gridx = 1;
        // addItemPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        addItemPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        addItemPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        addItemPanel.add(priceLabel, gbc);
        gbc.gridx = 1;
        addItemPanel.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        addItemPanel.add(categoryLabel, gbc);
        gbc.gridx = 1;
        addItemPanel.add(categoryField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        addItemPanel.add(addButton, gbc);

        // Action Listener for Add Item button
        addButton.addActionListener(e -> {
            // String idText = idField.getText().trim();
            String itemName = nameField.getText().trim();
            String priceText = priceField.getText().trim();
            String category = categoryField.getText().trim();

            if (!itemName.isEmpty() && !priceText.isEmpty() && !category.isEmpty()) {
                try {
                    // int id = Integer.parseInt(idText);
                    double price = Double.parseDouble(priceText);
                    managerFunctions.addMenuItem(itemName, category, price);
                    JOptionPane.showMessageDialog(frame, "Item added successfully!");

                    tableModel.addRow(new Object[]{itemName, price, category});
                    // idField.setText("");
                    nameField.setText("");
                    priceField.setText("");
                    categoryField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid numeric values for ID and Price.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Panel for Delete and Save All buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton deleteButton = new JButton("Delete");
        JButton saveAllButton = new JButton("Save All");

        buttonPanel.add(deleteButton);
        buttonPanel.add(saveAllButton);

        // Action Listener for Delete button
        deleteButton.addActionListener(e -> {
            int selectedRow = menuTable.getSelectedRow();
            if (selectedRow >= 0) {
                String itemname = (String) tableModel.getValueAt(selectedRow, 0);
                int response = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this item?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    managerFunctions.removeMenuItem(itemname);
                    tableModel.removeRow(selectedRow); 
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select an item to delete.");
            }
        });

        // Action Listener for Save All button
        saveAllButton.addActionListener(e -> {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                // Retrieve the Menu ID and ensure it is parsed correctly
                // Object menuIdObj = tableModel.getValueAt(i, 0);
                
                // int menuID;
                // if (menuIdObj instanceof Integer) {
                //     menuID = (Integer) menuIdObj;
                // } else {
                //     menuID = Integer.parseInt(menuIdObj.toString());
                // }
                
                String name = (String) tableModel.getValueAt(i, 0);
                double price = Double.parseDouble(tableModel.getValueAt(i, 1).toString());
                String category = (String) tableModel.getValueAt(i, 2);
                
                managerFunctions.setMenuItem(name, category, price);
            }
            JOptionPane.showMessageDialog(frame, "All changes saved successfully!");
        });

        // Add components to the main panel
        menuManagementPanel.add(tableScrollPane, BorderLayout.CENTER);
        menuManagementPanel.add(addItemPanel, BorderLayout.NORTH);
        menuManagementPanel.add(buttonPanel, BorderLayout.SOUTH);

        return menuManagementPanel;
    }

    /**
     * Loads menu items from the database into the provided table model.
     * 
     * @param tableModel The table model to populate with menu items.
     */
    private void loadMenuItems(DefaultTableModel tableModel) {
        try {
            List<MenuItem> items = managerFunctions.getMenuItems(); // Get detailed menu items from database

            for (MenuItem item : items) {
                // int id = item.getMenuID();
                String name = item.getMenuName();
                double price = item.getPrice();
                String category = item.getItemType();

                // Add the item details to the table model
                tableModel.addRow(new Object[]{name, price, category});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading menu items: " + ex.getMessage());
        }
    }

   /**
     * Creates the inventory management panel where the manager can add, remove, 
     * and update inventory items.
     * 
     * @return JPanel representing the inventory management panel.
     */
    private JPanel createInventoryPanel() {
        JPanel inventoryPanel = new JPanel(new BorderLayout());

        // Table to display the current inventory items
        String[] columnNames = {"Item Name", "Quantity In Stock", "Quantity Units", "Reorder Threshold", "Unit Price"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable inventoryTable = new JTable(tableModel);
        inventoryTable.setRowHeight(30);
        inventoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(inventoryTable);

        // Load inventory items into the table
        loadInventoryItems(tableModel);

        // Panel for adding new inventory items
        JPanel addItemPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel nameLabel = new JLabel("Item Name:");
        JTextField nameField = new JTextField(15);
        JLabel quantityLabel = new JLabel("Quantity In Stock:");
        JTextField quantityField = new JTextField(10);
        JLabel unitsLabel = new JLabel("Quantity Units:");
        JTextField unitsField = new JTextField(10);
        JLabel reorderLabel = new JLabel("Reorder Threshold:");
        JTextField reorderField = new JTextField(10);
        JLabel priceLabel = new JLabel("Unit Price:");
        JTextField priceField = new JTextField(10);

        JButton addButton = new JButton("Add Item");


        gbc.gridx = 0;
        gbc.gridy = 0;
        addItemPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        addItemPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        addItemPanel.add(quantityLabel, gbc);
        gbc.gridx = 1;
        addItemPanel.add(quantityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        addItemPanel.add(unitsLabel, gbc);
        gbc.gridx = 1;
        addItemPanel.add(unitsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        addItemPanel.add(reorderLabel, gbc);
        gbc.gridx = 1;
        addItemPanel.add(reorderField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        addItemPanel.add(priceLabel, gbc);
        gbc.gridx = 1;
        addItemPanel.add(priceField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        addItemPanel.add(addButton, gbc);

        // Action Listener for Add Item button
        addButton.addActionListener(e -> {
            String itemName = nameField.getText().trim();
            String quantityText = quantityField.getText().trim();
            String units = unitsField.getText().trim();
            String reorderText = reorderField.getText().trim();
            String priceText = priceField.getText().trim();

            if (!itemName.isEmpty() && !quantityText.isEmpty() && !units.isEmpty() && !reorderText.isEmpty() && !priceText.isEmpty()) {
                try {
                    double quantityInStock = Double.parseDouble(quantityText);
                    double reorderThreshold = Double.parseDouble(reorderText);
                    double unitPrice = Double.parseDouble(priceText);
                    
                    managerFunctions.addInventory(itemName, quantityInStock, units, reorderThreshold, unitPrice);
                    JOptionPane.showMessageDialog(frame, "Item added successfully!");

                    tableModel.addRow(new Object[] {itemName, quantityInStock, units, reorderThreshold, unitPrice});
                    nameField.setText("");
                    quantityField.setText("");
                    unitsField.setText("");
                    reorderField.setText("");
                    priceField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid numeric values for Inventory ID, Quantity, Reorder Threshold, and Unit Price.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Panel for Delete and Save All buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton deleteButton = new JButton("Delete");
        JButton saveAllButton = new JButton("Save All");

        buttonPanel.add(deleteButton);
        buttonPanel.add(saveAllButton);

        // Action Listener for Delete button
        deleteButton.addActionListener(e -> {
            int selectedRow = inventoryTable.getSelectedRow();
            if (selectedRow >= 0) {
                String inventoryName = (String) tableModel.getValueAt(selectedRow, 0);
                int response = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this item?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    managerFunctions.removeInventory(inventoryName);
                    tableModel.removeRow(selectedRow); 
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select an item to delete.");
            }
        });

        // Action Listener for Save All button
        saveAllButton.addActionListener(e -> {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String itemName = tableModel.getValueAt(i, 0).toString();
                double quantityInStock = Double.parseDouble(tableModel.getValueAt(i, 1).toString());
                String units = tableModel.getValueAt(i, 2).toString();
                double reorderThreshold = Double.parseDouble(tableModel.getValueAt(i, 3).toString());
                double unitPrice = Double.parseDouble(tableModel.getValueAt(i, 4).toString());
                
                // Use setInventoryItem to update all fields at once
                managerFunctions.setInventory(itemName, quantityInStock, units, reorderThreshold, unitPrice);
            }
            JOptionPane.showMessageDialog(frame, "All changes saved successfully!");
        });

        // Add components to the main panel
        inventoryPanel.add(tableScrollPane, BorderLayout.CENTER);
        inventoryPanel.add(addItemPanel, BorderLayout.NORTH);
        inventoryPanel.add(buttonPanel, BorderLayout.SOUTH);

        return inventoryPanel;
    }

    /**
     * Loads inventory items from the database into the provided table model.
     * 
     * @param tableModel The table model to populate with inventory items.
     */
    private void loadInventoryItems(DefaultTableModel tableModel) {
        try {
            List<Inventory> items = managerFunctions.getInventoryItems();
    
            tableModel.setRowCount(0);
    
            for (Inventory item : items) {
                String itemName = item.getItemname();
                double quantityInStock = item.getQuantityInStock();
                String quantityUnits = item.getQuantityUnits();
                double reorderThreshold = item.getReorderThreshold();
                double unitPrice = item.getUnitPrice();
    
                tableModel.addRow(new Object[]{itemName, quantityInStock, quantityUnits, reorderThreshold, unitPrice});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading inventory items: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Creates the employee management panel where the manager can add, remove, 
     * and update employee details.
     * 
     * @return JPanel representing the employee management panel.
     */
    private JPanel createEmployeePanel() {
        JPanel employeePanel = new JPanel(new BorderLayout());

        // Table to display current employees
        String[] columnNames = {"Name", "Job Title", "Start Date", "Hourly Wage"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable employeeTable = new JTable(tableModel);
        employeeTable.setRowHeight(30);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(employeeTable);

        // Load employees into the table
        loadEmployees(tableModel);

        // Panel for adding new employees
        JPanel addEmployeePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(15);
        JLabel jobTitleLabel = new JLabel("Job Title:");
        JTextField jobTitleField = new JTextField(10);
        JLabel startDateLabel = new JLabel("Start Date (YYYY-MM-DD):");
        JTextField startDateField = new JTextField(10);
        JLabel hourlyWageLabel = new JLabel("Hourly Wage:");
        JTextField hourlyWageField = new JTextField(10);

        JButton addButton = new JButton("Add Employee");


        gbc.gridx = 0;
        gbc.gridy = 0;
        addEmployeePanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        addEmployeePanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        addEmployeePanel.add(jobTitleLabel, gbc);
        gbc.gridx = 1;
        addEmployeePanel.add(jobTitleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        addEmployeePanel.add(startDateLabel, gbc);
        gbc.gridx = 1;
        addEmployeePanel.add(startDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        addEmployeePanel.add(hourlyWageLabel, gbc);
        gbc.gridx = 1;
        addEmployeePanel.add(hourlyWageField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        addEmployeePanel.add(addButton, gbc);

        // Action Listener for Add Employee button
        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String jobTitle = jobTitleField.getText().trim();
                Date startDate = Date.valueOf(startDateField.getText().trim());
                double hourlyWage = Double.parseDouble(hourlyWageField.getText().trim());

                if (!name.isEmpty() && !jobTitle.isEmpty()) {
                    managerFunctions.addEmployee(name, jobTitle, startDate, hourlyWage);
                    JOptionPane.showMessageDialog(frame, "Employee added successfully!");

                    // Update the table with the new employee
                    tableModel.addRow(new Object[]{name, jobTitle, startDate, hourlyWage});
                    nameField.setText("");
                    jobTitleField.setText("");
                    startDateField.setText("");
                    hourlyWageField.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Panel for Delete and Save All buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton deleteButton = new JButton("Delete");
        JButton saveAllButton = new JButton("Save All");

        buttonPanel.add(deleteButton);
        buttonPanel.add(saveAllButton);

        // Action Listener for Delete button
        deleteButton.addActionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow >= 0) {
                String employeename = (String) tableModel.getValueAt(selectedRow, 0);
                int response = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this employee?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    managerFunctions.removeEmployee(employeename);
                    tableModel.removeRow(selectedRow);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select an employee to delete.");
            }
        });

        // Action Listener for Save All button
        saveAllButton.addActionListener(e -> {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String name = tableModel.getValueAt(i, 0).toString();
                String jobTitle = tableModel.getValueAt(i, 1).toString();
                Date startDate = Date.valueOf(tableModel.getValueAt(i, 2).toString());
                double hourlyWage = Double.parseDouble(tableModel.getValueAt(i, 3).toString());

                managerFunctions.setEmployee(name, jobTitle, startDate, hourlyWage);
            }
            JOptionPane.showMessageDialog(frame, "All changes saved successfully!");
        });

        employeePanel.add(tableScrollPane, BorderLayout.CENTER);
        employeePanel.add(addEmployeePanel, BorderLayout.NORTH);
        employeePanel.add(buttonPanel, BorderLayout.SOUTH);

        return employeePanel;
    }

    /**
     * Loads employee details from the database into the provided table model.
     * 
     * @param tableModel The table model to populate with employee details.
     */
    private void loadEmployees(DefaultTableModel tableModel) {
        try {
            List<Employee> employees = managerFunctions.getEmployees();

            // Clear the table first
            tableModel.setRowCount(0);

            // Add each employee to the table model
            for (Employee employee : employees) {
                tableModel.addRow(new Object[]{
                    employee.getEmployeeName(),
                    employee.getJobTitle(),
                    employee.getStartDate(),
                    employee.getHourlyWage()
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading employees: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Creates the shifts calendar panel where the manager can view shifts 
     * and assigned employees for each day of the week.
     * 
     * @return JPanel representing the shifts calendar panel.
     */
    private JPanel createShiftsCalendarPanel() {
        JPanel shiftsCalendarPanel = new JPanel(new BorderLayout());
        
        // Top panel for navigation
        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton prevWeekButton = new JButton("< Previous Week");
        JButton nextWeekButton = new JButton("Next Week >");
        JLabel weekLabel = new JLabel("Week of " + currentWeekStartDate.format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        navigationPanel.add(prevWeekButton);
        navigationPanel.add(weekLabel);
        navigationPanel.add(nextWeekButton);
    
        shiftsCalendarPanel.add(navigationPanel, BorderLayout.NORTH);
    
        // Center panel for the calendar days
        JPanel daysPanel = new JPanel(new GridLayout(1, 7));
        String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    
        // Shift labels for display
        String[] shiftLabels = {"10am - 2pm", "2pm - 6pm", "6pm - 9pm"};
    
        // Creating a sub-panel for each day of the week
        for (int i = 0; i < daysOfWeek.length; i++) {
            JPanel dayPanel = new JPanel(new BorderLayout());
            dayPanel.setBackground(Color.LIGHT_GRAY);
    
            JLabel dayLabel = new JLabel(daysOfWeek[i], JLabel.CENTER);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 16));
            dayPanel.add(dayLabel, BorderLayout.NORTH);
    
            // Only create shift buttons for Monday through Friday
            if (i >= 1 && i <= 5) {
                JPanel shiftsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
                shiftsPanel.setBackground(Color.DARK_GRAY);
    
                // Add shift buttons
                for (int j = 0; j < shiftLabels.length; j++) {
                    JButton shiftButton = new JButton(shiftLabels[j]);
                    shiftButton.setBackground(new Color(34, 139, 34)); // Green button color
                    shiftButton.setForeground(Color.WHITE);
                    shiftButton.setFont(new Font("Arial", Font.PLAIN, 14));
    
                    // Store day and shift index for each button
                    int dayIndex = i;
                    int shiftIndex = j;
    
                    shiftButton.addActionListener(e -> {
                        LocalDate shiftDate = currentWeekStartDate.plusDays(dayIndex - 1);
                        String shiftStartTime = (shiftIndex == 0) ? "10:00:00" : (shiftIndex == 1) ? "14:00:00" : "18:00:00";
                        Integer shiftID = managerFunctions.getShiftID(shiftDate, shiftStartTime);
                    
                        if (shiftID != null) {
                            List<String> employees = managerFunctions.getEmployeesForShift(shiftID);
                            if (employees != null && !employees.isEmpty()) {
                                String employeeList = String.join("\n", employees);
                                JOptionPane.showMessageDialog(shiftsCalendarPanel, "Employees:\n" + employeeList, "Shift Details", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(shiftsCalendarPanel, "No employees assigned for this shift.", "Shift Details", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(shiftsCalendarPanel, "No shift scheduled at this time.", "Shift Details", JOptionPane.INFORMATION_MESSAGE);
                        }
                    });
                    
    
                    shiftsPanel.add(shiftButton);
                }
    
                dayPanel.add(shiftsPanel, BorderLayout.CENTER);
            }
    
            daysPanel.add(dayPanel);
        }
    
        shiftsCalendarPanel.add(daysPanel, BorderLayout.CENTER);
    
        // Event handlers for week navigation
        prevWeekButton.addActionListener(e -> {
            currentWeekStartDate = currentWeekStartDate.minusWeeks(1); // Move to the previous week
            updateShiftsCalendar(daysPanel, currentWeekStartDate);     // Update the displayed shifts
            weekLabel.setText("Week of " + currentWeekStartDate.format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        });
        
        nextWeekButton.addActionListener(e -> {
            currentWeekStartDate = currentWeekStartDate.plusWeeks(1); // Move to the next week
            updateShiftsCalendar(daysPanel, currentWeekStartDate);    // Update the displayed shifts
            weekLabel.setText("Week of " + currentWeekStartDate.format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        });
        
    
        return shiftsCalendarPanel;
    }
    
    /**
     * Updates the shifts calendar display based on the specified start date of the week.
     * 
     * @param daysPanel The panel to update with shift details.
     * @param weekStartDate The start date of the week to display.
     */
    private void updateShiftsCalendar(JPanel daysPanel, LocalDate weekStartDate) {
        // Remove all components first if refreshing the entire week
        daysPanel.removeAll();
    
        String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        String[] shiftLabels = {"10am - 2pm", "2pm - 6pm", "6pm - 9pm"};
    
        // Loop through the days of the week
        for (int i = 0; i < daysOfWeek.length; i++) {
            JPanel dayPanel = new JPanel(new BorderLayout());
            dayPanel.setBackground(Color.LIGHT_GRAY);
    
            JLabel dayLabel = new JLabel(daysOfWeek[i], JLabel.CENTER);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 16));
            dayPanel.add(dayLabel, BorderLayout.NORTH);
    
            // Only add shifts for Monday through Friday (index 1 to 5)
            if (i >= 1 && i <= 5) {
                JPanel shiftsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
                shiftsPanel.setBackground(Color.DARK_GRAY);
    
                // Calculate the date for the current day in the week
                LocalDate dayDate = weekStartDate.plusDays(i - 1);
    
                // Add shift buttons for each shift on this day
                for (int j = 0; j < shiftLabels.length; j++) {
                    String shiftTime = (j == 0) ? "10:00:00" : (j == 1) ? "14:00:00" : "18:00:00";
                    Integer shiftID = managerFunctions.getShiftID(dayDate, shiftTime);
    
                    JButton shiftButton = new JButton(shiftLabels[j]);
                    shiftButton.setBackground(new Color(34, 139, 34)); // Green button color
                    shiftButton.setForeground(Color.WHITE);
                    shiftButton.setFont(new Font("Arial", Font.PLAIN, 14));
    
                    if (shiftID != null) {
                        shiftButton.addActionListener(e -> {
                            List<String> employees = managerFunctions.getEmployeesForShift(shiftID);
                            if (employees != null && !employees.isEmpty()) {
                                String employeeList = String.join("\n", employees);
                                JOptionPane.showMessageDialog(
                                    shiftsCalendarPanel, 
                                    "Employees:\n" + employeeList, 
                                    "Shift Details", 
                                    JOptionPane.INFORMATION_MESSAGE
                                );
                            } else {
                                JOptionPane.showMessageDialog(
                                    shiftsCalendarPanel, 
                                    "No employees assigned for this shift.", 
                                    "Shift Details", 
                                    JOptionPane.INFORMATION_MESSAGE
                                );
                            }
                        });
                    } else {
                        shiftButton.setEnabled(false); // Disable the button if no shift is found
                    }
    
                    shiftsPanel.add(shiftButton);
                }
    
                dayPanel.add(shiftsPanel, BorderLayout.CENTER);
            }
    
            daysPanel.add(dayPanel);
        }
    
        daysPanel.revalidate();
        daysPanel.repaint();
    }
    
    /**
     * Creates the reporting panel with tabs for sales reports, inventory reports, 
     * and employee hours reports.
     * 
     * @return JPanel representing the reporting panel.
     */
    private JPanel createReportingPanel() {
        JPanel reportingPanel = new JPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Reports", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        reportingPanel.add(titleLabel, BorderLayout.NORTH);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        JPanel productUsageChartPanel = createProductUsageChartPanel();
        JPanel xReportPanel = createXReportPanel();
        JPanel zReportPanel = createZReportPanel();
        JPanel salesReportPanel = createSalesReportPanel();
        JPanel popularityAnalysisPanel = createPopularityAnalysisPanel();
        JPanel employeeHoursReportPanel = createEmployeeHoursReportPanel();
        
        tabbedPane.addTab("Product Usage Chart", productUsageChartPanel);
        tabbedPane.addTab("X Report", xReportPanel);
        tabbedPane.addTab("Z Report", zReportPanel);
        tabbedPane.addTab("Sales Report", salesReportPanel);
        tabbedPane.addTab("Popularity Analysis", popularityAnalysisPanel);
        tabbedPane.addTab("Employee Hours Report", employeeHoursReportPanel);
        
        reportingPanel.add(tabbedPane, BorderLayout.CENTER);
        
        return reportingPanel;
    }

    private JPanel createProductUsageChartPanel() {
        JPanel productUsageChartPanel = new JPanel(new BorderLayout());
    
        // Date range selection panel
        JPanel datePanel = new JPanel();
        JLabel fromLabel = new JLabel("From:");
        JTextField fromDateField = new JTextField(10); // For example, use YYYY-MM-DD format
        JLabel toLabel = new JLabel("To:");
        JTextField toDateField = new JTextField(10);
        JButton generateChartButton = new JButton("Generate Chart");
    
        datePanel.add(fromLabel);
        datePanel.add(fromDateField);
        datePanel.add(toLabel);
        datePanel.add(toDateField);
        datePanel.add(generateChartButton);
    
        productUsageChartPanel.add(datePanel, BorderLayout.NORTH);
    
        // Chart area
        JPanel chartArea = new JPanel(new BorderLayout());
        productUsageChartPanel.add(chartArea, BorderLayout.CENTER);
    
        // Action listener for chart generation
        generateChartButton.addActionListener(e -> {
            try {
                Date fromDate = Date.valueOf(fromDateField.getText().trim());
                Date toDate = Date.valueOf(toDateField.getText().trim());
    
                List<InventoryUsage> usageData = managerFunctions.getInventoryUsage(fromDate, toDate);
    
                // Clear previous chart
                chartArea.removeAll();
    
                // Create table model
                String[] columnNames = {"Item Name", "Quantity Used", "Units"};
                DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
    
                // Populate table model with usage data
                for (InventoryUsage usage : usageData) {
                    tableModel.addRow(new Object[]{usage.getItemName(), usage.getQuantityUsed(), usage.getUnits()});
                }
    
                // Create table
                JTable usageTable = new JTable(tableModel);
                JScrollPane scrollPane = new JScrollPane(usageTable);
                chartArea.add(scrollPane, BorderLayout.CENTER);
    
                // Refresh the panel
                chartArea.revalidate();
                chartArea.repaint();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(productUsageChartPanel, "Invalid date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        return productUsageChartPanel;
    }
    
    private JPanel createXReportPanel() {
        JPanel xReportPanel = new JPanel(new BorderLayout());
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton generateReportButton = new JButton("Generate X Report");
        
        buttonPanel.add(generateReportButton);
        
        xReportPanel.add(buttonPanel, BorderLayout.NORTH);
        
        // Text area for displaying the X report
        JTextArea reportTextArea = new JTextArea();
        reportTextArea.setEditable(false); // Make it non-editable
        reportTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Use monospaced font for better alignment
        JScrollPane scrollPane = new JScrollPane(reportTextArea);
        
        xReportPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Action listener for report generation
        generateReportButton.addActionListener(e -> {
            List<String> xReport = managerFunctions.getXReport();
            
            // Clear the text area and add the report content
            reportTextArea.setText(""); 
            reportTextArea.append("X Report (Today's Summary)\n");
            reportTextArea.append("==========================\n");
            reportTextArea.append("Date: " + LocalDate.now() + "\n\n");
            reportTextArea.append("Hour Range      Orders Sales\n");
            reportTextArea.append("-----------------------------\n");
            for (String line : xReport) {
                reportTextArea.append(line + "\n");
            }
        });
        
        return xReportPanel;
    }

    private JPanel createZReportPanel() {
        JPanel zReportPanel = new JPanel(new BorderLayout());
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton generateReportButton = new JButton("Generate Z Report");
        
        buttonPanel.add(generateReportButton);
        
        zReportPanel.add(buttonPanel, BorderLayout.NORTH);
        
        // Text area for displaying the X report
        JTextArea reportTextArea = new JTextArea();
        reportTextArea.setEditable(false); // Make it non-editable
        reportTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Use monospaced font for better alignment
        JScrollPane scrollPane = new JScrollPane(reportTextArea);
        
        zReportPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Action listener for report generation
        generateReportButton.addActionListener(e -> {
            String zReport = managerFunctions.getZReport();
            
            // Clear the text area and add the report content
            reportTextArea.setText(""); 
            reportTextArea.append("Z Report (Today's Summary)\n");
            reportTextArea.append("==========================\n\n");
            reportTextArea.append("Date: " + LocalDate.now() + "\n\n");
            reportTextArea.append(zReport);
        });
        
        return zReportPanel;
    }

    /**
     * Creates the sales report panel where the manager can generate reports based 
     * on a selected date range.
     * 
     * @return JPanel representing the sales report panel.
     */
    private JPanel createSalesReportPanel() {
        JPanel salesReportPanel = new JPanel(new BorderLayout());
    
        // Date range selection panel
        JPanel datePanel = new JPanel();
        JLabel fromLabel = new JLabel("From:");
        JTextField fromDateField = new JTextField(10); // For example, use YYYY-MM-DD format
        JLabel toLabel = new JLabel("To:");
        JTextField toDateField = new JTextField(10);
        JButton generateReportButton = new JButton("Generate Report");
    
        datePanel.add(fromLabel);
        datePanel.add(fromDateField);
        datePanel.add(toLabel);
        datePanel.add(toDateField);
        datePanel.add(generateReportButton);
    
        salesReportPanel.add(datePanel, BorderLayout.NORTH);
    
        // Text area for displaying the sales report
        JTextArea reportTextArea = new JTextArea();
        reportTextArea.setEditable(false); // Make it non-editable
        reportTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Use monospaced font for better alignment
        JScrollPane scrollPane = new JScrollPane(reportTextArea);
    
        salesReportPanel.add(scrollPane, BorderLayout.CENTER);
    
        // Action listener for report generation
        generateReportButton.addActionListener(e -> {
            try {
                Date fromDate = Date.valueOf(fromDateField.getText().trim());
                Date toDate = Date.valueOf(toDateField.getText().trim());
    
                List<String> salesReport = managerFunctions.getSalesReportByItem(fromDate, toDate);
    
                // Clear the text area and add the report content
                reportTextArea.setText(""); 
                for (String line : salesReport) {
                    reportTextArea.append(line); // Add each line of the report to the text area
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(salesReportPanel, "Invalid date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        return salesReportPanel;
    }

    private JPanel createPopularityAnalysisPanel() {
        JPanel popularityAnalysisPanel = new JPanel(new BorderLayout());

        // Date range selection panel
        JPanel datePanel = new JPanel();
        JLabel fromLabel = new JLabel("From:");
        JTextField fromDateField = new JTextField(10); // For example, use YYYY-MM-DD format
        JLabel toLabel = new JLabel("To:");
        JTextField toDateField = new JTextField(10);
        JButton generateAnalysisButton = new JButton("Generate Analysis");

        datePanel.add(fromLabel);
        datePanel.add(fromDateField);
        datePanel.add(toLabel);
        datePanel.add(toDateField);
        datePanel.add(generateAnalysisButton);

        popularityAnalysisPanel.add(datePanel, BorderLayout.NORTH);

        // Analysis area
        JPanel analysisArea = new JPanel(new BorderLayout());
        popularityAnalysisPanel.add(analysisArea, BorderLayout.CENTER);

        // Action listener for analysis generation
        generateAnalysisButton.addActionListener(e -> {
            try {
                Date fromDate = Date.valueOf(fromDateField.getText().trim());
                Date toDate = Date.valueOf(toDateField.getText().trim());

                List<String> popularityData = managerFunctions.getMenuItemsPopularityAnalysis(fromDate, toDate);

                // Clear previous analysis
                analysisArea.removeAll();

                // Create text area for displaying the report
                JTextArea reportArea = new JTextArea();
                reportArea.setEditable(false);
                reportArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

                // Populate text area with popularity data
                for (String line : popularityData) {
                    reportArea.append(line + "\n");
                }

                // Add text area to a scroll pane
                JScrollPane scrollPane = new JScrollPane(reportArea);
                analysisArea.add(scrollPane, BorderLayout.CENTER);

                // Refresh the panel
                analysisArea.revalidate();
                analysisArea.repaint();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(popularityAnalysisPanel, "Invalid date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return popularityAnalysisPanel;
    }
    
    /**
     * Creates the employee hours report panel where the manager can generate 
     * reports of employee working hours based on a selected date range.
     * 
     * @return JPanel representing the employee hours report panel.
     */
    private JPanel createEmployeeHoursReportPanel() {
        JPanel employeeHoursPanel = new JPanel(new BorderLayout());
    
        // Date range panel
        JPanel datePanel = new JPanel();
        JLabel fromLabel = new JLabel("From:");
        JTextField fromDateField = new JTextField(10);
        JLabel toLabel = new JLabel("To:");
        JTextField toDateField = new JTextField(10);
        JButton generateReportButton = new JButton("Generate Report");
    
        datePanel.add(fromLabel);
        datePanel.add(fromDateField);
        datePanel.add(toLabel);
        datePanel.add(toDateField);
        datePanel.add(generateReportButton);
    
        employeeHoursPanel.add(datePanel, BorderLayout.NORTH);
    
        String[] columnNames = {"Employee ID", "Employee Name", "Total Hours"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable hoursTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(hoursTable);
    
        employeeHoursPanel.add(tableScrollPane, BorderLayout.CENTER);
    
        generateReportButton.addActionListener(e -> {
            try {
                Date fromDate = Date.valueOf(fromDateField.getText().trim());
                Date toDate = Date.valueOf(toDateField.getText().trim());
    
                List<String[]> hoursData = managerFunctions.getEmployeeHoursReport(fromDate, toDate);
    
                tableModel.setRowCount(0); // Clear the table
                for (String[] row : hoursData) {
                    tableModel.addRow(row);
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(employeeHoursPanel, "Invalid date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        return employeeHoursPanel;
    }
    
    /**
     * Creates the cashier POS panel where the cashier can manage orders, 
     * display menu items, and submit orders.
     * 
     * @return JPanel representing the cashier POS panel.
     */
    private JPanel createCashierPOSPanel() {
        JPanel cashierPOSPanel = new JPanel(new BorderLayout());

        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel nameLabel = new JLabel("Welcome, " + userName);
        JLabel titleLabel = new JLabel("Cashier", JLabel.CENTER);
        JButton backButton = new JButton("Back to Initial Page");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "InitialPage"));
        
        headerPanel.add(nameLabel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(backButton, BorderLayout.EAST);

        cashierPOSPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel menuPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setPreferredSize(new Dimension((int) (frame.getWidth() * 0.3), frame.getHeight()));
        DefaultListModel<MenuItem> orderListModel = new DefaultListModel<>();
        JList<MenuItem> orderList = new JList<>(orderListModel);
        orderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        String[] categories = {"Combo", "Side", "Entree", "Appetizer"};
        Map<String, List<MenuItem>> categoryItems = new HashMap<>();

        for (String category : categories) {
            categoryItems.put(category, new ArrayList<>());
        }

        JButton[] categoryButtons = new JButton[categories.length];

        JPanel categoryPanel = new JPanel(new GridLayout(categories.length, 1, 5, 5));
        categoryPanel.setPreferredSize(new Dimension(200, frame.getHeight()));
        categoryPanel.setBackground(new Color(0, 0, 128)); 
        for (int i = 0; i < categories.length; i++) {
            categoryButtons[i] = new JButton(categories[i]);
            categoryButtons[i].setBackground(new Color(0, 0, 128));
            categoryButtons[i].setForeground(Color.WHITE);
            String category = categories[i];
            categoryButtons[i].addActionListener(e -> {
                menuPanel.removeAll();
                try {
                    List<MenuItem> items = cashier.getMenuItems(); 
                    
                    for (String cat : categories) {
                        categoryItems.get(cat).clear();
                    }

                    for (MenuItem item : items) {
                        String itemCategory = item.getItemType(); 
                        if (categoryItems.containsKey(itemCategory)) {
                            categoryItems.get(itemCategory).add(item);
                        }
                    }

                    List<MenuItem> itemsInCategory = categoryItems.get(category);
                    if (itemsInCategory != null) {
                        for (MenuItem item : itemsInCategory) {
                            JButton itemButton = new JButton(item.getMenuName()); 
                            itemButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e1) {
                                    orderListModel.addElement(item);
                                    cashier.addToOrder(item);
                                    updatePriceFields();
                                }
                            });
                            menuPanel.add(itemButton);
                            itemButton.setPreferredSize(new Dimension(100, 100));
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error loading menu items: " + ex.getMessage());
                }
                menuPanel.revalidate();
                menuPanel.repaint();
            });
            categoryPanel.add(categoryButtons[i]);
        }

        JButton removeButton = new JButton("Remove Item");
        subtotalField = new JLabel("Subtotal: $0.00");
        taxesField = new JLabel("Taxes: $0.00");
        tipField = new JLabel("Tip: $0.00");
        totalField = new JLabel("Total: $0.00");
        JButton orderButton = new JButton("Submit Order");

        JPanel pricePanel = new JPanel();
        pricePanel.setLayout(new GridLayout(4, 2, 10, 10));
        pricePanel.setPreferredSize(new Dimension(150, 150));
        pricePanel.setBackground(Color.WHITE); 
        pricePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        subtotalField.setForeground(Color.BLACK);
        taxesField.setForeground(Color.BLACK);
        tipField.setForeground(Color.BLACK);
        totalField.setForeground(Color.BLACK);

        subtotalField.setHorizontalAlignment(SwingConstants.RIGHT);
        taxesField.setHorizontalAlignment(SwingConstants.RIGHT);
        tipField.setHorizontalAlignment(SwingConstants.RIGHT);
        totalField.setHorizontalAlignment(SwingConstants.RIGHT);

        pricePanel.add(new JLabel("Subtotal", JLabel.LEFT));
        pricePanel.add(subtotalField);
        pricePanel.add(new JLabel("Taxes", JLabel.LEFT));
        pricePanel.add(taxesField);
        pricePanel.add(new JLabel("Tip", JLabel.LEFT));
        pricePanel.add(tipField);
        pricePanel.add(new JLabel("Total", JLabel.LEFT));
        pricePanel.add(totalField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(removeButton);
        buttonPanel.add(orderButton);

        orderPanel.add(new JScrollPane(orderList), BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(pricePanel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        orderPanel.add(bottomPanel, BorderLayout.SOUTH);

        cashierPOSPanel.add(categoryPanel, BorderLayout.WEST);
        cashierPOSPanel.add(menuPanel, BorderLayout.CENTER);
        cashierPOSPanel.add(orderPanel, BorderLayout.EAST);

        orderButton.addActionListener(e -> {
            try {
                cashier.submitOrder(cashier.getItemsOrdered());
                JOptionPane.showMessageDialog(frame, "Order submitted");
                orderListModel.clear();
                updatePriceFields();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        removeButton.addActionListener(e -> {
            MenuItem selectedItem = orderList.getSelectedValue();
            if (selectedItem != null) {
                orderListModel.removeElement(selectedItem);
                cashier.removeFromOrder(selectedItem);
                updatePriceFields();
            } else {
                JOptionPane.showMessageDialog(frame, "Please select an item to remove.");
            }
        });

        return cashierPOSPanel;
    }
    /**
     * Updates the price fields (subtotal, taxes, tip, and total) in the cashier POS panel.
     */
    private void updatePriceFields() {
        double subtotal = cashier.getSubtotal();
        double taxes = cashier.getTaxes();
        double tip = 0;  // Tip can be user-input later
        double total = cashier.getTotal();

        // Update the labels with the calculated values
        subtotalField.setText(String.format("%.2f", subtotal));
        taxesField.setText(String.format("%.2f", taxes));
        tipField.setText(String.format("%.2f", tip));
        totalField.setText(String.format("%.2f", total));
    }

    /**
     * Establishes a connection to the database for the POS system.
     */
    private void establishDatabaseConnection() {
        String teamName = dbSetup.user;
        String dbName = teamName + "_db";
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;

        try {
            this.conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * The main entry point for the POS system.
     * 
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        new POSSystem();
    }
}